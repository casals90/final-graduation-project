package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.model.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ListViewAdapterForAddMembers extends BaseAdapter {

	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	private ArrayList<User> users;
		
	public ListViewAdapterForAddMembers(Context context, ArrayList<User> users) {
		super();
		
		this.inflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
		this.users = users;
	}
	
	public ArrayList<String> getMembers() {
		ArrayList<String> members = new ArrayList<String>();
		Log.v("prototypev1", "getMembers "+users.size());
		for(int i = 0; i < users.size(); i++) {
			members.add(users.get(i).getId());
		}
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
        ImageButton buttonDelete;
    }

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		final User user = users.get(position);
		if(view == null) {
			holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_user_list_members,null);
            holder.username = (TextView) view.findViewById(R.id.username);
            holder.profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
            holder.buttonDelete = (ImageButton) view.findViewById(R.id.delete);
                        
            view.setTag(holder);
		}
		else 
			holder = (ViewHolder) view.getTag();
		
		holder.username.setText(user.getUsername());
		imageLoader.displayImage(user.getProfilePicture(),holder.profilePicture);
		//Default profile photo
        if(user.getProfilePicture() == null)
        	holder.profilePicture.setImageResource(R.drawable.ic_launcher);
		holder.buttonDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Delete user
				deleteUser(position);
			}
			
		});
		return view;
	}
	
	public void deleteUser(int position) {
		users.remove(position);
		notifyDataSetChanged();
	}
}
