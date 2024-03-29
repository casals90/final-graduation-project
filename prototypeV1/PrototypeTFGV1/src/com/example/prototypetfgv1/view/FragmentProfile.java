package com.example.prototypetfgv1.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prototypetfgv1.R;
import com.example.prototypetfgv1.controller.Controller;

public class FragmentProfile extends Fragment {
	
	private static final String MyPREFERENCES = "PrototypeTFGV1";
	
	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int REQUEST_PICK_IMAGE = 2;
	
	private Button buttonLogOut;
	private ImageView profilePicture;
	private TextView username, photosNumber,albumsNumber,friendsNumber;
	private ProgressBar mProgressBar;
	
	private Bitmap newProfilePicture;
	
	private Controller controller;
	
	private SharedPreferences sharedPreferences;
	
	public FragmentProfile() {
		super();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		controller = new Controller(this.getActivity().getApplicationContext());
		sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		getActivity().setTitle(R.string.profile);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_profile,container,false);
		
		//username from current user
		username = (TextView) view.findViewById(R.id.username);
		username.setText(controller.getUsername());
		
		photosNumber = (TextView) view.findViewById(R.id.photos_number);
		photosNumber.setText(controller.getPhotosNumber());
		albumsNumber = (TextView) view.findViewById(R.id.albums_number);
		albumsNumber.setText(controller.getPhotosNumber());
		friendsNumber = (TextView) view.findViewById(R.id.friends_number);
		friendsNumber.setText(controller.getFriendsNumber());
		
		//profile picture
		profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
		//download profile picture from Parse
		Bitmap picture = controller.getProfilePicture(); 
		if(picture != null)
			profilePicture.setImageBitmap(picture);
		//register the context menu to set profile picture
		registerForContextMenu(profilePicture);
		
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBarChangeProfilePicture);
		
		//logout
		buttonLogOut = (Button) view.findViewById(R.id.button_logout);
		buttonLogOut.setOnClickListener(new OnClickListener() {			
			public void onClick(View v) {
				logout();
			}
		});
		return view;
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
			Log.v("prototypev1","twitter");
			break;

		default:
			break;
		}
		return true;
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
		        newProfilePicture = Bitmap.createScaledBitmap(photo,80,80, true);
		        new SetProfilePictureTask().execute();
			}
			break;
		case REQUEST_PICK_IMAGE:
			Log.v("prototypev1","he entrat a triar foto");
			if(resultCode == Activity.RESULT_OK && data != null) {
				Bitmap yourSelectedImage = searchPhotoSelect(data);
		        newProfilePicture = Bitmap.createScaledBitmap(yourSelectedImage,80,80, true);
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
		new RemoveProfilePictureFromLibraryTask().execute();
	}
	
	// Task to remove profile picture
	public class RemoveProfilePictureFromLibraryTask extends AsyncTask<Void, Void, Boolean> {
		
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
}
