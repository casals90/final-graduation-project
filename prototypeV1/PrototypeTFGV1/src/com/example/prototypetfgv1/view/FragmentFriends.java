package com.example.prototypetfgv1.view;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.prototypetfgv1.R;
import com.example.prototypetfgv1.controller.Controller;
import com.example.prototypetfgv1.model.User;

public class FragmentFriends extends Fragment {
	
	private Controller controller;
	
	private EditText inputUsername;
	private TextView noResults;
	private ProgressBar progressBarSearch;
	
	private ListView listview;
	private ListViewAdapterForSearchUsers adapter;
    private List<User> users = null;
    
    private String input;
    
    private SearchTask searchTask;

	public FragmentFriends() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		controller = new Controller(getActivity().getApplicationContext());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_friends,container,false);
		
		noResults = (TextView) view.findViewById(R.id.no_results);
		listview = (ListView) view.findViewById(R.id.usersList);
		
		progressBarSearch = (ProgressBar) view.findViewById(R.id.progressBarSearch);
		
		inputUsername = (EditText) view.findViewById(R.id.inputName);
		//listener in edit text for text change
		inputUsername.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				input = s.toString();
				searchTask = null;
				searchTask = new SearchTask();
				searchTask.execute();
			}
		});
		return view;
	}
	
	public void updateListView(List<User> users) {
		adapter = new ListViewAdapterForSearchUsers(getActivity(),this.users);
        // Binds the Adapter to the ListView
        listview.setAdapter(adapter);
		if(this.users.size() >= 1) {
			noResults.setVisibility(View.INVISIBLE);
			listview.setVisibility(View.VISIBLE);
	        // Pass the results into listview
	        adapter = new ListViewAdapterForSearchUsers(getActivity(),this.users);
	        // Binds the Adapter to the ListView
	        listview.setAdapter(adapter);
		}
		else {
			noResults.setVisibility(View.VISIBLE);
			listview.setVisibility(View.INVISIBLE);
		}	
	}
	
	private class SearchTask extends AsyncTask<Void, Void, Void>
	{

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

	}
}
