package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
/**
 * Activity class for album settings
 * @author jordi
 *
 */
public class AlbumSettingsActivity extends Activity implements OnDeleteMemberFromAlbum {

	private Album album;
	private ArrayList<User> members;
	private Controller controller;
	private ImageLoader imageLoader;
	private String idAlbum;
	private String usernameAdmin;
	private View line2,line3;
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
	private LinearLayout panel_list;
	
	private LinearLayout mLinearLayoutHeader, mLinearLayoutPanelList;
	private DisplayImageOptions options;
	/*
	 * method to init the view
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
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
		
		this.mButtonDelete = (Button) findViewById(R.id.button_delete_album_admin);
		this.mButtonLeave = (Button) findViewById(R.id.button_leave_user);
		this.line2 = (View) findViewById(R.id.line2);
		this.line3 = (View) findViewById(R.id.line3);
		
		this.mButtonAddMembers = (Button) findViewById(R.id.add_members);
		
		this.mImageButtonEdit = (ImageButton) findViewById(R.id.button_edit_title);
		mImageButtonEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goToSetAlbumTitle();
			}
		});		
		
		initDisplayOptions();
	}
	/**
	 * method to hide all view
	 */
	public void hiddenAll() {
		mLinearLayoutHeader.setVisibility(View.INVISIBLE);
		mLinearLayoutPanelList.setVisibility(View.INVISIBLE);
		mButtonDelete.setVisibility(View.INVISIBLE);
		mButtonLeave.setVisibility(View.INVISIBLE);
	}
	/**
	 * method to show all view
	 */
	public void showAll() {
		mLinearLayoutHeader.setVisibility(View.VISIBLE);
		mLinearLayoutPanelList.setVisibility(View.VISIBLE);
	}
	/**
	 * method that init Universal Image Loader
	 */
	public void initDisplayOptions() {
		options = new DisplayImageOptions.Builder()
        .showImageForEmptyUri(R.drawable.ic_launcher_test) // resource or drawable
        .showImageOnFail(R.drawable.ic_launcher_test) // resource or drawable
        .resetViewBeforeLoading(true) 
        .considerExifParams(true)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .build();
	}
	/**
	 * method to change current Activity to AddMembersInAlbumActivity
	 */
	public void goToAddMembers() {
		Intent addMembers = new Intent(this,AddMembersInAlbumActivity.class);
		addMembers.putExtra("members",getMembersId(members));
		addMembers.putExtra("idAlbum",idAlbum);
		startActivity(addMembers);
	}
	/**
	 * get method that return id from members users 
	 * @param members list of album members
	 * @return id list from members
	 */
	public ArrayList<String> getMembersId(ArrayList<User> members) {
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < members.size(); i ++) {
			list.add(members.get(i).getId());
		}
		return list;
	}
	/*
	 * method to update view
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		getActionBar().setTitle("Album settings");
		
		new DownloadAlbumTask().execute();
	}
	/*
	 * method to create menu with specific options
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.album_settings, menu);
		return true;
	}
	/*
	 * method that specify what to do when user click menu option
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
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
	/**
	 * method to create custom format date
	 * @param createdAt date
	 * @return custom format date
	 */
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
	/**
	 * method to change current Activity to SetAlbumTitleActivity
	 */
	public void goToSetAlbumTitle() {
		Intent setTitle = new Intent(this,SetAlbumTitleActivity.class);
		setTitle.putExtra("idAlbum",album.getId());
		setTitle.putExtra("title",album.getAlbumTitle());
		startActivity(setTitle);
	}
	/**
	 * class to download album
	 * @author jordi
	 *
	 */
	private class DownloadAlbumTask extends AsyncTask<Void, Void, Boolean> {
    	/*
    	 * (non-Javadoc)
    	 * @see android.os.AsyncTask#onPreExecute()
    	 */
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	hiddenAll();
        	mProgressBar.setVisibility(View.VISIBLE);
        }
        /*
         * method to download album
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
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
        /*
         * method to update the view
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(final Boolean success) {
        	if(success) {
        		        		
        		imageLoader.displayImage(album.getAlbumCover(),mImageViewCover,options);
        		mTextViewAlbumTitle.setText(album.getAlbumTitle());
        		mTextViewDate.setText(getString(R.string.createdAt)+" "+album.getCreatedAt());
        		mTextViewAdmin.setText(getString(R.string.createdBy)+" "+usernameAdmin);
        		
        		//is admin
        		if(controller.getCurrentUser().getId().compareTo(album.getIdAdmin()) == 0) {
        			mButtonAddMembers.setOnClickListener(new OnClickListener() {
        				@Override
        				public void onClick(View arg0) {
        					goToAddMembers();
        				}
        			});
        		}
        		//not admin
        		else {
        			mButtonAddMembers.setVisibility(View.INVISIBLE);
        			panel_list = (LinearLayout) findViewById(R.id.panel_list);
        			panel_list.removeView(line2);
        			panel_list.removeView(mButtonAddMembers);
        			panel_list.removeView(line3);
        		}
        		
        		new DownloadMembersTask().execute();
        	}
        }
        /*
	     * method that execute if the thread is cancel
	     * @see android.os.AsyncTask#onCancelled()
	     */
		@Override
		protected void onCancelled() {
			super.onCancelled();
			Toast.makeText(getApplicationContext(),"Error download albums settings",Toast.LENGTH_LONG).show();
		}
    }
	/**
	 * class to download members of album
	 * @author jordi
	 *
	 */
	private class DownloadMembersTask extends AsyncTask<Void, Void, Boolean> {
    	/*
    	 * (non-Javadoc)
    	 * @see android.os.AsyncTask#onPreExecute()
    	 */
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	//mProgressBar.setVisibility(View.VISIBLE);
        }
        /*
         * method to download members
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected Boolean doInBackground(Void... params) {
        	members = controller.downloadMembersFromAlbum(idAlbum);
            if(members != null) {
            	return true;
            }
            return false;
            		
        }
        /*
         * (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
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
        /*
	     * method that execute if the thread is cancel
	     * @see android.os.AsyncTask#onCancelled()
	     */
		@Override
		protected void onCancelled() {
			super.onCancelled();
			Toast.makeText(getApplicationContext(),"Error download albums settings",Toast.LENGTH_LONG).show();
		}
    }
	
	/**
	 * method to change current Activity to MainActivity
	 */
	public void goToMainActivity() {
		Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();
	}
	/**
	 * class to delete member from album
	 * @author jordi
	 *
	 */
	private class DeleteAlbumMemberTask extends AsyncTask<String, Void, Boolean> {
		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        }
        /*
         * method to delete member
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected Boolean doInBackground(String... params) {
        	String idUser = params[0];
        	return controller.deleteAlbumMember(idAlbum, idUser);
        }
        /*
         * (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result) {
				//Toast.makeText(activity,"Delete Following!",  Toast.LENGTH_SHORT).show();
			}
		}
		/*
	     * method that execute if the thread is cancel
	     * @see android.os.AsyncTask#onCancelled()
	     */
		@Override
		protected void onCancelled() {
			super.onCancelled();
			//Toast.makeText(getActivity(),"Error download photos",  Toast.LENGTH_LONG).show();
		}	
    }
	/**
	 * class that delete album
	 * @author jordi
	 *
	 */
	private class DeleteAlbumTask extends AsyncTask<String, Void, Boolean> {
		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        }
        /*
         * method to delete album
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected Boolean doInBackground(String... params) {
        	return controller.deleteAlbum(idAlbum);
        }
        /*
         * (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result) {
				//Toast.makeText(activity,"Delete Following!",  Toast.LENGTH_SHORT).show();
				goToMainActivity();
			}
		}
		/*
	     * method that execute if the thread is cancel
	     * @see android.os.AsyncTask#onCancelled()
	     */
		@Override
		protected void onCancelled() {
			super.onCancelled();
			//Toast.makeText(getActivity(),"Error download photos",  Toast.LENGTH_LONG).show();
		}	
    }
	/*
	 * method that delete member of album 
	 * @see com.example.prototypetfgv2.view.OnDeleteMemberFromAlbum#onDeleteMember(java.lang.String)
	 */
	@Override
	public void onDeleteMember(String idMember) {
		members.remove(idMember);
		new DownloadMembersTask().execute();
	}
}
