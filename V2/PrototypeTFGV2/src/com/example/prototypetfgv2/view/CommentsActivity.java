package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Comment;
import com.example.prototypetfgv2.model.Photo;

public class CommentsActivity extends Activity {
	
	private ListView listComments;
	private EditText newComment;
	private ImageButton send;
	private ProgressBar mProgressBar;
	
	private ListViewAdapterForComments adapter;
	private ArrayList<Comment> comments;
	private ArrayList<String> likes;
	private Controller controller;
	private Photo currentPhoto;
	
	private String text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments);
		
		controller = (Controller) getApplication().getApplicationContext();
		comments = new ArrayList<Comment>();
		
		//Catch data
		Intent intent = getIntent();
		if(intent != null)
			currentPhoto = intent.getParcelableExtra("photo");
		
		listComments = (ListView) findViewById(R.id.list_comments);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
		newComment = (EditText) findViewById(R.id.new_comment);
		send = (ImageButton) findViewById(R.id.button_send_comment);
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Send comment	
				text = newComment.getText().toString();
				if(text.length() > 0) {
					newComment.setText("");
					new SendCommentTask().execute();
				}
				else
					Toast.makeText(getApplicationContext(), "Input comment",Toast.LENGTH_LONG).show();
			}
		});
		
		new DownloadCommentsTask().execute();
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
        	likes = controller.getParseFunctions().getLikes(currentPhoto.getId());
        	Log.v("prototypev1", "do in background comments "+comments.size()+" likes "+likes.size());
        	if(comments != null && likes != null) {
        		return true;
        	}
        	return false;		
        }
  
        @Override
        protected void onPostExecute(final Boolean success) {
        	Log.v("prototypev1", "onPostExecute success=  "+success);
        	if(success) {
        		mProgressBar.setVisibility(View.INVISIBLE);
	            adapter = new ListViewAdapterForComments(getApplicationContext(),comments);
	            Log.v("prototypev1", "success ");
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
			Log.v("prototypev1", "cancel ");
			mProgressBar.setVisibility(View.INVISIBLE);
		}
    }
	
	@Override
	public void onBackPressed() {
		Log.v("prototypev1", "press back button");
		super.onBackPressed();
	}
	
	
	
	private class SendCommentTask extends AsyncTask<Void, Void, Boolean> {
    	
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	mProgressBar.setVisibility(View.VISIBLE);
        }
 
        @Override
        protected Boolean doInBackground(Void... params) {
        	return controller.newComment(currentPhoto.getId(), text);
        }
 
        @Override
        protected void onPostExecute(final Boolean success) {
        	if(success) {
        		//Adding new comment in a arrayList and send to adapter
        		comments.add(new Comment(currentPhoto.getOwnerUser(),text,currentPhoto.getCreatedAt()));
        		mProgressBar.setVisibility(View.INVISIBLE);
	            adapter = new ListViewAdapterForComments(getApplicationContext(),comments);
	            listComments.setAdapter(adapter);	            
        	}
        	else {
        		mProgressBar.setVisibility(View.INVISIBLE);
        	}	
        }

		@Override
		protected void onCancelled() {
			super.onCancelled();
			//Toast.makeText(this,"Error download albums",  Toast.LENGTH_LONG).show();
		}
    }
}
