package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.model.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ListViewAdapterChooseUsersNewAlbum extends BaseAdapter {

	private LayoutInflater inflater;
	private ImageLoader imageLoader;
    
    private ArrayList<User> users;
    private ArrayList<String> members;
    
	public ListViewAdapterChooseUsersNewAlbum(Context context,ArrayList<User> users,ArrayList<String> members) {
		super();
		this.inflater = LayoutInflater.from(context);
        this.imageLoader = ImageLoader.getInstance();
        this.users = users;
        
        if(members != null)
        	this.members = members;
        else
        	this.members = new ArrayList<String>();
	}

	public ArrayList<String> getMembers() {
		return members;
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
	
	public class ViewHolder {
        ImageView profilePicture;
        TextView username;
        CheckBox checkbox;
    }

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		final User user = users.get(position);
		if(view == null) {
			holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_user_add_album,null);
            holder.username = (TextView) view.findViewById(R.id.username);
            holder.profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
            holder.checkbox = (CheckBox) view.findViewById(R.id.choose_user);
                        
            view.setTag(holder);
		}
		else
			holder = (ViewHolder) view.getTag();
		
		holder.username.setText(user.getUsername());
		imageLoader.displayImage(user.getProfilePicture(),holder.profilePicture);
        //Default profile photo
        if(user.getProfilePicture() == null)
        	holder.profilePicture.setImageResource(R.drawable.ic_launcher);
        
        if(members.contains(user.getId()))
        	holder.checkbox.setChecked(true);
        else
        	holder.checkbox.setChecked(false);
        
        holder.checkbox.setOnClickListener(new View.OnClickListener() { 
            @Override  
            public void onClick(View v) {  
            	String id = users.get(position).getId();
                if(((CheckBox)v).isChecked()) {  
                	members.add(id);
                }  
                else { 
                	members.remove(id);
                }  
            }  
        });  
		return view;
	}
	
	public boolean isUserCheck(String id) {
		return members.contains(id);
	}
}
