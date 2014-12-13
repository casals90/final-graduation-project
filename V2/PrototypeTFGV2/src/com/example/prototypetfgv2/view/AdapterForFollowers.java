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


public class AdapterForFollowers extends BaseAdapter {

	private ArrayList<User> followers;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Controller controller;
	private LayoutInflater inflater;
	private ArrayList<String> following;
		
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
	
	public void initDisplayOptions() {
		options = new DisplayImageOptions.Builder()
        .showImageForEmptyUri(R.drawable.ic_launcher) // resource or drawable
        .showImageOnFail(R.drawable.ic_launcher) // resource or drawable
        .resetViewBeforeLoading(true) 
        .considerExifParams(true)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return followers.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return followers.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public class ViewHolder {
		ImageView mImageViewProfilePicture;
		TextView mTextViewUsername;
		Button mButtonFollowing, mButtonFollow;
	}
	
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
	
	public void deleteFollowing(String idFollowing) {
		following.remove(idFollowing);
		//controller.deleteFollower(idFollowing);
		new DeleteFollowingTask().execute(idFollowing);
	}
	
	public void deleteFollower(String idFollower) {
		followers.remove(idFollower);
		//controller.deleteFollower(idFollower);
		new AddFollowingTask().execute(idFollower);
	}
	
	public void addFollowing(String idFollowing) {
		following.add(idFollowing);
		controller.addFollowing(idFollowing);
	}
	
	private class DeleteFollowingTask extends AsyncTask<String, Void, Boolean> {
		
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
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
				//Toast.makeText(activity,"Delete Following!",  Toast.LENGTH_SHORT).show();
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
				//Toast.makeText(activity,"Add following!",  Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			//Toast.makeText(getActivity(),"Error download photos",  Toast.LENGTH_LONG).show();
		}	
    }
}
