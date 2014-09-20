package com.example.prototypetfgv1.view;


import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.prototypetfgv1.R;
import com.example.prototypetfgv1.controller.Controller;
import com.example.prototypetfgv1.model.Photo;


public class FragmentAlbums extends Fragment {
	private Controller controller;
	private ListView listview;
	private ProgressDialog mProgressDialog;
	private ListViewAdapterForShowPhotos adapter;
    private List<Photo> myPhotos = null;
    private RemoteDataTask remoteDataTask = null;

	public FragmentAlbums() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		controller = new Controller(this.getActivity().getApplicationContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.v("prototypev1","onCreateView albums");
		
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_albums,container,false);
		
		listview = (ListView) getActivity().findViewById(R.id.listview);
		//Execute new Thread for download photos
		remoteDataTask = new RemoteDataTask();		
		remoteDataTask.execute();
		return view;
	}
	
	public void onRefresh() {
		remoteDataTask = null;
		remoteDataTask = new RemoteDataTask();		
		remoteDataTask.execute();
	}
	
	// RemoteDataTask AsyncTask
    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
    	
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("Downloading photos");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }
 
        //Treure aquest mètode i posar-lo a ParseFunctions
        @Override
        protected Void doInBackground(Void... params) {
        	myPhotos = controller.downloadPhotos();
            //Log.v("prototypev1","myPhotos"+myPhotos.size());
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            // Pass the results into ListViewAdapter.java
            adapter = new ListViewAdapterForShowPhotos(getActivity(),myPhotos);
            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
            Log.v("prototypev1","onPostExecute show photos");  
        }
    }

}
