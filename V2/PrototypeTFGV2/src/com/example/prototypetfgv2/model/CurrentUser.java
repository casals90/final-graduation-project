package com.example.prototypetfgv2.model;

import java.util.ArrayList;
/**
 * Class to store current user date in application
 * @author jordi
 *
 */
public class CurrentUser {
	
	private String id;
	private String username;
	private String profilePicture;
	private String currentAlbum;
	private int followingNumber;
	private int followersNumber;
	private int photosNumber;
	private int albumsNumber;
	private ArrayList<String> likes;
	private ArrayList<String> albums;
	private ArrayList<String> ownerPhotosFromAlbum;
	private ArrayList<String> albumsAdmin;
	private ArrayList<String> following;
	private ArrayList<String> followers;
	/**
	 * Constructor of current user
	 * @param id id of current user
	 * @param username username of current user
	 * @param profilePicture profile picture of current user
	 * @param currentAlbum current album of current user
	 * @param followingNumber followingNumber of current user
	 * @param followersNumber followersNumber of current user
	 * @param photosNumber photosNumber of current user
	 * @param albumsNumber albumsNumber of current user
	 * @param followers followers of current user
	 * @param following following of current user
	 */
	public CurrentUser(String id, String username, String profilePicture,String currentAlbum,
			int followingNumber,int followersNumber,int photosNumber,int albumsNumber,ArrayList<String> followers,ArrayList<String> following) {
		super();
		this.id = id;
		this.username = username;
		this.profilePicture = profilePicture;
		this.currentAlbum = currentAlbum;
		this.followingNumber = followingNumber;
		this.followersNumber = followersNumber;
		this.photosNumber = photosNumber;
		this.likes = new ArrayList<String>();
		this.albums = new ArrayList<String>();
		this.ownerPhotosFromAlbum = new ArrayList<String>();
		this.albumsAdmin = new ArrayList<String>();
		this.albumsNumber = albumsNumber;
		this.followers = followers;
		this.following = following;
	}

	public String getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	public ArrayList<String> getLikes() {
		return likes;
	}

	public void setLikes(ArrayList<String> likes) {
		this.likes = likes;
	}

	public ArrayList<String> getAlbums() {
		return albums;
	}

	public void setAlbums(ArrayList<String> albums) {
		this.albums = albums;
	}
	
	public ArrayList<String> getOwnerPhotosFromAlbum() {
		return ownerPhotosFromAlbum;
	}

	public void setOwnerPhotosFromAlbum(ArrayList<String> ownerPhotosFromAlbum) {
		this.ownerPhotosFromAlbum = ownerPhotosFromAlbum;
	}

	public ArrayList<String> getAlbumsAdmin() {
		return albumsAdmin;
	}

	public void setAlbumsAdmin(ArrayList<String> albumsAdmin) {
		this.albumsAdmin = albumsAdmin;
	}

	public String getCurrentAlbum() {
		return currentAlbum;
	}

	public void setCurrentAlbum(String currentAlbum) {
		this.currentAlbum = currentAlbum;
	}
	
	public int getAlbumsNumber() {
		return albumsNumber;
	}

	public void setAlbumsNumber(int albumsNumber) {
		this.albumsNumber = albumsNumber;
	}

	public int getFollowingNumber() {
		return followingNumber;
	}

	public void setFollowingNumber(int followingNumber) {
		this.followingNumber = followingNumber;
	}

	public int getFollowersNumber() {
		return followersNumber;
	}

	public void setFollowersNumber(int followersNumber) {
		this.followersNumber = followersNumber;
	}

	public int getPhotosNumber() {
		return photosNumber;
	}

	public void setPhotosNumber(int photosNumber) {
		this.photosNumber = photosNumber;
	}
	
	public ArrayList<String> getFollowing() {
		return following;
	}

	public void setFollowing(ArrayList<String> following) {
		this.following = following;
	}

	public ArrayList<String> getFollowers() {
		return followers;
	}

	public void setFollowers(ArrayList<String> followers) {
		this.followers = followers;
	}
	
	// End getters and setters
	/**
	 * Method to adding a new photo like
	 * @param idPhoto id of photo
	 */
	public void addLike(String idPhoto) {
		likes.add(idPhoto);
	}
	/**
	 * Method to adding a new album
	 * @param idAlbum id of new album
	 */
	public void addAlbum(String idAlbum) {
		albums.add(idAlbum);
	}
	/**
	 * Method to delete album
	 * @param id album to delete
	 */
	public void deleteAlbum(String idAlbum) {
		albums.remove(idAlbum);
	}
	/**
	 * Method that if current user like current photo
	 * @param idPhoto id of photo
	 * @return true is yes else false
	 */
	public boolean isUserLikedCurrentPhoto(String idPhoto) {
		return likes.contains(idPhoto);
	}
	/**
	 * Method that if current user is admin of album 
	 * @param idAlbum id of album
	 * @return true is yes else false
	 */
	public boolean isUserAdmin(String idAlbum) {
		return albums.contains(idAlbum);
	}
	/**
	 * Method that adding owner photos in a list
	 * @param idPhoto id of photo
	 */
	public void addOwnerPhotosFromAlbum(String idPhoto) {
		ownerPhotosFromAlbum.add(idPhoto);
	}
	/**
	 * Method that if current photo if from user 
	 * @param idPhoto
	 * @return true yes else false
	 */
	public boolean isOwnerUserCurrentUser(String idPhoto) {
		return ownerPhotosFromAlbum.contains(idPhoto);
	}
	/**
	 * Method that adding albums that current user is admin
	 * @param idAlbum
	 */
	public void addAlbumAdmin(String idAlbum) {
		albumsAdmin.add(idAlbum);
	}
	
	public boolean isCurrentUserAdmin(String idAlbum) {
		return albumsAdmin.contains(idAlbum);
	}
	
	public void incrementFollowersNumber() {
		followersNumber ++;
	}
	
	public void incrementFollowingNumber() {
		followingNumber ++;
	}
	
	public void decrementFollowersNumber() {
		if(followersNumber > 0)
			followersNumber --;
		else
			followersNumber = 0;
	}
	
	public void decrementFollowingNumber() {
		if(followingNumber > 0)
			followingNumber --;
		else
			followingNumber = 0;
	}
	
	public void addFollower(String idFollower) {
		followers.add(idFollower);
	}
	
	public void deleteFollower(String idFollower) {
		followers.remove(idFollower);
	}
	
	public void addFollowing(String idFollowing) {
		following.add(idFollowing);
	}
	
	public void deleteFollowing(String idFollowing) {
		following.remove(idFollowing);
	}
	
	public void deletePhotoLike(String id) {
		likes.remove(id);
	}
	
	public void decrementPhotosNumber() {
		photosNumber--;
		if(photosNumber <= 0)
			photosNumber = 0;
	}
	
	public void deleteOwnerPhotos(String id) {
		ownerPhotosFromAlbum.remove(id);
	}
}
