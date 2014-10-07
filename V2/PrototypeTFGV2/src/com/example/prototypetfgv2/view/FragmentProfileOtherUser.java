package com.example.prototypetfgv2.view;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.User;

public class FragmentProfileOtherUser extends Fragment {
	
	private final int VISIBLE = View.VISIBLE;
	private final int INVISIBLE = View.INVISIBLE;
	
	private ImageView profilePicture;
	private TextView username;
	private Button bAddFriend,bDeleteFriend;
	private ProgressBar mProgressBar;
	
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
		
		if(user.getProfilePicture() != null)
			imageLoader.DisplayImage(user.getProfilePicture(),profilePicture);
		
		username.setText(user.getUsername());
		
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarOtherProfile);
		
		bAddFriend = (Button) view.findViewById(R.id.button_add_friend);
		bAddFriend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//add in friend list
				new AddFriendTask().execute();
				
				bAddFriend.setVisibility(INVISIBLE);
				bDeleteFriend.setVisibility(VISIBLE);
			}
		
		});
		
		bDeleteFriend = (Button) view.findViewById(R.id.button_delete_friend);
		bDeleteFriend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Delete from friend list
				new DeleteFriendTask().execute();
				
				bAddFriend.setVisibility(VISIBLE);
				bDeleteFriend.setVisibility(INVISIBLE);
			}
		
		});
		
		if(controller.isMyFriend(user.getId())) {
			//is in friend list
			bAddFriend.setVisibility(INVISIBLE);
			bDeleteFriend.setVisibility(VISIBLE);
		}
		else {
			//no is in friends list
			bAddFriend.setVisibility(VISIBLE);
			bDeleteFriend.setVisibility(INVISIBLE);
		}
		return view;
	}
	
	private class DeleteFriendTask extends AsyncTask<Void, Void, Boolean> {
		
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
            bDeleteFriend.setVisibility(INVISIBLE);
            mProgressBar.setVisibility(VISIBLE);
        }
 
        @Override
        protected Boolean doInBackground(Void... params) {
        	return controller.deleteFriend(user.getId());
        }

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result) {
				mProgressBar.setVisibility(INVISIBLE);
				bAddFriend.setVisibility(VISIBLE);
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			mProgressBar.setVisibility(INVISIBLE);
			bDeleteFriend.setVisibility(VISIBLE);
			Toast.makeText(getActivity(),"Error delete friend",  Toast.LENGTH_LONG).show();
		}	
    }
	
	private class AddFriendTask extends AsyncTask<Void, Void, Boolean> {
		
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
           bAddFriend.setVisibility(INVISIBLE);
           mProgressBar.setVisibility(VISIBLE);
        }
 
        @Override
        protected  Boolean doInBackground(Void... params) {
        	return controller.addFriend(user.getId());
        }

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result) {
				mProgressBar.setVisibility(INVISIBLE);
				bDeleteFriend.setVisibility(VISIBLE);
				
				//controller.getParseFunctions().sendPush(user.getId());
			}
		}
 
		@Override
		protected void onCancelled() {
			super.onCancelled();
			mProgressBar.setVisibility(INVISIBLE);
			bAddFriend.setVisibility(VISIBLE);
			Toast.makeText(getActivity(),"Error add friend",  Toast.LENGTH_LONG).show();
		}	
    }

}
