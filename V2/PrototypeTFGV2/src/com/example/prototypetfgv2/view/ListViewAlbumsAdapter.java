package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.model.Album;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ListViewAlbumsAdapter extends BaseAdapter {

	private ArrayList<Album> albums;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	private int positionCurrentAlbum;
	
	public ListViewAlbumsAdapter(ArrayList<Album> albums,Context context) {
		this.albums = albums;
		this.inflater = LayoutInflater.from(context);
		this.imageLoader = ImageLoader.getInstance();
		this.positionCurrentAlbum = -1;
	}
	
	public ListViewAlbumsAdapter(ArrayList<Album> albums,Context context,int positionCurrentAlbum) {
		this.albums = albums;
		this.inflater = LayoutInflater.from(context);
		this.imageLoader = ImageLoader.getInstance();
		this.positionCurrentAlbum = positionCurrentAlbum;
	}

	@Override
	public int getCount() {
		return albums.size();
	}

	@Override
	public Object getItem(int position) {
		return albums.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public class ViewHolder {
		ImageView mImageView,currentAlbum;;
		TextView mTextViewTitle,mTextViewPhotosNumber,mTextViewMembersNumber;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		final Album album = albums.get(position);
		if(view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.item_album_listview, null);
			
			holder.mImageView = (ImageView) view.findViewById(R.id.album_cover);
			holder.mTextViewTitle = (TextView) view.findViewById(R.id.album_title);
			holder.mTextViewPhotosNumber = (TextView) view.findViewById(R.id.photos_number);  
			holder.mTextViewMembersNumber = (TextView) view.findViewById(R.id.members_number);  
			holder.currentAlbum = (ImageView) view.findViewById(R.id.current_album);
			view.setTag(holder);
		}
		else
			holder = (ViewHolder) view.getTag();
		
		imageLoader.displayImage(album.getAlbumCover(),holder.mImageView);
		if(album.getAlbumCover() == null)
			holder.mImageView.setImageResource(R.drawable.ic_launcher);
		
		holder.mTextViewTitle.setText(album.getAlbumTitle());
		holder.mTextViewPhotosNumber.setText(String.valueOf(album.getPhotosNumber()));
		holder.mTextViewMembersNumber.setText(String.valueOf(album.getMembersNumber()));
		if(positionCurrentAlbum == position)
			holder.currentAlbum.setVisibility(View.VISIBLE);
		else
			holder.currentAlbum.setVisibility(View.INVISIBLE);
		return view;
	}
}
