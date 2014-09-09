package com.example.prototypetfgv1.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prototypetfgv1.R;
import com.example.prototypetfgv1.model.Photo;
 
public class ListViewAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ImageLoader imageLoader;
    private List<Photo> myPhotos = null;
    private ArrayList<Photo> arraylist;
 
    public ListViewAdapter(Context context,List<Photo> myPhotos) {
        this.context = context;
        this.myPhotos = myPhotos;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<Photo>();
        this.arraylist.addAll(myPhotos);
        imageLoader = new ImageLoader(context);
    }
 
    public class ViewHolder {
        TextView title;
        ImageView photo;
        TextView createdAt;
    }
 
    @Override
    public int getCount() {
        return myPhotos.size();
    }
 
    @Override
    public Object getItem(int position) {
        return myPhotos.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);
            // Locate the TextViews in listview_item.xml
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.createdAt = (TextView) view.findViewById(R.id.createdAt);
            // Locate the ImageView in listview_item.xml
            holder.photo = (ImageView) view.findViewById(R.id.photo);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.title.setText(myPhotos.get(position).getTitle());
        holder.createdAt.setText(myPhotos.get(position).getCreatedAt());
        // Set the results into ImageView
        imageLoader.DisplayImage(myPhotos.get(position).getPhoto(),holder.photo);
        return view;
    }
}
