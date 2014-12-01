package com.example.prototypetfgv2.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.example.prototypetfgv2.model.CurrentAlbum;
import com.example.prototypetfgv2.model.Photo;
import com.example.prototypetfgv2.utils.Utils;
import com.example.prototypetfgv2.view.FragmentDialogChooseCurrentAlbum.OnSetCurrentAlbum;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FragmentProfile extends Fragment implements OnSetCurrentAlbum {
	
	private static final String MyPREFERENCES = "PrototypeTFGV1";
	
	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int REQUEST_PICK_IMAGE = 2;
	private static final int REQUEST_DIALOG_CHOOSE_CURRENT_ALBUM = 3;
	
	private Button buttonLogOut;
	private ImageButton setCurrentAlbum;
	private ImageView profilePicture,currentAlbumCover;
	private TextView username, photosNumber,albumsNumber,friendsNumber,noAlbums,currentAlbumName;
	private ProgressBar mProgressBar,mProgressBarCurrentAlbum,mProgressBarListPhotos;
	private ListView mListView;
	
	private CurrentAlbum currentAlbum;
	private ArrayList<Photo> photos;
	
	private Bitmap newProfilePicture;
	
	private Controller controller;
	private ShowPhotosInProfileAdapter adapter;
	private SharedPreferences sharedPreferences;
	
	public FragmentProfile() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		controller = (Controller) this.getActivity().getApplicationContext();
		sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		getActivity().setTitle(R.string.profile);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_profile,container,false);
		
		//Current album:
		currentAlbumCover = (ImageView) view.findViewById(R.id.current_album_cover);
		currentAlbumName = (TextView) view.findViewById(R.id.current_album_name);
		setCurrentAlbum = (ImageButton) view.findViewById(R.id.set_current_album);
		
		mProgressBarListPhotos = (ProgressBar) view.findViewById(R.id.progressBarListPhotos);
		mListView = (ListView) view.findViewById(R.id.list_my_photos);
		
		//username from current user
		username = (TextView) view.findViewById(R.id.username);
		username.setText(controller.getUsername());
		
		photosNumber = (TextView) view.findViewById(R.id.photos_number);
		photosNumber.setText(controller.getPhotosNumber());
		albumsNumber = (TextView) view.findViewById(R.id.albums_number);
		albumsNumber.setText(controller.getAlbumsNumber());
		friendsNumber = (TextView) view.findViewById(R.id.friends_number);
		friendsNumber.setText(controller.getFriendsNumber());
		
		//profile picture
		profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
		//download profile picture from Parse
		String url = controller.getProfilePictureUrl();
		if(url != null)
			ImageLoader.getInstance().displayImage(url,profilePicture);
		
		//register the context menu to set profile picture
		registerForContextMenu(profilePicture);
		
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarChangeProfilePicture);
		//mProgressBarCurrentAlbum = (ProgressBar) view.findViewById(R.id.progressbar_spinner);
		//logout
		buttonLogOut = (Button) view.findViewById(R.id.button_logout);
		buttonLogOut.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				logout();
			}
		});
		
		//Listeners to click info panel
		LinearLayout albums = (LinearLayout) view.findViewById(R.id.albums);
		//LinearLayout photos = (LinearLayout) view.findViewById(R.id.photos);
		LinearLayout friends = (LinearLayout) view.findViewById(R.id.friends);
		albums.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goToAlbums();			
			}
		});
		friends.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goToFragmentListFriends();
			}
		});
		
		noAlbums = (TextView) view.findViewById(R.id.no_albums);
		return view;
	}	
	
	@Override
	public void onResume() {
		super.onResume();
		new DownloadPhotosTask().execute();
		new DownloadCurrentAlbumTask().execute();
	}

	public void logout() {
		controller.logout();
		deleteSharedPReferences();
		Intent login = new Intent(getActivity(), LoginActivity.class);
		Utils.cleanBackStack(login);
		startActivity(login);
	}
	
	public void deleteSharedPReferences() {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean("rememberLogin",false);
		editor.remove("username");
		editor.remove("password");
		editor.commit();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.set_profile_picture, menu);
		menu.setHeaderTitle("Set a profile picture");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.remove_current_photo:
			removeProfilePicture();
			break;
		case R.id.take_photo:
			takePhoto();
			break;
		case R.id.choose_from_library:
			choosePhotoFromGallery();
			break;
		case R.id.import_from_facebook:
			Log.v("prototypev1","face");
			break;
		case R.id.import_from_twitter:
			importPhotoFromTwitter();
			Log.v("prototypev1","twitter");
			break;

		default:
			break;
		}
		return true;
	}
	
	public void importPhotoFromTwitter() {
		new ImportProfilePictureFromTwitterTask().execute();
	}
	
	public void showDialogChooseCurrentAlbum() {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		FragmentDialogChooseCurrentAlbum dialog = new FragmentDialogChooseCurrentAlbum();
		dialog.setTargetFragment(this, REQUEST_DIALOG_CHOOSE_CURRENT_ALBUM);
        dialog.show(fm, "add_friend_dialog");
	}
	

	@Override
	public void onSetCurrentAlbum(CurrentAlbum newCurrentAlbum) {
		currentAlbumName.setText(newCurrentAlbum.getTitle());
		ImageLoader.getInstance().displayImage(newCurrentAlbum.getCoverPhoto(),currentAlbumCover);
	}

	//Choose from gallery
	public void choosePhotoFromGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_PICK_IMAGE);
	}
	
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
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_IMAGE_CAPTURE:
			if(resultCode == Activity.RESULT_OK && data != null) {
				Log.v("prototypev1","he entrat a capturar fto");
				Bundle extras = data.getExtras();
		        Bitmap photo = (Bitmap)extras.get("data");
		        newProfilePicture = Bitmap.createScaledBitmap(photo,80,80,true);
		        new SetProfilePictureTask().execute();
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
	public void takePhoto() {
		Context context = getActivity(); 
		PackageManager packageManager = context.getPackageManager();
		
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(packageManager) != null) 
	        startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);	        
	}	
	
	// Task to change and upload profile picture
	public class SetProfilePictureTask extends AsyncTask<Void, Void, Boolean> {
		
		@Override
	    protected void onPreExecute() {
			mProgressBar.setVisibility(View.VISIBLE);
			profilePicture.setVisibility(View.INVISIBLE);
	    };
		
		@Override
		protected Boolean doInBackground(Void... params) {
			return controller.setProfilePicture(newProfilePicture);
		}

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

		@Override
		protected void onCancelled() {
			mProgressBar.setVisibility(View.VISIBLE);
			profilePicture.setVisibility(View.INVISIBLE);
			Toast.makeText(getActivity(),"Error set profile picture",  Toast.LENGTH_LONG).show();
		}
	}
	
	//Remove
	public void removeProfilePicture() {
		//default profile picture
		profilePicture.setImageResource(R.drawable.ic_launcher);
		new RemoveProfilePictureTask().execute();
	}
	
	public void goToAlbums() {
		FragmentAlbums fragmentAlbums = new FragmentAlbums();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container_fragment_main,fragmentAlbums);
		transaction.addToBackStack(null);
		transaction.commit();	
	}
	
	public void goToFragmentListFriends() {
		FragmentListFriends fragmentListFriends = new FragmentListFriends();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.container_fragment_main,fragmentListFriends);
		transaction.addToBackStack(null);
		transaction.commit();	
	}
	
	public void goToShowPhotoFullScreen(Photo photo,int position) {
		Intent showPhoto = new Intent(getActivity(),ShowFullScreenPhotoProfile.class);
		showPhoto.putParcelableArrayListExtra("photos",photos);
		showPhoto.putExtra("currentPosition",position);
		startActivity(showPhoto);
	}
	
	// Task to remove profile picture
	public class RemoveProfilePictureTask extends AsyncTask<Void, Void, Boolean> {
		
		@Override
	    protected void onPreExecute() {
			mProgressBar.setVisibility(View.VISIBLE);
			profilePicture.setVisibility(View.INVISIBLE);      
	    };
		
		@Override
		protected Boolean doInBackground(Void... params) {
			return controller.removeProfilePicture();
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mProgressBar.setVisibility(View.INVISIBLE);
			profilePicture.setVisibility(View.VISIBLE);
			if (success) {
				Log.v("prototypev1","correcte set profile picture");
			} else {
				Log.v("prototypev1","set profile picture cancelat");
			}
		}

		@Override
		protected void onCancelled() {
			mProgressBar.setVisibility(View.INVISIBLE);
			profilePicture.setVisibility(View.VISIBLE);
			Log.v("prototypev1","set profile picture cancelat ");
			Toast.makeText(getActivity(),"Error remove profile picture",  Toast.LENGTH_LONG).show();
		}
	}
	
	public class DownloadPhotosTask extends AsyncTask<Void, Void, ArrayList<Photo>> {
		
		@Override
	    protected void onPreExecute() {
			mProgressBarListPhotos.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.INVISIBLE);
			noAlbums.setVisibility(View.INVISIBLE);
	    };
		
		@Override
		protected ArrayList<Photo> doInBackground(Void... params) {
			return controller.downloadMyPhotos(null);
		}

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
				noAlbums.setVisibility(View.VISIBLE);
			}
			
		}

		@Override
		protected void onCancelled() {
			mProgressBarListPhotos.setVisibility(View.INVISIBLE);
			Log.v("prototypev1","download photos cancelat");
			Toast.makeText(getActivity(),"Error download photos",Toast.LENGTH_LONG).show();
		}
	}
	
	public class ImportProfilePictureFromTwitterTask extends AsyncTask<Void, Void, Boolean> {
		
		@Override
	    protected void onPreExecute() {
			mProgressBar.setVisibility(View.VISIBLE);
			profilePicture.setVisibility(View.INVISIBLE);
	    };
		
		@Override
		protected Boolean doInBackground(Void... params) {
			newProfilePicture = controller.getProfilePictureTwitterURL();
			if(newProfilePicture == null)
				return false;
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mProgressBar.setVisibility(View.INVISIBLE);
			profilePicture.setVisibility(View.VISIBLE);
			if (success) {
				Log.v("prototypev1","correcte set profile picture twitter");
				//Change photo in a view
				profilePicture.setImageBitmap(newProfilePicture);
			} else {
				Toast.makeText(getActivity(),"Error set profile picture twitter",  Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			mProgressBar.setVisibility(View.VISIBLE);
			profilePicture.setVisibility(View.INVISIBLE);
			Toast.makeText(getActivity(),"Error set profile picture",  Toast.LENGTH_LONG).show();
		}
	}
	
	private class DownloadCurrentAlbumTask extends AsyncTask<Void, Void, Boolean> {
    	
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	//mProgressBarCurrentAlbum.setVisibility(View.VISIBLE);
        	currentAlbumCover.setVisibility(View.INVISIBLE);
        	currentAlbumName.setVisibility(View.INVISIBLE);
        }
 
        @Override
        protected Boolean doInBackground(Void... params) {
        	currentAlbum = controller.downloadCurrentAlbum();	
            if(currentAlbum != null)
            	return true;
            return false;   		
        }
 
        @Override
        protected void onPostExecute(final Boolean success) {
        	if(success) {
        		//Current album:
        		currentAlbumCover.setVisibility(View.VISIBLE);
            	currentAlbumName.setVisibility(View.VISIBLE);
        		ImageLoader.getInstance().displayImage(currentAlbum.getCoverPhoto(),currentAlbumCover);
        		currentAlbumName.setText(currentAlbum.getTitle());
        		setCurrentAlbum.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO show dialog with all albums and check current album
						showDialogChooseCurrentAlbum();
					}
				});
        	}
        	else {
        		//noAlbums.setVisibility(View.VISIBLE);
        		//mProgressBarCurrentAlbum.setVisibility(View.INVISIBLE);
        	}
        		
        }

		@Override
		protected void onCancelled() {
			super.onCancelled();
			Toast.makeText(getActivity(),"Error download albums",  Toast.LENGTH_LONG).show();
		}
    }
	//TODO
	private class SetCurrentAlbumTask extends AsyncTask<Void, Void, Boolean> {
    	
        @Override
        protected void onPreExecute() {
        	super.onPreExecute();
        	
        	
        }
 
        @Override
        protected Boolean doInBackground(Void... params) {
        	return controller.setCurrentAlbum(currentAlbum);		
        }
 
        @Override
        protected void onPostExecute(final Boolean success) {
        	if(success) {
        		
        	}
        	else
        		Toast.makeText(getActivity(),"0 albums",  Toast.LENGTH_LONG).show();
        }

		@Override
		protected void onCancelled() {
			super.onCancelled();
			Toast.makeText(getActivity(),"Error download albums",  Toast.LENGTH_LONG).show();
		}
    }
}
