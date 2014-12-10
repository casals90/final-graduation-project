package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.User;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentListFriends extends Fragment {

	private ListView mListView;
	private ProgressBar mProgressBar;
	private TextView mTextView;
	private Controller controller;
	private ListviewFriendsAdapter adapter;
	
	public FragmentListFriends() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		controller = (Controller) getActivity().getApplicationContext();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getActivity().setTitle(R.string.friends);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_listview_friends,container,false);
		
		//getActivity().setTitle(R.string.friends);
		
		mListView = (ListView) view.findViewById(R.id.listview_friends);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		mTextView = (TextView) view.findViewById(R.id.no_friends);
		
		new DownloadFriendsTask().execute();
		return view;
	}

	
	public class DownloadFriendsTask extends AsyncTask<Void, Void, ArrayList<User>> {
		
		@Override
	    protected void onPreExecute() {
			mProgressBar.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.INVISIBLE); 
			mTextView.setVisibility(View.INVISIBLE);
	    };
		
		@Override
		protected ArrayList<User> doInBackground(Void... params) {
			return controller.downloadFriends();
		}

		@Override
		protected void onPostExecute(final ArrayList<User> friends) {
			mProgressBar.setVisibility(View.INVISIBLE);
			if(friends != null && friends.size() > 0) {
				adapter = new ListviewFriendsAdapter(getActivity().getApplicationContext(), friends);
				mListView.setVisibility(View.VISIBLE);
				mListView.setAdapter(adapter);
				mListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						goToUserProfile(friends.get(position));
					}
				});
			}
			else 
				mTextView.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onCancelled() {
			mProgressBar.setVisibility(View.INVISIBLE);
			//profilePicture.setVisibility(View.VISIBLE);
			Log.v("prototypev1","download friends cancelat");
			Toast.makeText(getActivity(),"Error download friends",Toast.LENGTH_LONG).show();
		}
	}
	
	public void goToUserProfile(User user) {
		Bundle data = new Bundle();
		data.putParcelable("User",user);
		FragmentProfileOtherUser fpou = new FragmentProfileOtherUser();
		fpou.setArguments(data);
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container_fragment_main,fpou);
		transaction.addToBackStack(null);
		transaction.commit();	
	}
}
