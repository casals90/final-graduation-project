package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.User;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class FragmentListFriends extends Fragment {

	private ListView mListView;
	private ProgressBar mProgressBar;
	
	private Controller controller;
	private ListviewFriendsAdapter adapter;
	
	public FragmentListFriends() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.friends);	
		
		controller = (Controller) getActivity().getApplicationContext();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_listview_friends,container,false);
		
		mListView = (ListView) view.findViewById(R.id.listview_friends);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		
		new DownloadFriendsTask().execute();
		return view;
	}

	
	public class DownloadFriendsTask extends AsyncTask<Void, Void, ArrayList<User>> {
		
		@Override
	    protected void onPreExecute() {
			mProgressBar.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.INVISIBLE);      
	    };
		
		@Override
		protected ArrayList<User> doInBackground(Void... params) {
			return controller.downloadFriends();
		}

		@Override
		protected void onPostExecute(final ArrayList<User> friends) {
			mProgressBar.setVisibility(View.INVISIBLE);
			if(friends != null) {
				adapter = new ListviewFriendsAdapter(getActivity().getApplicationContext(), friends);
				mListView.setVisibility(View.VISIBLE);
				mListView.setAdapter(adapter);
			}
			else {
				//TODO textview 0 friends
			}
			
		}

		@Override
		protected void onCancelled() {
			mProgressBar.setVisibility(View.INVISIBLE);
			//profilePicture.setVisibility(View.VISIBLE);
			Log.v("prototypev1","download friends cancelat");
			Toast.makeText(getActivity(),"Error download friends",Toast.LENGTH_LONG).show();
		}
	}
}
