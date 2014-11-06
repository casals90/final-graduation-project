package com.example.prototypetfgv2.view;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Album;
import com.example.prototypetfgv2.model.CurrentAlbum;

//implements OnBackStackChangedListener
public class FragmentMain extends Fragment implements OnClickListener {
	
	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int REQUEST_DIALOG_CHOOSE_CURRENT_ALBUM = 2;
	
	private Controller controller;
	
	private ImageButton ibAlbums,ibNews,ibTakePhoto,ibFriends,ibProfile;
	private FragmentTransaction transaction;
	private FragmentManager manager;
	
	//new variable
	private String mCurrentPhotoPath;
	
	public FragmentMain() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		controller = (Controller) this.getActivity().getApplicationContext();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_main_app,container, false);
		
		ibAlbums = (ImageButton) view.findViewById(R.id.ibAlbums);
		ibNews = (ImageButton) view.findViewById(R.id.ibNews);
		ibTakePhoto = (ImageButton) view.findViewById(R.id.ibTakePhoto);
		ibFriends = (ImageButton) view.findViewById(R.id.ibFriends);
		ibProfile = (ImageButton) view.findViewById(R.id.ibProfile);
		
		ibAlbums.setOnClickListener(this);
		ibNews.setOnClickListener(this);
		ibTakePhoto.setOnClickListener(this);
		ibFriends.setOnClickListener(this);
		ibProfile.setOnClickListener(this);
		
		// put the fragment news first
		initTransaction();
		goToNews();
		return view;
	}
	
	@Override
	public void onClick(View v) {
		initTransaction();
		int id = v.getId();
		switch (id) {
			case R.id.ibAlbums:
				goToAlbums();
				break;
				
			case R.id.ibNews:
				goToNews();
				break;
				
			case R.id.ibTakePhoto:
				new DownloadCurrentAlbumTask().execute();
				break;
				
			case R.id.ibFriends:
				goToFriends();
				break;
				
			case R.id.ibProfile:
				goToProfile();
				break;
			default:
				break;
		}
	}
	
	public void showFragmentDialog(ArrayList<CurrentAlbum> listCurrentAlbums) {
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
		AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
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
		
	}
	
	// init transaction variable to change the fragment 
	public void initTransaction() {
		manager = getActivity().getSupportFragmentManager();
		transaction = manager.beginTransaction();
		//transaction = getFragmentManager().beginTransaction();
	}
	
	// change the fragments 
	public void goToAlbums() {
		transaction.replace(R.id.container_fragment_main,new FragmentAlbums());
		changeFragment();
	}
	
	public void goToNews() {
		transaction.replace(R.id.container_fragment_main,new FragmentNews());
		changeFragment();
	}
	
	//Functions to take photo
	private void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    // Ensure that there's a camera activity to handle the intent
	    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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

	    // Save a file: path for use with ACTION_VIEW intents
	    mCurrentPhotoPath = image.getAbsolutePath();
	    return image;
	}
	
	private void galleryAddPic() {
	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    File f = new File(mCurrentPhotoPath);
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    getActivity().sendBroadcast(mediaScanIntent);
	}
	
	
		
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQUEST_IMAGE_CAPTURE:
				if(resultCode == Activity.RESULT_OK) {
					Log.v("prototypev1", "onactivityresult take photo path: "+mCurrentPhotoPath);
					galleryAddPic();
					goToUploadPhoto(mCurrentPhotoPath);
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
	    /*if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
	        Bundle extras = data.getExtras();
	        Bitmap photo = (Bitmap)extras.get("data");
	        //controller.getParseFunctions().updatePhoto(photo,this.getActivity());
	        controller.updatePhoto(photo,this.getActivity());
	    }
	    //else if(requestCode == REQUEST_DIALOG_CHOOSE_CURRENT_ALBUM && resultCode ==  Activity.RESULT_OK) {
	    else if(requestCode == REQUEST_DIALOG_CHOOSE_CURRENT_ALBUM) {
	    	Log.v("prototypev1", "onacivityResult return dialog resultcode "+resultCode+" == "+Activity.RESULT_OK);
	    	if(resultCode == Activity.RESULT_OK)
	    	Bundle extras = data.getExtras();
	    	int p = extras.getInt("selectedItem");
	    	//takePhoto();
	    }*/
	}
	
	
			
	public void goToFriends() {
		transaction.replace(R.id.container_fragment_main,new FragmentFriends());
		changeFragment();
	}
	
	public void goToProfile() {
		transaction.replace(R.id.container_fragment_main,new FragmentProfile());
		changeFragment();
	}
	
	// accept the change fragment
	public void changeFragment() {
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
	public void goToUploadPhoto(String path) {
		Intent uploadPhoto = new Intent(getActivity().getApplicationContext(),UploadPhotoActivity.class);
		uploadPhoto.putExtra("pathNewPhoto",path);
		startActivity(uploadPhoto);
	}
	
	private class DownloadCurrentAlbumTask extends AsyncTask<Void, Void, Integer> {
    	
		ProgressDialog mProgressDialog;
    	ArrayList<CurrentAlbum> currentAlbums;
    	CurrentAlbum currentAlbum;
    	ArrayList<Album> albums;
    	
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	mProgressDialog= ProgressDialog.show(getActivity(), "Check your albums","waiting", true);   
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
					showConfirmDialog();
					break;
				case 0:
					showFragmentDialog(currentAlbums);
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
			Toast.makeText(getActivity(),"Error download albums",  Toast.LENGTH_LONG).show();
		}
    }
}
