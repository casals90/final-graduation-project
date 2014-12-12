package com.example.prototypetfgv2.view;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prototypetfgv2.R;

public class FragmentFriends extends Fragment {
	
	//private Controller controller;
	
	private ActionBar mActionBar;
    
    
    private FragmentFollowersForTab fragmentFollowers;
    private FragmentFollowingForTab fragmentFollowing;
    //private GetFollowersTask getFollowersTask;
   // private GetFollowingTask getFollowingTask;
    
	public FragmentFriends() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//controller = (Controller) this.getActivity().getApplication();
		getActivity().setTitle(R.string.friends);
		
		mActionBar = getActivity().getActionBar();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//Change action bar title
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
		getActivity().getActionBar().setTitle(R.string.friends);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_friends,container,false);
		
		fragmentFollowers = new FragmentFollowersForTab();
		fragmentFollowing = new FragmentFollowingForTab();
		initTabs();
	    //mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarFriends);
	   
	    //mListViewFriends = (ListView) view.findViewById(R.id.list_friends);
		return view;
	}
	
	public void initTabs() {
		mActionBar.setHomeButtonEnabled(false);
	    mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
	    if(mActionBar.getTabCount() < 2) {
	    	Tab tFollowers = mActionBar.newTab().setText(R.string.followers).setTabListener(new TabListener(fragmentFollowers));
		    Tab tFollowing = mActionBar.newTab().setText(R.string.following).setTabListener(new TabListener(fragmentFollowing));
		    mActionBar.addTab(tFollowers);
		    mActionBar.addTab(tFollowing);
			//End configure tabs
	    } 
	}
	
	/*public void goToUserProfile(User user) {
		Bundle data = new Bundle();
		data.putParcelable("User",user);
		FragmentProfileOtherUser fpou = new FragmentProfileOtherUser();
		fpou.setArguments(data);
		
		transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container_fragment_main,fpou);
		transaction.addToBackStack(null);
		transaction.commit();	
	}*
	
	/*

	@Override
	public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
		int tabPosition = tab.getPosition();
		switch (tabPosition) {
			case 0:
				if(getFollowingTask != null) 
					getFollowingTask.cancel(true);
				getFollowersTask = new GetFollowersTask();
				getFollowersTask.execute();
				break;
			
			case 1:
				if(getFollowersTask != null)
					getFollowersTask.cancel(true);
				getFollowingTask = new GetFollowingTask();
				getFollowingTask.execute();
				break;
		default:
			break;
		}
	}

	@Override
	public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
		int tabPosition = tab.getPosition();
		switch (tabPosition) {
			case 0:
				break;
			
			case 1:
				break;
		default:
			break;
		}
	}*/
	
	public class TabListener implements ActionBar.TabListener {
		 
		android.app.Fragment fragment;
	 
		public TabListener(android.app.Fragment fragment) {
			this.fragment = fragment;
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
						
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.replace(R.id.fragment_container, fragment);
		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			ft.remove(fragment);
		}
	}
}
