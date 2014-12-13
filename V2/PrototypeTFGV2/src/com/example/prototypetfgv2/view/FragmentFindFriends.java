package com.example.prototypetfgv2.view;
import java.util.ArrayList;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.User;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


public class FragmentFindFriends extends Fragment {

	private ArrayList<User> recommended;
	private ListView mListView;
	private ProgressBar mProgressBar;
	private Controller controller;
	
	public FragmentFindFriends() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		
		controller = (Controller) getActivity().getApplication();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		getActivity().getActionBar().setTitle(R.string.find_friends);
		
		new DownloadRecommendedUsersTask().execute();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_find_friends,container,false);
		
		mListView = (ListView) view.findViewById(R.id.list_recommender);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		
		return view;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				getFragmentManager().popBackStack();
				return true;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class DownloadRecommendedUsersTask extends AsyncTask<Void, Void, Boolean> {
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        //this method will be running on UI thread
	        mProgressBar.setVisibility(View.VISIBLE);
	    }
	    @Override
	    protected Boolean doInBackground(Void... params) {
	    	recommended = controller.getRecommendedUsers();
	    	if(recommended != null)
	    		return true;
	    	return false;
	    }

	    @Override
	    protected void onPostExecute(Boolean result) {
	        super.onPostExecute(result);
	        //this method will be running on UI thread
	        mProgressBar.setVisibility(View.INVISIBLE);
	        if(result) {
	        	//mListView.setAdapter();
	        }
	    }
	    
		@Override
		protected void onCancelled() {
			super.onCancelled();
			mProgressBar.setVisibility(View.INVISIBLE);
			Toast.makeText(getActivity(),"Error download followers",  Toast.LENGTH_LONG).show();
		}
	}
}
