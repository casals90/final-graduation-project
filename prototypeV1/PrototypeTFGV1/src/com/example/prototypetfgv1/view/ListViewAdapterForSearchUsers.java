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
        friends = controller.getFriends();
        
        inflater = LayoutInflater.from(context);
        imageLoader = new ImageLoader(context);
               
        this.users = users;
    }
    
    public class ViewHolder {
        ImageView profilePicture;
        TextView username;
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
        holder.commonFriends.setText("NO is my friend");
        if(Utils.isElementExist(friends,users.get(position).getId())) {
        	holder.commonFriends.setText("is my friend");
        }
        /*AQUIIIIholder.commonFriends.setText("NO is my friend");
        if(controller.isMyFriend(users.get(position).getId())) {
        	holder.commonFriends.setText("is my friend");
        }*/
        
        
        // Set the results into ImageView
        //imageLoader.DisplayImage(users.get(position).getProfilePicture(),holder.profilePicture);     
        return view;
    }
}
