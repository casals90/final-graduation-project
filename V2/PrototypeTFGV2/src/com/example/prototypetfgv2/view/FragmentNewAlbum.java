package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.app.ActionBar;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.User;
/**
 * Class that provides create a new Album
 * @author jordi
 *
 */
public class FragmentNewAlbum extends Fragment {

	private Controller controller;
	
	private Button addMember;
	private ListView listMembers;
	private ProgressBar progressBar;
	
	private ArrayList<User> users;
	private ArrayList<String> members;
	
	private ListViewAdapterForAddMembers adapter;
	
	private String albumTitle;
	/**
	 * Constructor for Fragment
	 * @param albumTitle
	 */
	public FragmentNewAlbum(String albumTitle) {
		super();
		this.albumTitle = albumTitle;
	}
	/*
	 * Method that init params
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		controller = (Controller) getActivity().getApplicationContext();
		
		final Bundle args = this.getArguments();
		if(args == null) {
			members = new ArrayList<String>();
		}
		else {
			members = args.getStringArrayList("members");
		}
		
		//For show menu in action bar
		setHasOptionsMenu(true);
	}
	/*
	 * Method that update view
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		//Change action bar title
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		getActivity().getActionBar().setTitle(R.string.newAlbum);
	}
	/*
	 * Method that init view
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_new_album,container,false);
		
		addMember = (Button) view.findViewById(R.id.add_members);
		addMember.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				goToAddUsersNewAlbum();
			}
		});
		
		progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
		listMembers = (ListView) view.findViewById(R.id.list_members);
		progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
		
		new ShowMembersTask().execute();
		
		return view;
	}
	/*
	 * Method to create menu with specific options
	 * @see android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
		inflater.inflate(R.menu.menu_new_album, menu);
	}
	/*
	 * method that specify what to do when user click menu option
	 * @see android.support.v4.app.Fragment#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.create_album:
				//create album in parse
				if(createAlbum())
					getActivity().finish();
				break;
			case android.R.id.home:
				getActivity().finish();
		        return true;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	/**
	 * Method that change current Fragment for FragmentAddUsersNewAlbum
	 */
	public void goToAddUsersNewAlbum() {
		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		FragmentAddUsersNewAlbum addUsers = new FragmentAddUsersNewAlbum(albumTitle);
		//put data
		final Bundle data = new Bundle();
		if(members != null && members.size() > 0 && adapter != null) {
			members = adapter.getMembers();
			data.putStringArrayList("members",members);
			addUsers.setArguments(data);
		}
		transaction.replace(R.id.container_new_album,addUsers);
		transaction.addToBackStack(null);
		transaction.commit();
	}
	/**
	 * Method that create album
	 * @return true fine else false
	 */
	public boolean createAlbum() {
		members = adapter.getMembers();
		if(members == null || members.size() <= 0) {
			Toast.makeText(getActivity(),"Minimum adding 1 member",  Toast.LENGTH_LONG).show();
			return false;
		}	
		else {
			controller.newAlbum(members, albumTitle);
			return true;
		}
	}
	/**
	 * Class to download members of albums
	 * @author jordi
	 *
	 */
	private class ShowMembersTask extends AsyncTask<Void, Void, Boolean> { 
		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        //this method will be running on UI thread
	        listMembers.setVisibility(View.INVISIBLE);
	        progressBar.setVisibility(View.VISIBLE);
	    }
		/*
		 * Method to download members of album
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
	    @Override
	    protected Boolean doInBackground(Void... params) {
	    	users = controller.downloadUsersList(members);
	        if(users.size() > 0)
	        	return true;
	        return false;
	    }
	    /*
	     * Method that update view
	     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	     */
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
	    /*
	     * (non-Javadoc)
	     * @see android.os.AsyncTask#onCancelled()
	     */
		@Override
		protected void onCancelled() {
			super.onCancelled();
			progressBar.setVisibility(View.INVISIBLE);
			Toast.makeText(getActivity(),"Error search people",  Toast.LENGTH_LONG).show();
		}
	}
}
