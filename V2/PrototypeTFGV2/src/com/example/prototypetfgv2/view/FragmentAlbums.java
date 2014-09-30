package com.example.prototypetfgv2.view;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Photo;

public class FragmentAlbums extends Fragment {
	
	private Controller controller;
	
	private ListView listview;
	private ProgressBar mProgressBar;
	
	private ListViewAdapterForShowPhotos adapter;
    private List<Photo> myPhotos;
    
	public FragmentAlbums() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		controller = new Controller(this.getActivity().getApplicationContext());
		getActivity().setTitle(R.string.albums);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_albums,container,false);
		
		listview = (ListView) view.findViewById(R.id.listview);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarDownloadPhotos);
		//Execute new Thread for download photos
		new RemoteDataTask().execute();
		return view;
	}
	
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
    	
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	mProgressBar.setVisibility(View.VISIBLE);
        }
 
        @Override
        protected Void doInBackground(Void... params) {
        	myPhotos = controller.downloadPhotos();
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            // Pass the results into ListViewAdapter.java
            adapter = new ListViewAdapterForShowPhotos(getActivity(),myPhotos);
            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
            mProgressBar.setVisibility(View.INVISIBLE);
        }

		@Override
		protected void onCancelled() {
			super.onCancelled();
			Toast.makeText(getActivity(),"Error download photos",  Toast.LENGTH_LONG).show();
		}
    }
}
