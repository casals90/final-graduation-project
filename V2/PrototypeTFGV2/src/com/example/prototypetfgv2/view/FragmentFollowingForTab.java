package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.User;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class FragmentFollowingForTab extends Fragment {

	private ListView mListViewFollowing;
	private ProgressBar mProgressBar;
	private ArrayList<User> following;
	private Controller controller;
	//private GetFollowingTask getFollowingTask
	
	public FragmentFollowingForTab() {
		super();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_following, container, false);
        
        controller = (Controller) getActivity().getApplication();
        
        mListViewFollowing = (ListView) rootView.findViewById(R.id.list_following);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        
        new GetFollowingTask().execute();
        
        return rootView;
    }

	private class GetFollowingTask extends AsyncTask<Void, Void, Boolean> {
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        //this method will be running on UI thread
	        mProgressBar.setVisibility(View.VISIBLE);
	       // mListViewFriends.setAdapter(null);
	        
	    }
	    @Override
	    protected Boolean doInBackground(Void... params) {
	    	following = controller.getFollowing();
	    	if(following != null)
	    		return true;
	    	return false;
	    }

	    @Override
	    protected void onPostExecute(Boolean result) {
	        super.onPostExecute(result);
	        //this method will be running on UI thread
	        mProgressBar.setVisibility(View.INVISIBLE);
	        if(result) {
	        	mListViewFollowing.setAdapter(new AdapterForFollowing(following, getActivity().getApplicationContext()));
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
