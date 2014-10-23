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

public class GridViewAdapterForShowPhotos extends BaseAdapter {
	
	private Context context;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;
	private List<Photo> photos;
	private Controller controller;
	
	public GridViewAdapterForShowPhotos(Context context, List<Photo> photos) {
		super();
		this.context = context;
		this.photos = photos;
		this.imageLoader = new ImageLoader(context);
		this.inflater = LayoutInflater.from(context);
		controller = (Controller) context.getApplicationContext();
		Log.v("prototypev1", "size photos grid "+photos.size());
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
		imageLoader.DisplayImage(photos.get(position).getPhoto(),holder.photo);
		//holder.photo.setImageResource(R.drawable.ic_launcher);
		return view;
	}

}
