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
/**
 * Class that allow user find friends from recommendation
 * @author jordi
 *
 */
public class FragmentFindFriends extends Fragment {

	private ArrayList<User> recommended;
	private Controller controller;
	
	private Button mButtonFollowAll,mButtonFollowingAll;
	private ListView mListView;
	private ProgressBar mProgressBar;
	private AdapterForRecommenderUsers adapter;
	/**
	 * Constructor of Fragment
	 */
	public FragmentFindFriends() {
		super();
	}
	/*
	 * Method that init params
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		
		controller = (Controller) getActivity().getApplication();
		
		//For show menu in action bar
		setHasOptionsMenu(true);
	}
	/*
	 * Method that update the view
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		getActivity().getActionBar().setTitle(R.string.find_friends);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
		new DownloadRecommendedUsersTask().execute();
	}
	/*
	 * Method that init the view
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
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
	/*
	 * method to create menu with specific options
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
		inflater.inflate(R.menu.menu_find_friends, menu);
	}
	/*
	 * method that specify what to do when user click menu option
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
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
	/**
	 * Class that download users from recommendation
	 * @author jordi
	 *
	 */
	private class DownloadRecommendedUsersTask extends AsyncTask<Void, Void, Boolean> {
	    /*
	     * Method that init view
	     * @see android.os.AsyncTask#onPreExecute()
	     */
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        //this method will be running on UI thread
	        mProgressBar.setVisibility(View.VISIBLE);
	    }
		/*
		 * Method that download recommender users
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
	    @Override
	    protected Boolean doInBackground(Void... params) {
	    	recommended = controller.getRecommendedUsers();
	    	if(recommended != null)
	    		return true;
	    	return false;
	    }
	    /*
	     * Method that update view
	     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	     */
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
	    /*
	     * (non-Javadoc)
	     * @see android.os.AsyncTask#onCancelled()
	     */
		@Override
		protected void onCancelled() {
			super.onCancelled();
			mProgressBar.setVisibility(View.INVISIBLE);
			Toast.makeText(getActivity(),"Error download followers",  Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * Class that user allow all users from recommendation
	 * @author jordi
	 *
	 */
	private class FollowingAllTask extends AsyncTask<Void, Void, Boolean> {
	    /*
	     * (non-Javadoc)
	     * @see android.os.AsyncTask#onPreExecute()
	     */
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        //this method will be running on UI thread
	    }
		/*
		 * Method that adding all users
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
	    @Override
	    protected Boolean doInBackground(Void... params) {
	    	return controller.addFollowingAll(getIdFromAllRecommended(recommended));
	    }
	    /*
	     * Method that update view
	     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	     */
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
	    /*
	     * (non-Javadoc)
	     * @see android.os.AsyncTask#onCancelled()
	     */
		@Override
		protected void onCancelled() {
			super.onCancelled();
			mProgressBar.setVisibility(View.INVISIBLE);
			Toast.makeText(getActivity(),"Error download followers",  Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * method to chnage current Fragment for FragmentFriends
	 */
	public void goToFriends() {
		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.container_fragment_main,new FragmentFriends());
		transaction.commit();
	}
	/**
	 * method to chnage current Fragment for ProfileOtherUser
	 */
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
	/**
	 * Method that adding all users recommender id
	 * @param recommended
	 * @return
	 */
	public ArrayList<String> getIdFromAllRecommended(ArrayList<User> recommended) {
		ArrayList<String> idRecommended = new ArrayList<String>();
		for(int i = 0; i < recommended.size(); i++) {
			idRecommended.add(recommended.get(i).getId());
		}
		return idRecommended;
	}
}
