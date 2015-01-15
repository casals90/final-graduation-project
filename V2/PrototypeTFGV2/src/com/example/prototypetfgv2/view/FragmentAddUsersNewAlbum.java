package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
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
/**
 * class that provides Fragment for add users in album
 * @author jordi
 *
 */
public class FragmentAddUsersNewAlbum extends Fragment {

	private ListView list_friends;
	private ProgressBar progressBar;
	private EditText search;
	
	private ListViewAdapterChooseUsersNewAlbum adapter;
    private ArrayList<User> users;
	private Controller controller;
	private ArrayList<String> members;
	private ArrayList<String> membersOld;
	private String input;
	
	private DownloadFriendsTask download;
    
	private String albumTitle;
	
	public FragmentAddUsersNewAlbum(String albumTitle) {
		super();
		
		this.albumTitle = albumTitle;
	}
	/*
	 * method to init Fragment params
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		controller = (Controller) getActivity().getApplication();
		
		final Bundle args = this.getArguments();
		if(args == null)
			members = new ArrayList<String>();
		else 
			members = args.getStringArrayList("members");
		membersOld = (ArrayList<String>)members.clone();
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
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().setTitle(R.string.add_new_album);
	}
	/*
	 * method to create menu with specific options
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
		inflater.inflate(R.menu.menu_new_album, menu);
	}
	/*
	 * method that specify what to do when user click menu option
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.create_album:
				if(adapter != null) {
					members = adapter.getMembers();
					//pass to new Album
					goToNewAlbum();
				}
			break;
			case android.R.id.home:
				//getFragmentManager().popBackStack();
				goToNewAlbumClear();
		        return true;	
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
		
		View view = inflater.inflate(R.layout.fragment_add_users_new_album,container,false);
		list_friends = (ListView) view.findViewById(R.id.list_friends);
		progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
		search = (EditText) view.findViewById(R.id.search); 
		search.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				input = s.toString();
				download = new DownloadFriendsTask();
				download.execute();				
			}
		});
		download = new DownloadFriendsTask();
		download.execute();
		return view;
	}
	/**
	 * class to download friends
	 * @author jordi
	 *
	 */
	private class DownloadFriendsTask extends AsyncTask<Void, Void, Boolean> { 
		/*
		 * method that init view
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        //this method will be running on UI thread
	        list_friends.setVisibility(View.INVISIBLE);
	        progressBar.setVisibility(View.VISIBLE);
	    }
		/*
		 * method to download friends
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
	    @Override
	    protected Boolean doInBackground(Void... params) {
	    	if(input == null)  {
	    		//users = controller.downloadFriends();
	    		users = controller.getFollowing();
	    	}
	    	else {
	    		users = controller.downloadFriendsInputSearch(input);
	    	}
	        if(users != null)
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
	        	//this method will be running on UI thread
		        progressBar.setVisibility(View.INVISIBLE);
		        list_friends.setVisibility(View.VISIBLE); 
	        	adapter = new ListViewAdapterChooseUsersNewAlbum(getActivity(),users,members);
		        // Binds the Adapter to the ListView
		        list_friends.setAdapter(adapter);
	        }
	        else
	        	progressBar.setVisibility(View.INVISIBLE);
	    }
	    /*
	     * method that execute if the thread is cancel
	     * @see android.os.AsyncTask#onCancelled()
	     */
		@Override
		protected void onCancelled() {
			super.onCancelled();
			progressBar.setVisibility(View.INVISIBLE);
			Toast.makeText(getActivity(),"Error search people",  Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * method to change current Fragment for FragmentNewAlbum
	 */
	public void goToNewAlbum() {
		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		FragmentNewAlbum newAlbum = new FragmentNewAlbum(albumTitle);
		//put data
		final Bundle data = new Bundle();
		if(members != null && members.size() > 0) {
			data.putStringArrayList("members",members);
			newAlbum.setArguments(data);
		}
		transaction.replace(R.id.container_new_album,newAlbum);
		transaction.commit();
	}
	/**
	 * method to change current Fragment for FragmentNewAlbum
	 */
	public void goToNewAlbumClear() {
		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		FragmentNewAlbum newAlbum = new FragmentNewAlbum(albumTitle);
		//put data
		final Bundle data = new Bundle();
		if(membersOld != null && membersOld.size() > 0) {
			data.putStringArrayList("members",membersOld);
			newAlbum.setArguments(data);
		}
		transaction.replace(R.id.container_new_album,newAlbum);
		transaction.commit();
	}
}
