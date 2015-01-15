package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
/**
 * Class that provides adapter for followers
 * @author jordi
 *
 */
public class AdapterForFollowers extends BaseAdapter {

	private ArrayList<User> followers;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Controller controller;
	private LayoutInflater inflater;
	private ArrayList<String> following;
	
	/**
	 * Constructor for adapter
	 * @param followers
	 * @param context
	 */
	public AdapterForFollowers(ArrayList<User> followers,Context context) {
		super();
		this.followers = followers;
		
		this.imageLoader = ImageLoader.getInstance();
		initDisplayOptions();
		this.inflater = LayoutInflater.from(context);
		this.controller = (Controller) context.getApplicationContext();
		
		//Get following with my followers
		following = controller.getCurrentUser().getFollowing();
	}
	
	/**
	 * method that init Universal Image Loader
	 */
	public void initDisplayOptions() {
		options = new DisplayImageOptions.Builder()
        .showImageForEmptyUri(R.drawable.ic_launcher) // resource or drawable
        .showImageOnFail(R.drawable.ic_launcher) // resource or drawable
        .resetViewBeforeLoading(true) 
        .considerExifParams(true)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .build();
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return followers.size();
	}
	/*
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return followers.get(position);
	}
	/*
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}
	/*
	 * class that contains the views of each row
	 */
	public class ViewHolder {
		ImageView mImageViewProfilePicture;
		TextView mTextViewUsername;
		Button mButtonFollowing, mButtonFollow;
	}
	/*
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		final User follower;
		if(view == null) {
			holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_friend_list, null);
            
            holder.mImageViewProfilePicture = (ImageView) view.findViewById(R.id.profile_picture);
            holder.mTextViewUsername = (TextView) view.findViewById(R.id.username);
            holder.mButtonFollowing = (Button) view.findViewById(R.id.button_following);
            holder.mButtonFollow = (Button) view.findViewById(R.id.button_follow);
		}
		else
			holder = (ViewHolder) view.getTag();
		
		follower = followers.get(position);
		
		imageLoader.displayImage(follower.getProfilePicture(),holder.mImageViewProfilePicture,options);
		holder.mTextViewUsername.setText(follower.getUsername());
		
		//Check if I following this follower
		if(following.contains(follower.getId())) {
			showButtonFollowing(holder.mButtonFollowing, holder.mButtonFollow,follower.getId());
		}
		else {
			showButtonFollow(holder.mButtonFollowing, holder.mButtonFollow,follower.getId());
		}
		
		return view;
	}
	
	/**
	 * Method that show following button
	 * @param following a button that will be show
	 * @param follow a button that will not be show
	 * @param idFollowing id of following
	 */
	public void showButtonFollowing(final Button following,final Button follow,final String idFollowing) {
		//Hidde button follow
		follow.setOnClickListener(null);
		follow.setVisibility(View.INVISIBLE);
		//Show button following
		following.setVisibility(View.VISIBLE);
		following.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteFollowing(idFollowing);
				showButtonFollow(following, follow,idFollowing);
			}
		});
	}
	
	/**
	 * Method that show follow button
	 * @param following a button that will not be show 
	 * @param follow a button that will be show
	 * @param idFollower id of follower
	 */
	public void showButtonFollow(final Button following,final Button follow,final String idFollower) {
		//Hidde button follow
		following.setOnClickListener(null);
		following.setVisibility(View.INVISIBLE);
		//Show button following
		follow.setVisibility(View.VISIBLE);
		follow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addFollowing(idFollower);
				showButtonFollowing(following, follow,idFollower);
			}
		});
	}
	/**
	 * Method that delete following
	 * @param idFollowing id of following that I will be delete
	 */
	public void deleteFollowing(String idFollowing) {
		following.remove(idFollowing);
		//controller.deleteFollower(idFollowing);
		new DeleteFollowingTask().execute(idFollowing);
	}
	
	/*public void deleteFollower(String idFollower) {
		followers.remove(idFollower);
		//controller.deleteFollower(idFollower);
		new AddFollowingTask().execute(idFollower);
	}*/
	/**
	 * Method that add following
	 * @param idFollowing id of following that I will be adding
	 */
	public void addFollowing(String idFollowing) {
		following.add(idFollowing);
		//controller.addFollowing(idFollowing);
		new AddFollowingTask().execute(idFollowing);
	}
	/**
	 * Class that delete a following 
	 * @author jordi
	 *
	 */
	private class DeleteFollowingTask extends AsyncTask<String, Void, Boolean> {
		
		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        }
        /*
         * delete following
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected Boolean doInBackground(String... params) {
        	String idFollowing = params[0];
        	return controller.deleteFollowing(idFollowing);
        }
        /*
         * (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result) {
				//Toast.makeText(activity,"Delete Following!",  Toast.LENGTH_SHORT).show();
			}
		}
		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onCancelled()
		 */
		@Override
		protected void onCancelled() {
			super.onCancelled();
			//Toast.makeText(getActivity(),"Error download photos",  Toast.LENGTH_LONG).show();
		}	
    }
	/**
	 * Class that add following
	 * @author jordi
	 *
	 */
	private class AddFollowingTask extends AsyncTask<String, Void, Boolean> {
		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        }
        /*
         * add following
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected Boolean doInBackground(String... params) {
        	String idFollowing = params[0];
        	return controller.addFollowing(idFollowing);
        }
        /*
         * (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result) {
				//Toast.makeText(activity,"Add following!",  Toast.LENGTH_SHORT).show();
			}
		}
		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onCancelled()
		 */
		@Override
		protected void onCancelled() {
			super.onCancelled();
			//Toast.makeText(getActivity(),"Error download photos",  Toast.LENGTH_LONG).show();
		}	
    }
}
