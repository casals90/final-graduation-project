package com.example.prototypetfgv2.model;

import java.util.ArrayList;

public class CurrentUser {
	
	private String id;
	private String username;
	private String profilePicture;
	private String currentAlbum;
	private int friendsNumber;
	private int photosNumber;
	private int albumsNumber;
	private ArrayList<String> likes;
	private ArrayList<String> albums;
	private ArrayList<String> ownerPhotosFromAlbum;
	private ArrayList<String> albumsAdmin;
	
	public CurrentUser(String id, String username, String profilePicture,String currentAlbum,int friendsNumber,int photosNumber,int albumsNumber) {
		super();
		this.id = id;
		this.username = username;
		this.profilePicture = profilePicture;
		this.currentAlbum = currentAlbum;
		this.friendsNumber = friendsNumber;
		this.photosNumber = photosNumber;
		this.likes = new ArrayList<String>();
		this.albums = new ArrayList<String>();
		this.ownerPhotosFromAlbum = new ArrayList<String>();
		this.albumsAdmin = new ArrayList<String>();
		this.albumsNumber = albumsNumber;
	}

	/*public CurrentUser(String id, String username, String profilePicture,ArrayList<String> likes,ArrayList<String> albums) {
		super();
		this.id = id;
		this.username = username;
		this.profilePicture = profilePicture;
		this.likes = likes;
		this.albums = albums;
		this.ownerPhotosFromAlbum = new ArrayList<String>();
		this.albumsAdmin = new ArrayList<String>();
	}*/

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
	
	public int getFriendsNumber() {
		return friendsNumber;
	}

	public void setFriendsNumber(int friendsNumber) {
		this.friendsNumber = friendsNumber;
	}

	public int getPhotosNumber() {
		return photosNumber;
	}

	public void setPhotosNumber(int photosNumber) {
		this.photosNumber = photosNumber;
	}
	
	// End getters and setters

	public void addLike(String idPhoto) {
		likes.add(idPhoto);
	}
	
	public void addAlbum(String idAlbum) {
		albums.add(idAlbum);
	}
	
	public void deleteAlbum(String idAlbum) {
		albums.remove(idAlbum);
	}
	
	public boolean isUserLikedCurrentPhoto(String idPhoto) {
		return likes.contains(idPhoto);
	}
	
	public boolean isUserAdmin(String idAlbum) {
		return albums.contains(idAlbum);
	}
	
	public void addOwnerPhotosFromAlbum(String idPhoto) {
		ownerPhotosFromAlbum.add(idPhoto);
	}
	
	public boolean isOwnerUserCurrentUser(String idPhoto) {
		return ownerPhotosFromAlbum.contains(idPhoto);
	}
	
	public void addAlbumAdmin(String idAlbum) {
		albumsAdmin.add(idAlbum);
	}
	
	public boolean isCurrentUserAdmin(String idAlbum) {
		return albumsAdmin.contains(idAlbum);
	}
}
