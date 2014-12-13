package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.User;

public class FragmentFindFriends extends Fragment {

	private ArrayList<User> recommended;
	private Controller controller;
	
	private Button mButtonFollowAll,mButtonFollowingAll;
	private ListView mListView;
	private ProgressBar mProgressBar;
	private AdapterForRecommenderUsers adapter;
	
	public FragmentFindFriends() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		
		controller = (Controller) getActivity().getApplication();
		
		//For show menu in action bar
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getActivity().getActionBar().setTitle(R.string.find_friends);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
		new DownloadRecommendedUsersTask().execute();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_find_friends,container,false);
		
		mButtonFollowAll = (Button) view.findViewById(R.id.button_follow_all);
		mButtonFollowingAll = (Button) view.findViewById(R.id.button_followingAll);
		mListView = (ListView) view.findViewById(R.id.list_recommender);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		
		return view;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
		inflater.inflate(R.menu.menu_find_friends, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				getFragmentManager().popBackStack();
				return true;
			case R.id.accept:
				goToFriends();
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
	        	adapter = new AdapterForRecommenderUsers(recommended, getActivity());
	        	mListView.setAdapter(adapter);
	        	mListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						goToUserProfile(recommended.get(position));
					}
				});
	        	mButtonFollowAll.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						new FollowingAllTask().execute();
						adapter.setFollowingAll(true);
						mButtonFollowAll.setOnClickListener(null);
						mButtonFollowAll.setVisibility(View.INVISIBLE);
						mButtonFollowingAll.setVisibility(View.VISIBLE);
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
	
	private class FollowingAllTask extends AsyncTask<Void, Void, Boolean> {
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        //this method will be running on UI thread
	    }
	    @Override
	    protected Boolean doInBackground(Void... params) {
	    	return controller.addFollowingAll(getIdFromAllRecommended(recommended));
	    }

	    @Override
	    protected void onPostExecute(Boolean result) {
	        super.onPostExecute(result);
	        //this method will be running on UI thread
	        if(result) {
	        	mListView.setAdapter(new AdapterForRecommenderUsers(recommended, getActivity()));
	        	mListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						goToUserProfile(recommended.get(position));
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
	
	public void goToFriends() {
		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.container_fragment_main,new FragmentFriends());
		transaction.commit();
	}
	
	public void goToUserProfile(User user) {
		Bundle data = new Bundle();
		data.putParcelable("User",user);
		FragmentProfileOtherUser fpou = new FragmentProfileOtherUser();
		fpou.setArguments(data);
		
		android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container_fragment_main,fpou);
		transaction.addToBackStack(null);
		transaction.commit();	
	}
	
	public ArrayList<String> getIdFromAllRecommended(ArrayList<User> recommended) {
		ArrayList<String> idRecommended = new ArrayList<String>();
		for(int i = 0; i < recommended.size(); i++) {
			idRecommended.add(recommended.get(i).getId());
		}
		return idRecommended;
	}
}
