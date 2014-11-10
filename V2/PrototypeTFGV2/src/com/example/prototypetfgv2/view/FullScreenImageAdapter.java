package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.model.Photo;
import com.example.prototypetfgv2.model.User;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FullScreenImageAdapter extends PagerAdapter {

	private Activity activity;
	private ArrayList<Photo> photos;
	
	private LayoutInflater inflater;
	private ViewHolderActionBar viewHolderActionBar;
	
	public FullScreenImageAdapter(Activity activity, ArrayList<Photo> photos) {
		super();
		this.activity = activity;
		this.photos = photos;
		ActionBar actionbar = activity.getActionBar();
		initializeActionBar(actionbar);
		
	}
	
	public void initializeActionBar(ActionBar actionBar) {
		viewHolderActionBar = new ViewHolderActionBar();
		viewHolderActionBar.mImageView = (ImageView) actionBar.getCustomView().findViewById(R.id.profile_picture);
		viewHolderActionBar.mTextViewPhotoTitle = (TextView) actionBar.getCustomView().findViewById(R.id.photo_title);
		viewHolderActionBar.mTextViewUsername = (TextView) actionBar.getCustomView().findViewById(R.id.username);
		
	}
	
	public void updateActionBar(User user,String title) {
		ImageLoader.getInstance().displayImage(user.getProfilePicture(),viewHolderActionBar.mImageView);
		viewHolderActionBar.mTextViewPhotoTitle.setText(title);
		viewHolderActionBar.mTextViewUsername.setText(user.getUsername());
	}

	@Override
	public int getCount() {
		return photos.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,false);
		ViewHolder viewHolder = new ViewHolder();
		
		Photo photo = photos.get(position);
		
        viewHolder.mImageViewPhoto = (ImageView) viewLayout.findViewById(R.id.imgDisplay);
        ImageLoader.getInstance().displayImage(photo.getPhoto(),viewHolder.mImageViewPhoto);
        
        viewHolder.like = (Button) viewLayout.findViewById(R.id.like);
        viewHolder.like.setText(String.valueOf(photo.getLikesNumber()));
        //TODO listeners
        viewHolder.comment = (Button) viewLayout.findViewById(R.id.comment);
        viewHolder.comment.setText(String.valueOf(photo.getCommentsNumber()));
        
        ((ViewPager) container).addView(viewLayout);
        
        //configure action bar
        updateActionBar(photos.get(position).getOwnerUser(),photos.get(position).getTitle());
        
        return viewLayout;
	}
	
	@Override  
    public void destroyItem(View collection, int position, Object view) {  
        ((ViewPager) collection).removeView((RelativeLayout) view);  
    }
	
	private class ViewHolderActionBar {
		ImageView mImageView;
		TextView mTextViewUsername,mTextViewPhotoTitle;
	}
	
	private class ViewHolder {
		ImageView mImageViewPhoto;
		Button like,comment;
	}
}
