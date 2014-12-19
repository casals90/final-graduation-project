package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.User;

public class AddMembersInAlbumActivity extends Activity {

	private ListView mListViewFollowing;
	private ProgressBar mProgressBar;
	private EditText mEditTextSearch;
	
	private String input;
	private String idAlbum;
	
	private ArrayList<User> users;
	private ArrayList<String> members;
	private ArrayList<String> membersOld;
	private ArrayList<String> onlyNewMembers;
	
	private Controller controller;
	
	private DownloadFriendsTask download;
	
	private ListViewAdapterChooseUsersNewAlbum adapter;
	private Activity activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_members_in_album);
		
		this.controller = (Controller) getApplication();
		this.activity = this;
		
		Bundle data = getIntent().getExtras();
		if(data != null) {
			members = data.getStringArrayList("members");
			idAlbum = data.getString("idAlbum");
		}
		else 
			members = new ArrayList<String>();
		
		
		
		this.membersOld = (ArrayList<String>) members.clone();
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mListViewFollowing = (ListView) findViewById(R.id.list_friends);
		mEditTextSearch = (EditText) findViewById(R.id.search);
		mEditTextSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {	
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				input = s.toString();
				if(download != null) {
					download.cancel(true);
					download = new DownloadFriendsTask();
					download.execute();	
				}
				else
					download = new DownloadFriendsTask();
					download.execute();	
			}
		});
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
		
		download = new DownloadFriendsTask();
		download.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_members_in_album, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.accept:
				members = adapter.getMembers();
				Log.v("prototypev1","members "+members.size());
				Log.v("prototypev1","newsmembers "+membersOld.size());
				onlyNewMembers = getNewsMembers(membersOld,members);
				if(onlyNewMembers.size() <= 0) {
					Toast.makeText(getApplicationContext(),"Don't select any following",  Toast.LENGTH_LONG).show();
				}
				else {
					Log.v("prototypev1"," nous membres "+onlyNewMembers.size());
				}
				new AddMembersTask().execute();
				break;
			case android.R.id.home:
				finish();
		        return true;	
			default:
				break;
	}
		return super.onOptionsItemSelected(item);
	}
	
	private class DownloadFriendsTask extends AsyncTask<Void, Void, Boolean> { 
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        //this method will be running on UI thread
	        mListViewFollowing.setVisibility(View.INVISIBLE);
	        mProgressBar.setVisibility(View.VISIBLE);
	    }
	    @Override
	    protected Boolean doInBackground(Void... params) {
	    	if(input == null)  {
	    		//users = controller.getFollowing();
	    		users = controller.getFollowingThatNotInAlbum(members);
	    	}
	    	else {
	    		users = controller.downloadFriendsInputSearch(input);
	    	}
	        if(users != null)
	        	return true;
	        return false;
	    }

	    @Override
	    protected void onPostExecute(final Boolean success) {
	    	mProgressBar.setVisibility(View.INVISIBLE);
	    	if(success) {	   
	        	//this method will be running on UI thread
		        mListViewFollowing.setVisibility(View.VISIBLE); 
	        	adapter = new ListViewAdapterChooseUsersNewAlbum(activity,users,members);
		        // Binds the Adapter to the ListView
		        mListViewFollowing.setAdapter(adapter);
	        }
	    }
	    
		@Override
		protected void onCancelled() {
			super.onCancelled();
			mProgressBar.setVisibility(View.INVISIBLE);
			Toast.makeText(getApplicationContext(),"Error search people",  Toast.LENGTH_LONG).show();
		}
	}
	
	public ArrayList<String> getNewsMembers(ArrayList<String> membersOld,ArrayList<String> members) {
		for(int i = 0; i < membersOld.size(); i++) {
			members.remove(membersOld.get(i));
		}
		return members;
	}
	
	private class AddMembersTask extends AsyncTask<Void, Void, Boolean> { 
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        //this method will be running on UI thread
	        //mListViewFollowing.setVisibility(View.INVISIBLE);
	        //mProgressBar.setVisibility(View.VISIBLE);
	    }
	    @Override
	    protected Boolean doInBackground(Void... params) {
	    	Log.v("prototypev1","controller "+controller);
	    	return controller.addAlbumMembersFromSettings(onlyNewMembers, idAlbum);
	    }

	    @Override
	    protected void onPostExecute(final Boolean success) {
	    	//mProgressBar.setVisibility(View.INVISIBLE);
	    	if(success) {	   
	        	/*//this method will be running on UI thread
		        mListViewFollowing.setVisibility(View.VISIBLE); 
	        	adapter = new ListViewAdapterChooseUsersNewAlbum(activity,users,members);
		        // Binds the Adapter to the ListView
		        mListViewFollowing.setAdapter(adapter);*/
	    		finish();
	        }
	    }
	    
		@Override
		protected void onCancelled() {
			super.onCancelled();
			mProgressBar.setVisibility(View.INVISIBLE);
			Toast.makeText(getApplicationContext(),"Error search people",  Toast.LENGTH_LONG).show();
		}
	}
}
