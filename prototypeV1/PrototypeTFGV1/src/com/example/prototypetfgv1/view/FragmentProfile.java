package com.example.prototypetfgv1.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.prototypetfgv1.R;
import com.example.prototypetfgv1.controller.Controller;

public class FragmentProfile extends Fragment {
	
	//private TextView tv;
	private Button buttonLogOut;
	private Controller controller;

	public FragmentProfile() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		controller = new Controller(this.getActivity().getApplicationContext());
		
		//Log.v("prototypev1"," num fotos "+controller.getUser().getPhotos().length());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_profile,container,false);
		
		buttonLogOut = (Button) view.findViewById(R.id.button_logout);
		buttonLogOut.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//ParseUser.getCurrentUser();
				// TODO Auto-generated method stub
				logout();
			}
		});
		return view;
	}
	
	public void logout() {
		controller.logout();
		Intent loginScreen = new Intent(getActivity(), LoginActivity.class);
		Utils.cleanBackStack(loginScreen);
		startActivity(loginScreen);
	}
}
