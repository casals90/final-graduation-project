package com.example.prototypetfgv2.view;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.model.User;

public class FragmentFriends extends Fragment implements GoToProfileUserInterface {
	
	private ActionBar mActionBar;
    
    private FragmentFollowersForTab fragmentFollowers;
    private FragmentFollowingForTab fragmentFollowing;
    private GoToProfileUserInterface goToProfileUserInterface;
    
    private boolean fromProfile;
    private int selectedTab;
    
	public FragmentFriends() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.friends);
		
		fromProfile = false;
		selectedTab = 0;
		
		Bundle data = this.getArguments();
		if(data != null) {
			fromProfile = data.getBoolean("fromProfile",false);
			selectedTab = data.getInt("initTab",0);
		}
		Log.v("prototypev1","tab init "+selectedTab);
		
		mActionBar = getActivity().getActionBar();
		goToProfileUserInterface = this;
		
		//For show menu in action bar
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//Change action bar title
		getActivity().getActionBar().setTitle(R.string.friends);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_friends,container,false);
		
		clearTabs();
		
		fragmentFollowers = new FragmentFollowersForTab(goToProfileUserInterface);
		fragmentFollowing = new FragmentFollowingForTab(goToProfileUserInterface);
		initTabs();
		
		if(fromProfile)
			//For show menu in action bar
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		else 
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
		
		return view;
	}
	
	public void clearTabs() {
		mActionBar.removeAllTabs();
	}
	
	public void initTabs() {
		Log.v("prototypev1","tab init initTabs");
		//mActionBar.setHomeButtonEnabled(false);
	    mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
	    if(mActionBar.getTabCount() < 2) {
	    	Tab tFollowers = mActionBar.newTab().setText(R.string.followers).setTabListener(new TabListener(fragmentFollowers));
		    Tab tFollowing = mActionBar.newTab().setText(R.string.following).setTabListener(new TabListener(fragmentFollowing));
		    
		    switch (selectedTab) {
				case 0:
					 mActionBar.addTab(tFollowers,true);
					 mActionBar.addTab(tFollowing,false);
					break;
				case 1:
					 mActionBar.addTab(tFollowers,false);
					 mActionBar.addTab(tFollowing,true);
					break;
	
				default:
					break;
			    }
	    }
	    
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
		if(fromProfile)
			inflater.inflate(R.menu.no_menu, menu);
		else
			inflater.inflate(R.menu.menu_friends, menu);
		//inflater.inflate(R.menu.menu_friends, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.find_friends:
				goToFindFriends();
				break;
			case R.id.find_people:
				goToUserSearch();
				break;
			case android.R.id.home:
				goToProfile();
		        break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void goToFindFriends() {
		android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container_fragment_main,new FragmentFindFriends());
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
	public void goToProfile() {
		android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container_fragment_main,new FragmentProfile());
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
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
	
	public void goToUserSearch() {
		android.support.v4.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container_fragment_main,new FragmentSearchPeople());
		transaction.addToBackStack(null);
		transaction.commit();	
	}

	@Override
	public void goToProfileUser(User user) {
		goToUserProfile(user);		
	}
}
