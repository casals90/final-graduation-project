package com.example.prototypetfgv2.controller;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

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
	
	public ParseFunctions getParseFunctions() {
		return parseFunctions;
	}
	
	//UniversalImageLoader init
	public void clearImageLoader() {
		ImageLoader.getInstance().clearDiskCache();
		ImageLoader.getInstance().clearMemoryCache();
	}
	
	public void configureDefaultImageLoader(Context context) {
		// Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
	}
	
	//Current user
	public CurrentUser getCurrentUser() {
		return currentUser;
	}

	public void downloadCurrentUser() {
		this.currentUser = parseFunctions.getCurrentUser();
	}
	
	public void setCurrentUser(CurrentUser currentUser) {
		this.currentUser = currentUser;
	}
	
	public String getFollowersNumber() {
		return String.valueOf(currentUser.getFollowersNumber());
	}
	
	public String getFollowingNumber() {
		return String.valueOf(currentUser.getFollowingNumber());
	}
	
	public String getAlbumsNumber() {
		return String.valueOf(currentUser.getAlbumsNumber());
	}
	
	public String getPhotosNumber() {
		return String.valueOf(currentUser.getPhotosNumber());
	}
	
	public void deleteCurrentUser() {
		currentUser = null;
	}
	
	public User getCurrentUserToUserModel() {
		ParseUser user = ParseUser.getCurrentUser();
		return new User(user.getObjectId(),user.getUsername(),user.getString("profilePictureUrl"),user.getInt("followersNumber"),user.getInt("followingNumber"),user.getInt("photosNumber"),user.getInt("albumsNumber"));
	}
	
	//Parse User methods
	public boolean setUsername(String username) {
		return parseFunctions.setUsername(username);
	}
	
	public String getUsernameFromUserId(String idUser) {
		return parseFunctions.getUsernameFromIdUser(idUser);
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
	
	public String getUsername() {
		return parseFunctions.getUsername();
	}
	
	public String getUsername(String id) {
		return parseFunctions.getUsername(id);
	}
	
	//Parse login and signup
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
	
	public void logout() {
		parseFunctions.logout();
		deleteCurrentUser();
	}
		
	//Albums methods
	public boolean setAlbumTitle(String idAlbum,String newAlbumTitle) {
		return parseFunctions.setAlbumTitle(idAlbum, newAlbumTitle);
	}
	
	public boolean deleteAlbumMember(String idAlbum,String idUser) {
		return parseFunctions.deleteMemberOfAlbum(idAlbum, idUser);
	}
	
	public boolean deleteAlbum(String idAlbum) {
		return parseFunctions.deleteAlbum(idAlbum);
	}
	
	public ArrayList<Album> downloadAlbumsList(ArrayList<String> idAlbums) {
		return parseFunctions.downloadAlbums(idAlbums, currentUser);
	}

	public Album downloadAlbum(String idAlbum) {
		return parseFunctions.downloadAlbum(idAlbum);
	}
	
	public boolean newAlbum(ArrayList<String> members,String albumName) {
		return parseFunctions.newAlbum(members,albumName,currentUser);
	}
	
	public ArrayList<Album> getAlbums() {
		return parseFunctions.getAlbums(currentUser);
	}
	
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
	
	public CurrentAlbum downloadCurrentAlbum() {
		return parseFunctions.getCurrentAlbum();
	}
	
	public String getCurrentAlbumId() {
		return getCurrentAlbum();
	}
	
	public ArrayList<Album> downloadCommonAlbums(String idUser) {
		return parseFunctions.downloadCommonAlbums(currentUser, idUser);
	}
	
	//Photos methods	
	public ArrayList<Photo> downloadMyPhotos(String idUser) {
		if(idUser == null)
			return parseFunctions.downloadMyPhotos(currentUser.getId());
		return parseFunctions.downloadMyPhotos(idUser);
		
	}
	
	public ArrayList<Photo> downloadAllPhotosFromCurrentUser() {
		return parseFunctions.downloadAllPhotosFromCurrentUser(currentUser);
	}
	
	//Function to update news
	public ArrayList<Photo> getNewsPhotosFromCreatedAt(String lastPhotoDate,ArrayList<Album> albums) {
		return parseFunctions.getNewsPhotosFromCreatedAt(lastPhotoDate,currentUser,Utils.getAlbumsId(albums));
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
	
	public ArrayList<Photo> downloadPhotosFromAlbum(String albumId) {
		return parseFunctions.downloadPhotosFromAlbum(albumId);
	}
	
	//Users methods
	public ArrayList<User> getUsers(String username) {
		return parseFunctions.getUsers(username,currentUser);
	}
	
	public ArrayList<User> downloadUsersList(ArrayList<String> users) {
		return parseFunctions.downloadUsersList(users);
	}
	
	public ArrayList<User> downloadUsersInputSearch(String input){
		return parseFunctions.downloadUsersInputSearch(input,currentUser.getId());
	}
	
	public ArrayList<User> downloadFriendsInputSearch(String input){
		return parseFunctions.downloadFriendsInputSearch(input,currentUser);
	}
	
	//Social Network module
	public boolean isLinkedWithTwitter() {
		return parseFunctions.isLinkedWithTwitter();
	}
	
	public boolean isLinkedWithFacebook() {
		return parseFunctions.isLinkedWithFacebook();
	}
	
	public void logInFacebook(Activity activity) {
		parseFunctions.logInFacebook(activity);
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
	
	public void importProfilePhotoFromFacebook() {
		parseFunctions.importProfilePhotoFromFacebook();
	}
	
	public boolean importProfilePhotoTwitter() {
		String url = parseFunctions.getProfilePictureTwitterURL();
		return parseFunctions.setProfilePictureFromSocialNetworks(url);
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
		return parseFunctions.deleteFollowing(currentUser.getId(), idFollowing);
	}
	
	/*public boolean deleteFollower(String idFollower) {
		currentUser.getFollowers().remove(idFollower);
		return parseFunctions.deleteFollower(currentUser.getId(), idFollower);
	}*/
	
	public boolean addFollowingAll(ArrayList<String> followings) {
		for(int i = 0; i < followings.size(); i++) {
			if(!addFollowing(followings.get(i)))
				return false;
		}
		return true;
	}
	
	public boolean addFollowing(String idFollowing) {
		currentUser.getFollowing().add(idFollowing);
		return parseFunctions.addFollowing(currentUser.getId(),idFollowing);
	}
	
	public ArrayList<User> getRecommendedUsers() {
		return parseFunctions.getUsersRecommended(currentUser.getId(),currentUser.getFollowing());
	}
	
	//Functions to change activity
	public void goToSignUp() {
		Intent signUp = new Intent(Controller.this,SignUpActivity.class);
		startActivity(signUp);
	}
	
	//Funcions a modificar:
/*public void setProfilePictureFromTwitter() {
	Bitmap b = getProfilePictureTwitterURL();
	setProfilePicture(b);
}*/
}
