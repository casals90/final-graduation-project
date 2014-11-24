package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Photo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class FullScreenImageAdapter extends PagerAdapter {
	
	private Activity activity;
	private ArrayList<Photo> photos;
	private Controller controller;
	private LayoutInflater inflater;
	private boolean like;
	private boolean fullScreen;
	private ActionBar actionBar;
	private String idAlbum;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	
	public FullScreenImageAdapter(Activity activity, ArrayList<Photo> photos, int position,String idAlbum) {
		super();
		this.activity = activity;
		this.photos = photos;
		this.controller = (Controller) activity.getApplication();
		this.fullScreen = false;
		this.actionBar = activity.getActionBar();
		this.imageLoader = ImageLoader.getInstance();
		this.idAlbum = idAlbum;
		//Put the start user 
		changeActionBarForFirstUser(position);
		//Download likes from albums
		controller.getLikesPhotosFromAlbum(idAlbum);
		initDisplayOptions();
	}
	
	public void initDisplayOptions() {
		options = new DisplayImageOptions.Builder()
        .showImageForEmptyUri(R.drawable.ic_launcher) // resource or drawable
        .showImageOnFail(R.drawable.ic_launcher) // resource or drawable
        .resetViewBeforeLoading(true) 
        .considerExifParams(true)
        .imageScaleType(ImageScaleType.EXACTLY)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .build();
	}
	
	public void changeActionBarForFirstUser(int position) {
		//ActionBar actionBar = activity.getActionBar();
		ViewHolderActionBar viewHolderActionBar = new ViewHolderActionBar();
		viewHolderActionBar.mImageView = (ImageView) actionBar.getCustomView().findViewById(R.id.profile_picture);
		viewHolderActionBar.mTextViewPhotoTitle = (TextView) actionBar.getCustomView().findViewById(R.id.photo_title);
		viewHolderActionBar.mTextViewUsername = (TextView) actionBar.getCustomView().findViewById(R.id.username);
		
		ImageLoader.getInstance().displayImage(photos.get(position).getOwnerUser().getProfilePicture(),viewHolderActionBar.mImageView);
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
	public Object instantiateItem(ViewGroup container, final int position) {
		
		final Photo photo = photos.get(position);
		final int nLikes = photo.getLikesNumber();
        final int nComments = photo.getCommentsNumber();
		
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container,false);
        
		final ViewHolder viewHolder = new ViewHolder();
		
		viewHolder.mProgressBar = (ProgressBar) viewLayout.findViewById(R.id.progressBar);
        viewHolder.mImageViewPhoto = (ImageView) viewLayout.findViewById(R.id.imgDisplay);
        imageLoader.displayImage(photo.getPhoto(),viewHolder.mImageViewPhoto,options,new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            	viewHolder.mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            	viewHolder.mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            	viewHolder.mProgressBar.setVisibility(View.INVISIBLE);            	
            }
        });
        
        viewHolder.like = (Button) viewLayout.findViewById(R.id.like);
        viewHolder.comment = (Button) viewLayout.findViewById(R.id.comment);
        
        viewHolder.like.setText(String.valueOf(nLikes));
        viewHolder.comment.setText(String.valueOf(nComments));
        
        like = controller.currentUserLikedCurrentPhoto(photo.getId());
        
        if(!like) {
        	//Change color
        	changeShapeColorBlack(viewHolder.like);
        	viewHolder.like.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				new LikePhotoTask().execute(photo.getId());
    				photos.get(position).incrementNumberLikes();
    				incrementLikesNumberInButton(viewHolder.like,photo.getId());
    				viewHolder.like.setOnClickListener(null);
    			}
    		});
        }
        else 
        	changeShapeColorCyan(viewHolder.like);
        
        changeShapeColorBlack(viewHolder.comment);
        viewHolder.comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goToCommentsActivity(photo);
			}
		});
        
        //Check if full screen or not
        showOrHiddenButtons(fullScreen, viewHolder.like,viewHolder.comment);
        
        //Listener to detect clik
	    viewLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO hidden or show full screen
				Log.v("prototypev1","click viewPager ");
				fullScreen(viewHolder.like,viewHolder.comment);
			}
		});
        
        ((ViewPager) container).addView(viewLayout);
        return viewLayout;
	}
	
	@Override  
    public void destroyItem(View collection, int position, Object view) {  
        ((ViewPager) collection).removeView((RelativeLayout) view);  
    }
	
	public void incrementLikesNumberInButton(Button button,String idPhoto) {
		int n = Integer.valueOf(button.getText().toString());
		n++;
		button.setText(String.valueOf(n));
		controller.addLikePhotoCurrentUser(idPhoto);
		changeShapeColorCyan(button);
	}
	
	public void changeShapeColorCyan(Button button) {
		GradientDrawable bgShape = (GradientDrawable)button.getBackground();
		//cyan
		bgShape.setColor(Color.rgb(51,181,229));
	}
	
	public void changeShapeColorBlack(Button button) {
		GradientDrawable bgShape = (GradientDrawable)button.getBackground();
		bgShape.setColor(Color.argb(204,44,44,44));
	}
	
	private class ViewHolderActionBar {
		ImageView mImageView;
		TextView mTextViewUsername,mTextViewPhotoTitle;
	}
	
	private class ViewHolder {
		ImageView mImageViewPhoto;
		ProgressBar mProgressBar;
		Button like,comment;
	}
	
	public void goToCommentsActivity(Photo currentPhoto) {
		Intent commentsActivity = new Intent(activity,CommentsActivity.class);
		commentsActivity.putExtra("photo",currentPhoto);
		activity.startActivity(commentsActivity);
	}
	
	public void fullScreen(Button like,Button comment) {
		fullScreen = !fullScreen;
		if(fullScreen) {
			actionBar.hide();
			showOrHiddenButtons(fullScreen, like, comment);
		} 
		else {
			actionBar.show();
			showOrHiddenButtons(fullScreen, like, comment);
		}
	}
	
	public void showOrHiddenButtons(boolean show,Button like,Button comment) {
		if(!show) {
			like.setVisibility(View.VISIBLE);
			comment.setVisibility(View.VISIBLE);
		}
		else {
			like.setVisibility(View.INVISIBLE);
			comment.setVisibility(View.INVISIBLE);
		}
	}
	
	private class LikePhotoTask extends AsyncTask<String, Void, Boolean> {
		
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        }
 
        @Override
        protected Boolean doInBackground(String... params) {
        	String idPhoto = params[0];
        	return controller.likePhoto(idPhoto,idAlbum);
        }

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result) {
				Toast.makeText(activity,"Liked!",  Toast.LENGTH_SHORT).show();
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
