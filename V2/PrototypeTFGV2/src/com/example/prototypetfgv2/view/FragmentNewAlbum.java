package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;


public class FragmentNewAlbum extends Fragment {

	private Controller controller;
	private ImageButton add;
	
	private ArrayList<String> members;
	
	public FragmentNewAlbum() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().setTitle(R.string.friends);
		
		Log.v("prototypev1", "FragmentNewAlbum ");
		
		controller = (Controller) getActivity().getApplicationContext();
		
		Bundle b = this.getArguments();
		if(b != null) {
			members = b.getStringArrayList("members");
			if(members == null && members.size() <= 0)
				members = new ArrayList<String>();
			Log.v("prototypev1", "members != null and size "+members.size());
		}
		else {
			members = new ArrayList<String>();
			Log.v("prototypev1", "members == null");
		}
		
		//For show menu in action bar
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_new_album,container,false);
		
		add = (ImageButton) view.findViewById(R.id.add_members);
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//go to add members
				goToAddUsersNewAlbum();
			}
		});
		
		return view;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
		// TODO Auto-generated method stub
		inflater.inflate(R.menu.menu_new_album, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
			case R.id.create_album:
				//create album in parse
				
				//go to albums
				goToAddUsersNewAlbum();
				break;
				
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void goToAddUsersNewAlbum() {
		FragmentManager manager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		FragmentAddUsersNewAlbum addUsers = new FragmentAddUsersNewAlbum();
		Bundle b = new Bundle();
		Log.v("prototypev1", "members size before put "+members.size());
		Log.v("prototypev1", "------------------------------------------");
		b.putStringArrayList("members",members);
		addUsers.setArguments(b);
		transaction.replace(R.id.container_fragment_main,new FragmentAddUsersNewAlbum());
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
	public void createAlbum() {
		
		/*if(adapter != null) {
			albumName = inputAlbumName.getText().toString();
			//revisar
			if(albumName.length() > 0) {
				members = adapter.getMembers();
				// update in parse and create album
				if(members != null && members.length() > 0) {
					controller.newAlbum(members,albumName);
					//go to albums
					goToAlbums();
				}
				else
					//comunicate error with toast
					Toast.makeText(getActivity(),"Minimum adding one friend",  Toast.LENGTH_LONG).show();
			}
			else
				Toast.makeText(getActivity(),"Input album name",  Toast.LENGTH_LONG).show();		
		}*/
	}

}
