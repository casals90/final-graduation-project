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


public class AdapterForFollowing extends BaseAdapter {

	private ArrayList<User> following;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Controller controller;
	private LayoutInflater inflater;
		
	public AdapterForFollowing(ArrayList<User> following,Context context) {
		super();
		this.following = following;
		//this.deleteFollowings = new ArrayList<User>();
		this.imageLoader = ImageLoader.getInstance();
		initDisplayOptions();
		this.inflater = LayoutInflater.from(context);
		this.controller = (Controller) context.getApplicationContext();
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
		return following.size();
	}

	@Override
	public Object getItem(int position) {
		return following.get(position);
	}

	@Override
	public long getItemId(int position) {
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
		final User followingUser;
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
		
		followingUser = following.get(position);
		
		imageLoader.displayImage(followingUser.getProfilePicture(),holder.mImageViewProfilePicture,options);
		holder.mTextViewUsername.setText(followingUser.getUsername());
		
		showButtonFollowing(holder.mButtonFollowing, holder.mButtonFollow,followingUser);
		
		return view;
	}
	
	public void showButtonFollowing(final Button following,final Button follow,final User followingUser) {
		//Hidde button follow
		follow.setOnClickListener(null);
		follow.setVisibility(View.INVISIBLE);
		//Show button following
		following.setVisibility(View.VISIBLE);
		following.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteFollowing(followingUser);
				showButtonFollow(following, follow,followingUser);
			}
		});
	}
	
	public void showButtonFollow(final Button following,final Button follow,final User followingUser) {
		//Hidde button follow
		following.setOnClickListener(null);
		following.setVisibility(View.INVISIBLE);
		//Show button following
		follow.setVisibility(View.VISIBLE);
		follow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addFollowing(followingUser);
				showButtonFollowing(following, follow,followingUser);
			}
		});
	}
	
	public void deleteFollowing(User followingUser) {
		//deleteFollowings.add(followingUser);
		following.remove(followingUser);
		new DeleteFollowingTask().execute(followingUser.getId());
	}
	
	public void addFollowing(User followingUser) {
		//deleteFollowings.remove(followingUser);
		following.add(followingUser);
		new AddFollowingTask().execute(followingUser.getId());
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
