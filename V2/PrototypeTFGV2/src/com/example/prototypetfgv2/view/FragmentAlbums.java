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
import com.example.prototypetfgv2.model.Photo;
import com.example.prototypetfgv2.model.User;

public class FragmentAlbums extends Fragment {
	
	private Controller controller;
	
	private ListView listview;
	private ProgressBar mProgressBar;
	private Button newAlbum;
	
	private ListViewAdapterForShowPhotos adapter;
    private List<Photo> myPhotos;
    
    private FragmentTransaction transaction;
	private FragmentManager manager;
	
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
		
		newAlbum = (Button) view.findViewById(R.id.add_album);
		newAlbum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//change Fragment
				goToNewAlbum();
			}
		});
		
		listview = (ListView) view.findViewById(R.id.listview);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarDownloadPhotos);
		//Execute new Thread for download photos
		new RemoteDataTask().execute();
		return view;
	}
	
	public void goToNewAlbum() {
		manager = getActivity().getSupportFragmentManager();
		transaction = manager.beginTransaction();
		transaction.replace(R.id.container_fragment_main,new FragmentNewAlbum());
		transaction.addToBackStack(null);
		transaction.commit();
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
