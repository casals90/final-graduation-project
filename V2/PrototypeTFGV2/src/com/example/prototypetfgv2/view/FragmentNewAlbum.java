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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.User;


public class FragmentNewAlbum extends Fragment {

	private Controller controller;
	
	private EditText inputAlbum;
	private ImageButton add;
	private ListView listMembers;
	private ProgressBar progressBar;
	
	private List<User> users;
	private ArrayList<String> members;
	private String albumName;
	
	private ListViewAdapterForAddMembers adapter;
	
	public FragmentNewAlbum() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.friends);
		
		controller = (Controller) getActivity().getApplicationContext();
		
		final Bundle args = this.getArguments();
		if(args == null) {
			//Log.v("prototypev1", "args = null");
			members = new ArrayList<String>();
		}
		else {
			members = args.getStringArrayList("members");
			albumName = args.getString("albumName");
		}
		Log.v("prototypev1", "albumName ="+albumName);		
		//For show menu in action bar
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_new_album,container,false);
		
		inputAlbum = (EditText) view.findViewById(R.id.album_name);
		
		add = (ImageButton) view.findViewById(R.id.add_members);
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//go to add members
				albumName = inputAlbum.getText().toString();
				goToAddUsersNewAlbum();
			}
		});
		progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
		listMembers = (ListView) view.findViewById(R.id.list_members);
		
		if(albumName != null)
			inputAlbum.setText(albumName);
		
		new ShowMembersTask().execute();
		
		return view;
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
				albumName = inputAlbum.getText().toString();
				//create album in parse
				if(createAlbum())
					goToAlbums();
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void goToAddUsersNewAlbum() {
		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		FragmentAddUsersNewAlbum addUsers = new FragmentAddUsersNewAlbum();
		//put data
		final Bundle data = new Bundle();
		if(members != null && members.size() > 0 && adapter != null) {
			//Log.v("prototypev1", " members != null to send "+members);
			members = adapter.getMembers();
			data.putStringArrayList("members",members);
			albumName = inputAlbum.getText().toString();
			if(albumName != null)
				data.putString("albumName",albumName);
			addUsers.setArguments(data);
		}
		transaction.replace(R.id.container_fragment_main,addUsers);
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
	public void goToAlbums() {
		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		FragmentAlbums albums = new FragmentAlbums();
		transaction.replace(R.id.container_fragment_main,albums);
		//transaction.addToBackStack(null);
		transaction.commit();
	}
	
	public boolean createAlbum() {
		if(members == null || members.size() <= 0) {
			Toast.makeText(getActivity(),"Minimum adding 1 member",  Toast.LENGTH_LONG).show();
			return false;
		}	
		else if(albumName == null || albumName.length() <= 0) {
			Toast.makeText(getActivity(),"Input album name",  Toast.LENGTH_LONG).show();
			return false;
		}
		else {
			controller.newAlbum(members, albumName);
			return true;
		}
	}
	
	private class ShowMembersTask extends AsyncTask<Void, Void, Boolean> { 
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        //this method will be running on UI thread
	        listMembers.setVisibility(View.INVISIBLE);
	        progressBar.setVisibility(View.VISIBLE);
	    }
	    @Override
	    protected Boolean doInBackground(Void... params) {
	    	users = controller.downloadUsersList(members);
	        if(users.size() > 0)
	        	return true;
	        return false;
	    }

	    @Override
	    protected void onPostExecute(final Boolean success) {
	        if(success) {
	        	//this method will be running on UI thread
		        progressBar.setVisibility(View.INVISIBLE);
		        listMembers.setVisibility(View.VISIBLE); 
	        	//List all friends
	 	        //Delete members that not in album
	        	adapter = new ListViewAdapterForAddMembers(getActivity(),users);
		        // Binds the Adapter to the ListView
	        	listMembers.setAdapter(adapter);
	        }
	        else
	        	progressBar.setVisibility(View.INVISIBLE);
	    }
	    
		@Override
		protected void onCancelled() {
			super.onCancelled();
			progressBar.setVisibility(View.INVISIBLE);
			Toast.makeText(getActivity(),"Error search people",  Toast.LENGTH_LONG).show();
		}
	}
}
