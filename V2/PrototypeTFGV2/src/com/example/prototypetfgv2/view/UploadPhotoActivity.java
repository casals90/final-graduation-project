package com.example.prototypetfgv2.view;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.example.prototypetfgv2.utils.BitmapUtils;

public class UploadPhotoActivity extends Activity {

	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int REQUEST_DIALOG_CHOOSE_CURRENT_ALBUM = 2;
	
	private ImageView mImageView;
	private ImageButton accept,cancel;
	private EditText mEditText;
	private View mProgressBar;
	
	private String mCurrentPhotoPath;
	private String idAlbum;
	private Bitmap photo;
	private Boolean comeFromTakePhoto;
	
	private boolean takePhoto; 
	
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
		
		//Take photo
		Intent data = getIntent();
		
		mCurrentPhotoPath = data.getStringExtra("photo");
		idAlbum = data.getStringExtra("idAlbum");
		
		if(mCurrentPhotoPath == null && idAlbum == null) {
			comeFromTakePhoto = true;
			//Put current album id
			idAlbum = controller.getCurrentAlbum();
			if(idAlbum != null)
				dispatchTakePictureIntent();
			else {
				//Clicar ok gotoprofile
				showConfirmDialog();
			}
		}	
		else {
			comeFromTakePhoto = false;
			new BitmapWorkerTask(mImageView).execute(mCurrentPhotoPath);
		}
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
				if(resultCode == Activity.RESULT_OK)
					new BitmapWorkerTask(mImageView).execute(mCurrentPhotoPath);
				else
					finish();
				break;
			case REQUEST_DIALOG_CHOOSE_CURRENT_ALBUM:
				if(resultCode == Activity.RESULT_OK)
					dispatchTakePictureIntent();
				break;
			default:
				break;
		}
	}
	
	public void showConfirmDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.no_current_album))
		       .setCancelable(false)
		       .setTitle(getString(R.string.title_info_dialog))
		       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   finish();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
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
	        					controller.uploadPhoto(photo, title,activity,idAlbum);
	        					galleryAddPic();
	        					finish();
	        				}
	        				else {
	        					Toast.makeText(getApplication(),"Input title",  Toast.LENGTH_SHORT).show();
	        				}
	        				
	        			}
	        		});
	                cancel.setOnClickListener(new OnClickListener() {
	        			
	        			@Override
	        			public void onClick(View v) {
	        				if(comeFromTakePhoto)
	        					dispatchTakePictureIntent();
	        				else
	        					finish();
	        			}
	        		});
	            }
	        }
	    }
	}
}
