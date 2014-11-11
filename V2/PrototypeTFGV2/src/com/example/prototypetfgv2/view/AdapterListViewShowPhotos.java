package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Photo;
import com.example.prototypetfgv2.view.ListViewAdapterForShowPhotosOld.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterListViewShowPhotos extends BaseAdapter {

	private ArrayList<Photo> photos;
	private Controller controller;
	private LayoutInflater inflater;
	
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	
	public AdapterListViewShowPhotos(Context context,ArrayList<Photo> photos) {
		super();
		this.photos = photos;
		this.inflater = LayoutInflater.from(context);
		
		controller = (Controller) context.getApplicationContext();
	}
	
	public void configureDisplayOptions() {
		options = new DisplayImageOptions.Builder()
        .showImageOnLoading(R.drawable.ic_launcher) // resource or drawable
        .resetViewBeforeLoading(false)  // default
        .delayBeforeLoading(1000)
        .cacheInMemory(false) // default
        .cacheOnDisk(false) // default
        .considerExifParams(false) // default
        .build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return photos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return photos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public class ViewHolder {
		ImageView mImageViewProfilePicture,mImageViewPhoto;
		TextView mTextViewUsername,mTextViewTitle;
		Button mButtonLike, mButtonComment;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parenmt) {
		final ViewHolder holder;
		final Photo photo;
		if(view == null) {
			holder = new ViewHolder();
            view = inflater.inflate(R.layout.photo_item_list_photos, null);
            
            holder.mImageViewProfilePicture = (ImageView) view.findViewById(R.id.profile_picture);
            holder.mTextViewUsername = (TextView) view.findViewById(R.id.username);
            holder.mTextViewTitle = (TextView) view.findViewById(R.id.title);
            holder.mImageViewPhoto = (ImageView) view.findViewById(R.id.photo);
            holder.mButtonLike = (Button) view.findViewById(R.id.button_like);
            holder.mButtonComment = (Button) view.findViewById(R.id.button_comment);
            
			view.setTag(holder);
		}
		else
			holder = (ViewHolder) view.getTag();
		photo = photos.get(position);
		ImageLoader.getInstance().displayImage(photo.getPhoto(),holder.mImageViewPhoto,options);
		ImageLoader.getInstance().displayImage(photo.getOwnerUser().getProfilePicture(),holder.mImageViewProfilePicture);
		
		holder.mTextViewUsername.setText(photo.getOwnerUser().getUsername());
		holder.mTextViewTitle.setText(photo.getTitle());
		
		holder.mButtonLike.setText(String.valueOf(photo.getLikesNumber()));
		holder.mButtonComment.setText(String.valueOf(photo.getCommentsNumber()));
		
		return view;
	}

}
