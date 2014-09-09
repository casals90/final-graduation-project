package com.example.prototypetfgv1.view;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.prototypetfgv1.R;
import com.example.prototypetfgv1.controller.Controller;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class FragmentMain extends Fragment implements OnClickListener {
	
	private Controller controller;

	private static final int REQUEST_IMAGE_CAPTURE = 1;
	
	private ImageButton ibAlbums,ibNews,ibTakePhoto,ibFriends,ibProfile;
	private FragmentTransaction transaction;
	
	public FragmentMain() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		controller = new Controller(this.getActivity().getApplicationContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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
		
		/* put the fragment news in init */
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
			Log.v("news","news");
			goToNews();
			break;
			
		case R.id.ibTakePhoto:
			Log.v("take photo","take photo");
			//goToTakePhoto();
			dispatchTakePictureIntent();
			break;
			
		case R.id.ibFriends:
			Log.v("friends","friends");
			goToFriends();
			break;
			
		case R.id.ibProfile:
			Log.v("profile","profile");
			goToProfile();
			break;

		default:
			
			break;
		}
	}
	
	/* functions to do photo */
	private void dispatchTakePictureIntent() {
		Context context = getActivity(); 
		PackageManager packageManager = context.getPackageManager();
		
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(packageManager) != null) {
	        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	        
	    }
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
	        Bundle extras = data.getExtras();
	        Bitmap photo = (Bitmap)extras.get("data");
	      
	        controller.getParseFunctions().updatePhoto(photo,this.getActivity());    
	    }
	}
	
	 //For update photo
    //Activity param is temporal
	public void updatePhoto(Bitmap photo,final Activity activity) {
		// Create the ParseFile
        ParseFile file = new ParseFile("photo.jpeg",Utils.bitmapToByteArray(photo));
        // Upload the image into Parse Cloud
        file.saveInBackground();
        final ParseObject imgupload = new ParseObject("SimpleImage");
        imgupload.put("image", file);
        imgupload.put("name", ParseUser.getCurrentUser().getUsername());
        //Save photo
        imgupload.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				// TODO Auto-generated method stub
				if(e == null) {
					Toast.makeText(activity.getApplicationContext(), "Correct update photo",Toast.LENGTH_LONG).show();
					// Associate photo with user
					controller.getUser().addPhoto(imgupload.getObjectId());
				} else {
					Toast.makeText(activity.getApplicationContext(), "Error update photo",Toast.LENGTH_LONG).show();
				}
			}
		});
        //Log.v("prototypev1","surt");
	}
	
	
	/* init transaction variable to change the fragment */
	public void initTransaction() {
		transaction = getFragmentManager().beginTransaction();
	}
	
	/* change the fragments */
	
	public void goToAlbums() {
		transaction.replace(R.id.container_fragment_main,new FragmentAlbums());
		changeFragment();
	}
	
	public void goToNews() {
		transaction.replace(R.id.container_fragment_main,new FragmentNews());
		changeFragment();
	}
	
	public void goToTakePhoto() {
		transaction.replace(R.id.container_fragment_main,new FragmentTakePhoto());
		changeFragment();
	}
	
	public void goToFriends() {
		transaction.replace(R.id.container_fragment_main,new FragmentFriends());
		changeFragment();
	}
	
	public void goToProfile() {
		transaction.replace(R.id.container_fragment_main,new FragmentProfile());
		changeFragment();
	}
	
	/* accept the change fragment */
	public void changeFragment() {
		transaction.addToBackStack(null);
		transaction.commit();
	}
}
