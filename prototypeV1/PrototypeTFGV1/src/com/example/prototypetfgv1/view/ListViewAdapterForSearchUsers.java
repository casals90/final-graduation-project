package com.example.prototypetfgv1.view;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prototypetfgv1.R;
import com.example.prototypetfgv1.model.User;
 
public class ListViewAdapterForSearchUsers extends BaseAdapter {
	
    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    
    private List<User> users;
    
    public ListViewAdapterForSearchUsers(Context context,List<User> users) {      
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
        // Set the results into ImageView
        imageLoader.DisplayImage(users.get(position).getProfilePicture(),holder.profilePicture);
        return view;
    }
}
