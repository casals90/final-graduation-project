package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.CurrentUser;
import com.example.prototypetfgv2.model.Photo;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FullScreenImageAdapter extends PagerAdapter {

	//private GestureDetectorCompat mDetector;
	
	private Activity activity;
	private ArrayList<Photo> photos;
	private Controller controller;
	private LayoutInflater inflater;
	private boolean like;
	private CurrentUser currentUser;
	
	public FullScreenImageAdapter(Activity activity, ArrayList<Photo> photos, int position) {
		super();
		this.activity = activity;
		this.photos = photos;
		controller = (Controller) activity.getApplication();
		currentUser = controller.getCurrentUser();
		Log.v("prototypev1","current user "+currentUser.getLikes().size());
		//Put the start user 
		changeActionBarForFirstUser(position);
	}
	
	public void changeActionBarForFirstUser(int position) {
		
		ActionBar actionBar = activity.getActionBar();
		ViewHolderActionBar viewHolderActionBar = new ViewHolderActionBar();
		viewHolderActionBar.mImageView = (ImageView) actionBar.getCustomView().findViewById(R.id.profile_picture);
		viewHolderActionBar.mTextViewPhotoTitle = (TextView) actionBar.getCustomView().findViewById(R.id.photo_title);
		viewHolderActionBar.mTextViewUsername = (TextView) actionBar.getCustomView().findViewById(R.id.username);
		
		ImageLoader.getInstance().displayImage(photos.get(position).getPhoto(),viewHolderActionBar.mImageView);
		viewHolderActionBar.mTextViewPhotoTitle.setText(photos.get(position).getTitle());
		viewHolderActionBar.mTextViewUsername.setText(photos.get(position).getOwnerUser().getUsername());
		
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
		
		final Photo photo = photos.get(position);
		final int nLikes = photo.getLikesNumber();
        final int nComments = photo.getCommentsNumber();
		
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,false);
		final ViewHolder viewHolder = new ViewHolder();
		
        viewHolder.mImageViewPhoto = (ImageView) viewLayout.findViewById(R.id.imgDisplay);
        ImageLoader.getInstance().displayImage(photo.getPhoto(),viewHolder.mImageViewPhoto);
        
        viewHolder.like = (Button) viewLayout.findViewById(R.id.like);
        viewHolder.comment = (Button) viewLayout.findViewById(R.id.comment);
        
        viewHolder.like.setText(String.valueOf(nLikes));
        viewHolder.comment.setText(String.valueOf(nComments));
        
        like = currentUser.isUserLikedCurrentPhoto(photo.getId());
        
        if(!like) {
        	viewHolder.like.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				new LikePhotoTask().execute(photo.getId());
    				currentUser.addLike(photo.getId());
    				incrementLikesNumberInButton(viewHolder.like);
    				viewHolder.like.setOnClickListener(null);
    			}
    		});
        }
        else 
        	viewHolder.like.setBackgroundResource(R.color.cyan);
        
			
        viewHolder.comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goToCommentsActivity(photo);
			}
		});
        ((ViewPager) container).addView(viewLayout);
        Log.v("prototypev1", "end prepare view ");
        return viewLayout;
	}
	
	@Override  
    public void destroyItem(View collection, int position, Object view) {  
        ((ViewPager) collection).removeView((RelativeLayout) view);  
    }
	
	public void incrementLikesNumberInButton(Button button) {
		int n = Integer.valueOf(button.getText().toString());
		n++;
		button.setText(String.valueOf(n));
		button.setBackgroundResource(R.color.cyan);
	}
	
	private class ViewHolderActionBar {
		ImageView mImageView;
		TextView mTextViewUsername,mTextViewPhotoTitle;
	}
	
	private class ViewHolder {
		ImageView mImageViewPhoto;
		Button like,comment;
	}
	
	public void goToCommentsActivity(Photo currentPhoto) {
		Intent commentsActivity = new Intent(activity,CommentsActivity.class);
		commentsActivity.putExtra("photo",currentPhoto);
		activity.startActivity(commentsActivity);
	}
	
	
	private class LikePhotoTask extends AsyncTask<String, Void, Boolean> {
		
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        }
 
        @Override
        protected Boolean doInBackground(String... params) {
        	String id = params[0];
        	return controller.likePhoto(id);
        }

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result) {
				Toast.makeText(activity,"Liked!",  Toast.LENGTH_LONG).show();
				like = !like;
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			//Toast.makeText(getActivity(),"Error download photos",  Toast.LENGTH_LONG).show();
		}	
    }
}
