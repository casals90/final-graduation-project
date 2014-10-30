package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.R.id;
import com.example.prototypetfgv2.R.layout;
import com.example.prototypetfgv2.R.menu;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Album;
import com.example.prototypetfgv2.model.Comment;
import com.example.prototypetfgv2.model.Photo;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CommentsActivity extends Activity {
	
	private ListView listComments;
	private EditText newComment;
	private ImageButton send;
	private ProgressBar mProgressBar;
	
	private ListViewAdapterForComments adapter;
	private Controller controller;

	private ArrayList<Comment> comments;
	private Photo currentPhoto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments);
		
		controller = (Controller) getApplication().getApplicationContext();
		
		//Catch data
		Intent intent = getIntent();
		if(intent != null) {
			currentPhoto = intent.getParcelableExtra("photo");
		}
		
		listComments = (ListView) findViewById(R.id.like_comments);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
		newComment = (EditText) findViewById(R.id.new_comment);
		send = (ImageButton) findViewById(R.id.button_send_comment);
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Send comment
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comments, menu);
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
	
	private class DownloadCommentsTask extends AsyncTask<Void, Void, Boolean> {
    	
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	mProgressBar.setVisibility(View.VISIBLE);
        }
 
        @Override
        protected Boolean doInBackground(Void... params) {
        	comments = controller.getParseFunctions().getCommentsFromPhoto(currentPhoto.getId());
        	//TODO fer el mateix pels likes
        	//return true;
        	if(comments == null)
        		return false;
        	else {
        		currentPhoto.setComments(comments);
        		return true;
        	}		
        }
 
        @Override
        protected void onPostExecute(final Boolean success) {
        	if(success) {
	            adapter = new ListViewAdapterForComments(getApplicationContext(),comments);
	            listComments.setAdapter(adapter);	            
        	}
        	else {
        		mProgressBar.setVisibility(View.INVISIBLE);
        		Log.v("prototypev1", "no comments ");
        	}	
        }

		@Override
		protected void onCancelled() {
			super.onCancelled();
			//Toast.makeText(this,"Error download albums",  Toast.LENGTH_LONG).show();
		}
    }
	
}
