package com.example.prototypetfgv2.view;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototypetfgv2.R;
import com.example.prototypetfgv2.controller.Controller;
import com.example.prototypetfgv2.model.Photo;
import com.example.prototypetfgv2.utils.BitmapUtils;
import com.example.prototypetfgv2.utils.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;
/**
 * Class to show user profile
 * @author jordi
 *
 */
public class FragmentProfile extends Fragment {
	
	private static final String MyPREFERENCES = "PhotoCloudData";
	
	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int REQUEST_PICK_IMAGE = 2;
	
	private ImageView profilePicture;//currentAlbumCover;
	private TextView albumsNumber,followersNumber,followingNumber,noPhotos;
	private ProgressBar mProgressBar,mProgressBarListPhotos;
	private ListView mListView;
	
	private String currentAlbumId;
	private ArrayList<Photo> photos;
	
	private Bitmap newProfilePicture;
	private Controller controller;
	private ShowPhotosInProfileAdapter adapter;
	private SharedPreferences sharedPreferences;

	private String mCurrentPhotoPath;
	
	public FragmentProfile() {
		super();
	}
	/*
	 * Method that init params
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		controller = (Controller) this.getActivity().getApplication();
		sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		
		//For show menu in action bar
		setHasOptionsMenu(true);
	}
	/*
	 * Method that init view
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_profile,container,false);
		
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
		mProgressBarListPhotos = (ProgressBar) view.findViewById(R.id.progressBarListPhotos);
		mListView = (ListView) view.findViewById(R.id.list_my_photos);
		
		//photosNumber = (TextView) view.findViewById(R.id.photos_number);
		//photosNumber.setText(controller.getPhotosNumber());
		albumsNumber = (TextView) view.findViewById(R.id.albums_number);
		albumsNumber.setText(controller.getAlbumsNumber());
		
		followersNumber = (TextView) view.findViewById(R.id.followers_number);
		followersNumber.setText(controller.getFollowersNumber());
		
		followingNumber = (TextView) view.findViewById(R.id.following_number);
		followingNumber.setText(controller.getFollowingNumber());
		
		//profile picture
		profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
		//download profile picture from Parse
		String url = controller.getProfilePictureUrl();
		if(url != null)
			ImageLoader.getInstance().displayImage(url,profilePicture);
		
		//register the context menu to set profile picture
		registerForContextMenu(profilePicture);
		
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarChangeProfilePicture);
		
		//Listeners to click info panel
		LinearLayout albums = (LinearLayout) view.findViewById(R.id.albums);
		albums.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goToAlbums();			
			}
		});
		LinearLayout followers = (LinearLayout) view.findViewById(R.id.followers);
		followers.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goToFragmentFriends(0);				
			}
		});
		LinearLayout following = (LinearLayout) view.findViewById(R.id.following);
		following.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goToFragmentFriends(1);				
			}
		});
		
		noPhotos = (TextView) view.findViewById(R.id.no_photos);
		return view;
	}	
	/*
	 * Method update view
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
		getActivity().getActionBar().setTitle((controller.getCurrentUser().getUsername()));
		new DownloadPhotosTask().execute();
	}
	/*
	 * method to create menu with specific options
	 * @see android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
		inflater.inflate(R.menu.menu_profile, menu);
	}
	/*
	 * method that specify what to do when user click menu option
	 * @see android.support.v4.app.Fragment#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.logout:
				logout();
				break;
			case R.id.set_current_album:
				showDialogChooseCurrentAlbum();
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	/**
	 * Method to delete user data
	 */
	public void deleteSharedPreferences() {
		getActivity().getApplicationContext().getSharedPreferences(MyPREFERENCES, 0).edit().clear().commit();
	}
    /**
     * Method to logout current session
     */
	public void logout() {
		controller.logout();
		deleteSharedPreferences();
		Intent login = new Intent(getActivity(), LoginActivity.class);
		Utils.cleanBackStack(login);
		startActivity(login);
	}
	/*
	 * Method to create a context menu for set profile picture
	 * @see android.support.v4.app.Fragment#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if(controller.isLinkedWithFacebook()) {
			getActivity().getMenuInflater().inflate(R.menu.set_profile_picture_facebook, menu);
		}
		else if(controller.isLinkedWithTwitter())
			getActivity().getMenuInflater().inflate(R.menu.set_profile_picture_twitter, menu);
		else
			getActivity().getMenuInflater().inflate(R.menu.set_profile_picture, menu);
		menu.setHeaderTitle("Set a profile picture");
	}
	/*
	 * Method that specify what to do when user click contextmenu option
	 * @see android.support.v4.app.Fragment#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.take_photo:
				dispatchTakePictureIntent();
				break;
			case R.id.choose_from_library:
				choosePhotoFromGallery();
				break;
			case R.id.import_from_facebook:
				importPhotoFromFacebook();
				break;
			case R.id.import_from_twitter:
				importPhotoFromTwitter();
				break;
	
			default:
				break;
		}
		return true;
	}
	/**
	 * Method that import photo from Twitter
	 */
	public void importPhotoFromTwitter() {
		new ImportProfilePictureFromTwitterTask().execute();
	}
	/**
	 * Method that import photo from Facebook
	 */
	public void importPhotoFromFacebook() {
		new ImportProfilePictureFromFacebookTask().execute();
	}
	/**
	 * Method that show Dialog for choose current album
	 */
	public void showDialogChooseCurrentAlbum() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		Bundle data = new Bundle();
		currentAlbumId = controller.getCurrentAlbum();
		data.putString("currentAlbumId",currentAlbumId);
		FragmentDialogChooseCurrentAlbum dialog = new FragmentDialogChooseCurrentAlbum();
		dialog.setArguments(data);
        dialog.show(fm, "add_friend_dialog");
	}

	//Choose from gallery
	/**
	 * Method that choose image from gallery
	 */
	public void choosePhotoFromGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_PICK_IMAGE);
	}
	/**
	 * Method that find image in gallery
	 * @param data photo selected
	 * @return a bitmap is selected photo
	 */
	public Bitmap searchPhotoSelect(Intent data) {
		Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return BitmapFactory.decodeFile(filePath);
	}
	
	//Result from choose image from gallery and take photo
	/*
	 * Method that show photo that take and show photo that select of gallery
	 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_IMAGE_CAPTURE:
			if(resultCode == Activity.RESULT_OK && data != null) {
				new BitmapWorkerTask(profilePicture).execute(mCurrentPhotoPath);
				
			}
			break;
		case REQUEST_PICK_IMAGE:
			Log.v("prototypev1","he entrat a triar foto");
			if(resultCode == Activity.RESULT_OK && data != null) {
				Bitmap yourSelectedImage = searchPhotoSelect(data);
		        newProfilePicture = Bitmap.createScaledBitmap(yourSelectedImage,80,80,true);
		        //upload to parse
		        new SetProfilePictureTask().execute();
			}
			break;
			
		default:
			break;
		}
		profilePicture.setImageBitmap(newProfilePicture);
	}
	
	//Take photo
	//Functions to take photo
	/**
	 * Method to take photo
	 */
	private void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    // Ensure that there's a camera activity to handle the intent
	    if (takePictureIntent.resolveActivity(this.getActivity().getPackageManager()) != null) {
	        // Create the File where the photo should go
	        File photoFile = null;
	        try {
	            photoFile = createImageFile();
	        } catch (IOException ex) {
	            // Error occurred while creating the File
	        }
	        // Continue only if the File was successfully created
	        if (photoFile != null) {
	            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile));
	            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	        }
		}
	}
	/**
	 * Method that create image file	
	 * @return file
	 * @throws IOException
	 */
	private File createImageFile() throws IOException {
	    // Create an image file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    File storageDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);
	    File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpeg",         /* suffix */
	        storageDir      /* directory */
	    );

	    mCurrentPhotoPath = image.getAbsolutePath();
	    return image;
	}
	/**
	 * Method that adding pic on gallery	
	 */
	private void galleryAddPic() {
		    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		    File f = new File(mCurrentPhotoPath);
		    Uri contentUri = Uri.fromFile(f);
		    mediaScanIntent.setData(contentUri);
		    this.getActivity().sendBroadcast(mediaScanIntent);
	}
	
	// Task to change and upload profile picture
	/**
	 * Class to set profile picture
	 * @author jordi
	 *
	 */
	public class SetProfilePictureTask extends AsyncTask<Void, Void, Boolean> {
		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
	    protected void onPreExecute() {
			mProgressBar.setVisibility(View.VISIBLE);
			profilePicture.setVisibility(View.INVISIBLE);
	    };
		/*
		 * Method that set profile picture
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Boolean doInBackground(Void... params) {
			return controller.setProfilePicture(newProfilePicture);
		}
		/*
		 * Method that update view
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(final Boolean success) {
			mProgressBar.setVisibility(View.INVISIBLE);
			profilePicture.setVisibility(View.VISIBLE);
			if (success) {
				Log.v("prototypev1","correcte set profile picture");
			} else {
				Toast.makeText(getActivity(),"Error set profile picture",  Toast.LENGTH_LONG).show();
			}
		}
		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onCancelled()
		 */
		@Override
		protected void onCancelled() {
			mProgressBar.setVisibility(View.VISIBLE);
			profilePicture.setVisibility(View.INVISIBLE);
			Toast.makeText(getActivity(),"Error set profile picture",  Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * Method that change current Fragment for FragmentAlbums
	 */
	public void goToAlbums() {
		FragmentAlbums fragmentAlbums = new FragmentAlbums();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container_fragment_main,fragmentAlbums);
		transaction.addToBackStack(null);
		transaction.commit();	
	}
	/**
	 * Method that change current Activity for ShowFullScreenPhotoProfile
	 */
	public void goToShowPhotoFullScreen(Photo photo,int position) {
		Intent showPhoto = new Intent(getActivity(),ShowFullScreenPhotoProfile.class);
		showPhoto.putParcelableArrayListExtra("photos",photos);
		showPhoto.putExtra("currentPosition",position);
		startActivity(showPhoto);
	}
	/**
	 * Method that change current Fragment for FragmentFriends
	 */
	public void goToFragmentFriends(int tab) {
		FragmentFriends fragmentFriends = new FragmentFriends();
		Bundle data = new Bundle();
		data.putBoolean("fromProfile",true);
		data.putInt("initTab", tab);
		fragmentFriends.setArguments(data);
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container_fragment_main,fragmentFriends);
		transaction.addToBackStack(null);
		transaction.commit();
	}
	/**
	 * Class to download photos of user
	 * @author jordi
	 *
	 */
	public class DownloadPhotosTask extends AsyncTask<Void, Void, ArrayList<Photo>> {
		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
	    protected void onPreExecute() {
			mProgressBarListPhotos.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.INVISIBLE);
			noPhotos.setVisibility(View.INVISIBLE);
	    };
		/*
		 * Method that download photos
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected ArrayList<Photo> doInBackground(Void... params) {
			return controller.downloadMyPhotos(null);
		}
		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(final ArrayList<Photo> photosUser) {
			mProgressBarListPhotos.setVisibility(View.INVISIBLE);
			if(photosUser != null && photosUser.size() > 0) {
				photos = photosUser;
				adapter = new ShowPhotosInProfileAdapter(getActivity().getApplicationContext(),photos,controller,getActivity());
				mListView.setVisibility(View.VISIBLE);
				mListView.setAdapter(adapter);
				mListView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						goToShowPhotoFullScreen(photos.get(position),position);
					}
				});
			}
			else {
				noPhotos.setVisibility(View.VISIBLE);
			}
			
		}
		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onCancelled()
		 */
		@Override
		protected void onCancelled() {
			mProgressBarListPhotos.setVisibility(View.INVISIBLE);
			Log.v("prototypev1","download photos cancelat");
			Toast.makeText(getActivity(),"Error download photos",Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * Class to import profile picture from Twitter
	 * @author jordi
	 *
	 */
	public class ImportProfilePictureFromTwitterTask extends AsyncTask<Void, Void, Boolean> {
		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
	    protected void onPreExecute() {
			mProgressBar.setVisibility(View.VISIBLE);
			profilePicture.setVisibility(View.INVISIBLE);
	    };
		/*
		 * Method that import profile picture from Twitter
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Boolean doInBackground(Void... params) {
			return controller.importProfilePhotoTwitter();
		}
		/*
		 * Method that update view
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(final Boolean success) {
			mProgressBar.setVisibility(View.INVISIBLE);
			profilePicture.setVisibility(View.VISIBLE);
			if (success) {
				Log.v("prototypev1","correcte set profile picture twitter");
				//Change photo in a view
				//profilePicture.setImageBitmap(newProfilePicture);
				ImageLoader.getInstance().displayImage(controller.getCurrentUser().getProfilePicture(),profilePicture);
			} else {
				Toast.makeText(getActivity(),"Error set profile picture twitter",  Toast.LENGTH_LONG).show();
			}
		}
		/*
		 * (non-Javadoc)
		 * @see android.os.AsyncTask#onCancelled()
		 */
		@Override
		protected void onCancelled() {
			mProgressBar.setVisibility(View.VISIBLE);
			profilePicture.setVisibility(View.VISIBLE);
			Toast.makeText(getActivity(),"Error set profile picture",  Toast.LENGTH_LONG).show();
		}
	}
	
	public class ImportProfilePictureFromFacebookTask extends AsyncTask<Void, Void, Boolean> {
		
		@Override
	    protected void onPreExecute() {
			mProgressBar.setVisibility(View.VISIBLE);
			profilePicture.setVisibility(View.INVISIBLE);
	    };
		
		@Override
		protected Boolean doInBackground(Void... params) {
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			controller.importProfilePhotoFromFacebook();
			mProgressBar.setVisibility(View.INVISIBLE);
			profilePicture.setVisibility(View.VISIBLE);
			if (success) {
				Log.v("prototypev1","correcte set profile picture twitter");
				//Change photo in a view
				//profilePicture.setImageBitmap(newProfilePicture);
				ImageLoader.getInstance().displayImage(controller.getProfilePictureUrl(),profilePicture);
			} else {
				Toast.makeText(getActivity(),"Error set profile picture twitter",  Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			mProgressBar.setVisibility(View.VISIBLE);
			profilePicture.setVisibility(View.VISIBLE);
			Toast.makeText(getActivity(),"Error set profile picture",  Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * Class to show photo that had been taken
	 * @author jordi
	 *
	 */
	class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
	    private final WeakReference<ImageView> imageViewReference;
	    /**
	     * Constructor of class
	     * @param imageView imagview to show photo
	     */
	    public BitmapWorkerTask(ImageView imageView) {
	        // Use a WeakReference to ensure the ImageView can be garbage collected
	        imageViewReference = new WeakReference<ImageView>(imageView);
	    }
	    /*
	     * (non-Javadoc)
	     * @see android.os.AsyncTask#onPreExecute()
	     */
	    @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	mProgressBar.setVisibility(View.VISIBLE);
        }

	    // Decode image in background.
	    /*
	     * Method that fetch image file and convert a bitmap
	     * @see android.os.AsyncTask#doInBackground(Params[])
	     */
	    @Override
	    protected Bitmap doInBackground(String... params) {
	        String filePath = params[0];
	        File file = new File(filePath);
	        //return BitmapFactory.decodeFile(file.getAbsolutePath());
	        return BitmapUtils.decodeFileForDisplay(file,getActivity());
	    }

	    // Once complete, see if ImageView is still around and set bitmap.
	    /*
	     * Method that update view
	     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	     */
	    @Override
	    protected void onPostExecute(Bitmap bitmap) {
	        if (imageViewReference != null && bitmap != null) {
	            final ImageView imageView = imageViewReference.get();
	            if (imageView != null) {
	            	//Put bitmap in a global field
	            	//photo = bitmap;
	            	mProgressBar.setVisibility(View.INVISIBLE);
	            	Bitmap b = Bitmap.createScaledBitmap(bitmap, 80, 80, true);
	            	//mImageView.setVisibility(View.VISIBLE);
	                imageView.setImageBitmap(b);
	                controller.setProfilePicture(b);
	            }
	        }
	    }
	}
}
