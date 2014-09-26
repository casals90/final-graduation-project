package com.example.prototypetfgv1.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prototypetfgv1.R;
import com.example.prototypetfgv1.controller.Controller;
import com.example.prototypetfgv1.model.User;

public class FragmentProfileOtherUser extends Fragment {
	
	private ImageView profilePicture;
	private TextView username;
	private Button bAddFriend,bDeleteFriend;
	
	private User user;

	private ImageLoader imageLoader;
	
	private Controller controller;
	
	public FragmentProfileOtherUser() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Context context = getActivity().getApplicationContext();
		
		imageLoader = new ImageLoader(context);
		controller = new Controller(context);
		
		Bundle data = this.getArguments();
		user = data.getParcelable("User");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_profile_other_user,container,false);
		
		profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
		username = (TextView) view.findViewById(R.id.username);
		
		imageLoader.DisplayImage(user.getProfilePicture(),profilePicture);
		username.setText(user.getUsername());
		
		bAddFriend = (Button) view.findViewById(R.id.button_add_friend);
		bAddFriend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//add in friend list
				new AddFriendTask().execute();
				
				bAddFriend.setVisibility(View.INVISIBLE);
				bDeleteFriend.setVisibility(View.VISIBLE);
			}
		
		});
		
		bDeleteFriend = (Button) view.findViewById(R.id.button_delete_friend);
		bDeleteFriend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Delete from friend list
				new DeleteFriendTask().execute();
				
				bAddFriend.setVisibility(View.VISIBLE);
				bDeleteFriend.setVisibility(View.INVISIBLE);
			}
		
		});
		
		if(controller.isMyFriend(user.getId())) {
			//is in friend list
			bAddFriend.setVisibility(View.INVISIBLE);
			bDeleteFriend.setVisibility(View.VISIBLE);
			Log.v("prototypev1", "is my friend");
		}
		else {
			//no is in friends list
			bAddFriend.setVisibility(View.VISIBLE);
			bDeleteFriend.setVisibility(View.INVISIBLE);
			Log.v("prototypev1", "NO is my friend");
		}		
		return view;
	}
	
	private class DeleteFriendTask extends AsyncTask<Void, Void, Void> {
    	ProgressDialog mProgressDialog;
		
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("Delete friend");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }
 
        @Override
        protected Void doInBackground(Void... params) {
        	controller.deleteFriend(user.getId());
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }
	
	private class AddFriendTask extends AsyncTask<Void, Void, Void> {
    	ProgressDialog mProgressDialog;
		
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(getActivity());
            // Set progressdialog title
            mProgressDialog.setTitle("Add friend");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }
 
        @Override
        protected Void doInBackground(Void... params) {
        	controller.addFriend(user.getId());
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
            // Close the progressdialog
            mProgressDialog.dismiss();
        }
    }

}
