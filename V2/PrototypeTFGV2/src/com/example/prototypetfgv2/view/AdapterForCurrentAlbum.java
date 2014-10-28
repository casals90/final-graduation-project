package com.example.prototypetfgv2.view;

import java.util.List;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.model.CurrentAlbum;
import com.example.prototypetfgv2.view.ListViewAdapterChooseUsersNewAlbum.ViewHolder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterForCurrentAlbum extends BaseAdapter {

	private LayoutInflater inflater;
    private ImageLoader imageLoader;
	
	List<CurrentAlbum> currentAlbums;
	
	public AdapterForCurrentAlbum(Context context,List<CurrentAlbum> currentAlbums) {
		super();
		this.inflater = LayoutInflater.from(context);
        this.imageLoader = new ImageLoader(context);
        
		this.currentAlbums = currentAlbums;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return currentAlbums.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return currentAlbums.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public class ViewHolder {
        ImageView albumCover;
        TextView albumTitle;
    }


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
		imageLoader.DisplayImage(currentAlbums.get(position).getCoverPhoto(),holder.albumCover);
        //Default profile photo
        if(currentAlbums.get(position).getCoverPhoto() == null)
        	holder.albumCover.setImageResource(R.drawable.ic_launcher);
        
		return view;
	}

}
