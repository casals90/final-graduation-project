package com.example.prototypetfgv2.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.CurrentUser;

public class FragmentNews extends Fragment {

	private Controller controller;
	
	public FragmentNews() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.news);
		
		controller = (Controller) getActivity().getApplication();
		
		/*CurrentUser u = controller.downloadCurrentUser();
		Log.v("prototypev1","download current user complete");
		
		controller.setCurrentUser(u);
		Log.v("prototypev1","set controller");
		
		CurrentUser u2 = controller.getCurrentUser();
		
		//controller.setCurrentUser(u);
		Log.v("prototypev1","current user get controller "+u2);*/
		//Log.v("prototypev1","current user "+u);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_news,container,false);
		
		return view;
	}

}
