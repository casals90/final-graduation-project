package com.example.prototypetfgv2.view;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;

public class SetAlbumTitleActivity extends Activity {

	private Controller controller;
	
	private EditText mEditTextAlbumTitle;
	private ImageButton mImageButtonAccept, mImageButtonRemove;

	private TextView mTextViewTitleActionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_album_title);
		
		initActionBar();
		
		Intent bundle = getIntent();
		String idAlbum = bundle.getStringExtra("idAlbum");
		String title = bundle.getStringExtra("title");
		
		controller = (Controller) getApplicationContext();
		
		mEditTextAlbumTitle = (EditText) findViewById(R.id.albumTitle);
		mEditTextAlbumTitle.setText(title);
		mEditTextAlbumTitle.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,KeyEvent event) {
				int result = actionId & EditorInfo.IME_MASK_ACTION;
		        switch(result) {
			        case EditorInfo.IME_ACTION_DONE:
			            // done stuff
			        	//new UpdateUseNameTask().execute();
			            break;
			    }
		        return false;
			}
		});
		
		//Request focus and show soft keyboard automatically
        mEditTextAlbumTitle.requestFocus();
        getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.input_username, menu);
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
	
	public void initActionBar() {
		ActionBar actionBar = getActionBar();

		actionBar.setCustomView(R.layout.custom_action_bar_log_in);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		
		mTextViewTitleActionBar = (TextView) getActionBar().getCustomView().findViewById(R.id.label);
		mTextViewTitleActionBar.setText(getString(R.string.set_album_title_upper));
		
		mImageButtonRemove = (ImageButton) getActionBar().getCustomView().findViewById(R.id.button_back);
		mImageButtonRemove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Go to login activity
				controller.logout();
				finish();
			}
		});
		mImageButtonAccept = (ImageButton) getActionBar().getCustomView().findViewById(R.id.button_accept);
		mImageButtonAccept.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//new UpdateUseNameTask().execute();
			}
		});
	}
	
	
	/*public class UpdateUseNameTask extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog progressDialog;
		
		@Override
	    protected void onPreExecute() {
			username = mEditTextAlbumTitle.getText().toString();
			mTextViewInocrrectUsername.setVisibility(View.INVISIBLE);
	        progressDialog= ProgressDialog.show(SetAlbumTitleActivity.this, "Set username","waiting", true);       
	    };
		
		@Override
		protected Boolean doInBackground(Void... params) {
			if(controller.setUsername(username)) {
				controller.downloadCurrentUser();
				controller.getAllLikes();
				//import profile picture from social network
				//check if user login with Twitter
				if(controller.isLinkedWithTwitter())
					controller.setProfilePictureFromTwitter();
				return true;
			}
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			progressDialog.dismiss();
			if (success) {
				if(controller.isLinkedWithFacebook()) {
					Session session = ParseFacebookUtils.getSession();
				    if (session != null && session.isOpened())
				    	controller.importProfilePhotoFromFacebook();
				}
			    //Save data
			    saveData();
				goToMainActivity();
			} 
			else {
				Log.v("prototypev1","incorrecte onPostExecute update username");
				mTextViewInocrrectUsername.setVisibility(View.VISIBLE);
			}
		}

		@Override
		protected void onCancelled() {
			progressDialog.dismiss();
			mTextViewInocrrectUsername.setVisibility(View.VISIBLE);
			Log.v("prototypev1","log in cancelat 2");
		}
	}*/
}
