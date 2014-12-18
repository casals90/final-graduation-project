package com.example.prototypetfgv2.view;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.support.v4.widget.SwipeRefreshLayout;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Album;
import com.example.prototypetfgv2.model.Photo;
import com.example.prototypetfgv2.model.User;

public class FragmentNews extends Fragment implements NewsInterface, SwipeRefreshLayout.OnRefreshListener {

	private ArrayList<Photo> photos;
	private ArrayList<Photo> update;
	private Controller controller;
	private ProgressBar mProgressBar;
	private HashMap<String, String> albums;
	private ListView listNews;
	private ArrayList<Album> arrayListAlbums;
	private NewsInterface newsInterface;
	private Activity activity;
	private SwipeRefreshLayout swipeLayout;
	private ListViewNewsAdapter adapter;
	
	public FragmentNews() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.controller = (Controller) getActivity().getApplication();
		this.albums = new HashMap<String, String>();
		this.newsInterface = this;
		this.activity = getActivity();
		
		controller.clearImageLoader();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//Change action bar title
		getActivity().getActionBar().setTitle(R.string.news);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		new DownloadNewsTask().execute();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_news,container,false);
		
		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		
		swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
		android.R.color.holo_green_light,
		android.R.color.holo_orange_light,
		android.R.color.holo_red_light);
		
		mProgressBar = (ProgressBar)view.findViewById(R.id.progressBar);
		listNews = (ListView) view.findViewById(R.id.list_news);
		
		return view;
	}


	private class DownloadNewsTask extends AsyncTask<Void, Void, Boolean> {
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        mProgressBar.setVisibility(View.VISIBLE);
	        listNews.setVisibility(View.INVISIBLE);
	    }
	    @Override
	    protected Boolean doInBackground(Void... params) {
	    	photos = controller.downloadAllPhotosFromCurrentUser();
	    	arrayListAlbums = controller.getAlbums();
	    	if(photos != null && arrayListAlbums != null)
	    		return true;
	    	return false;
	    }

	    @Override
	    protected void onPostExecute(Boolean result) {
	        super.onPostExecute(result);
	        mProgressBar.setVisibility(View.INVISIBLE);
	        if(result) {
	        	//new DownloadAlbumsTask().execute();
	        	for(int i = 0; i < arrayListAlbums.size(); i++) {
        			Album album = arrayListAlbums.get(i);
        			albums.put(album.getId(),album.getAlbumTitle());
        		}
        		listNews.setVisibility(View.VISIBLE);
        		//listNews.setAdapter(new ListViewNewsAdapter(getActivity().getApplicationContext(), photos, albums,newsInterface,activity));
        		adapter = new ListViewNewsAdapter(getActivity().getApplicationContext(), photos, albums,newsInterface,activity);
        		listNews.setAdapter(adapter);
        		listNews.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						goToShowPhotoFullScreen(photos.get(position));
					}
				});
	        }
	        else {
	        	Log.v("prototypev1", "error download");
	        }
	    }
	    
		@Override
		protected void onCancelled() {
			super.onCancelled();
			
			Toast.makeText(getActivity(),"Error download photos",  Toast.LENGTH_LONG).show();
		}
	}
	
	private class UpdateNewsTask extends AsyncTask<Void, Void, Boolean> {
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        //mProgressBar.setVisibility(View.VISIBLE);
	        //listNews.setVisibility(View.INVISIBLE);
	    }
	    @Override
	    protected Boolean doInBackground(Void... params) {
	    	update = controller.getNewsPhotosFromCreatedAt(photos.get(0).getId(),arrayListAlbums);
	    	if(update != null)
	    		return true;
	    	return false;
	    }

	    @Override
	    protected void onPostExecute(Boolean result) {
	        super.onPostExecute(result);
	        //mProgressBar.setVisibility(View.INVISIBLE);
	        if(result) {
	        	swipeLayout.setRefreshing(false);
	        	photos = adapter.updateNews(update);
	        }
	        else {
	        	Log.v("prototypev1", "error download");
	        }
	    }
	    
		@Override
		protected void onCancelled() {
			super.onCancelled();
			
			Toast.makeText(getActivity(),"Error download photos",  Toast.LENGTH_LONG).show();
		}
	}
	
	
	public void goToShowPhotoFullScreen(Photo photo) {
		Intent showPhoto = new Intent(getActivity(),ShowFullScreenPhotoOfNews.class);
		showPhoto.putExtra("photo",photo);
		startActivity(showPhoto);
	}

	@Override
	public void goToProfileUser(User user) {
		if(user.getId().compareTo(controller.getCurrentUser().getId())== 0)
			goToProfile();
		else
			goToUserProfile(user);
	}

	@Override
	public void goToAlbum(String idAlbum) {
		ArrayList<String> idAlbums = new ArrayList<String>();
		idAlbums.add(idAlbum);
		ArrayList<Album> albums = controller.downloadAlbumsList(idAlbums);

		goToShowAlbumListMode(albums.get(0));
	}
	
	public void goToShowAlbumListMode(Album album) {
		Bundle data = new Bundle();
		data.putParcelable("Album",album);
		data.putBoolean("goToNews",true);
		ListViewPhotosFragment listViewPhotos = new ListViewPhotosFragment();
		listViewPhotos.setArguments(data);
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container_fragment_main,listViewPhotos);
		transaction.addToBackStack(null);
		transaction.commit();	
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

	@Override
	public void onRefresh() {
		swipeLayout.setRefreshing(true);
		//TODO mirar si hi ha news mes noves
		new UpdateNewsTask().execute();
		Log.v("prototypev1","on refresh "+photos.get(0).getCreatedAt());
	}
}
