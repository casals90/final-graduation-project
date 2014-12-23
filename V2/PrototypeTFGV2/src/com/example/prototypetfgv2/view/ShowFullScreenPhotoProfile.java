package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Photo;


public class ShowFullScreenPhotoProfile extends Activity {

	private ArrayList<Photo> photos;
	private int currentPosition;
	private ViewPager mViewPager;
	private FullScreenPhotosProfileAdapter adapter;
	private Controller controller;
	private String idAlbum;
	private Activity activity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.activity_show_full_screen_photo_profile);
		this.controller = (Controller) getApplication();
		
		activity = this;
		Intent data = getIntent();
		if(data != null) {
			photos = data.getParcelableArrayListExtra("photos");
			currentPosition = data.getIntExtra("currentPosition",0);
		}
		
		adapter = new FullScreenPhotosProfileAdapter(this,photos);
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(currentPosition);
		mViewPager.setPageMargin(50);
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				currentPosition = position;		
				Log.v("prototypev1", "currentpos "+currentPosition);
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
		getActionBar().setTitle(R.string.my_photos);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_full_screen_photo_profile, menu);
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
				Log.v("prototypev1","home button");
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
		idAlbum = photo.getOwnerAlbum();
		new ConfirmationDeleteDialogFragment().show(getFragmentManager(),"");
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
					mProgressDialog.dismiss();
					mViewPager.setAdapter(new FullScreenPhotosProfileAdapter(activity,photos));
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
