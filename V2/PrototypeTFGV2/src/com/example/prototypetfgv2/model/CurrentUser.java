package com.example.prototypetfgv2.model;

import java.util.ArrayList;

public class CurrentUser {
	
	private String id;
	private String username;
	private String profilePicture;
	private ArrayList<String> likesPhotosInsideAlbum;
	private ArrayList<String> albums;
	private ArrayList<String> ownerPhotosFromAlbum;
	private ArrayList<String> albumsAdmin;
	
	public CurrentUser(String id, String username, String profilePicture) {
		super();
		this.id = id;
		this.username = username;
		this.profilePicture = profilePicture;
		this.likesPhotosInsideAlbum = new ArrayList<String>();
		this.albums = new ArrayList<String>();
		this.ownerPhotosFromAlbum = new ArrayList<String>();
		this.albumsAdmin = new ArrayList<String>();
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
	
	public ArrayList<String> getLikesPhotosInsideAlbum() {
		return likesPhotosInsideAlbum;
	}

	public void setLikesPhotosInsideAlbum(ArrayList<String> likesPhotosInsideAlbum) {
		this.likesPhotosInsideAlbum = likesPhotosInsideAlbum;
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

	// End getters and setters
	
	public void addLike(String idPhoto) {
		likesPhotosInsideAlbum.add(idPhoto);
	}
	
	public void addAlbum(String idAlbum) {
		albums.add(idAlbum);
	}
	
	public void deleteAlbum(String idAlbum) {
		albums.remove(idAlbum);
	}
	
	public boolean isUserLikedCurrentPhoto(String idPhoto) {
		return likesPhotosInsideAlbum.contains(idPhoto);
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
