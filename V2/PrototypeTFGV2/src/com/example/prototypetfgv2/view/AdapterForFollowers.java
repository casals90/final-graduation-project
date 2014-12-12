package com.example.prototypetfgv2.view;
import java.util.ArrayList;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


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
		Button mButton;
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
            holder.mButton = (Button) view.findViewById(R.id.bFollow);
		}
		else
			holder = (ViewHolder) view.getTag();
		
		follower = followers.get(position);
		
		imageLoader.displayImage(follower.getProfilePicture(),holder.mImageViewProfilePicture,options);
		holder.mTextViewUsername.setText(follower.getUsername());
		
		holder.mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
								
			}
		});
		
		//Check that if i following this follower
		if(following.contains(follower.getId())) {
			holder.mButton.setText("Following");
		}
		else {
			holder.mButton.setText("No Following");
		}
		
		return view;
	}

}
