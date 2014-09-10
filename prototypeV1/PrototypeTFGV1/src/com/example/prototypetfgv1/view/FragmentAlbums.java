package com.example.prototypetfgv1.view;


import java.util.ArrayList;
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
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class FragmentAlbums extends Fragment {
	Controller controller;
	List<ParseObject> ob;
    ListView listview;
    ProgressDialog mProgressDialog;
    ListViewAdapter adapter;
    private List<Photo> myPhotos = null;

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
		//Execute new Thread for download photos
		new RemoteDataTask().execute();		
		return view;
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
            //myPhotos = new ArrayList<Photo>();
            Log.v("prototypev1","myPhotos"+myPhotos.size());
            /*try {
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("SimpleImage");
                query.whereEqualTo("usersId",controller.getUser().getId());
                query.orderByDescending("createdAt");
                ob = query.find();
                for (ParseObject p : ob) {
                    ParseFile image = (ParseFile) p.get("image");
                    Photo photo = new Photo();
                    photo.setId(p.getObjectId());
                    photo.setTitle("title");
                    photo.setPhoto(image.getUrl());
                    photo.setCreatedAt(String.valueOf(p.getCreatedAt()));
                    myPhotos.add(photo);
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }*/
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
        	// Locate the listview in listview_main.xml
            listview = (ListView) getActivity().findViewById(R.id.listview);
            // Pass the results into ListViewAdapter.java
            adapter = new ListViewAdapter(getActivity(),myPhotos);
            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss();
            Log.v("prototypev1","onPostExecute show photos");  
        }
    }

}
