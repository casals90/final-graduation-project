package com.example.prototypetfgv2.view;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.User;

public class FragmentFriends extends Fragment {
	
	private Controller controller;
	
	private EditText inputUsername;
	private TextView noResults;
	private ProgressBar progressBarSearch;
	
	private ListView listview;
	private ListViewAdapterForSearchUsers adapter;
    private ArrayList<User> users;
    
    private String input;
    
    private SearchTask searchTask;
    
    private FragmentTransaction transaction;

	public FragmentFriends() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		controller = (Controller) this.getActivity().getApplication();
		getActivity().setTitle(R.string.friends);
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
		
		noResults = (TextView) view.findViewById(R.id.no_results);
		listview = (ListView) view.findViewById(R.id.usersList);
		
		progressBarSearch = (ProgressBar) view.findViewById(R.id.progressBarSearch);
		
		inputUsername = (EditText) view.findViewById(R.id.inputName);
		//listener in edit when text change
		inputUsername.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				input = s.toString();
				if(input.length() > 0) {
					searchTask = null;
					searchTask = new SearchTask();
					searchTask.execute();
				}
				else {
					clearListView();
					showNoResults(false);
				}
			}
		});
		return view;
	}
	
	public void updateListView(final List<User> users) {
		adapter = new ListViewAdapterForSearchUsers(getActivity(),this.users);
        // Binds the Adapter to the ListView
        listview.setAdapter(adapter);
		if(this.users.size() >= 1) {
			showNoResults(false);
	        adapter = new ListViewAdapterForSearchUsers(getActivity(),this.users);
	        listview.setAdapter(adapter);
		}
		else {
			noResults.setVisibility(View.VISIBLE);
			showNoResults(true);
			clearListView();
		}	
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
				User user = users.get(position);
				//pass the user to new fragment and change fragment
				goToUserProfile(user);
			}
		});
	}
	
	public void clearListView() {
		listview.setAdapter(null);
	}
	
	public void showNoResults(boolean show) {
		if(show)
			noResults.setVisibility(View.VISIBLE);
		else
			noResults.setVisibility(View.INVISIBLE);
	}
	
	public void goToUserProfile(User user) {
		Bundle data = new Bundle();
		data.putParcelable("User",user);
		FragmentProfileOtherUser fpou = new FragmentProfileOtherUser();
		fpou.setArguments(data);
		
		transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container_fragment_main,fpou);
		transaction.addToBackStack(null);
		transaction.commit();	
	}
	
	private class SearchTask extends AsyncTask<Void, Void, Void> {
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        //this method will be running on UI thread
	        progressBarSearch.setVisibility(View.VISIBLE);
	    }
	    @Override
	    protected Void doInBackground(Void... params) {
	    	users = controller.getUsers(input);
	        return null;
	    }

	    @Override
	    protected void onPostExecute(Void result) {
	        super.onPostExecute(result);
	        //this method will be running on UI thread
	        progressBarSearch.setVisibility(View.INVISIBLE);
	        updateListView(users);     
	    }
	    
		@Override
		protected void onCancelled() {
			super.onCancelled();
			progressBarSearch.setVisibility(View.INVISIBLE);
			Toast.makeText(getActivity(),"Error search people",  Toast.LENGTH_LONG).show();
		}
	}
}
