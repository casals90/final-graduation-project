package com.example.prototypetfgv2.view;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.model.User;
import com.nostra13.universalimageloader.core.ImageLoader;


public class ListviewFriendsAdapter extends BaseAdapter {

	private ArrayList<User> friends;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	
	
	public ListviewFriendsAdapter(Context context,ArrayList<User> friends) {
		super();
		this.friends = friends;
		this.inflater = LayoutInflater.from(context);
		this.imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return friends.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return friends.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
        final User friend = friends.get(position);
        if (view == null) { 
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_listview_friends,null);
            holder.mTextView = (TextView) view.findViewById(R.id.username);
            holder.mImageView = (ImageView) view.findViewById(R.id.profile_picture);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.mTextView.setText(friend.getUsername());
        
        imageLoader.displayImage(friend.getProfilePicture(),holder.mImageView);
        //Default profile photo
        if(friend.getProfilePicture() == null)
        	holder.mImageView.setImageResource(R.drawable.ic_launcher);
        return view;
	}
	
	public class ViewHolder {
		ImageView mImageView;
		TextView mTextView;
	}

}
