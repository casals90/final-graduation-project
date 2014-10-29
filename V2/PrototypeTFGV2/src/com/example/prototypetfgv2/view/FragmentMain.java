package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
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
	
	// functions to do photo 
	private void takePhoto() {
		Context context = getActivity().getApplicationContext(); 
		PackageManager packageManager = context.getPackageManager();
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(packageManager) != null) {
	        startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);    
	    }
	}
		
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQUEST_IMAGE_CAPTURE:
				if(resultCode == Activity.RESULT_OK) {
					Bundle extras = data.getExtras();
			        Bitmap photo = (Bitmap)extras.get("data");
			        controller.updatePhoto(photo,this.getActivity());
				}
				break;
			case REQUEST_DIALOG_CHOOSE_CURRENT_ALBUM:
				if(resultCode == Activity.RESULT_OK) {
			    	takePhoto();
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

	/*
	//Per mostrar la icona d'anar enrera
	@Override
	public void onBackStackChanged() {
		
		
	}*/
	
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
                    	currentAlbums.add(new CurrentAlbum(a.getId(),a.getAlbumTitle(),a.getAlbumCover()));
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
					takePhoto();
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
