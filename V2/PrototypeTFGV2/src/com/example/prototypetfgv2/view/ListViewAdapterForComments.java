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
import com.example.prototypetfgv2.model.Comment;

public class ListViewAdapterForComments extends BaseAdapter {

	private ArrayList<Comment> comments;
	private LayoutInflater inflater;
    private ImageLoader imageLoader;
    
    //private Controller controller;
    
	public ListViewAdapterForComments(Context context,ArrayList<Comment> comments) {
		super();
		inflater = LayoutInflater.from(context);
        imageLoader = new ImageLoader(context);
		this.comments = comments;
		//Log.v("prototypev1", "on adapter comments"+comments.size()+" text "+comments.get(0).getUser().getUsername()+" text "+comments.get(0).getComment()+" date "+comments.get(0).getDate());
		//controller = (Controller) context.getApplicationContext();
	}

	@Override
	public int getCount() {
		return comments.size();
	}

	@Override
	public Object getItem(int position) {
		return comments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public class ViewHolder {
        ImageView profilePicture;
        TextView username;
        TextView comment;
        TextView date;
    }

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.item_comment,null);
			holder.profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
			holder.username = (TextView) view.findViewById(R.id.username);
			holder.comment = (TextView) view.findViewById(R.id.comment);
			holder.date = (TextView) view.findViewById(R.id.date_of_comment);
			view.setTag(holder);
		}
		else
			 holder = (ViewHolder) view.getTag();
		
		holder.username.setText(comments.get(position).getUser().getUsername());
		holder.comment.setText(comments.get(position).getComment());
		holder.date.setText(comments.get(position).getDate());
		
		imageLoader.DisplayImage(comments.get(position).getUser().getProfilePicture(),holder.profilePicture);
		if(comments.get(position).getUser().getProfilePicture() == null)
			holder.profilePicture.setImageResource(R.drawable.ic_launcher);
		return view;
	}

}
