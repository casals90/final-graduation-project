package com.example.prototypetfgv2.view;

import java.util.List;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Album;
import com.example.prototypetfgv2.model.User;
import com.example.prototypetfgv2.view.ListViewAdapterForSearchUsers.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ListViewAdapterForAlbums extends BaseAdapter {

	private LayoutInflater inflater;
    private ImageLoader imageLoader;
    //private ImageThreadLoader it;
	
	private List<Album> albums;
	
	private Controller controller;
	Activity activity;
	
	public ListViewAdapterForAlbums(Context context,List<Album> albums) { 
		
        inflater = LayoutInflater.from(context);
        imageLoader = new ImageLoader(context);
        controller = (Controller) context.getApplicationContext();
          
        this.albums = albums;
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
        ImageView albumCover;
        TextView albumTitle;
    }
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_album_view,null);
            holder.albumTitle = (TextView) view.findViewById(R.id.album_title);
            holder.albumCover = (ImageView) view.findViewById(R.id.photo_cover_album);
            view.setTag(holder);
        } 
		else {
            holder = (ViewHolder) view.getTag();
        }
		holder.albumTitle.setText(albums.get(position).getAlbumTitle());
		
		imageLoader.DisplayImage(albums.get(position).getAlbumCover(),holder.albumCover);
		if(albums.get(position).getAlbumCover() == null)
			holder.albumCover.setImageResource(R.drawable.ic_launcher);
		
		return view;
	}

}
