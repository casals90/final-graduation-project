package com.example.prototypetfgv2.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.example.prototypetfgv2.R;

public class InputAlbumMembersActivity extends FragmentActivity {
	
	private String albumTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_album_members);
		
		Bundle data = getIntent().getExtras();
		albumTitle = data.getString("albumTitle");
		
		goToFragmentNewAlbum();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		//getActionBar().setTitle("New album");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.no_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		/*switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
		    case android.R.id.home:
		        finish();
		        return true;
		   /* case R.id.accept:
		    	//TODO go Activity to add members
		    	//finish();
				break;
		    }*/
		return super.onOptionsItemSelected(item);
	}
	
	public void goToAddMembers() {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.container_new_album,new FragmentAddUsersNewAlbum(albumTitle));
		transaction.commit();
	}
	
	public void goToFragmentNewAlbum() {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.container_new_album,new FragmentNewAlbum(albumTitle));
		transaction.commit();
	}
}
