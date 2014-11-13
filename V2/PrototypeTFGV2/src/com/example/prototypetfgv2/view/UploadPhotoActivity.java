package com.example.prototypetfgv2.view;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.utils.BitmapUtils;

public class UploadPhotoActivity extends Activity {

	private ImageView mImageView;
	private ImageButton send;
	private EditText mEditText;
	private View mProgressBar;
	
	private String mCurrentPhotoPath;
	
	private Bitmap photo;
	
	private Controller controller;
	private static Activity activity;
	
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_photo);
		
		controller = (Controller) getApplicationContext();
		
		activity = this;
		
		mEditText = (EditText) findViewById(R.id.photo_title);
		mImageView = (ImageView) findViewById(R.id.new_image);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
		send = (ImageButton) findViewById(R.id.send_title);
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String title = mEditText.getText().toString();
				if(title.length() > 0) {
					mEditText.setText("");
					controller.updatePhoto(photo, title,activity);
				}
				finish();
			}
		});
		Bundle data = getIntent().getExtras();
		if(data != null)
			mCurrentPhotoPath = data.getString("pathNewPhoto");

    	new CreateBitmapTask().execute();
	}
	
	/*public void initDisplayOptions() {
		options = new DisplayImageOptions.Builder()
        .showImageForEmptyUri(R.drawable.ic_launcher) // resource or drawable
        .showImageOnFail(R.drawable.ic_launcher) // resource or drawable
        .resetViewBeforeLoading(true) 
        .considerExifParams(true)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .build();
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.upload_photo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class CreateBitmapTask extends AsyncTask<Void, Void, Boolean> {
    	
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	mProgressBar.setVisibility(View.VISIBLE);
        	mImageView.setVisibility(View.INVISIBLE);
        }
 
        @Override
        protected Boolean doInBackground(Void... params) {
        	File f = new File(mCurrentPhotoPath);
        	photo = BitmapUtils.decodeFileForDisplay(f,activity);
        	if(photo != null)
        		return true;
        	return false;
        }
 
        @Override
        protected void onPostExecute(Boolean result) {
        	mProgressBar.setVisibility(View.INVISIBLE);
        	mImageView.setVisibility(View.VISIBLE);
        	mImageView.setImageBitmap(photo);
        }

		@Override
		protected void onCancelled() {
			super.onCancelled();
			Toast.makeText(getApplicationContext(),"Error download albums",  Toast.LENGTH_LONG).show();
		}
    }
}
