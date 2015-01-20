package com.example.prototypetfgv2.controller;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.prototypetfgv2.model.Album;
import com.example.prototypetfgv2.model.Comment;
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
import com.parse.ParseUser;
/**
 * Class that do a controller of application 
 * @author jordi
 *
 */
public class Controller extends Application {
	
	private ParseFunctions parseFunctions;
	private CurrentUser currentUser;
	
	/*
	 * Method that init all params of the application
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		        
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
	
	public ParseFunctions getParseFunctions() {
		return parseFunctions;
	}
	
	/**
	 * Method that clear memory cache from application 
	 */
	public void clearImageLoader() {
		ImageLoader.getInstance().clearDiskCache();
		ImageLoader.getInstance().clearMemoryCache();
	}
	/**
	 * Method that configure init params from Universal Image Loader
	 * @param context context from application
	 */
	public void configureDefaultImageLoader(Context context) {
		// Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
	}
	
	//Current user
	public CurrentUser getCurrentUser() {
		return currentUser;
	}
	/**
	 * Method to download current user from server
	 */
	public void downloadCurrentUser() {
		this.currentUser = parseFunctions.getCurrentUser();
	}
	
	public void setCurrentUser(CurrentUser currentUser) {
		this.currentUser = currentUser;
	}
	/**
	 * Get method that return followers number from current user 
	 * @return followers number
	 */
	public String getFollowersNumber() {
		return String.valueOf(currentUser.getFollowersNumber());
	}
	/**
	 * Get method that return following number from current user 
	 * @return following number
	 */
	public String getFollowingNumber() {
		return String.valueOf(currentUser.getFollowingNumber());
	}
	/**
	 * Get method that return albums number from current user 
	 * @return albums number
	 */
	public String getAlbumsNumber() {
		return String.valueOf(currentUser.getAlbumsNumber());
	}
	/**
	 * Get method that return photos number from current user 
	 * @return photos number
	 */
	public String getPhotosNumber() {
		return String.valueOf(currentUser.getPhotosNumber());
	}
	/**
	 * Method that delete current user from model
	 */
	public void deleteCurrentUser() {
		currentUser = null;
	}
	/**
	 * Method that parse user from server to data model
	 * @return
	 */
	public User getCurrentUserToUserModel() {
		ParseUser user = ParseUser.getCurrentUser();
		return new User(user.getObjectId(),user.getUsername(),user.getString("profilePictureUrl"),user.getInt("followersNumber"),user.getInt("followingNumber"),user.getInt("photosNumber"),user.getInt("albumsNumber"));
	}
	
	//Parse User methods
	public boolean setUsername(String username) {
		return parseFunctions.setUsername(username);
	}
	/**
	 * Method that get username from spececify user id
	 * @param idUser user id
	 * @return username
	 */
	public String getUsernameFromUserId(String idUser) {
		return parseFunctions.getUsernameFromIdUser(idUser);
	}
	/**
	 * Get method that return profile picture url from current user 
	 * @return profile picture url
	 */
	public String getProfilePictureUrl() {
		return parseFunctions.getProfilePictureUrl();
	}
	/**
	 * Method that change profile picture from current user
	 * @param b new bitmap
	 * @return true is possible false is not possible
	 */
	public boolean setProfilePicture(Bitmap b) {
		if(parseFunctions.setProfilePicture(b)) {
			currentUser.setProfilePicture(parseFunctions.getProfilePictureFromCurrerUser());
			return true;
		}
		return false; 
	}
	/**
	 * Method that delete profile picture
	 * @return true is possible, false is not possible
	 */
	public boolean removeProfilePicture() {
		return parseFunctions.removeProfilePicture();
	}
	
	public String getUsername() {
		return parseFunctions.getUsername();
	}
	
	public String getUsername(String id) {
		return parseFunctions.getUsername(id);
	}
	
	//Parse login and signup
	/**
	 * Method that provides a signup
	 * @param username new username
	 * @param password new password
	 * @return true is possible, false is not possible
	 */
	public boolean signUp(String username,String password) {
		ParseUser parseUser = parseFunctions.signUpInParse(username, password);
		if(parseUser != null) {
			return true;
		}
		return false;
	}
	/**
	 * Method that provides login for user
	 * @param username username to log in
	 * @param password password to log in
	 * @return true is possible, false is not possible
	 */
	public boolean logIn(String username, String password) {
		if(parseFunctions.logInParse(username, password)) {
			return true;
		}
		return false;
	}
	/**
	 * Method that logout session and clear data store
	 */
	public void logout() {
		parseFunctions.logout();
		deleteCurrentUser();
	}
		
	//Albums methods
	public boolean setAlbumTitle(String idAlbum,String newAlbumTitle) {
		return parseFunctions.setAlbumTitle(idAlbum, newAlbumTitle);
	}
	/**
	 * Method that return following users that they are not members of album
	 * @param members list from users id
	 * @return users list 
	 */
	public ArrayList<User> getFollowingThatNotInAlbum(ArrayList<String> members) {
		return parseFunctions.getFollowingThatNotInAlbum(currentUser.getId(), members);
	}
	/**
	 * Method that adding members in album
	 * @param newMembers list of new members
	 * @param idAlbum album id
	 * @return true is possible, false is not possible
	 */
	public boolean addAlbumMembersFromSettings(ArrayList<String> newMembers, String idAlbum) {
		return parseFunctions.addMembersInAlbumFromAlbumSettings(newMembers, idAlbum);
	}
	/**
	 * Method that delete member from album
	 * @param idAlbum id from album
	 * @param idUser user id to delete
	 * @return true is possible, false is not possible
	 */
	public boolean deleteAlbumMember(String idAlbum,String idUser) {
		return parseFunctions.deleteMemberOfAlbum(idAlbum, idUser);
	}
	/**
	 * Method that delete album
	 * @param idAlbum album id to delete
	 * @return true is possible, false is not possible
	 */
	public boolean deleteAlbum(String idAlbum) {
		return parseFunctions.deleteAlbum(idAlbum);
	}
	/**
	 * Method that download all albums from current user
	 * @param idAlbums id albums from current user
	 * @return list of albums
	 */
	public ArrayList<Album> downloadAlbumsList(ArrayList<String> idAlbums) {
		return parseFunctions.downloadAlbums(idAlbums, currentUser);
	}
	/**
	 * Method that download a specific album
	 * @param idAlbum id album to download
	 * @return album
	 */
	public Album downloadAlbum(String idAlbum) {
		return parseFunctions.downloadAlbum(idAlbum);
	}
	/**
	 * Mhetod that create a new album
	 * @param members list of members
	 * @param albumName album title
	 * @return true is possible, false is not possible
	 */
	public boolean newAlbum(ArrayList<String> members,String albumName) {
		return parseFunctions.newAlbum(members,albumName,currentUser);
	}
	/**
	 * Method that return all albums from current user
	 * @return list of albums from current user
	 */
	public ArrayList<Album> getAlbums() {
		return parseFunctions.getAlbums(currentUser);
	}
	/**
	 * Method that download members from specific album
	 * @param idAlbum id from album
	 * @return list of members
	 */
	public ArrayList<User> downloadMembersFromAlbum(String idAlbum) {
		ArrayList<String> members = parseFunctions.getMembers(idAlbum);
		return parseFunctions.downloadUsersList(members);
	}
	
	public void setCoverPhotoAlbum(String idAlbum,String idPhoto) {
		parseFunctions.setCoverPhotoFromAlbum(idAlbum, idPhoto);
	}
	
	//Current Album
	public boolean setCurrentAlbum(CurrentAlbum currentAlbum) {
		currentUser.setCurrentAlbum(currentAlbum.getId());
		return parseFunctions.setCurrentAlbum(currentAlbum);
	}
	
	public void setCurrentAlbumId(String id) {
		currentUser.setCurrentAlbum(id);
	}
	/**
	 * Method that return a random photo from album
	 * @param idAlbum id from album
	 * @return photo url
	 */
	public String getRandomPhotoFromAlbum(String idAlbum) {
		int position = 0;
		
		ArrayList<Photo> photos = downloadPhotosFromAlbum(idAlbum);
		if(photos.size() > 0) {
			position = Utils.getRandomInt(photos.size());
			return photos.get(position).getId();
		}
		//No photos
		return null;
	}
	
	public String getCurrentAlbum() {
		return currentUser.getCurrentAlbum();
	}
	/**
	 * Method that download current album
	 * @return current album
	 */
	public CurrentAlbum downloadCurrentAlbum() {
		return parseFunctions.getCurrentAlbum();
	}
	
	public String getCurrentAlbumId() {
		return getCurrentAlbum();
	}
	/**
	 * Method that download common albums between current user and specific user
	 * @param idUser id user to get common albums
	 * @return list of albums
	 */
	public ArrayList<Album> downloadCommonAlbums(String idUser) {
		return parseFunctions.downloadCommonAlbums(currentUser, idUser);
	}
	
	//Photos methods
	/**
	 * Method that download photos from specific user
	 * @param idUser id of user
	 * @return list of photos
	 */
	public ArrayList<Photo> downloadMyPhotos(String idUser) {
		if(idUser == null)
			return parseFunctions.downloadMyPhotos(currentUser.getId());
		return parseFunctions.downloadMyPhotos(idUser);
		
	}
	/**
	 * Method that download all photos from current user
	 * @return list of photos
	 */
	public ArrayList<Photo> downloadAllPhotosFromCurrentUser() {
		return parseFunctions.downloadAllPhotosFromCurrentUser(currentUser);
	}
	/**
	 * Method that delete photo
	 * @param photo photo to delete
	 * @param idAlbum id from album
	 * @return true is possible, false is not possible
	 */
	public boolean deletePhoto(Photo photo,String idAlbum) {
		currentUser.decrementPhotosNumber();
		currentUser.deleteOwnerPhotos(photo.getId());
		currentUser.deletePhotoLike(photo.getId());
		parseFunctions.deletePhotoObject(photo.getId());
		parseFunctions.decrementPhotosNumberAlbum(idAlbum);
		parseFunctions.decrementPhotosUser(currentUser.getId());
		parseFunctions.deleteLikesFromPhoto(photo.getId());
		parseFunctions.deleteCommentsFromPhoto(photo.getId());
		return true;
	}
	
	//Function to update news
	/**
	 * Method that get new photos from specific date
	 * @param lastPhotoDate last date
	 * @param albums possible albums to search
	 * @return a list of photos
	 */
	public ArrayList<Photo> getNewsPhotosFromCreatedAt(String lastPhotoDate,ArrayList<Album> albums) {
		return parseFunctions.getNewsPhotosFromCreatedAt(lastPhotoDate,currentUser,Utils.getAlbumsId(albums));
	}
	/**
	 * Method that upload photo
	 * @param photo photo to upload
	 * @param title title of photo
	 * @param activity current activity
	 * @param idAlbum id from album to upload
	 */
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
	/**
	 * Method that download all photos from specific album
	 * @param albumId id from album
	 * @return list of photos
	 */
	public ArrayList<Photo> downloadPhotosFromAlbum(String albumId) {
		return parseFunctions.downloadPhotosFromAlbum(albumId);
	}
	
	//Users methods
	/**
	 * Method that return all users that his name starts with username
	 * @param username username that download users
	 * @return a list of users
	 */
	public ArrayList<User> getUsers(String username) {
		return parseFunctions.getUsers(username,currentUser);
	}
	/**
	 * Method that download users list from list of ids
	 * @param users list of ids to download
	 * @return list of users
	 */
	public ArrayList<User> downloadUsersList(ArrayList<String> users) {
		return parseFunctions.downloadUsersList(users);
	}
	/**
	 * Method that download users that they start with input
	 * @param input string to starts username
	 * @return a list of users
	 */
	public ArrayList<User> downloadUsersInputSearch(String input){
		return parseFunctions.downloadUsersInputSearch(input,currentUser.getId());
	}
	/**
	 * Method that download friends that they start with input
	 * @param input string to starts username
	 * @return list of friends
	 */
	public ArrayList<User> downloadFriendsInputSearch(String input){
		return parseFunctions.downloadFriendsInputSearch(input,currentUser);
	}
	/**
	 * Method that download friends that start with input and they are friends
	 * @param input  string to starts username
	 * @param members list of members
	 * @return users list
	 */
	public ArrayList<User> downloadFriendsInputSearchInAlbumSettings(String input,ArrayList<String> members){
		return parseFunctions.downloadFriendsInputSearchInAlbumSettings(input,currentUser,members);
	}
	
	//Social Network module
	/**
	 * Method that return if current user is login with Twitter
	 * @return true is yes, and false in other case
	 */
	public boolean isLinkedWithTwitter() {
		return parseFunctions.isLinkedWithTwitter();
	}
	/**
	 * Method that return if current user is login with Facebook
	 * @return true is yes, and false in other case
	 */
	public boolean isLinkedWithFacebook() {
		return parseFunctions.isLinkedWithFacebook();
	}
	/**
	 * Method that provides a login wih Facebook
	 * @param activity current activity
	 */
	public void logInFacebook(Activity activity) {
		parseFunctions.logInFacebook(activity);
	}
	/**
	 * Method that provides a login with Twitter
	 * @param activity current activity
	 * @return true is possible, false is not possible
	 */
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
	
	public void importProfilePhotoFromFacebook() {
		parseFunctions.importProfilePhotoFromFacebook();
		String url = ParseUser.getCurrentUser().getString("profilePictureUrl");
		currentUser.setProfilePicture(url);
	}
	
	public boolean importProfilePhotoTwitter() {
		String url = parseFunctions.getProfilePictureTwitterURL();
		currentUser.setProfilePicture(url);
		return parseFunctions.setProfilePictureFromSocialNetworks(url);
	}
	/**
	 * Method that provides a user to like photo
	 * @param idPhoto photo that user likes
	 * @param idAlbum id from album
	 * @return true if operation fine else false
	 */
	public boolean likePhoto(String idPhoto,String idAlbum) {
		currentUser.addLike(idPhoto);
		return parseFunctions.likePhoto(idPhoto,idAlbum);
	}
	/**
	 * Method that return if current like current photo
	 * @param idPhoto id of photo
	 * @return true yes, else false
	 */
	public boolean currentUserLikedCurrentPhoto(String idPhoto) {
		return currentUser.isUserLikedCurrentPhoto(idPhoto);
	}
	/**
	 * Method that adding like of photo from current user
	 * @param idPhoto ip of photo
	 */
	public void addLikePhotoCurrentUser(String idPhoto) {
		currentUser.addLike(idPhoto);
	}
	/**
	 * Method that create a new comment
	 * @param idPhoto id of photo
	 * @param text a comment
	 * @return return a comment
	 */
	public Comment newComment(String idPhoto,String text) {
		User user = getCurrentUserToUserModel();
		return parseFunctions.newComment(idPhoto, text,user);
	}
	
	public int countPhotoLikes(String id) {
		return parseFunctions.countPhotoLikes(id);
	}
	
	public int countPhotoComments(String id) {
		return parseFunctions.countPhotoComments(id);
	}
	
	public int getCommentsNumbersFromPhoto(String id) {
		Photo p = parseFunctions.downloadPhoto(id);
		return p.getCommentsNumber();
	}
	/**
	 * Method that put in current user all photo that he likes
	 */
	public void getAllLikes() {
		parseFunctions.getAllLikes(currentUser);
	}
	
	//Social network module : following followers
	public ArrayList<User> getFollowers() {
		return parseFunctions.getFollowers(currentUser.getId());
	}
	
	public ArrayList<User> getFollowing() {
		return parseFunctions.getFollowing(currentUser.getId());
	}
	
	public boolean deleteFollowing(String idFollowing) {
		currentUser.getFollowing().remove(idFollowing);
		currentUser.decrementFollowingNumber();
		return parseFunctions.deleteFollowing(currentUser.getId(), idFollowing);
	}
	
	public boolean addFollowingAll(ArrayList<String> followings) {
		for(int i = 0; i < followings.size(); i++) {
			if(!addFollowing(followings.get(i)))
				return false;
		}
		return true;
	}
	/**
	 * Method that adding following in current user
	 * @param idFollowing id of new following
	 * @return true if operation fine else false
	 */
	public boolean addFollowing(String idFollowing) {
		Log.v("prototypev1", "add following "+currentUser.getFollowingNumber());
		currentUser.getFollowing().add(idFollowing);
		currentUser.incrementFollowingNumber();
		Log.v("prototypev1", "despres "+currentUser.getFollowingNumber());
		return parseFunctions.addFollowing(currentUser.getId(),idFollowing);
	}
	/**
	 * Method that return a list of recommender users to following
	 * @return a list of recommender users
	 */
	public ArrayList<User> getRecommendedUsers() {
		return parseFunctions.getUsersRecommended(currentUser.getId(),currentUser.getFollowing());
	}
	
	//Functions to change activity
	/**
	 * Method that change current activity to SignUpActivity 
	 */
	public void goToSignUp() {
		Intent signUp = new Intent(Controller.this,SignUpActivity.class);
		startActivity(signUp);
	}
}
