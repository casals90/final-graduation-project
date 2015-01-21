package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.RelativeLayout;
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
	private TextView albumsNumber,followersNumber,followingNumber,no_watch_profile;
	private Button buttonFollowing,buttonFollow;
	private ProgressBar mProgressBar,mProgressBarDownloadCommonAlbums;
	private ListView listCommonAlbums;
	private boolean followingThisUser;
	private User user;
	private ArrayList<Album> commonAlbums;
	private ImageLoader imageLoader;
	private Controller controller;
	private RelativeLayout container;
	
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
		
		albumsNumber = (TextView) view.findViewById(R.id.albums_number);
		albumsNumber.setText(String.valueOf(user.getAlbumsNumber()));
		
		followersNumber = (TextView) view.findViewById(R.id.followers_number);
		followersNumber.setText(String.valueOf(user.getFollowersNumber()));
		
		followingNumber = (TextView) view.findViewById(R.id.following_number);
		followingNumber.setText(String.valueOf(user.getFollowingNumber()));
		
		no_watch_profile = (TextView) view.findViewById(R.id.no_watch);
		
		//profile picture
		profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
		
		if(user.getProfilePicture() != null)
			imageLoader.displayImage(user.getProfilePicture(),profilePicture);
		
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarOtherProfile);
		mProgressBarDownloadCommonAlbums = (ProgressBar) view.findViewById(R.id.progressBarCommonAlbums);
		
		listCommonAlbums = (ListView) view.findViewById(R.id.list_albums);
		
		buttonFollow = (Button) view.findViewById(R.id.button_follow);
		buttonFollowing = (Button) view.findViewById(R.id.button_following);
		
		if(followingThisUser) {
			new DownloadCommonAlbumsTask().execute();
			showButtonFollowing();
			no_watch_profile.setVisibility(View.INVISIBLE);
		}
		else
			showButtonFollow();
		return view;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				getFragmentManager().popBackStack();
		        return true;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void showButtonFollowing() {
		//Hidde button follow
		buttonFollow.setOnClickListener(null);
		buttonFollow.setVisibility(View.INVISIBLE);
		//Show button following
		buttonFollowing.setVisibility(View.VISIBLE);
		buttonFollowing.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteFollowing();
				showButtonFollow();
			}
		});
	}
	
	public void showButtonFollow() {
		//Hidde button follow
		buttonFollowing.setOnClickListener(null);
		buttonFollowing.setVisibility(View.INVISIBLE);
		//Show button following
		buttonFollow.setVisibility(View.VISIBLE);
		buttonFollow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addFollowing();
				showButtonFollowing();
			}
		});
	}
	
	public void deleteFollowing() {
		controller.getCurrentUser().deleteFollowing(user.getId());
		user.decrementFollowersNumber();
		new DeleteFollowingTask().execute(user.getId());
	}
	
	public void addFollowing() {
		controller.getCurrentUser().addFollowing(user.getId());
		user.incrementFollowersNumber();
		new AddFollowingTask().execute(user.getId());
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
	
	private class DeleteFollowingTask extends AsyncTask<String, Void, Boolean> {
		
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	listCommonAlbums.setVisibility(View.INVISIBLE);
			no_watch_profile.setVisibility(View.VISIBLE);
        }
 
        @Override
        protected Boolean doInBackground(String... params) {
        	String idFollowing = params[0];
        	return controller.deleteFollowing(idFollowing);
        }

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result) {
				followersNumber.setText(String.valueOf(user.getFollowersNumber()));
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			//Toast.makeText(getActivity(),"Error download photos",  Toast.LENGTH_LONG).show();
		}	
    }
	
	private class AddFollowingTask extends AsyncTask<String, Void, Boolean> {
		
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	no_watch_profile.setVisibility(View.INVISIBLE);
        }
 
        @Override
        protected Boolean doInBackground(String... params) {
        	String idFollowing = params[0];
        	return controller.addFollowing(idFollowing);
        }

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result) {
				followersNumber.setText(String.valueOf(user.getFollowersNumber()));
				new DownloadCommonAlbumsTask().execute();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			//Toast.makeText(getActivity(),"Error download photos",  Toast.LENGTH_LONG).show();
		}	
    }

}
