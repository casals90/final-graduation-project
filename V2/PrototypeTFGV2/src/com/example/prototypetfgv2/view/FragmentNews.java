package com.example.prototypetfgv2.view;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Album;
import com.example.prototypetfgv2.model.Photo;
import com.example.prototypetfgv2.model.User;

public class FragmentNews extends Fragment implements NewsInterface {

	private ArrayList<Photo> photos;
	private Controller controller;
	private ProgressBar mProgressBar;
	private HashMap<String, String> albums;
	private ListView listNews;
	private ArrayList<Album> arrayListAlbums;
	private NewsInterface newsInterface;
	private Activity activity;
	
	public FragmentNews() {
		super();
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.news);
		
		this.controller = (Controller) getActivity().getApplication();
		this.albums = new HashMap<String, String>();
		this.newsInterface = this;
		this.activity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_news,container,false);
		
		mProgressBar = (ProgressBar)view.findViewById(R.id.progressBar);
		listNews = (ListView) view.findViewById(R.id.list_news);
		
		new DownloadNewsTask().execute();
		
		return view;
	}


	private class DownloadNewsTask extends AsyncTask<Void, Void, Boolean> {
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        mProgressBar.setVisibility(View.VISIBLE);
	    }
	    @Override
	    protected Boolean doInBackground(Void... params) {
	    	photos = controller.downloadAllPhotosFromCurrentUser();
	    	if(photos != null)
	    		return true;
	    	return false;
	    }

	    @Override
	    protected void onPostExecute(Boolean result) {
	        super.onPostExecute(result);
	        //mProgressBar.setVisibility(View.INVISIBLE);
	        if(result) {
	        	new DownloadAlbumsTask().execute();
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
	
	private class DownloadAlbumsTask extends AsyncTask<Void, Void, Boolean> {
    	
		@Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	//mProgressBar.setVisibility(View.VISIBLE);
        }
 
        @Override
        protected Boolean doInBackground(Void... params) {
        	arrayListAlbums = controller.getAlbums();
            if(arrayListAlbums != null)
            	return true;
            return false;
            		
        }
 
        @Override
        protected void onPostExecute(final Boolean success) {
        	mProgressBar.setVisibility(View.INVISIBLE);
        	if(success) {
        		//Log.v("prototypev1", "success");
        		for(int i = 0; i < arrayListAlbums.size(); i++) {
        			Album album = arrayListAlbums.get(i);
        			albums.put(album.getId(),album.getAlbumTitle());
        		}
        		//Log.v("prototypev1", "albums hashmap "+albums.size());
        		listNews.setAdapter(new ListViewNewsAdapter(getActivity().getApplicationContext(), photos, albums,newsInterface,activity));
        	}
        	else {
        		//No albums
        		//TODO no news
        	}
        }

		@Override
		protected void onCancelled() {
			super.onCancelled();
			Toast.makeText(getActivity(),"Error download albums",Toast.LENGTH_LONG).show();
		}
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
	
}
