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

import com.example.prototypetfgv1.R;
import com.example.prototypetfgv1.controller.Controller;

public class FragmentMain extends Fragment implements OnClickListener {
	
	private Controller controller;

	private static final int REQUEST_IMAGE_CAPTURE = 1;
	//private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	
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
	
	/* functions to do photo */
	private void dispatchTakePictureIntent() {
		Context context = getActivity(); 
		PackageManager packageManager = context.getPackageManager();
		
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		//Intent takePictureIntent = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
	    if (takePictureIntent.resolveActivity(packageManager) != null) {
	        startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);    
	    }
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
	        Bundle extras = data.getExtras();
	        Bitmap photo = (Bitmap)extras.get("data");
	      
	        controller.getParseFunctions().updatePhoto(photo,this.getActivity());    
	    }
		/*if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == Activity.RESULT_OK) {
	            // Image captured and saved to fileUri specified in the Intent
	            //Toast.makeText(this, "Image saved to:\n" +data.getData(), Toast.LENGTH_LONG).show();
	        	Log.v("prototypev1", "camera "+data.getData());
	        } else if (resultCode == Activity.RESULT_CANCELED) {
	            // User cancelled the image capture
	        } else {
	            // Image capture failed, advise user
	        }
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
	
	/* accept the change fragment */
	public void changeFragment() {
		transaction.addToBackStack(null);
		transaction.commit();
	}
}
