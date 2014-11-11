package com.example.prototypetfgv2.model;

import java.util.ArrayList;

import android.util.Log;

public class CurrentUser {
	
	private String id;
	private String username;
	private String profilePicture;
	private ArrayList<String> likes;
	private ArrayList<String> albums;
	
	public CurrentUser(String id, String username, String profilePicture,
			ArrayList<String> likes,ArrayList<String> albums) {
		super();
		this.id = id;
		this.username = username;
		this.profilePicture = profilePicture;
		this.likes = likes;
		this.albums = albums;
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

	public void addLike(String id) {
		likes.add(id);
	}
	
	public void addAlbum(String id) {
		albums.add(id);
	}
	
	public void deleteAlbum(String id) {
		albums.remove(id);
	}
	
	public boolean isUserLikedCurrentPhoto(String id) {
		return likes.contains(id);
	}
	
	public boolean isUserAdmin(String id) {
		return albums.contains(id.toString());
	}
}
