package com.example.prototypetfgv2.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;

//implements OnBackStackChangedListener
public class FragmentMain extends Fragment implements OnClickListener {
	
	private static final int REQUEST_IMAGE_CAPTURE = 1;
	
	private Controller controller;
	
	private ImageButton ibAlbums,ibNews,ibTakePhoto,ibFriends,ibProfile;
	private FragmentTransaction transaction;
	private FragmentManager manager;
	
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
				takePhoto();
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
	
	// functions to do photo 
	private void takePhoto() {
		Context context = getActivity(); 
		PackageManager packageManager = context.getPackageManager();
		
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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

	/*
	//Per mostrar la icona d'anar enrera
	@Override
	public void onBackStackChanged() {
		// TODO Auto-generated method stub
		
	}*/
}
