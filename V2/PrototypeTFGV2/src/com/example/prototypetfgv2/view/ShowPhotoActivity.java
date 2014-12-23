package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Photo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ShowPhotoActivity extends Activity {
	
	private ArrayList<Photo> photos;
	private int currentPosition;
	private String idAlbum;
	private DisplayImageOptions options;
	private Controller controller;
	
	private FullScreenImageAdapter fullScreenAdapter;
	private ViewPager mViewPager;
	private ViewHolderActionBar viewHolderActionBar;
	private Activity activity;

	public void initDisplayOptions() {
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_launcher) // resource or drawable
        .showImageForEmptyUri(R.drawable.ic_launcher) // resource or drawable
        .showImageOnFail(R.drawable.ic_launcher) // resource or drawable
        .cacheInMemory(true) 
        .cacheOnDisk(true) 
        .considerExifParams(true)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .build();	 
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.activity_fullscreen_view);
		initActionBar();
		this.activity = this;
		controller = (Controller) getApplication();
		initDisplayOptions();
		Intent data = getIntent();
		if(data != null) {
			photos = data.getParcelableArrayListExtra("photos");
			currentPosition = data.getIntExtra("currentPosition",0);
			idAlbum = data.getStringExtra("idAlbum");
		}
		
		fullScreenAdapter = new FullScreenImageAdapter(this, photos,currentPosition,idAlbum);
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(fullScreenAdapter);
		mViewPager.setCurrentItem(currentPosition);
		mViewPager.setPageMargin(50);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				updateActionBar(position);
				currentPosition = position;
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	public void updateActionBar(int position) {
		Photo photo = photos.get(position);
		ImageLoader.getInstance().displayImage(photo.getOwnerUser().getProfilePicture(),viewHolderActionBar.mImageView,options);
		viewHolderActionBar.mTextViewPhotoTitle.setText(photo.getTitle());
		viewHolderActionBar.mTextViewUsername.setText(photo.getOwnerUser().getUsername());
	}
	
	public void initActionBar() {
		ActionBar actionBar = getActionBar();
		
		actionBar.setCustomView(R.layout.custom_action_bar_full_screen);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
		
		viewHolderActionBar = new ViewHolderActionBar();
		viewHolderActionBar.mImageView = (ImageView) actionBar.getCustomView().findViewById(R.id.profile_picture);
		viewHolderActionBar.mTextViewPhotoTitle = (TextView) actionBar.getCustomView().findViewById(R.id.photo_title);
		viewHolderActionBar.mTextViewUsername = (TextView) actionBar.getCustomView().findViewById(R.id.username);
	}
	
	private class ViewHolderActionBar {
		ImageView mImageView;
		TextView mTextViewUsername,mTextViewPhotoTitle;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_photo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
			case android.R.id.home:
				finish();
		        break;
			case R.id.delete_photo:
				deletePhoto(photos.get(currentPosition));
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void deletePhoto(Photo photo) {
		if(photo.getOwnerUser().getId().compareTo(controller.getCurrentUser().getId()) == 0) 
			new ConfirmationDeleteDialogFragment().show(getFragmentManager(),"confirm_delete");
		else 
			new InformationDialogFragment().show(getFragmentManager(),"no_delete");
	}
	
	public class InformationDialogFragment extends DialogFragment {
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage(R.string.not_delete_photo)
	               .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   dismiss();
	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	}
	
	public class ConfirmationDeleteDialogFragment extends DialogFragment {
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage(R.string.confirm_delete_photo)
	               .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // FIRE ZE MISSILES!
	                	   dismiss();
	                	   new DeletePhotosTask().execute();
	                   }
	               })
	               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                	   dismiss();
                   }
               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	}

	private class DeletePhotosTask extends AsyncTask<Void, Void, Boolean> {
		
		private ProgressDialog mProgressDialog;
		
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	mProgressDialog = ProgressDialog.show(activity,"Delete Photo","waiting", true);
        }
 
        @Override
        protected Boolean doInBackground(Void... params) {
        	return controller.deletePhoto(photos.get(currentPosition),idAlbum);
        }

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result) {
				photos.remove(currentPosition);
				if(photos.size() > 0) {
					mViewPager.setAdapter(new FullScreenImageAdapter(activity, photos,currentPosition,idAlbum));
					mProgressDialog.dismiss();
				}
				else
					finish();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			//Toast.makeText(getActivity(),"Error download photos",  Toast.LENGTH_LONG).show();
		}	
    }
}
