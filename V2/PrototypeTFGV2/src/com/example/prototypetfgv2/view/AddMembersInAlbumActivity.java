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
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.User;
/**
 * Activity class for add members in album
 * @author jordi
 *
 */
public class AddMembersInAlbumActivity extends Activity {

	private ListView mListViewFollowing;
	private ProgressBar mProgressBar;
	private EditText mEditTextSearch;
	private TextView mTextViewZeroMatches;
	
	private String input;
	private String idAlbum;
	
	private ArrayList<User> users;
	private ArrayList<String> members;
	private ArrayList<String> membersOld;
	private ArrayList<String> onlyNewMembers;
	
	private Controller controller;
	
	private ListViewAdapterChooseUsersNewAlbum adapter;
	private Activity activity;
	/*
	 * method to init the view
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
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
				new DownloadFriendsTask().execute();
			}
		});
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
		mTextViewZeroMatches = (TextView) findViewById(R.id.zero_matches);
		new DownloadFriendsTask().execute();
	}
	/*
	 * method to create menu with specific options
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_members_in_album, menu);
		return true;
	}
	/*
	 * method that specify what to do when user click menu option
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.accept:
				members = adapter.getMembers();
				onlyNewMembers = getNewsMembers(membersOld,members);
				if(onlyNewMembers.size() <= 0) {
					Toast.makeText(getApplicationContext(),"Don't select any following",  Toast.LENGTH_LONG).show();
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
	/**
	 * class to download friends of user
	 * @author jordi
	 *
	 */
	private class DownloadFriendsTask extends AsyncTask<Void, Void, Boolean> { 
		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        //this method will be running on UI thread
	        mListViewFollowing.setVisibility(View.INVISIBLE);
	        mTextViewZeroMatches.setVisibility(View.INVISIBLE);
	        mProgressBar.setVisibility(View.VISIBLE);
	    }
		/*
		 * method to download friends
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
	    @Override
	    protected Boolean doInBackground(Void... params) {
	    	
	    	if(input == null) 
	    		users = controller.getFollowingThatNotInAlbum(members);
	    	
	    	else 
	    		users = controller.downloadFriendsInputSearchInAlbumSettings(input,membersOld);
	    	Log.v("prototypev1","users "+users);
	        if(users != null)
	        	return true;
	        return false;
	    }
	    /*
	     * method to update the view 
	     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	     */
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
	    	else
	    		mTextViewZeroMatches.setVisibility(View.VISIBLE);
	    }
	    /*
	     * method that execute if the thread is cancel
	     * @see android.os.AsyncTask#onCancelled()
	     */
		@Override
		protected void onCancelled() {
			super.onCancelled();
			mProgressBar.setVisibility(View.INVISIBLE);
			Toast.makeText(getApplicationContext(),"Error download following",  Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * get method that return new members of album 
	 * @param membersOld list of old members of album
	 * @param members list of new members of album
	 * @return
	 */
	public ArrayList<String> getNewsMembers(ArrayList<String> membersOld,ArrayList<String> members) {
		for(int i = 0; i < membersOld.size(); i++) {
			members.remove(membersOld.get(i));
		}
		return members;
	}
	/**
	 * class that adding members in album
	 * @author jordi
	 *
	 */
	private class AddMembersTask extends AsyncTask<Void, Void, Boolean> { 
		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        //this method will be running on UI thread
	        //mListViewFollowing.setVisibility(View.INVISIBLE);
	        //mProgressBar.setVisibility(View.VISIBLE);
	    }
		/*
		 * method adding members in album
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
	    @Override
	    protected Boolean doInBackground(Void... params) {
	    	return controller.addAlbumMembersFromSettings(onlyNewMembers, idAlbum);
	    }
	    /*
	     * (non-Javadoc)
	     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	     */
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
	    /*
	     * method that execute if the thread is cancel
	     * @see android.os.AsyncTask#onCancelled()
	     */
		@Override
		protected void onCancelled() {
			super.onCancelled();
			mProgressBar.setVisibility(View.INVISIBLE);
			Toast.makeText(getApplicationContext(),"Error search people",  Toast.LENGTH_LONG).show();
		}
	}
}
