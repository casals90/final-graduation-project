package com.example.prototypetfgv2.view;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.example.prototypetfgv2.model.User;

public class FragmentNewAlbum extends Fragment {

	private ListView list_friends;
	private ProgressBar progressBar;
	private Button create;
	
	private ListViewAdapterChooseUsersNewAlbum adapter;
    private List<User> users;
	private Controller controller;
    private JSONArray members;
    
	public FragmentNewAlbum() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.add_new_album);
		
		controller = (Controller) getActivity().getApplicationContext();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_new_album,container,false);
		
		list_friends = (ListView) view.findViewById(R.id.list_friends);
		progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
		create = (Button) view.findViewById(R.id.createAlbum);
		create.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(adapter != null) {
					members = adapter.getMembers();
					// update in parse and create album
					/*Log.v("prototypev1", "checkbox "+members.length());
					for(int i = 0; i < members.length(); i++) {
						try {
							Log.v("prototypev1", "id "+members.getString(i));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}*/
				}	
			}
		});
		
		new DownloadFriendsTask().execute();
		return view;
	}
	 
	private class DownloadFriendsTask extends AsyncTask<Void, Void, Boolean> { 
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        //this method will be running on UI thread
	        list_friends.setVisibility(View.INVISIBLE);
	        progressBar.setVisibility(View.VISIBLE);
	    }
	    @Override
	    protected Boolean doInBackground(Void... params) {
	    	users = controller.downloadFriends();
	        if(users.size() > 0)
	        	return true;
	        return false;
	    }

	    @Override
	    protected void onPostExecute(final Boolean success) {
	        if(success) {
	        	adapter = new ListViewAdapterChooseUsersNewAlbum(getActivity(),users);
		        // Binds the Adapter to the ListView
		        list_friends.setAdapter(adapter);
	        }
	        //this method will be running on UI thread
	        progressBar.setVisibility(View.INVISIBLE);
	        list_friends.setVisibility(View.VISIBLE); 
	    }
	    
		@Override
		protected void onCancelled() {
			super.onCancelled();
			progressBar.setVisibility(View.INVISIBLE);
			Toast.makeText(getActivity(),"Error search people",  Toast.LENGTH_LONG).show();
		}
	}

}
