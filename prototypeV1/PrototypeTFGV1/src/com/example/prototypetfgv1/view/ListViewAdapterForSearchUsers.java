package com.example.prototypetfgv1.view;

import java.util.List;

import org.json.JSONArray;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prototypetfgv1.R;
import com.example.prototypetfgv1.controller.Controller;
import com.example.prototypetfgv1.model.User;
 
public class ListViewAdapterForSearchUsers extends BaseAdapter {
    
	private Context context;
	private Controller controller;
	
    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    
    private List<User> users;
    private JSONArray friends; 
    
    public ListViewAdapterForSearchUsers(Context context,List<User> users) {
        this.context = context;
        
        controller = new Controller(this.context);
        //Download friends list
        friends = controller.getFriends();
        
        inflater = LayoutInflater.from(context);
        imageLoader = new ImageLoader(context);
               
        this.users = users;
    }
    
    public class ViewHolder {
        ImageView profilePicture;
        TextView username;
        //ImageButton addFriend;
        TextView commonFriends;
    }
 
    @Override
    public int getCount() {
        return users.size();
    }
 
    @Override
    public Object getItem(int position) {
        return users.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_user_view, null);
            // Locate the TextViews in listview_item.xml
            holder.username = (TextView) view.findViewById(R.id.username);
            //holder.addFriend = (ImageButton) view.findViewById(R.id.button_add_friend);
            holder.commonFriends = (TextView) view.findViewById(R.id.commonFriends);
            // Locate the ImageView in listview_item.xml@string/head_arrowImage
            holder.profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.username.setText(users.get(position).getUsername());
        //holder.commonFriends.setText(users.get(position).getCommonFriends());
        //holder.commonFriends.setText("0 common friends");
        /*final String id = users.get(position).getId();
        if((!Utils.isElementExist(friends,id))) {
        	//Add new friend
        	/*holder.addFriend.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//add id in request friends
					controller.addFriend(id);
					Log.v("prototypev1", "new Friend "+id);
					//destroy image button
					holder.addFriend.setOnClickListener(null);
					holder.addFriend.setVisibility(View.INVISIBLE);
				}
			});
        }
        else {
        	//is in friend list
        	//Log.v("prototypev1", "is in friend list");
        	//holder.addFriend.setVisibility(View.INVISIBLE);
        }*/
        // Set the results into ImageView
        imageLoader.DisplayImage(users.get(position).getProfilePicture(),holder.profilePicture);
        return view;
    }
}
