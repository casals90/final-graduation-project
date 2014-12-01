package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Album;
import com.example.prototypetfgv2.model.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FragmentProfileOtherUser extends Fragment {
	
	private final int VISIBLE = View.VISIBLE;
	private final int INVISIBLE = View.INVISIBLE;
	
	private ImageView profilePicture;
	private TextView username, photosNumber,albumsNumber,friendsNumber,commonAlbumsLabel;
	private Button bAddFriend,bDeleteFriend;
	private ProgressBar mProgressBar,mProgressBarDownloadCommonAlbums;
	private ListView listCommonAlbums;
	
	private User user;
	private ArrayList<Album> commonAlbums;
	private ImageLoader imageLoader;
	private Controller controller;
	
	public FragmentProfileOtherUser() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		imageLoader = ImageLoader.getInstance();
		controller = (Controller) this.getActivity().getApplicationContext();
		
		Bundle data = this.getArguments();
		user = data.getParcelable("User");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_profile_other_user,container,false);
		
		username = (TextView) view.findViewById(R.id.username);
		username.setText(user.getUsername());
		
		commonAlbumsLabel = (TextView) view.findViewById(R.id.common_albums);
		
		photosNumber = (TextView) view.findViewById(R.id.photos_number);
		photosNumber.setText(String.valueOf(user.getPhotosNumber()));
		albumsNumber = (TextView) view.findViewById(R.id.albums_number);
		albumsNumber.setText(String.valueOf(controller.getAlbumsNumber()));
		friendsNumber = (TextView) view.findViewById(R.id.friends_number);
		friendsNumber.setText(String.valueOf(controller.getFriendsNumber()));
		
		//profile picture
		profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
		
		if(user.getProfilePicture() != null)
			//imageLoader.DisplayImage(user.getProfilePicture(),profilePicture);/
			imageLoader.displayImage(user.getProfilePicture(),profilePicture);
		
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarOtherProfile);
		mProgressBarDownloadCommonAlbums = (ProgressBar) view.findViewById(R.id.progressBarCommonAlbums);
		
		listCommonAlbums = (ListView) view.findViewById(R.id.list_albums);
		
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
			new DownloadCommonAlbumsTask().execute();
		}
		else {
			//no is in friends list
			bAddFriend.setVisibility(VISIBLE);
			bDeleteFriend.setVisibility(INVISIBLE);
			commonAlbumsLabel.setText(R.string.private_albums);
		}
		
		return view;
	}
	
	private class DownloadCommonAlbumsTask extends AsyncTask<Void, Void, Boolean> {
		
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
            mProgressBarDownloadCommonAlbums.setVisibility(View.VISIBLE);
            listCommonAlbums.setVisibility(View.INVISIBLE);
        }
 
        @Override
        protected Boolean doInBackground(Void... params) {
        	commonAlbums = controller.downloadCommonAlbums(user.getId());
        	if(commonAlbums != null)
        		return true;
        	return false;
        }

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result) {
				mProgressBarDownloadCommonAlbums.setVisibility(View.INVISIBLE);
	            listCommonAlbums.setVisibility(View.VISIBLE);
	            listCommonAlbums.setAdapter(new ListViewAlbumsAdapter(commonAlbums,getActivity().getApplicationContext()));
	            listCommonAlbums.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						goToShowAlbumListMode(commonAlbums.get(position));
					}
				});
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			mProgressBarDownloadCommonAlbums.setVisibility(View.INVISIBLE);
			Toast.makeText(getActivity(),"Error download common albums",  Toast.LENGTH_LONG).show();
		}	
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
	
	public void goToShowAlbumListMode(Album album) {
		Bundle data = new Bundle();
		data.putParcelable("Album",album);
		ListViewPhotosFragment listViewPhotos = new ListViewPhotosFragment();
		listViewPhotos.setArguments(data);
		
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container_fragment_main,listViewPhotos);
		transaction.addToBackStack(null);
		transaction.commit();	
	}

}
