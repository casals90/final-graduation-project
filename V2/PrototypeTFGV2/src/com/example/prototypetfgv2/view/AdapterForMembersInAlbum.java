package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AdapterForMembersInAlbum extends BaseAdapter {

	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	private ArrayList<User> members;
	private ArrayList<String> following;
	private ArrayList<String> followers;
	private Controller controller;
	private DisplayImageOptions options;
	private String idAdmin;
	private String idAlbum;
		
	public AdapterForMembersInAlbum(Context context, ArrayList<User> members,String idAdmin,String idAlbum) {
		super();
		
		this.inflater = LayoutInflater.from(context);
		this.imageLoader = ImageLoader.getInstance();
		this.members = members;
		this.controller = (Controller) context.getApplicationContext();
		this.following = controller.getCurrentUser().getFollowing();
		this.followers = controller.getCurrentUser().getFollowers();
		this.idAdmin = idAdmin;
		this.idAlbum = idAlbum;
		initDisplayOptions();
	}
	
	public void initDisplayOptions() {
		options = new DisplayImageOptions.Builder()
        .showImageForEmptyUri(R.drawable.ic_launcher) // resource or drawable
        .showImageOnFail(R.drawable.ic_launcher) // resource or drawable
        .resetViewBeforeLoading(true) 
        .considerExifParams(true)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .build();
	}

	@Override
	public int getCount() {
		return members.size();
	}

	@Override
	public Object getItem(int position) {
		return members.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public class ViewHolder {
		ImageView mImageViewProfilePicture;
		TextView mTextViewUsername,mTextViewLabelAdmin;
		ImageButton mImageButtonDelete;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		final User user = members.get(position);
		if(view == null) {
			holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_members_list, null);
            
            holder.mImageViewProfilePicture = (ImageView) view.findViewById(R.id.profile_picture);
            holder.mTextViewUsername = (TextView) view.findViewById(R.id.username);
            holder.mTextViewLabelAdmin = (TextView) view.findViewById(R.id.admin);
            holder.mImageButtonDelete = (ImageButton) view.findViewById(R.id.button_delete);
                        
            view.setTag(holder);
		}
		else 
			holder = (ViewHolder) view.getTag();
		
		imageLoader.displayImage(user.getProfilePicture(),holder.mImageViewProfilePicture,options);
		
		if(user.getId().compareTo(idAdmin) == 0) {
			Log.v("prototypev1","soc admin");
			holder.mTextViewLabelAdmin.setVisibility(View.VISIBLE);
			holder.mImageButtonDelete.setVisibility(View.VISIBLE);
			holder.mImageButtonDelete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.v("prototypev1","delete user from album");
					new DeleteAlbumMember().execute(user.getId());
				}
			});
		}
		else {
			holder.mTextViewLabelAdmin.setVisibility(View.INVISIBLE);
			holder.mImageButtonDelete.setVisibility(View.INVISIBLE);
			holder.mImageButtonDelete.setOnClickListener(null);
		}
		
		if(user.getId().compareTo(controller.getCurrentUser().getId()) == 0) {
			holder.mTextViewUsername.setText("You");
			holder.mImageButtonDelete.setVisibility(View.INVISIBLE);
			holder.mImageButtonDelete.setOnClickListener(null);
		}
		else 
			holder.mTextViewUsername.setText(user.getUsername());
		
		return view;
	}
		
	private class DeleteAlbumMember extends AsyncTask<String, Void, Boolean> {
		
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        }
 
        @Override
        protected Boolean doInBackground(String... params) {
        	String idUser = params[0];
        	return controller.deleteAlbumMember(idAlbum, idUser);
        }

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result) {
				//Toast.makeText(activity,"Delete Following!",  Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			//Toast.makeText(getActivity(),"Error download photos",  Toast.LENGTH_LONG).show();
		}	
    }
}
