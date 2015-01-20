package com.example.prototypetfgv2.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.prototypetfgv2.R;
/**
 * Class that provide the structure for main screen
 * @author jordi
 *
 */
public class FragmentMain extends Fragment implements OnClickListener {
	
	private ImageButton ibAlbums,ibNews,ibTakePhoto,ibFriends,ibProfile;
	private FragmentTransaction transaction;
	private FragmentManager manager;
	/**
	 * Constructor for Fragment
	 */
	public FragmentMain() {
		super();
	}
	/*
	 * (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	/*
	 * Method that init view
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
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
	/*
	 * Method to change Fragment when click the menu option
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
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
				goToUploadPhoto();
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
	/**
	 * Method that init FragmentManager
	 */
	public void initTransaction() {
		manager = getActivity().getSupportFragmentManager();
		transaction = manager.beginTransaction();
	}
	
	// change the fragments 
	/**
	 * Method that change fragment container for FragmentAlbums
	 */
	public void goToAlbums() {
		transaction.replace(R.id.container_fragment_main,new FragmentAlbums());
		changeFragment();
	}
	/**
	 * Method that change fragment container for FragmentNews
	 */
	public void goToNews() {
		transaction.replace(R.id.container_fragment_main,new FragmentNews());
		changeFragment();
	}
	/**
	 * Method that change fragment container for FragmentFriends
	 */		
	public void goToFriends() {
		transaction.replace(R.id.container_fragment_main,new FragmentFriends());
		changeFragment();
	}
	/**
	 * Method that change fragment container for FragmentProfile
	 */
	public void goToProfile() {
		transaction.replace(R.id.container_fragment_main,new FragmentProfile());
		changeFragment();
	}
	
	// accept the change fragment
	/**
	 * Method that accept the change fragment
	 */
	public void changeFragment() {
		transaction.addToBackStack(null);
		transaction.commit();
	}
	/**
	 * Method that change current Activity for UploadPhotoActivity 
	 */
	public void goToUploadPhoto() {
		Intent uploadPhoto = new Intent(getActivity().getApplicationContext(),UploadPhotoActivity.class);
		startActivity(uploadPhoto);
	}
}
