package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	private TextView photosNumber,albumsNumber,friendsNumber,commonAlbumsLabel;
	private Button buttonFriend;
	private ProgressBar mProgressBar,mProgressBarDownloadCommonAlbums;
	private ListView listCommonAlbums;
	private boolean followingThisUser;
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
		
		//this.isMyFriend = controller.isMyFriend(user.getId());
		this.followingThisUser = controller.getCurrentUser().getFollowing().contains(user.getId());
		
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		//Change action bar title
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().getActionBar().setTitle(user.getUsername());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_profile_other_user,container,false);
		
		commonAlbumsLabel = (TextView) view.findViewById(R.id.common_albums);
		
		photosNumber = (TextView) view.findViewById(R.id.photos_number);
		photosNumber.setText(String.valueOf(user.getPhotosNumber()));
		albumsNumber = (TextView) view.findViewById(R.id.albums_number);
		albumsNumber.setText(String.valueOf(user.getAlbumsNumber()));
		friendsNumber = (TextView) view.findViewById(R.id.friends_number);
		//friendsNumber.setText(String.valueOf(user.getFriendsNumber()));
		
		//profile picture
		profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
		
		if(user.getProfilePicture() != null)
			imageLoader.displayImage(user.getProfilePicture(),profilePicture);
		
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarOtherProfile);
		mProgressBarDownloadCommonAlbums = (ProgressBar) view.findViewById(R.id.progressBarCommonAlbums);
		
		listCommonAlbums = (ListView) view.findViewById(R.id.list_albums);
		
		buttonFriend = (Button) view.findViewById(R.id.button_friend);
		
		buttonFriend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO Following follow
				/*//add in friend list
				//if(followingThisUser) 
					new DeleteFriendTask().execute();
				else
					new AddFriendTask().execute();
				followingThisUser = !followingThisUser;*/
			}
		});
		
		if(followingThisUser) {
			//is in friend list
			new DownloadCommonAlbumsTask().execute();
			//Change button
			buttonIsFriend();
		}
		else {
			//no is in friends list
			buttonAddFriend();
		}
		
		return view;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.v("prototypev1","get item id "+item.getItemId());
		switch (item.getItemId()) {
			case android.R.id.home:
				getFragmentManager().popBackStack();
		        return true;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void buttonIsFriend() {
		buttonFriend.setText(getString(R.string.friend));
		int imgResource = R.drawable.ic_action_good;
		buttonFriend.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);
	}
	
	public void buttonAddFriend() {
		buttonFriend.setText(getString(R.string.add_friend));
		int imgResource = R.drawable.ic_action_new;
		buttonFriend.setCompoundDrawablesWithIntrinsicBounds(imgResource, 0, 0, 0);
		commonAlbumsLabel.setText(getString(R.string.private_albums));
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
