package com.example.prototypetfgv1.view;

import com.example.prototypetfgv1.R;
import com.example.prototypetfgv1.controller.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

public class FragmentFriends extends Fragment {
	
	private Controller controller;
	
	private ImageButton bSearch;
	private EditText inputUsername;
	
	private String username;

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
		
		inputUsername = (EditText) view.findViewById(R.id.inputName);
		
		bSearch = (ImageButton) view.findViewById(R.id.button_search);
		bSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				username = inputUsername.getText().toString();
				controller.getUsers(username);
			}
		});
		
		return view;
	}

}
