package com.example.prototypetfgv2.view;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Photo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ShowPhotosInProfileAdapter extends BaseAdapter {

	private ArrayList<Photo> photos;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private Controller controller;
	private Activity activity;
	private boolean like;
	
	private String idPhoto;
	private String idAlbum;
	
	public ShowPhotosInProfileAdapter(Context context,ArrayList<Photo> photos,Controller controller,Activity activity) {
		super();
		this.photos = photos;
		this.inflater = LayoutInflater.from(context);
		this.imageLoader = ImageLoader.getInstance();
		this.controller = (Controller) context.getApplicationContext();
		this.activity = activity;
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
		
		return photos.size();
	}

	@Override
	public Object getItem(int position) {
		return photos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		final Photo photo;
		if (view == null) { 
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_photo_profile,null);
            holder.mImageView = (ImageView) view.findViewById(R.id.photo);
            holder.mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            holder.mButtonLike = (Button) view.findViewById(R.id.button_like);
            holder.mButtonComment = (Button) view.findViewById(R.id.button_comment);
            
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        photo = photos.get(position);
        imageLoader.displayImage(photo.getPhoto(),holder.mImageView,options,new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            	holder.mProgressBar.setVisibility(View.VISIBLE);
            	holder.mImageView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            	holder.mImageView.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            	holder.mProgressBar.setVisibility(View.INVISIBLE);
            	holder.mImageView.setVisibility(View.VISIBLE);
            }
        });
        //Default profile photo
        if(photo.getPhoto() == null)
        	holder.mImageView.setImageResource(R.drawable.ic_launcher);
        
        holder.mButtonLike.setText(String.valueOf(photo.getLikesNumber()));
        holder.mButtonComment.setText(String.valueOf(photo.getCommentsNumber()));
        changeShapeColorBlack(holder.mButtonComment);
        holder.mButtonComment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goToCommentsActivity(photo);
			}
		});
        
        like = controller.currentUserLikedCurrentPhoto(photo.getId());
		if(!like) {
			changeShapeColorBlack(holder.mButtonLike);
			holder.mButtonLike.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				idPhoto = photo.getId();
    				idAlbum = photo.getOwnerAlbum();
    				new LikePhotoTask().execute();
    				photos.get(position).incrementNumberLikes();
    				incrementLikesNumberInButton(holder.mButtonLike,photo.getId());
    				holder.mButtonLike.setOnClickListener(null);
    			}
    		});
        }
        else {
        	changeShapeColorCyan(holder.mButtonLike);
        	holder.mButtonLike.setOnClickListener(null);
        }
        
		return view;
	}

	public class ViewHolder {
		ImageView mImageView;
		Button mButtonLike,mButtonComment;
		ProgressBar mProgressBar;
	}
	
	public void goToCommentsActivity(Photo currentPhoto) {
		Intent commentsActivity = new Intent(activity,CommentsActivity.class);
		commentsActivity.putExtra("photo",currentPhoto);
		activity.startActivity(commentsActivity);
	}
	
	public void changeShapeColorCyan(Button button) {
		GradientDrawable bgShape = (GradientDrawable)button.getBackground();
		bgShape.setColor(Color.rgb(51,181,229));
	}
	
	public void changeShapeColorBlack(Button button) {
		GradientDrawable bgShape = (GradientDrawable)button.getBackground();
		bgShape.setColor(Color.argb(204,44,44,44));
	}
	
	public void incrementLikesNumberInButton(Button button,String idPhoto) {
		int n = Integer.valueOf(button.getText().toString());
		n++;
		button.setText(String.valueOf(n));
		controller.addLikePhotoCurrentUser(idPhoto);
		changeShapeColorCyan(button);
	}
	
	private class LikePhotoTask extends AsyncTask<Void, Void, Boolean> {
		
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        }
 
        @Override
        protected Boolean doInBackground(Void... params) {
        	Log.v("prototypev1", "doinbackgrouns idPhoto "+idPhoto+" idAlbum = "+idAlbum);
        	return controller.likePhoto(idPhoto,idAlbum);
        }

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result) {
				Toast.makeText(activity,"Liked!",  Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			//Toast.makeText(getActivity(),"Error download photos",  Toast.LENGTH_LONG).show();
		}	
    }
}
