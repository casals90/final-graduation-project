package com.example.prototypetfgv2.view;

import java.util.List;

import org.json.JSONArray;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.User;
 
public class ListViewAdapterForSearchUsers extends BaseAdapter {
	
    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    
    private List<User> users;
    private JSONArray friends;
    
    private Controller controller;
    
    public ListViewAdapterForSearchUsers(Context context,List<User> users) {      
        inflater = LayoutInflater.from(context);
        imageLoader = new ImageLoader(context);
        
        controller = (Controller) context.getApplicationContext();
               
        this.users = users;
        friends = controller.getFriends();
    }
    
    public class ViewHolder {
        ImageView profilePicture;
        TextView username;
        TextView commonFriends;
        ImageView imageFriend;
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
            view = inflater.inflate(R.layout.item_user_view,null);
            // Locate the TextViews in listview_item.xml
            holder.username = (TextView) view.findViewById(R.id.username);
            //holder.addFriend = (ImageButton) view.findViewById(R.id.button_add_friend);
            holder.commonFriends = (TextView) view.findViewById(R.id.commonFriends);
            // Locate the ImageView in listview_item.xml@string/head_arrowImage
            holder.profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
            holder.imageFriend = (ImageView) view.findViewById(R.id.image_is_friend);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.username.setText(users.get(position).getUsername());
        imageLoader.DisplayImage(users.get(position).getProfilePicture(),holder.profilePicture);
        //Default profile photo
        if(users.get(position).getProfilePicture() == null)
        	holder.profilePicture.setImageResource(R.drawable.ic_launcher);
        //Show image when user is in friend list
        if(!Utils.isElementExist(friends,users.get(position).getId()))
        	holder.imageFriend.setVisibility(View.INVISIBLE);
        else
        	holder.imageFriend.setVisibility(View.VISIBLE);
        return view;
    }
}
