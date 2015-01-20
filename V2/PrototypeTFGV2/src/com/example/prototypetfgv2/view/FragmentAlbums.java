package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Album;
/**
 * Fragment class for albums
 * @author jordi
 *
 */
public class FragmentAlbums extends Fragment {
	
	private Controller controller;
	private ProgressBar mProgressBar;
	private TextView noAlbums;
	private ListView mListViewAlbums;
	
	private ListViewAlbumsAdapter adapter;
	private ArrayList<Album> albums;
	
	public FragmentAlbums() {
		super();
	}
	/*
	 * method to init Fragment params
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		controller = (Controller) this.getActivity().getApplication();
		controller.clearImageLoader();
		
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		//For show menu in action bar
		setHasOptionsMenu(true);
	}
	/*
	 * method to update view
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		//Change action bar title
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
		getActivity().getActionBar().setTitle(R.string.albums);
		new DownloadAlbumsTask().execute();
	}
	/*
	 * method to create menu with specific options
	 * @see android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
		inflater.inflate(R.menu.menu_fragment_albums, menu);
	}
	/*
	 * method that specify what to do when user click menu option
	 * @see android.support.v4.app.Fragment#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.new_album:
				goToNewAlbum();
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	/*
	 * method to init view
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_albums,container,false);
		
		mListViewAlbums = (ListView) view.findViewById(R.id.list_albums);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar_albums);
		noAlbums = (TextView) view.findViewById(R.id.no_albums);
		return view;
	}
	/**
	 * method to chnage current Activity for InputAlbumTitleActivity
	 */
	public void goToNewAlbum() {
		Intent inputAlbumTitle = new Intent(getActivity(),InputAlbumTitleActivity.class);
		startActivity(inputAlbumTitle);
	}
	/**
	 * class to donwload albums from user
	 * @author jordi
	 *
	 */
	private class DownloadAlbumsTask extends AsyncTask<Void, Void, Boolean> {
    	/*
    	 * method init view
    	 * @see android.os.AsyncTask#onPreExecute()
    	 */
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	mProgressBar.setVisibility(View.VISIBLE);
        	mListViewAlbums.setVisibility(View.INVISIBLE);
        }
        /*
         * method to download albums
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected Boolean doInBackground(Void... params) {
        	albums = controller.getAlbums();
            if(albums != null)
            	return true;
            return false;
            		
        }
        /*
	     * method to update view
	     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	     */
        @Override
        protected void onPostExecute(final Boolean success) {
        	if(success) {
        		adapter = new ListViewAlbumsAdapter(albums,getActivity().getApplicationContext());
        		mProgressBar.setVisibility(View.INVISIBLE);
        		mListViewAlbums.setVisibility(View.VISIBLE);
        		mListViewAlbums.setAdapter(adapter);
        		mListViewAlbums.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						goToShowAlbumListMode(albums.get(position));
					}
				});
        	}
        	else {
        		noAlbums.setVisibility(View.VISIBLE);
        		mProgressBar.setVisibility(View.INVISIBLE);
        	}
        }
        /*
	     * method that execute if the thread is cancel
	     * @see android.os.AsyncTask#onCancelled()
	     */
		@Override
		protected void onCancelled() {
			super.onCancelled();
			Toast.makeText(getActivity(),"Error download albums",Toast.LENGTH_LONG).show();
		}
    }
	/**
	 * method to change current Fragment for ListViewPhotosFragment 
	 * @param album album to show
	 */
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
	/**
	 * method to change current Fragment for FragmentShowPhotosGrid 
	 * @param album album to show
	 */
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
}
