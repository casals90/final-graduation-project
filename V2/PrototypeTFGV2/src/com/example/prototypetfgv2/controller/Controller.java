package com.example.prototypetfgv2.controller;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.prototypetfgv2.model.Album;
import com.example.prototypetfgv2.model.CurrentAlbum;
import com.example.prototypetfgv2.model.Photo;
import com.example.prototypetfgv2.model.User;
import com.example.prototypetfgv2.view.SignUpActivity;
import com.example.prototypetfgv2.view.Utils;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.ParseUser;
 
public class Controller extends Application {
	
	private ParseFunctions parseFunctions;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//Parse.initialize(this, "Pz2ope2OFVDLDypgpdFMpeZiXhnPjm62tDv40b35", "ISRt37kcr6frHkhzvJ3Y9cxhvZxyocO7bP795y4c");
        //ParseTwitterUtils.initialize("1LRilPY6fB23EKrqq6LkD6DPN", "oOsUsmOcRihiBpdy8ILSvjX4lcKTyb2Dnqaz9ChaQado7ZFyFj");
        
        //PushService.setDefaultPushCallback(this, InitActivity.class);
        //ParseInstallation.getCurrentInstallation().saveInBackground();
        
        parseFunctions = new ParseFunctions(getApplicationContext());
        parseFunctions.initParse(getApplicationContext());
        
        
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
        .denyCacheImageMultipleSizesInMemory()
        .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
        .memoryCacheSize(2 * 1024 * 1024)
        .diskCacheSize(50 * 1024 * 1024)
        .diskCacheFileCount(100)
        .writeDebugLogs()
        .build();        
        
        ImageLoader.getInstance().init(config);
        
	}
	
	public void clearImageLoader() {
		ImageLoader.getInstance().clearDiskCache();
		ImageLoader.getInstance().clearMemoryCache();
	}
	
	public void configureDefaultImageLoader(Context context) {
		// Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
	}
	
	public boolean setUsername(String username) {
		return parseFunctions.setUsername(username);
	}
	
	public void importProfilePictureFromSocialNetwork() {
		Log.v("prototypev1", "isLinked  ");
		if(isLinkedWithTwitter(ParseUser.getCurrentUser())) {
			//set profile picture from twitter
			Log.v("prototypev1", "isLinked yes twitter");
			setProfilePictureFromTwitter();
		}
		else if(isLinkedWithFacebook(ParseUser.getCurrentUser())) {
			//set profile picture from facebook
		}
	}
	
	public boolean isLinkedWithTwitter(ParseUser user) {
		return parseFunctions.isLinkedWithTwitter(user);
	}
	
	public boolean isLinkedWithFacebook(ParseUser user) {
		return parseFunctions.isLinkedWithFacebook(user);
	}
	
	public ParseFunctions getParseFunctions() {
		return parseFunctions;
	}

	public boolean signUp(String username,String password) {
		ParseUser parseUser = parseFunctions.signUpInParse(username, password);
		if(parseUser != null) {
			return true;
		}
		return false;
	}
	
	public boolean logIn(String username, String password) {
		if(parseFunctions.logInParse(username, password)) {
			return true;
		}
		return false;
	}
	
	public void updatePhoto(Bitmap photo,String title,Activity activity) {
		parseFunctions.updatePhoto(photo,title,activity);
	}
	
	public void logout() {
		parseFunctions.logout();
		//Log.v("prototypev1", "users logout "+ParseUser.getCurrentUser()+" local "+appClass.getUser());
	}
	
	public ArrayList<Photo> downloadPhotosFromAlbum(String albumId) {
		return parseFunctions.downloadPhotosFromAlbum(albumId);
	}
	
	/*public void deletePhoto(String id) {
		parseFunctions.deletePhoto(id);
	}*/
	
	public ArrayList<User> getUsers(String username) {
		return parseFunctions.getUsers(username);
	}
	
	public ArrayList<User> downloadFriends() {
		return parseFunctions.downloadFriends();
	}
	
	public JSONArray getFriends() {
		return parseFunctions.getFriends();
	}
	
	public JSONArray getFriendsRequest() {
		return parseFunctions.getFriendsRequest();
	}
	
	public boolean addFriend(String id) {
		return parseFunctions.addFriend(id);
	}
	
	public String getUsername() {
		return parseFunctions.getUsername();
	}
	
	public String getProfilePictureUrl() {
		return parseFunctions.getProfilePictureUrl();
	}
	
	public boolean setProfilePicture(Bitmap b) {
		return parseFunctions.setProfilePicture(b);
	}
	
	public boolean removeProfilePicture() {
		return parseFunctions.removeProfilePicture();
	}
	
	public boolean isMyFriend(String id) {
		return parseFunctions.isMyFriend(id);
	}
	
	public boolean deleteFriend(String id) {
		return parseFunctions.deleteFriend(id);
	}
	
	public String getPhotosNumber() {
		return parseFunctions.getPhotosNumber();
	}
	
	public String getFriendsNumber() {
		return parseFunctions.getFriendsNumber();
	}
	
	public boolean logInTwitter(Activity activity) {
		parseFunctions.logInTwitter(activity);
		if(parseFunctions.isParseUserExist())
			return true;
		return false;
	}
	
	public Bitmap getProfilePictureTwitterURL() {
		String url = parseFunctions.getProfilePictureTwitterURL();
		Log.v("prototypev1", "url "+url);
		//put the image in profile picture parse
		Bitmap b = Bitmap.createScaledBitmap(getBitmapFromURL(url),80,80, true);
		Log.v("prototypev1", "bytecount "+b.getByteCount());
		if(url == null || b == null)
			return null;
		parseFunctions.setProfilePicture(b);
		return b;
	}
	
	public Bitmap getBitmapFromURL(String imageUrl) {
		try {
			URL url = new URL(imageUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void setProfilePictureFromTwitter() {
		Bitmap b = getProfilePictureTwitterURL();
		setProfilePicture(b);
	}
	
	public ArrayList<User> downloadUsersList(ArrayList<String> users) {
		return parseFunctions.downloadUsersList(users);
	}
	
	// Functions to change activity
	public void goToSignUp() {
		Intent signUp = new Intent(Controller.this,SignUpActivity.class);
		startActivity(signUp);
	}
	
	public boolean newAlbum(ArrayList<String> members,String albumName) {
		JSONArray m = Utils.arrayListStringToJsonArray(members);
		return parseFunctions.newAlbum(m,albumName);
	}
	
	public ArrayList<User> downloadFriendsInputSearch(String input){
		return parseFunctions.downloadFriendsInputSearch(input);
	}
	
	public ArrayList<Album> getAlbums() {
		return parseFunctions.getAlbums();
	}
	
	public boolean setCurrentAlbum(CurrentAlbum currentAlbum) {
		return parseFunctions.setCurrentAlbum(currentAlbum);
	}
	
	public String getRandomPhotoFromAlbum(String idAlbum) {
		int position = 0;
		
		ArrayList<Photo> photos = downloadPhotosFromAlbum(idAlbum);
		if(photos.size() > 0) {
			position = Utils.getRandomInt(photos.size());
			Log.v("prototypev1", "random n = "+photos.size()+"random "+position);
			return photos.get(position).getId();
		}
		//No photos
		return null;
	}
	
	public void setCoverPhotoAlbum(String idAlbum,String idPhoto) {
		parseFunctions.setCoverPhotoFromAlbum(idAlbum, idPhoto);
	}
	
	public String getUsername(String id) {
		return parseFunctions.getUsername(id);
	}
	
	public CurrentAlbum getCurrentAlbum() {
		return parseFunctions.getCurrentAlbum();
	}
	
	public int getAlbumsNumber() {
		return parseFunctions.getAlbumsNumber();
	}

	//Social Network module
	public boolean likePhoto(String id) {
		return parseFunctions.likePhoto(id);
	}
	
	public boolean unlikePhoto(String id) {
		return parseFunctions.unlikePhoto(id);
	}
	
	public boolean currentUserLikesCurrentPhoto(String id) {
		return parseFunctions.currentUserLikesCurrentPhoto(id);
	}
	
	public boolean newComment(String idPhoto,String text) {
		return parseFunctions.newComment(idPhoto, text);
	}
	
	public int countPhotoLikes(String id) {
		return parseFunctions.countPhotoLikes(id);
	}
	
	public int countPhotoComments(String id) {
		return parseFunctions.countPhotoComments(id);
	}
}
