package com.example.prototypetfgv2.view;

import java.util.List;

import org.json.JSONArray;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.User;

public class FragmentNewAlbum extends Fragment {

	private ListView list_friends;
	private EditText inputAlbumName;
	private ProgressBar progressBar;
	
	private ListViewAdapterChooseUsersNewAlbum adapter;
    private List<User> users;
	private Controller controller;
    private JSONArray members;
    
    private String albumName;
    
	public FragmentNewAlbum() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.add_new_album);
		
		controller = (Controller) getActivity().getApplicationContext();
		//For show menu in action bar
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
		// TODO Auto-generated method stub
		inflater.inflate(R.menu.menu_new_album, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
			case R.id.create_album:
				//Add album name
				if(adapter != null) {
					albumName = inputAlbumName.getText().toString();
					//revisar
					if(albumName.length() > 0) {
						members = adapter.getMembers();
						// update in parse and create album
						if(members != null && members.length() > 0) {
							controller.newAlbum(members,albumName);
							//go to albums
							goToAlbums();
						}
						else
							//comunicate error with toast
							Toast.makeText(getActivity(),"Minimum adding one friend",  Toast.LENGTH_LONG).show();
					}
					else
						Toast.makeText(getActivity(),"Input album name",  Toast.LENGTH_LONG).show();		
				}
				break;
				
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_new_album,container,false);
		list_friends = (ListView) view.findViewById(R.id.list_friends);
		progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
		inputAlbumName = (EditText) view.findViewById(R.id.album_name);
		
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
	
	public void goToAlbums() {
		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.container_fragment_main,new FragmentAlbums());
		transaction.addToBackStack(null);
		transaction.commit();
	}
}
