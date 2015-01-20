package com.example.prototypetfgv2.view;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;

public class SetAlbumTitleActivity extends Activity {

	private Controller controller;
	
	private EditText mEditTextAlbumTitle;
	private String newAlbumTitle ,idAlbum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_album_title);
		
		//initActionBar
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
		Intent bundle = getIntent();
		idAlbum = bundle.getStringExtra("idAlbum");
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
			        	new SetAlbumTitleTask().execute();
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
		getMenuInflater().inflate(R.menu.input_album_title, menu);
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
			case R.id.accept:
				new SetAlbumTitleTask().execute();
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	public class SetAlbumTitleTask extends AsyncTask<Void, Void, Boolean> {
		ProgressDialog progressDialog;
		
		@Override
	    protected void onPreExecute() {
			newAlbumTitle = mEditTextAlbumTitle.getText().toString();
	        progressDialog= ProgressDialog.show(SetAlbumTitleActivity.this, "Set album title","waiting", true);       
	    };
		
		@Override
		protected Boolean doInBackground(Void... params) {
			if(controller.setAlbumTitle(idAlbum, newAlbumTitle)) {
				return true;
			}
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			progressDialog.dismiss();
			if (success) {
				finish();
			} 
			else {
				Log.v("prototypev1","incorrecte onPostExecute update username");
			}
		}

		@Override
		protected void onCancelled() {
			progressDialog.dismiss();
			Log.v("prototypev1","set album title cancelat 2");
		}
	}
}
