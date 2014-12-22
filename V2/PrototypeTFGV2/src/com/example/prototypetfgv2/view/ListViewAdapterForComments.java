package com.example.prototypetfgv2.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.model.Comment;
import com.example.prototypetfgv2.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ListViewAdapterForComments extends BaseAdapter {

	private ArrayList<Comment> comments;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	private SimpleDateFormat simpleDateFormat;
	private String currentDate;
    private Activity activity;
	
	public ListViewAdapterForComments(Context context,ArrayList<Comment> comments,Activity activity) {
		super();
		inflater = LayoutInflater.from(context);
        imageLoader = ImageLoader.getInstance();
		this.comments = comments;
		this.activity = activity;
		//Current date
		simpleDateFormat = new SimpleDateFormat("dd/M/yyyy HH:mm:ss");
		currentDate = simpleDateFormat.format(new Date());
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
			holder.date = (TextView) view.findViewById(R.id.date);
			view.setTag(holder);
		}
		else
			 holder = (ViewHolder) view.getTag();
		
		Comment comment = comments.get(position);
		holder.username.setText(comments.get(position).getUser().getUsername());
		holder.comment.setText(comments.get(position).getComment());
		holder.date.setText(comments.get(position).getDate());
		
		String d = Utils.newDateFormatFromCreatedAt(comment.getDate());
		String difference = Utils.substracDates(currentDate,d);
		
		holder.date.setText(getLabel(difference));
		
		imageLoader.displayImage(comments.get(position).getUser().getProfilePicture(),holder.profilePicture);
		if(comments.get(position).getUser().getProfilePicture() == null)
			holder.profilePicture.setImageResource(R.drawable.ic_launcher);
		return view;
	}
	
	public String getLabel(String d) {
		String[] t = d.split(":");
		if(t[1].compareTo("s") == 0) 
			return t[0]+" "+activity.getString(R.string.seconds_ago); 
		else if(t[1].compareTo("m") == 0)
			return t[0]+" "+activity.getString(R.string.minutes_ago);
		else if(t[1].compareTo("h") == 0)
			return t[0]+" "+activity.getString(R.string.hours_ago);
		else
			return t[0]+" "+activity.getString(R.string.days_ago); 
	}
}
