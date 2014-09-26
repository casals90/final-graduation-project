package com.example.prototypetfgv1.view;

import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    private List<Photo> myPhotos;
    
	public FragmentAlbums() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		controller = new Controller(this.getActivity().getApplicationContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_albums,container,false);
		
		listview = (ListView) view.findViewById(R.id.listview);
		//Execute new Thread for download photos
		new RemoteDataTask().execute();
		
		return view;
	}
	
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
            mProgressDialog.dismiss();
        }
    }
}
