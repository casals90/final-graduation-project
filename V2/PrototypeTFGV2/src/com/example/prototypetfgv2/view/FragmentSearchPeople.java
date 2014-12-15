package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.app.ActionBar;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.User;

public class FragmentSearchPeople extends Fragment {

	private EditText mEditTextSearch;
	private ListView mListView;
	private ProgressBar mProgressBar;
	private Controller controller;
	private ArrayList<User> users;
	private SearchTask searchTask;
	private GoToProfileUserInterface goToProfileUserInterface;
	
	public FragmentSearchPeople() {
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
		//Change action bar title
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		getActivity().getActionBar().setTitle(R.string.search_people_lower);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				closeKeyboard();
				getFragmentManager().popBackStack();
				//goToFindFriends();
		        return true;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search_people,container,false);
		
		mEditTextSearch = (EditText) view.findViewById(R.id.editTextSearch);
		mEditTextSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Log.v("prototypev1","onchange "+s);
				
				if(searchTask == null) {
					searchTask = new SearchTask();
					searchTask.execute(s.toString());
				}
				else {
					searchTask.cancel(true);
					searchTask = new SearchTask();
					searchTask.execute(s.toString());
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});
		
		
		mListView = (ListView) view.findViewById(R.id.listview_search);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		//TODO clear when click right
		//mEditTextSearch.setOnClickListener();
		
		return view;
	}
	
	public void closeKeyboard() {
		InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditTextSearch.getWindowToken(), 
        InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}
	
	private class SearchTask extends AsyncTask<String, Void, Boolean> {
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        //this method will be running on UI thread
	        mProgressBar.setVisibility(View.VISIBLE);
	    }
	    @Override
	    protected Boolean doInBackground(String... params) {
	    	String input = params[0];
	    	users = controller.downloadUsersInputSearch(input);
	    	Log.v("prototypev1","users size "+users.size());
	    	if(users != null)
	    		return true;
	    	return false;
	    }

	    @Override
	    protected void onPostExecute(Boolean result) {
	        super.onPostExecute(result);
	        //this method will be running on UI thread
	        mProgressBar.setVisibility(View.INVISIBLE);
	        if(result) {
	        	mListView.setAdapter(new AdapterForSearchPeople(getActivity().getApplicationContext(),users));
	        	mListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						goToUserProfile(users.get(position));
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
}
