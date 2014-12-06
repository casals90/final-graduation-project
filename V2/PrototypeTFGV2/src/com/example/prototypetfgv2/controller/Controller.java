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
import com.example.prototypetfgv2.model.CurrentUser;
import com.example.prototypetfgv2.model.Photo;
import com.example.prototypetfgv2.model.User;
import com.example.prototypetfgv2.utils.Utils;
import com.example.prototypetfgv2.view.SignUpActivity;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.parse.ParseException;
import com.parse.ParseUser;
 
public class Controller extends Application {
	
	private ParseFunctions parseFunctions;
	private CurrentUser currentUser;
	
	@Override
	public void onCreate() {
		super.onCreate();
		//Parse.initialize(this, "Pz2ope2OFVDLDypgpdFMpeZiXhnPjm62tDv40b35", "ISRt37kcr6frHkhzvJ3Y9cxhvZxyocO7bP795y4c");
        //ParseTwitterUtils.initialize("1LRilPY6fB23EKrqq6LkD6DPN", "oOsUsmOcRihiBpdy8ILSvjX4lcKTyb2Dnqaz9ChaQado7ZFyFj");
        
        //PushService.setDefaultPushCallback(this, InitActivity.class);
        //ParseInstallation.getCurrentInstallation().saveInBackground();
        
        parseFunctions = new ParseFunctions(getApplicationContext());
        parseFunctions.initParse(getApplicationContext());
        
        
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
        .memoryCache(new WeakMemoryCache())
        .denyCacheImageMultipleSizesInMemory()
        .tasksProcessingOrder(QueueProcessingType.LIFO)
        .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
        .memoryCacheSize(2 * 1024 * 1024)
        .diskCacheSize(50 * 1024 * 1024)
        .diskCacheFileCount(100)
        .build();        
        
        ImageLoader.getInstance().init(config);
        
	}
	
	/*public void initCurrentUser() {
		this.currentUser = parseFunctions.getCurrentUser();
	}*/
	
	public CurrentUser getCurrentUser() {
		return currentUser;
	}

	public void downloadCurrentUser() {
		this.currentUser = parseFunctions.getCurrentUser();
	}
	
	public void setCurrentUser(CurrentUser currentUser) {
		this.currentUser = currentUser;
	}
	
	public boolean importProfilePhotoTwitter() {
		String url = parseFunctions.getProfilePictureTwitterURL();
		return parseFunctions.setProfilePictureFromSocialNetworks(url);
	}
	
	
	/*public void getLikesPhotosFromAlbum(String idAlbum) {
		 ArrayList<String> likes = parseFunctions.getPhotosFromAlbumLikedCurrentUser(currentUser.getId(), idAlbum);
		 this.currentUser.setLikesPhotosInsideAlbum(likes);
	}
	

	public void getLikesFromUserPhotos() {
		ArrayList<String> likes = parseFunctions.getLikesFromMyPhotos(currentUser.getId());
		Log.v("prototypev1", "likes size"+likes.size());
		currentUser.setLikesPhotosInsideAlbum(likes);
	}*/
	
	public void importProfilePhotoFromFacebook() {
		parseFunctions.importProfilePhotoFromFacebook();
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
		//Log.v("prototypev1", "controller get setusername "+parseFunctions.setUsername(username));
		return parseFunctions.setUsername(username);
	}
	
	public boolean isLinkedWithTwitter() {
		return parseFunctions.isLinkedWithTwitter();
	}
	
	public boolean isLinkedWithFacebook() {
		return parseFunctions.isLinkedWithFacebook();
	}
	
	public ParseFunctions getParseFunctions() {
		return parseFunctions;
	}
	
	public ArrayList<Album> downloadAlbumsList(ArrayList<String> idAlbums) {
		return parseFunctions.downloadAlbums(idAlbums, currentUser);
	}

	public boolean signUp(String username,String password) {
		ParseUser parseUser = parseFunctions.signUpInParse(username, password);
		if(parseUser != null) {
			return true;
		}
		return false;
	}
	
	public ArrayList<Photo> downloadMyPhotos(String idUser) {
		if(idUser == null)
			return parseFunctions.downloadMyPhotos(currentUser.getId());
		return parseFunctions.downloadMyPhotos(idUser);
		
	}
	
	public ArrayList<Photo> downloadAllPhotosFromCurrentUser() {
		return parseFunctions.downloadAllPhotosFromCurrentUser(currentUser);
	}
	
	public boolean logIn(String username, String password) {
		if(parseFunctions.logInParse(username, password)) {
			return true;
		}
		return false;
	}
	
	public void uploadPhoto(Bitmap photo,String title,Activity activity,String idAlbum) {
		//Use de current album (only in take photo)
		if(idAlbum == null) {
			//CurrentAlbum
			String currentAlbum = getCurrentUser().getCurrentAlbum();
			parseFunctions.uploadPhoto(photo,title,activity,currentAlbum);
		}
		//Pass the album (only in import photo from gallery)
		else
			parseFunctions.uploadPhoto(photo,title,activity,idAlbum);
		
	}
	
	public void logout() {
		parseFunctions.logout();
		deleteCurrentUser();
	}
	
	public ArrayList<Photo> downloadPhotosFromAlbum(String albumId) {
		//TODO guardar les fotos que soc admin
		return parseFunctions.downloadPhotosFromAlbum(albumId);
	}
	
	/*public void deletePhoto(String id) {
		parseFunctions.deletePhoto(id);
	}*/
	
	public ArrayList<User> getUsers(String username) {
		return parseFunctions.getUsers(username,currentUser);
	}
	
	public ArrayList<User> downloadFriends() {
		return parseFunctions.downloadFriends(currentUser);
	}
	
	public ArrayList<String> getFriends() {
		return parseFunctions.getFriends(currentUser);
	}
	
	public JSONArray getFriendsRequest() {
		return parseFunctions.getFriendsRequest();
	}
	
	public boolean addFriend(String id) {
		currentUser.incrementFriendsNumber();
		return parseFunctions.addFriend(id,currentUser);
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
		ArrayList<String> friends = parseFunctions.getFriends(currentUser);
		return friends.contains(id);
	}
	
	public boolean deleteFriend(String id) {
		currentUser.decrementFriendsNumber();
		return parseFunctions.deleteFriend(id,currentUser);
	}
	
	public String getPhotosNumber() {
		return String.valueOf(currentUser.getPhotosNumber());
	}
	
	public String getFriendsNumber() {
		return String.valueOf(currentUser.getFriendsNumber());
	}
	
	public String getAlbumsNumber() {
		return String.valueOf(currentUser.getAlbumsNumber());
	}
	
	public boolean logInTwitter(Activity activity) {
		parseFunctions.logInTwitter(activity);
		if(parseFunctions.isParseUserExist())
			return true;
		return false;
	}
	
	public boolean setProfilePictureFromTwitter() {
		String url = parseFunctions.getProfilePictureTwitterURL();
		return parseFunctions.setProfilePictureFromSocialNetworks(url);
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
	
	/*public void setProfilePictureFromTwitter() {
		Bitmap b = getProfilePictureTwitterURL();
		setProfilePicture(b);
	}*/
	
	public ArrayList<User> downloadUsersList(ArrayList<String> users) {
		return parseFunctions.downloadUsersList(users);
	}
	
	// Functions to change activity
	public void goToSignUp() {
		Intent signUp = new Intent(Controller.this,SignUpActivity.class);
		startActivity(signUp);
	}
	
	public boolean newAlbum(ArrayList<String> members,String albumName) {
		//JSONArray m = Utils.arrayListStringToJsonArray(members);
		return parseFunctions.newAlbum(members,albumName,currentUser);
	}
	
	public ArrayList<User> downloadFriendsInputSearch(String input){
		return parseFunctions.downloadFriendsInputSearch(input,currentUser);
	}
	
	public ArrayList<Album> getAlbums() {
		return parseFunctions.getAlbums(currentUser);
	}
	
	public boolean setCurrentAlbum(CurrentAlbum currentAlbum) {
		currentUser.setCurrentAlbum(currentAlbum.getId());
		return parseFunctions.setCurrentAlbum(currentAlbum);
	}
	
	public void setCurrentAlbumId(String id) {
		currentUser.setCurrentAlbum(id);
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
	
	public String getCurrentAlbum() {
		return currentUser.getCurrentAlbum();
	}
	
	public CurrentAlbum downloadCurrentAlbum() {
		//String idCurrentAlbum = getCurrentAlbum();
		return parseFunctions.getCurrentAlbum();
	}
	
	public String getCurrentAlbumId() {
		return getCurrentAlbum();
	}
	
	public void deleteCurrentUser() {
		currentUser = null;
	}

	public ArrayList<Album> downloadCommonAlbums(String idFriend) {
		return parseFunctions.downloadCommonAlbums(currentUser, idFriend);
	}
	
	public void getAllLikes() {
		parseFunctions.getAllLikes(currentUser);
	}
	
	//Social Network module
	public void logInFacebook(Activity activity) {
		parseFunctions.logInFacebook(activity);
	}
	
	public boolean likePhoto(String idPhoto,String idAlbum) {
		currentUser.addLike(idPhoto);
		return parseFunctions.likePhoto(idPhoto,idAlbum);
	}
	
	public boolean currentUserLikedCurrentPhoto(String idPhoto) {
		return currentUser.isUserLikedCurrentPhoto(idPhoto);
	}
	
	public void addLikePhotoCurrentUser(String idPhoto) {
		currentUser.addLike(idPhoto);
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
