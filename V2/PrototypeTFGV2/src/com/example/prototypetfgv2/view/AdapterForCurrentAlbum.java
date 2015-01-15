package com.example.prototypetfgv2.view;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.model.CurrentAlbum;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Class that provides adapter for current album list
 * @author jordi
 *
 */
public class AdapterForCurrentAlbum extends BaseAdapter {

	private LayoutInflater inflater;
    private ImageLoader imageLoader;
	private DisplayImageOptions options;
    
	List<CurrentAlbum> currentAlbums;
	
	/**
	 * Constructor of adapter
	 * @param context contex of app
	 * @param currentAlbums list of current albums
	 */
	public AdapterForCurrentAlbum(Context context,List<CurrentAlbum> currentAlbums) {
		super();
		this.inflater = LayoutInflater.from(context);
        this.imageLoader = ImageLoader.getInstance();
        
		this.currentAlbums = currentAlbums;
		initDisplayOptions();
	}
	
	/**
	 * method that init Universal Image Loader
	 */
	public void initDisplayOptions() {
		options = new DisplayImageOptions.Builder()
        .showImageOnLoading(R.drawable.ic_launcher) // resource or drawable
        .showImageForEmptyUri(R.drawable.ic_launcher) // resource or drawable
        .showImageOnFail(R.drawable.ic_launcher) // resource or drawable
        .resetViewBeforeLoading(true) 
        .considerExifParams(true)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .build();
	}

	/*
	 * (non-Javadoc) 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return currentAlbums.size();
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return currentAlbums.get(position);
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	/*
	 * class that contains the views of each row
	 */
	public class ViewHolder {
        ImageView albumCover;
        TextView albumTitle;
    }

	/*
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if(view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.item_current_album,null);
			holder.albumCover = (ImageView) view.findViewById(R.id.album_cover);
			holder.albumTitle = (TextView) view.findViewById(R.id.album_title);
			view.setTag(holder);
		}
		else
			holder = (ViewHolder) view.getTag();
		holder.albumTitle.setText(currentAlbums.get(position).getTitle());
		imageLoader.displayImage(currentAlbums.get(position).getCoverPhoto(),holder.albumCover,options);
        
		return view;
	}

}
