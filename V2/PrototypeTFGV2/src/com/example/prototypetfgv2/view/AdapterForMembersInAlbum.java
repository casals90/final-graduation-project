package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
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
	private Controller controller;
	private DisplayImageOptions options;
	private String idAdmin;
	private String idAlbum;
	private boolean currentUserIdAdmin;
	private Activity activity;
	private OnDeleteMemberFromAlbum callback;
		
	public AdapterForMembersInAlbum(Context context, ArrayList<User> members,String idAdmin,String idAlbum,Activity activity,OnDeleteMemberFromAlbum callback) {
		super();
		
		this.callback = callback;
		this.inflater = LayoutInflater.from(context);
		this.imageLoader = ImageLoader.getInstance();
		this.members = members;
		this.controller = (Controller) context.getApplicationContext();
		this.idAdmin = idAdmin;
		this.idAlbum = idAlbum;
		initDisplayOptions();
		if(controller.getCurrentUser().getId().compareTo(idAdmin) == 0)
			this.currentUserIdAdmin = true;
		else
			this.currentUserIdAdmin = false;
		this.activity = activity;
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
		
		if(currentUserIdAdmin) {
			holder.mImageButtonDelete.setVisibility(View.VISIBLE);
			holder.mImageButtonDelete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					DeleteMemberDialogFragment dialog = new DeleteMemberDialogFragment(user.getUsername(),user.getId(),idAlbum,members);
			        dialog.show(activity.getFragmentManager(), "add_friend_dialog");
				}
			});
		}
		else {
			holder.mImageButtonDelete.setVisibility(View.INVISIBLE);
			holder.mImageButtonDelete.setOnClickListener(null);
		}
		
		if(user.getId().compareTo(idAdmin) == 0) 
			holder.mTextViewLabelAdmin.setVisibility(View.VISIBLE);
		else
			holder.mTextViewLabelAdmin.setVisibility(View.INVISIBLE);
		
		if(user.getId().compareTo(controller.getCurrentUser().getId()) == 0) {
			holder.mTextViewUsername.setText("You");
			holder.mImageButtonDelete.setVisibility(View.INVISIBLE);
			holder.mImageButtonDelete.setOnClickListener(null);
		}
		else 
			holder.mTextViewUsername.setText(user.getUsername());
		
		
		
		return view;
	}
		
	private class DeleteAlbumMember extends AsyncTask<Void, Void, Boolean> {
		
		private String idUser,idAlbum;
		
		public DeleteAlbumMember(String idUser,String idAlbum) {
			this.idUser = idUser;
			this.idAlbum = idAlbum;
		}
		
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        }
 
        @Override
        protected Boolean doInBackground(Void... params) {
        	return controller.deleteAlbumMember(idAlbum, idUser);
        }

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result) {
				
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			//Toast.makeText(getActivity(),"Error download photos",  Toast.LENGTH_LONG).show();
		}	
    }
	
	public class DeleteMemberDialogFragment extends DialogFragment {
		
		private String username,idUser,idAlbum;
		private ArrayList<User> members;
		
		public DeleteMemberDialogFragment(String username, String idUser,String idAlbum,ArrayList<User> members) {
			this.username = username;
			this.idUser = idUser;
			this.idAlbum = idAlbum;
			this.members = members;
		}
		
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setTitle(R.string.delete_user);
	        //builder.setMessage(R.string.delete_user)
	        builder.setMessage("Delete "+username)
	               .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       new DeleteAlbumMember(idUser,idAlbum).execute();
	                       callback.onDeleteMember(idUser);	                       
	                   }
	               })
	               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       dialog.dismiss();
	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	}
}
