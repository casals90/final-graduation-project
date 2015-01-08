package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.User;

public class FragmentFollowersForTab extends Fragment {

	private ListView mListViewFollowers;
	private ProgressBar mProgressBar;
	private ArrayList<User> followers;
	private Controller controller;
	private GoToProfileUserInterface goToProfileUserInterface;
	
	public FragmentFollowersForTab(GoToProfileUserInterface goToProfileUserInterface) {
		super();
		this.goToProfileUserInterface = goToProfileUserInterface;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_followers, container, false);
        
        controller = (Controller) getActivity().getApplication();
        
        mListViewFollowers = (ListView) rootView.findViewById(R.id.list_followers);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        
        new GetFollowersTask().execute();
        
        return rootView;
    }

	private class GetFollowersTask extends AsyncTask<Void, Void, Boolean> {
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        //this method will be running on UI thread
	        mProgressBar.setVisibility(View.VISIBLE);
	    }
	    @Override
	    protected Boolean doInBackground(Void... params) {
	    	followers = controller.getFollowers();
	    	if(followers != null) {
	    		if(followers.size() > 0)
	    			return true;
	    	}
	    	return false;
	    }

	    @Override
	    protected void onPostExecute(Boolean result) {
	        super.onPostExecute(result);
	        //this method will be running on UI thread
	        mProgressBar.setVisibility(View.INVISIBLE);
	        if(result) {
	        	mListViewFollowers.setAdapter(new AdapterForFollowers(followers, getActivity().getApplicationContext()));
	        	mListViewFollowers.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						goToProfileUserInterface.goToProfileUser(followers.get(position));
					}
	        		
				});
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
