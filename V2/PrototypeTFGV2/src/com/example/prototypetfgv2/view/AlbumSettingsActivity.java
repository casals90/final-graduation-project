package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Album;
import com.example.prototypetfgv2.model.User;
import com.example.prototypetfgv2.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AlbumSettingsActivity extends Activity implements OnDeleteMemberFromAlbum {

	private Album album;
	private ArrayList<User> members;
	private Controller controller;
	private ImageLoader imageLoader;
	private String idAlbum;
	private String usernameAdmin;
	
	private ProgressBar mProgressBar;
	private ListView mListView;
	private TextView mTextViewAlbumTitle;
	private TextView mTextViewDate;
	private TextView mTextViewAdmin;
	private ImageView mImageViewCover;
	private ImageButton mImageButtonEdit;
	private Button mButtonLeave,mButtonDelete,mButtonAddMembers;
	private Activity activity;
	private OnDeleteMemberFromAlbum callback;
	
	private LinearLayout mLinearLayoutHeader, mLinearLayoutPanelList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album_settings);
		
		this.controller = (Controller) getApplication();
		this.callback = this;
		Intent extras = getIntent();
		idAlbum = extras.getStringExtra("idAlbum");
	
		this.imageLoader = ImageLoader.getInstance();
		this.activity = this;
		this.mLinearLayoutHeader = (LinearLayout) findViewById(R.id.header);
		this.mLinearLayoutPanelList = (LinearLayout) findViewById(R.id.panel_list);
		
		this.mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
		this.mTextViewAlbumTitle = (TextView) findViewById(R.id.album_title);
		this.mTextViewDate = (TextView) findViewById(R.id.createdAt);
		this.mTextViewAdmin = (TextView) findViewById(R.id.createdBy);
		this.mImageViewCover = (ImageView) findViewById(R.id.album_cover);	
		this.mListView = (ListView) findViewById(R.id.list_view_members);
		this.mButtonAddMembers = (Button) findViewById(R.id.add_members);
		this.mButtonAddMembers.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				goToAddMembers();
				
			}
		});
		this.mImageButtonEdit = (ImageButton) findViewById(R.id.button_edit_title);
		mImageButtonEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goToSetAlbumTitle();
			}
		});
		
		mButtonDelete = (Button) findViewById(R.id.button_delete_album_admin);
		mButtonLeave = (Button) findViewById(R.id.button_leave_user);
		
		
	}
	
	public void hiddenAll() {
		mLinearLayoutHeader.setVisibility(View.INVISIBLE);
		mLinearLayoutPanelList.setVisibility(View.INVISIBLE);
		mButtonDelete.setVisibility(View.INVISIBLE);
		mButtonLeave.setVisibility(View.INVISIBLE);
	}
	
	public void showAll() {
		mLinearLayoutHeader.setVisibility(View.VISIBLE);
		mLinearLayoutPanelList.setVisibility(View.VISIBLE);
	}
	
	public void goToAddMembers() {
		Intent addMembers = new Intent(this,AddMembersInAlbumActivity.class);
		addMembers.putExtra("members",getMembersId(members));
		addMembers.putExtra("idAlbum",idAlbum);
		startActivity(addMembers);
	}
	
	public ArrayList<String> getMembersId(ArrayList<User> members) {
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < members.size(); i ++) {
			list.add(members.get(i).getId());
		}
		return list;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		getActionBar().setTitle("Album settings");
		
		new DownloadAlbumTask().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.album_settings, menu);
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
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public String createdAtDate(String createdAt) {
		String[] split = createdAt.split(" ");
		String month = split[1];
		int monthNumber = Utils.monthNameToInt(month);
		String day = split[2];
		String time = split[3];
		String[] timeSplit = time.split(":");
		String hours = timeSplit[0];
		String minuts = timeSplit[1];
		String year = split[5];
		//dd/M/yyyy HH:mm
		return day+"/"+monthNumber+"/"+year+" "+hours+":"+minuts;
	}
	
	public void goToSetAlbumTitle() {
		Intent setTitle = new Intent(this,SetAlbumTitleActivity.class);
		setTitle.putExtra("idAlbum",album.getId());
		setTitle.putExtra("title",album.getAlbumTitle());
		startActivity(setTitle);
	}
	
	private class DownloadAlbumTask extends AsyncTask<Void, Void, Boolean> {
    	
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	hiddenAll();
        	mProgressBar.setVisibility(View.VISIBLE);
        }
 
        @Override
        protected Boolean doInBackground(Void... params) {
        	album = controller.downloadAlbum(idAlbum);
            if(album != null) {
            	usernameAdmin = controller.getUsernameFromUserId(album.getIdAdmin());
            	album.setCreatedAt(createdAtDate(album.getCreatedAt()));
            	return true;
            }
            return false;
            		
        }
 
        @Override
        protected void onPostExecute(final Boolean success) {
        	if(success) {
        		
        		if(controller.getCurrentUser().getId().equals(album.getIdAdmin()))
        			Log.v("prototypev1","admin");
        		else
        			Log.v("prototypev1","noAdmin");
        		
        		imageLoader.displayImage(album.getAlbumCover(),mImageViewCover);
        		
        		mTextViewAlbumTitle.setText(album.getAlbumTitle());
        		
        		mTextViewDate.setText(getString(R.string.createdAt)+" "+album.getCreatedAt());
        		mTextViewAdmin.setText(getString(R.string.createdBy)+" "+usernameAdmin);
        		
        		new DownloadMembersTask().execute();
        	}
        }

		@Override
		protected void onCancelled() {
			super.onCancelled();
			Toast.makeText(getApplicationContext(),"Error download albums settings",Toast.LENGTH_LONG).show();
		}
    }
	
	private class DownloadMembersTask extends AsyncTask<Void, Void, Boolean> {
    	
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	//mProgressBar.setVisibility(View.VISIBLE);
        }
 
        @Override
        protected Boolean doInBackground(Void... params) {
        	members = controller.downloadMembersFromAlbum(idAlbum);
            if(members != null) {
            	return true;
            }
            return false;
            		
        }
 
        @Override
        protected void onPostExecute(final Boolean success) {
        	if(success) {
        		mProgressBar.setVisibility(View.INVISIBLE);
        		showAll();
        		//Check if current user is admin
        		if(controller.getCurrentUser().getId().compareTo(album.getIdAdmin()) == 0) {
        			mButtonDelete.setVisibility(View.VISIBLE);
        			mButtonDelete.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							new DeleteAlbumTask().execute();
						}
					});
        		}
        		else {
        			mButtonLeave.setVisibility(View.VISIBLE);
        			mButtonLeave.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							new DeleteAlbumMemberTask().execute(controller.getCurrentUser().getId());
							goToMainActivity();
						}
					});
        		}
        		mListView.setAdapter(new AdapterForMembersInAlbum(getApplicationContext(),members,album.getIdAdmin(),album.getId(),activity,callback));
        	}
        }

		@Override
		protected void onCancelled() {
			super.onCancelled();
			Toast.makeText(getApplicationContext(),"Error download albums settings",Toast.LENGTH_LONG).show();
		}
    }
	
	//Functions to change activities
	public void goToMainActivity() {
		Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();
	}
	
	private class DeleteAlbumMemberTask extends AsyncTask<String, Void, Boolean> {
		
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        }
 
        @Override
        protected Boolean doInBackground(String... params) {
        	String idUser = params[0];
        	return controller.deleteAlbumMember(idAlbum, idUser);
        }

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result) {
				//Toast.makeText(activity,"Delete Following!",  Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			//Toast.makeText(getActivity(),"Error download photos",  Toast.LENGTH_LONG).show();
		}	
    }
	
	private class DeleteAlbumTask extends AsyncTask<String, Void, Boolean> {
		
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        }
 
        @Override
        protected Boolean doInBackground(String... params) {
        	return controller.deleteAlbum(idAlbum);
        }

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result) {
				//Toast.makeText(activity,"Delete Following!",  Toast.LENGTH_SHORT).show();
				goToMainActivity();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			//Toast.makeText(getActivity(),"Error download photos",  Toast.LENGTH_LONG).show();
		}	
    }

	@Override
	public void onDeleteMember(String idMember) {
		members.remove(idMember);
		new DownloadMembersTask().execute();
		
	}
}
