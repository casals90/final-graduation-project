package com.example.prototypetfgv2.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Photo;
import com.example.prototypetfgv2.utils.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ListViewNewsAdapter extends BaseAdapter {

	private Controller controller;
	private LayoutInflater inflater;
	
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	
	private boolean like;
	
	private ArrayList<Photo> photos;
	private HashMap<String, String> albums;
	private Activity activity;
	private NewsInterface newsInterface;
	private String idAlbum;
	//private Date currentDate;
	private SimpleDateFormat simpleDateFormat;
	private String currentDate;
		
	public ListViewNewsAdapter(Context context,ArrayList<Photo> photos,HashMap<String, String> albums,NewsInterface newsInterface,Activity activity) {
		super();
		this.photos = photos;
		this.inflater = LayoutInflater.from(context);
		this.controller = (Controller) context.getApplicationContext();
		this.imageLoader = ImageLoader.getInstance();
		initDisplayOptions();
		this.albums = albums;
		this.newsInterface = newsInterface;
		this.activity = activity;
		//Current date
		simpleDateFormat = new SimpleDateFormat("dd/M/yyyy HH:mm:ss");
		currentDate = simpleDateFormat.format(new Date());
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
	
	public class ViewHolder {
		ImageView mImageViewPhoto;
		ProgressBar mProgressBar;
		TextView mTextViewUsername,mTextViewAlbumTitle,mTextViewDate;
		Button mButtonLike, mButtonComment;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		final Photo photo;
		
		if(view == null) {
			holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_news, null);
            
            holder.mTextViewUsername = (TextView) view.findViewById(R.id.username);
            holder.mTextViewAlbumTitle = (TextView) view.findViewById(R.id.album_name);
            holder.mTextViewDate = (TextView) view.findViewById(R.id.date);
            holder.mImageViewPhoto = (ImageView) view.findViewById(R.id.photo);
            holder.mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            holder.mButtonLike = (Button) view.findViewById(R.id.button_like);
            holder.mButtonComment = (Button) view.findViewById(R.id.button_comment);
            
			view.setTag(holder);
		}
		else
			holder = (ViewHolder) view.getTag();
		
		photo = photos.get(position);
		
		imageLoader.displayImage(photo.getPhoto(),holder.mImageViewPhoto,options,new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            	holder.mProgressBar.setVisibility(View.VISIBLE);
            	holder.mImageViewPhoto.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            	holder.mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            	holder.mProgressBar.setVisibility(View.INVISIBLE);
            	holder.mImageViewPhoto.setVisibility(View.VISIBLE);
            }
        });
		
		holder.mTextViewUsername.setText(photo.getOwnerUser().getUsername());
		holder.mTextViewUsername.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				newsInterface.goToProfileUser(photo.getOwnerUser());
			}
			
		});
		idAlbum = photo.getOwnerAlbum();
		String albumName = albums.get(idAlbum);
		holder.mTextViewAlbumTitle.setText(albumName);
		holder.mTextViewAlbumTitle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				newsInterface.goToAlbum(photo.getOwnerAlbum());
			}
		});
		
		
		String d = Utils.newDateFormatFromCreatedAt(photo.getCreatedAt());
		String difference = Utils.substracDates(currentDate,d);
		
		holder.mTextViewDate.setText(getLabel(difference));
		
		holder.mButtonLike.setText(String.valueOf(photo.getLikesNumber()));
		holder.mButtonComment.setText(String.valueOf(photo.getCommentsNumber()));
		like = controller.currentUserLikedCurrentPhoto(photo.getId());
		if(!like) {
			changeShapeColorBlack(holder.mButtonLike);
			holder.mButtonLike.setOnClickListener(new OnClickListener() {

				@Override
    			public void onClick(View v) {
    				new LikePhotoTask().execute(photo.getId());
    				idAlbum = photo.getOwnerAlbum();
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
		changeShapeColorBlack(holder.mButtonComment);
		holder.mButtonComment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goToCommentsActivity(photo);
			}
		});
		
		return view;
	}
	
	public String getLabel(String d) {
		Log.v("prototypev1","getLabel "+d);
		String[] t = d.split(":");
		if(t[1].compareTo("s") == 0) 
			return t[0]+" "+activity.getString(R.string.seconds_ago); 
		else if(t[1].compareTo("m") == 0)
			return t[0]+" "+activity.getString(R.string.minutes_ago);
		else if(t[1].compareTo("h") == 0)
			return t[0]+" "+activity.getString(R.string.hours_ago);
		else
			return t[0]+" "+activity.getString(R.string.days_ago); 
	}
	
	public void goToCommentsActivity(Photo currentPhoto) {
		Intent commentsActivity = new Intent(activity,CommentsActivity.class);
		commentsActivity.putExtra("photo",currentPhoto);
		activity.startActivity(commentsActivity);
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
		bgShape.setColor(Color.rgb(51,181,229));
	}
	
	public void changeShapeColorBlack(Button button) {
		GradientDrawable bgShape = (GradientDrawable)button.getBackground();
		bgShape.setColor(Color.argb(204,44,44,44));
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
