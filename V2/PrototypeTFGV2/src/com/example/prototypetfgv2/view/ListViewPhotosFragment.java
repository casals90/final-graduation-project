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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.prototypetfgv2.AlbumSettingsActivity;
import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Album;
import com.example.prototypetfgv2.model.Photo;
import com.example.prototypetfgv2.model.User;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

public class ListViewPhotosFragment extends Fragment implements GoToProfileUserInterface {

	private static final int REQUEST_PICK_IMAGE = 2;
	
	private ListView mListViewPhotos;
	private TextView mTextView;
	private AdapterListViewShowPhotos adapter;
	private Controller controller;
	private ArrayList<Photo> photos;
	private ProgressBar mProgressBar;
	private Album album;
	private ImageLoader imageLoader;
	private String mCurrentPhotoPath;
	private GoToProfileUserInterface goToProfileUserInterface;
	
	public ListViewPhotosFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.albums);
		
		controller = (Controller) this.getActivity().getApplication();
		
		this.goToProfileUserInterface = this;
		
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
		mTextView = (TextView) view.findViewById(R.id.no_photos);
		
		return view;
	}
	
	@Override
	public void onResume() {
		//show up navigation
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
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
			case R.id.settings:
				goToAlbumSettings();
				break;
			case R.id.add_photo_from_gallery:
				choosePhotoFromGallery();
				break;
			case android.R.id.home:
				goToFragmentAlbums();
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
	
	public void goToFragmentAlbums() {
		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.container_fragment_main,new FragmentAlbums());
		transaction.addToBackStack(null);
		transaction.commit();
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
        	mTextView.setVisibility(View.INVISIBLE);
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
				mListViewPhotos.setVisibility(View.VISIBLE);
	        	mProgressBar.setVisibility(View.INVISIBLE);
	        	boolean pauseOnScroll = false; 
				boolean pauseOnFling = true;
				PauseOnScrollListener listener = new PauseOnScrollListener(imageLoader, pauseOnScroll, pauseOnFling);
				mListViewPhotos.setOnScrollListener(listener);
	        	
				adapter = new AdapterListViewShowPhotos(getActivity().getApplicationContext(), photos,imageLoader,album.getId(),getActivity(),goToProfileUserInterface);
				mListViewPhotos.setAdapter(adapter);
				mListViewPhotos.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						goToShowPhotoFullScreen(photos.get(position),position);
					}
				});
			}
			else 
				mTextView.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			//Toast.makeText(getActivity(),"Error download photos",  Toast.LENGTH_LONG).show();
		}	
    }

	@Override
	public void goToProfileUser(User user) {
		if(user.getId().compareTo(controller.getCurrentUser().getId())== 0)
			goToProfile();
		else
			goToUserProfile(user);	
	}
	
	public void goToProfile() {
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container_fragment_main,new FragmentProfile());
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
	public void goToUserProfile(User user) {
		Bundle data = new Bundle();
		data.putParcelable("User",user);
		FragmentProfileOtherUser fpou = new FragmentProfileOtherUser();
		fpou.setArguments(data);
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container_fragment_main,fpou);
		transaction.addToBackStack(null);
		transaction.commit();	
	}
	
	public void goToAlbumSettings() {
		Intent albumSettings = new Intent(getActivity(),AlbumSettingsActivity.class);
		albumSettings.putExtra("Album",album);
		startActivity(albumSettings);
	}
}
