package com.example.prototypetfgv2.view;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Album;
import com.example.prototypetfgv2.model.CurrentAlbum;
import com.example.prototypetfgv2.utils.BitmapUtils;

public class UploadPhotoActivity extends Activity {

	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int REQUEST_DIALOG_CHOOSE_CURRENT_ALBUM = 2;
	
	private ImageView mImageView;
	private ImageButton accept,cancel;
	private EditText mEditText;
	private View mProgressBar;
	
	private String mCurrentPhotoPath;
	
	private Bitmap photo;
	
	private Controller controller;
	private static Activity activity;
	
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_upload_photo);
		
		controller = (Controller) getApplicationContext();
		
		activity = this;
		
		mEditText = (EditText) findViewById(R.id.photo_title);
		mImageView = (ImageView) findViewById(R.id.photo);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
		accept = (ImageButton) findViewById(R.id.accept);
		cancel = (ImageButton) findViewById(R.id.cancel);
		//check current album
		//new DownloadCurrentAlbumTask().execute();
		//Take photo
		dispatchTakePictureIntent();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.upload_photo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//Functions to take photo
		private void dispatchTakePictureIntent() {
		    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		    // Ensure that there's a camera activity to handle the intent
		    if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
		        // Create the File where the photo should go
		        File photoFile = null;
		        try {
		            photoFile = createImageFile();
		        } catch (IOException ex) {
		            // Error occurred while creating the File
		        }
		        // Continue only if the File was successfully created
		        if (photoFile != null) {
		            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
		            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		        }
		    }
		}
		
		private File createImageFile() throws IOException {
		    // Create an image file name
		    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		    String imageFileName = "JPEG_" + timeStamp + "_";
		    File storageDir = Environment.getExternalStoragePublicDirectory(
		            Environment.DIRECTORY_PICTURES);
		    File image = File.createTempFile(
		        imageFileName,  /* prefix */
		        ".jpeg",         /* suffix */
		        storageDir      /* directory */
		    );

		    mCurrentPhotoPath = image.getAbsolutePath();
		    return image;
		}
		
		private void galleryAddPic() {
		    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		    File f = new File(mCurrentPhotoPath);
		    Uri contentUri = Uri.fromFile(f);
		    mediaScanIntent.setData(contentUri);
		    sendBroadcast(mediaScanIntent);
		}
			
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			switch (requestCode) {
				case REQUEST_IMAGE_CAPTURE:
					if(resultCode == Activity.RESULT_OK) {
						new BitmapWorkerTask(mImageView).execute(mCurrentPhotoPath);
					}
					break;
				case REQUEST_DIALOG_CHOOSE_CURRENT_ALBUM:
					if(resultCode == Activity.RESULT_OK) {
						dispatchTakePictureIntent();
					}
					break;
				default:
					break;
			}
		}
	
	/*public void showFragmentDialog(ArrayList<CurrentAlbum> listCurrentAlbums) {
        FragmentManager manager = getFragmentManager();
        FragmentDialogChooseCurrentAlbum dialog = new FragmentDialogChooseCurrentAlbum();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("listCurrentAlbums",listCurrentAlbums);
        dialog.setArguments(bundle);
        dialog.setTargetFragment(this, REQUEST_DIALOG_CHOOSE_CURRENT_ALBUM);
        dialog.show(manager,"dialog");

    }
	
	public void showConfirmDialog() {
		//AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity().getApplicationContext());
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.no_album))
		       .setCancelable(false)
		       .setTitle(getString(R.string.title_info_dialog))
		       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                //do things
		        	    goToAlbums();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}*/
	
	class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
	    private final WeakReference<ImageView> imageViewReference;

	    public BitmapWorkerTask(ImageView imageView) {
	        // Use a WeakReference to ensure the ImageView can be garbage collected
	        imageViewReference = new WeakReference<ImageView>(imageView);
	    }
	    
	    @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	mProgressBar.setVisibility(View.VISIBLE);
        	mImageView.setVisibility(View.INVISIBLE);
        }

	    // Decode image in background.
	    @Override
	    protected Bitmap doInBackground(String... params) {
	        String filePath = params[0];
	        File file = new File(filePath);
	        return BitmapUtils.decodeFileForDisplay(file,activity);
	    }

	    // Once complete, see if ImageView is still around and set bitmap.
	    @Override
	    protected void onPostExecute(Bitmap bitmap) {
	        if (imageViewReference != null && bitmap != null) {
	            final ImageView imageView = imageViewReference.get();
	            if (imageView != null) {
	            	//Put bitmap in a global field
	            	photo = bitmap;
	            	mProgressBar.setVisibility(View.INVISIBLE);
	            	mImageView.setVisibility(View.VISIBLE);
	                imageView.setImageBitmap(bitmap);
	                accept.setOnClickListener(new OnClickListener() {
	        			
	        			@Override
	        			public void onClick(View v) {
	        				String title = mEditText.getText().toString();
	        				if(title.length() > 0) {
	        					mEditText.setText("");
	        					controller.uploadPhoto(photo, title,activity);
	        					galleryAddPic();
	        				}
	        				finish();
	        			}
	        		});
	                cancel.setOnClickListener(new OnClickListener() {
	        			
	        			@Override
	        			public void onClick(View v) {
	        				dispatchTakePictureIntent();
	        			}
	        		});
	            }
	        }
	    }
	}
	private class DownloadCurrentAlbumTask extends AsyncTask<Void, Void, Integer> {
    	
		ProgressDialog mProgressDialog;
    	ArrayList<CurrentAlbum> currentAlbums;
    	CurrentAlbum currentAlbum;
    	ArrayList<Album> albums;
    	
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	mProgressDialog= ProgressDialog.show(getApplication(), "Check your albums","waiting", true);   
        }
 
        @Override
        protected Integer doInBackground(Void... params) {
        	currentAlbum = controller.getCurrentAlbum();
        	Log.v("prototypev1", "getCurrentalbum  "+currentAlbum);
        	if(currentAlbum == null) {
        		albums = controller.getAlbums();
        		Log.v("prototypev1", "getAlbums  "+albums);
        		if(albums == null)
        			return -1;
        		else {
        			currentAlbums = new ArrayList<CurrentAlbum>();
                	for(Album a: albums) {
                    	currentAlbums.add(new CurrentAlbum(a.getId(),a.getAlbumTitle()));
                    }
        			return 0;
        		}
        	}
        	else
        		return 1;
        }
 
        @Override
        protected void onPostExecute(final Integer check) {
        	mProgressDialog.dismiss();
        	mCurrentPhotoPath = null;
        	switch (check) {
				case -1:
					//All null
					//showConfirmDialog();
					break;
				case 0:
					//showFragmentDialog(currentAlbums);
					break;
				case 1:
					//nothing null take photo
					//newTakePhoto();;
					dispatchTakePictureIntent();
					break;
	
				default:
					break;
				}
        }

		@Override
		protected void onCancelled() {
			super.onCancelled();
			Toast.makeText(getApplication(),"Error download albums",  Toast.LENGTH_LONG).show();
		}
    }
}
