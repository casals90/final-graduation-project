package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Album;
import com.example.prototypetfgv2.utils.Utils;

public class FragmentAlbums extends Fragment {
	
	private Controller controller;
	private FragmentAlbums fragmentAlbums = this;
	
	private ViewPager mViewPager;
	private ProgressBar mProgressBar;
	private TextView noAlbums;
	
	private ChooseAlbumAdapter adapter;
	private ArrayList<Album> albums;
	
	public FragmentAlbums() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		controller = (Controller) this.getActivity().getApplication();
		controller.clearImageLoader();
		getActivity().setTitle(R.string.albums);
		//For show menu in action bar
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
		inflater.inflate(R.menu.menu_fragment_albums, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
			case R.id.new_album:
				goToNewAlbum();
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_albums,container,false);
		
		mViewPager = (ViewPager) view.findViewById(R.id.pagerAlbums);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar_albums);
		
		new DownloadAlbumsTask().execute();
		return view;
	}
	
	public void goToNewAlbum() {
		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.container_fragment_main,new FragmentNewAlbum());
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
	private void configureSizeOfViewPager(ViewPager viewPager) {
		ArrayList<Integer> metrics = Utils.getMetrics(getActivity());
		int height = metrics.get(1);
		//Calculate 70% of pixels
		int newHeight = (int)Math.round((height * 0.75));
		viewPager.getLayoutParams().height = newHeight;
	}
   

	private class DownloadAlbumsTask extends AsyncTask<Void, Void, Boolean> {
    	
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	mProgressBar.setVisibility(View.VISIBLE);
        	mViewPager.setVisibility(View.INVISIBLE);
        }
 
        @Override
        protected Boolean doInBackground(Void... params) {
        	albums = controller.getAlbums();
            if(albums != null)
            	return true;
            return false;
            		
        }
 
        @Override
        protected void onPostExecute(final Boolean success) {
        	if(success) {
	            adapter = new ChooseAlbumAdapter(albums, getActivity(),fragmentAlbums);
	            mViewPager.setAdapter(adapter);
	            mViewPager.setPageMargin(50);
	            //Configure size of viewPager
	            configureSizeOfViewPager(mViewPager);
	            mProgressBar.setVisibility(View.INVISIBLE);
	            mViewPager.setVisibility(View.VISIBLE);
        	}
        	else {
        		noAlbums.setVisibility(View.VISIBLE);
        		mProgressBar.setVisibility(View.INVISIBLE);
        	}
        }

		@Override
		protected void onCancelled() {
			super.onCancelled();
			Toast.makeText(getActivity(),"Error download albums",Toast.LENGTH_LONG).show();
		}
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
   
   public void goToShowAlbumGridMode(Album album) {
		Bundle data = new Bundle();
		data.putParcelable("Album",album);
		FragmentShowAlbum showAlbum = new FragmentShowAlbum();
		showAlbum.setArguments(data);
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container_fragment_main,showAlbum);
		transaction.addToBackStack(null);
		transaction.commit();	
	}
}
