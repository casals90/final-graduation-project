package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.model.Album;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ChooseAlbumAdapter extends PagerAdapter {

	private ArrayList<Album> albums;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	private Activity activity;
	private DisplayImageOptions options;
	private FragmentAlbums fragmentAlbums;
	
	public ChooseAlbumAdapter(ArrayList<Album> albums,Activity activity,FragmentAlbums fragmentAlbums) {
		super();
		this.albums = albums;
		this.imageLoader = ImageLoader.getInstance();
		this.activity = activity;
		this.fragmentAlbums = fragmentAlbums;
		initDisplayOptions();
	}
	
	public void initDisplayOptions() {
		options = new DisplayImageOptions.Builder()
        .showImageForEmptyUri(R.drawable.ic_launcher)
        .showImageOnFail(R.drawable.ic_launcher) 
        .resetViewBeforeLoading(true) 
        .considerExifParams(true)
        .imageScaleType(ImageScaleType.EXACTLY)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .build();
	}
	
	@Override
	public int getCount() {
		return albums.size();
	}
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object);
	}
	
	@Override  
    public void destroyItem(View collection, int position, Object view) {  
        ((ViewPager) collection).removeView((RelativeLayout) view);  
    }
	
	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
	
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.item_album_view_pager, container,false);
		
        final Album album = albums.get(position);
        final ViewHolder holder = new ViewHolder();
        
        //Create a listener for the view
        viewLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.v("prototypev1","click album "+position);
				fragmentAlbums.goToShowAlbumListMode(album);
			}
		});
        
        holder.mTextViewAlbumTitle = (TextView) viewLayout.findViewById(R.id.album_title);
        holder.mImageView = (ImageView) viewLayout.findViewById(R.id.cover_album);
        holder.mTextViewPhotoNumbers = (TextView) viewLayout.findViewById(R.id.photos_number);
        holder.mTextViewMembersNumber = (TextView) viewLayout.findViewById(R.id.members_number);
        //show image cover
        imageLoader.displayImage(album.getAlbumCover(),holder.mImageView,options);
        holder.mTextViewPhotoNumbers.setText(String.valueOf(album.getPhotosNumber()));
        holder.mTextViewMembersNumber.setText(String.valueOf(album.getMembersNumber()));
        holder.mTextViewAlbumTitle.setText(album.getAlbumTitle());
        
        ((ViewPager) container).addView(viewLayout);
        
        return viewLayout;
	}
	
	public class ViewHolder {
		ImageView mImageView;
		TextView mTextViewAlbumTitle,mTextViewPhotoNumbers, mTextViewMembersNumber;
	}
}
