package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Album;
import com.example.prototypetfgv2.model.Photo;

public class FragmentShowPhotosGrid extends Fragment {
	
	private static final int REQUEST_PICK_IMAGE = 2;
	
	private GridView gridView;
	private ProgressBar mProgressBar;
	private TextView noPhotos;
	
	private Controller controller;
	private Album album;
	private ArrayList<Photo> photos;
	//Photo that user selected
	private Photo photo;
	private String mCurrentPhotoPath;
	
	private GridViewAdapterForShowPhotos adapter;
	
	private boolean goToNews;
	
	public FragmentShowPhotosGrid() {
		super();
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		controller = (Controller) this.getActivity().getApplicationContext();
		
		//Fetch data album
		Bundle data = this.getArguments();
		album = data.getParcelable("Album");
		goToNews = data.getBoolean("goToNews");	
		
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_show_album,container,false);
		
		gridView = (GridView) view.findViewById(R.id.gridView_photos);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar_download_albums);
		noPhotos = (TextView) view.findViewById(R.id.no_photos);
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		new DownloadPhotosTask().execute(album.getId());
		//Put album title in action bar
		getActivity().setTitle((album.getAlbumTitle()));
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
		inflater.inflate(R.menu.menu_show_photos, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.list_view_mode:
				goToShowAlbumListMode(album);
				break;
				//Overflow menu options
			case R.id.settings:
				//TODO albums settings
				Log.v("prototypev1","gotoalbums settings");
				break;
			case R.id.add_photo_from_gallery:
				choosePhotoFromGallery();
				break;
			case android.R.id.home:
				if(goToNews)
					goToShowAlbumListMode(album);
				else
					getFragmentManager().popBackStack();
		        break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//Choose from gallery
	public void choosePhotoFromGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent,"Select Picture"),REQUEST_PICK_IMAGE);
	}
	
	public String searchPhotoSelect(Intent data) {
		Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQUEST_PICK_IMAGE:
				if(resultCode == Activity.RESULT_OK && data != null) {
					mCurrentPhotoPath = searchPhotoSelect(data);
					Intent uploadPhoto = new Intent(getActivity(), UploadPhotoActivity.class);
					uploadPhoto.putExtra("photo",mCurrentPhotoPath);
					uploadPhoto.putExtra("idAlbum",album.getId());
					startActivity(uploadPhoto);
				}
				break;
	
			default:
				break;
		}
	}
	
	public void goToShowAlbumListMode(Album album) {
		Bundle data = new Bundle();
		data.putParcelable("Album",album);
		data.putBoolean("goToNews",goToNews);
		ListViewPhotosFragment listViewPhotos = new ListViewPhotosFragment();
		listViewPhotos.setArguments(data);
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container_fragment_main,listViewPhotos);
		transaction.addToBackStack(null);
		transaction.commit();	
	}
	
	public void goToFragmentAlbums() {
		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.container_fragment_main,new FragmentAlbums());
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
	private class DownloadPhotosTask extends AsyncTask<String, Void, Boolean> {
		
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	gridView.setVisibility(View.INVISIBLE);
        	noPhotos.setVisibility(View.INVISIBLE);
        	mProgressBar.setVisibility(View.VISIBLE);
        }
 
        @Override
        protected Boolean doInBackground(String... params) {
        	String id = params[0];
        	photos = controller.downloadPhotosFromAlbum(id);
        	if(photos != null)
        		return true;
        	return false;
        }

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			mProgressBar.setVisibility(View.INVISIBLE); 
			if(result) {
				gridView.setVisibility(View.VISIBLE);
	        	mProgressBar.setVisibility(View.INVISIBLE);        	
	        	// Pass the results into ListViewAdapter.java
	            adapter = new GridViewAdapterForShowPhotos(getActivity(),photos);
	            // Binds the Adapter to the ListView
	            gridView.setAdapter(adapter);
	            gridView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						goToShowPhotoFullScreen(photo,position);
					}
				});
			}
			else
				noPhotos.setVisibility(View.INVISIBLE);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			//Toast.makeText(getActivity(),"Error download photos",  Toast.LENGTH_LONG).show();
		}	
    }

	public void goToShowPhotoFullScreen(Photo photo,int position) {
		Intent showPhoto = new Intent(getActivity(),ShowPhotoActivity.class);
		showPhoto.putParcelableArrayListExtra("photos",photos);
		showPhoto.putExtra("currentPosition",position);
		showPhoto.putExtra("idAlbum",album.getId());
		startActivity(showPhoto);
	}
}
