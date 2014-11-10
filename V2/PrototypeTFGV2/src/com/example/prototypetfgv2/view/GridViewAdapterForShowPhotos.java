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

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Photo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

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
	
	public void initializeDisplayImageOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
        .build();
		
		/*
		 .showImageOnLoading(R.drawable.ic_stub) // resource or drawable
        .showImageForEmptyUri(R.drawable.ic_empty) // resource or drawable
        .showImageOnFail(R.drawable.ic_error) // resource or drawable*/
		  
		  
		 
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
    }

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if(view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.item_photo_inside_album, null);
			holder.photo = (ImageView) view.findViewById(R.id.photo);
			view.setTag(holder);
		}
		else
			holder = (ViewHolder) view.getTag();
		imageLoader.displayImage(photos.get(position).getPhoto(),holder.photo);
		return view;
	}

}
