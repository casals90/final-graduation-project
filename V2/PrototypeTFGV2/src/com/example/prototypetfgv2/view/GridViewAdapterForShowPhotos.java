package com.example.prototypetfgv2.view;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Photo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class GridViewAdapterForShowPhotos extends BaseAdapter {
	
	private Context context;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    DisplayImageOptions options;
   // private com.example.prototypetfgv2.view.ImageLoader imageOld;
	private List<Photo> photos;
	private Controller controller;
	
	public GridViewAdapterForShowPhotos(Context context, List<Photo> photos) {
		super();
		this.context = context;
		this.photos = photos;
		this.imageLoader = ImageLoader.getInstance();
		this.inflater = LayoutInflater.from(context);
		controller = (Controller) context.getApplicationContext();
		Log.v("prototypev1", "size photos grid "+photos.size());
		//imageOld = new com.example.prototypetfgv2.view.ImageLoader(context);
	}
	
	public void configureDisplayImageOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_launcher) // resource or drawable
        .showImageForEmptyUri(R.drawable.ic_launcher) // resource or drawable
        .showImageOnFail(R.drawable.ic_launcher) // resource or drawable
        .cacheInMemory(true) 
        .cacheOnDisk(true) 
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
        ImageView photo;
        ProgressBar mProgressBar;
    }

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if(view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.item_photo_inside_album, null);
			holder.photo = (ImageView) view.findViewById(R.id.photo);
			holder.mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
			view.setTag(holder);
		}
		else
			holder = (ViewHolder) view.getTag();
		imageLoader.displayImage(photos.get(position).getPhoto(),holder.photo,options,new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
            	holder.mProgressBar.setVisibility(View.VISIBLE);
            	holder.photo.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            	holder.mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            	holder.mProgressBar.setVisibility(View.INVISIBLE);
            	holder.photo.setVisibility(View.VISIBLE);
            }
        });
		return view;
	}

}
