package com.example.prototypetfgv2.view;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    
	public FragmentFriends() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.friends);
		
		mActionBar = getActivity().getActionBar();
		goToProfileUserInterface = this;
		//For show menu in action bar
		setHasOptionsMenu(true);
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
		
		fragmentFollowers = new FragmentFollowersForTab(goToProfileUserInterface);
		fragmentFollowing = new FragmentFollowingForTab(goToProfileUserInterface);
		initTabs();
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
	
	@Override
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
		inflater.inflate(R.menu.menu_friends, menu);
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
