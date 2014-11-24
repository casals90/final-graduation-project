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
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Album;
import com.example.prototypetfgv2.model.Photo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

public class ListViewPhotosFragment extends Fragment {

	private static final int REQUEST_PICK_IMAGE = 2;
	
	private ListView mListViewPhotos;
	private AdapterListViewShowPhotos adapter;
	private Controller controller;
	private ArrayList<Photo> photos;
	private ProgressBar mProgressBar;
	private Album album;
	private ImageLoader imageLoader;
	private String mCurrentPhotoPath;
	
	public ListViewPhotosFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.albums);
		
		controller = (Controller) this.getActivity().getApplication();
		
		//Fetch album data
		Bundle data = this.getArguments();
		album = data.getParcelable("Album");
		//Put album title in action bar
		getActivity().setTitle((album.getAlbumTitle()));
		controller.clearImageLoader();
		imageLoader = ImageLoader.getInstance();
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_list_view_photos,container,false);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		mListViewPhotos = (ListView) view.findViewById(R.id.photos);
		//New task
		//new DownloadPhotosTask().execute(album.getId());
		return view;
	}
	
	@Override
	public void onResume() {
		new DownloadPhotosTask().execute(album.getId());
		super.onResume();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
		inflater.inflate(R.menu.menu_show_photos, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.grid_view_mode:
				goToShowAlbumGridMode(album);
				break;
			//Overflow menu options
			case R.id.settings:
				//TODO albums settings
				Log.v("prototypev1","gotoalbums settings");
				break;
			case R.id.add_photo_from_gallery:
				choosePhotoFromGallery();
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
	
	public void goToShowAlbumGridMode(Album album) {
		Bundle data = new Bundle();
		data.putParcelable("Album",album);
		FragmentShowPhotosGrid showAlbum = new FragmentShowPhotosGrid();
		showAlbum.setArguments(data);
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container_fragment_main,showAlbum);
		transaction.addToBackStack(null);
		transaction.commit();	
	}
	
	public void goToShowPhotoFullScreen(Photo photo,int position) {
		Intent showPhoto = new Intent(getActivity(),ShowPhotoActivity.class);
		showPhoto.putParcelableArrayListExtra("photos",photos);
		showPhoto.putExtra("currentPosition",position);
		showPhoto.putExtra("idAlbum",album.getId());
		startActivity(showPhoto);
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
	
	private class DownloadPhotosTask extends AsyncTask<String, Void, Boolean> {
		
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	mListViewPhotos.setVisibility(View.INVISIBLE);
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
			if(result) {
				mListViewPhotos.setVisibility(View.VISIBLE);
	        	mProgressBar.setVisibility(View.INVISIBLE);
	        	
	        	boolean pauseOnScroll = false; 
				boolean pauseOnFling = true;
				PauseOnScrollListener listener = new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling);
				mListViewPhotos.setOnScrollListener(listener);
	        	
				adapter = new AdapterListViewShowPhotos(getActivity().getApplicationContext(), photos,imageLoader,album.getId(),getActivity());
				mListViewPhotos.setAdapter(adapter);
				mListViewPhotos.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						goToShowPhotoFullScreen(photos.get(position),position);
					}
				});
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			//Toast.makeText(getActivity(),"Error download photos",  Toast.LENGTH_LONG).show();
		}	
    }
}
