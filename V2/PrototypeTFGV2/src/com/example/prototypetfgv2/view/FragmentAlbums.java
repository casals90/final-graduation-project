package com.example.prototypetfgv2.view;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Album;

public class FragmentAlbums extends Fragment {
	
	private Controller controller;
	
	private ListView listviewAlbums;
	private ProgressBar mProgressBar;
	private Button newAlbum;
	
	private ListViewAdapterForAlbums adapter;
	private List<Album> albums;
	//private ListViewAdapterForShowPhotos adapter;
    //private List<Photo> myPhotos;
    
	public FragmentAlbums() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		controller = (Controller) this.getActivity().getApplicationContext();
		getActivity().setTitle(R.string.albums);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_albums,container,false);
		
		listviewAlbums = (ListView) view.findViewById(R.id.list_albums);
		newAlbum = (Button) view.findViewById(R.id.add_album);
		newAlbum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//change Fragment
				goToNewAlbum();
			}
		});
		
		//listview = (ListView) view.findViewById(R.id.listview);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar_albums);
		//Execute new Thread for download photos
		//new RemoteDataTask().execute();
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
	
   private class DownloadAlbumsTask extends AsyncTask<Void, Void, Boolean> {
    	
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	mProgressBar.setVisibility(View.VISIBLE);
        }
 
        @Override
        protected Boolean doInBackground(Void... params) {
        	//albums = controller.getParseFunctions().getAlbums();
        	albums = controller.getAlbums();
            if(albums != null && albums.size() > 0)
            	return true;
            return false;
            		
        }
 
        @Override
        protected void onPostExecute(final Boolean success) {
        	if(success) {
				// Pass the results into ListViewAdapter.java
	            adapter = new ListViewAdapterForAlbums(getActivity(),albums);
	            // Binds the Adapter to the ListView
	            listviewAlbums.setAdapter(adapter);
	            // Close the progressdialog
	            mProgressBar.setVisibility(View.INVISIBLE);	
        	}
        	else
        		Toast.makeText(getActivity(),"0 albums",  Toast.LENGTH_LONG).show();
        }

		@Override
		protected void onCancelled() {
			super.onCancelled();
			Toast.makeText(getActivity(),"Error download albums",  Toast.LENGTH_LONG).show();
		}
    }
}
