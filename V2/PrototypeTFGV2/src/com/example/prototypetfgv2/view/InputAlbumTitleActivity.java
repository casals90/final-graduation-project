package com.example.prototypetfgv2.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prototypetfgv2.R;

public class InputAlbumTitleActivity extends Activity {

	private EditText mEditTextAlbumTitle;
	private String albumTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_album_title);
		
		mEditTextAlbumTitle = (EditText) findViewById(R.id.album_title);
		//Request focus and show soft keyboard automatically
		mEditTextAlbumTitle.requestFocus();
        getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("New album");
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
		switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
		    case android.R.id.home:
		        finish();
		        return true;
		    case R.id.accept:
		    	//TODO go Activity to add members
		    	albumTitle = mEditTextAlbumTitle.getText().toString();
		    	if(albumTitle.length() < 1) {
		    		Toast.makeText(this,R.string.input_album_title,  Toast.LENGTH_LONG).show();
		    	}
		    	else
		    		goToAddMembers();
				break;
		    }
		return super.onOptionsItemSelected(item);
	}
	
	public void goToAddMembers() {
		Intent addMembers = new Intent(this,InputAlbumMembersActivity.class);
		addMembers.putExtra("albumTitle",albumTitle);
		startActivity(addMembers);
		finish();
	}
	
}
