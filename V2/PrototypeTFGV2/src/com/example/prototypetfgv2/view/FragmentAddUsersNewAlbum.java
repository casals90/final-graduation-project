package com.example.prototypetfgv2.view;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.User;

public class FragmentAddUsersNewAlbum extends Fragment {

	private ListView list_friends;
	private ProgressBar progressBar;
	private EditText search;
	
	private ListViewAdapterChooseUsersNewAlbum adapter;
    private List<User> users;
	private Controller controller;
	private ArrayList<String> members;
	private String albumName;
	private String nameToSearch;
	private String input;
	
	private DownloadFriendsTask download;
    
	public FragmentAddUsersNewAlbum() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.add_new_album);
		
		controller = (Controller) getActivity().getApplicationContext();
		
		final Bundle args = this.getArguments();
		if(args == null) {
			members = new ArrayList<String>();
		}
		else {
			members = args.getStringArrayList("members");
			albumName = args.getString("albumName");
		}
		
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
				if(adapter != null) {
					members = adapter.getMembers();
					//pass to new Album
					goToNewAlbum();
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
		View view = inflater.inflate(R.layout.fragment_add_users_new_album,container,false);
		list_friends = (ListView) view.findViewById(R.id.list_friends);
		progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
		search = (EditText) view.findViewById(R.id.search);
		search.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				//TODO
				input = s.toString();
				Log.v("prototypev1", "input change ");
				//controller.getParseFunctions().downloadFriendsInputSearch(input);
				download = new DownloadFriendsTask();
				download.execute();
				
				
			}
		});
		
		download = new DownloadFriendsTask();
		download.execute();
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
	    	Log.v("prototypev1", "DownloadFriendsTask input "+input);
	    	if(input == null || input.length() <= 0)  {
	    		users = controller.downloadFriends();
	    		Log.v("prototypev1", "downloadFriends");
	    	}
	    	else {
	    		Log.v("prototypev1", "downloadFriendsInputSearch(input)");
	    		users = controller.downloadFriendsInputSearch(input);
	    	}
	    	Log.v("prototypev1", "DownloadFriendsTask users size = "+users.size());
	        if(users.size() > 0)
	        	return true;
	        return false;
	    }

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
	        Log.v("prototypev1", "fi DownloadFriendsTask");
	    }
	    
		@Override
		protected void onCancelled() {
			super.onCancelled();
			progressBar.setVisibility(View.INVISIBLE);
			Toast.makeText(getActivity(),"Error search people",  Toast.LENGTH_LONG).show();
		}
	}
	
	public void goToNewAlbum() {
		//Log.v("prototypev1", "--------------------------------");
		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		FragmentNewAlbum newAlbum = new FragmentNewAlbum();
		//put data
		final Bundle data = new Bundle();
		if(members != null && members.size() > 0) {
			//Log.v("prototypev1", " members != null to send "+members);
			data.putStringArrayList("members",members);
			if(albumName != null)
				data.putString("albumName",albumName);
			newAlbum.setArguments(data);
		}
		transaction.replace(R.id.container_fragment_main,newAlbum);
		//transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//Log.v("prototypev1", "on destroy addusers");
	}
	
	
}
