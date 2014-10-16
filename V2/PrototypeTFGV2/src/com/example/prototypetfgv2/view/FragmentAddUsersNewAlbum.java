package com.example.prototypetfgv2.view;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.User;

public class FragmentAddUsersNewAlbum extends Fragment {

	private ListView list_friends;
	private ProgressBar progressBar;
	
	private ListViewAdapterChooseUsersNewAlbum adapter;
    private List<User> users;
	private Controller controller;
    //private JSONArray members;
	private ArrayList<String> members;
    
	public FragmentAddUsersNewAlbum() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.add_new_album);
		
		Log.v("prototypev1", "FragmentAddUsersNewAlbum");
		
		controller = (Controller) getActivity().getApplicationContext();
		
		Bundle b = this.getArguments();
		if(b != null) {
			members = b.getStringArrayList("members");
			if(members == null && members.size() <= 0)
				members = new ArrayList<String>();
			Log.v("prototypev1", "members != and size "+members.size());
		}
		else {
			members = new ArrayList<String>();
			Log.v("prototypev1", "members == null");
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
	        	
	        	//falla aqui
	        	/*Bundle b =  getArguments();
	    		if(b != null) {
	    			members = b.getStringArrayList("members");
	    			if(members == null && members.size() <= 0)
	    				members = new ArrayList<String>();
	    			Log.v("prototypev1", "diferent de null download task "+members.size());
	    		}
	    		else {
	    			members = new ArrayList<String>();
	    			Log.v("prototypev1", "On postexecute new album null ");
	    		}*/
	        	
	        	adapter = new ListViewAdapterChooseUsersNewAlbum(getActivity(),users,members);
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
	
	public void goToNewAlbum() {
		Log.v("prototypev1", "--------------------------------");
		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		FragmentNewAlbum newAlbum = new FragmentNewAlbum();
		final Bundle bundle = new Bundle();
		bundle.putStringArrayList("members",members);
		newAlbum.setArguments(bundle);
		transaction.replace(R.id.container_fragment_main,newAlbum);
		transaction.addToBackStack(null);
		transaction.commit();
	}
}
