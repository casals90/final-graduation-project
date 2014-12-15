package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

public class FragmentFollowingForTab extends Fragment {

	private ListView mListViewFollowing;
	private ProgressBar mProgressBar;
	private ArrayList<User> following;
	//ArrayList to save users that I delete but now shows
	private ArrayList<User> allFollowing;
	private Controller controller;
	private GoToProfileUserInterface goToProfileUserInterface;
	
	public FragmentFollowingForTab(GoToProfileUserInterface goToProfileUserInterface) {
		super();
		this.goToProfileUserInterface = goToProfileUserInterface;
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
	    	allFollowing = (ArrayList<User>) following.clone();
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
	        	mListViewFollowing.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						//Revisar bug que quant deixo de seguir un usuari al borrar-lo de la llista peta, quant vui tornar al seu perfil
						Log.v("prototypev1","username to show profile"+allFollowing.get(position).getUsername());
						Log.v("prototypev1","followingdelete size "+allFollowing.size()+"following size "+following.size());
						goToProfileUserInterface.goToProfileUser(allFollowing.get(position));
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
