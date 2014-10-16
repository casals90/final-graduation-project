package com.example.prototypetfgv2.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.model.User;

public class ListViewAdapterChooseUsersNewAlbum extends BaseAdapter{

	private LayoutInflater inflater;
    private ImageLoader imageLoader;
    
    private List<User> users;
    private ArrayList<String> members;
    
	public ListViewAdapterChooseUsersNewAlbum(Context context,List<User> users,ArrayList<String> members) {
		super();
		this.inflater = LayoutInflater.from(context);
        this.imageLoader = new ImageLoader(context);
        //this.controller = (Controller) context.getApplicationContext();       
        this.users = users;
        
        Log.v("prototypev1", "Constructor ListViewAdapterChooseUsersNewAlbum");
        
        if(members != null)
        	this.members = members;
        else
        	this.members = new ArrayList<String>();
        
        Log.v("prototypev1", "members size = "+members.size()); 
	}

	public ArrayList<String> getMembers() {
		return members;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return users.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return users.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public class ViewHolder {
        ImageView profilePicture;
        TextView username;
        CheckBox checkbox;
    }

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
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
		
		holder.username.setText(users.get(position).getUsername());
		imageLoader.DisplayImage(users.get(position).getProfilePicture(),holder.profilePicture);
        //Default profile photo
        if(users.get(position).getProfilePicture() == null)
        	holder.profilePicture.setImageResource(R.drawable.ic_launcher);
        
        //Log.v("prototypev1", "comprovar si esta selecionat "+Utils.stringIsInArrayList(members,users.get(position).getId()));
        if(Utils.stringIsInArrayList(members,users.get(position).getId()))
        	holder.checkbox.setChecked(true);
        holder.checkbox.setOnClickListener(new View.OnClickListener() { 
            @Override  
            public void onClick(View v) {  
            	String id = users.get(position).getId();
                if(((CheckBox)v).isChecked()) {  
                    //Add to checkbox array
                	Log.v("prototypev1", "clico la de"+id);
                	members.add(id);
                }  
                else { 
                    //Remove from checkbox array
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
