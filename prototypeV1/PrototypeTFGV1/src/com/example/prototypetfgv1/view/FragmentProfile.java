package com.example.prototypetfgv1.view;

import org.json.JSONException;

import com.example.prototypetfgv1.R;
import com.example.prototypetfgv1.controller.ApplicationClass;
import com.example.prototypetfgv1.model.User;
import com.parse.ParseUser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentProfile extends Fragment {
	
	private TextView tv;
	private ApplicationClass app;

	public FragmentProfile() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		app = (ApplicationClass)this.getActivity().getApplicationContext();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_profile,container,false);
		
		tv = (TextView)view.findViewById(R.id.textView1);
		User u = app.getUser();
		
		String info = "id = "+u.getId()+" username "+u.getUserName()+" pass "+u.getPassword()+" update "+u.getUpdatedAt()+ " id photos ";
		for(int i = 0; i < u.getPhotos().length();i++) {
			try {
				info += u.getPhotos().get(i);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String d = String.valueOf(ParseUser.getCurrentUser().getUpdatedAt());
		info += " i el update de l'usuari parse es "+d;
		
		tv.setText(info);
		return view;
	}

}
