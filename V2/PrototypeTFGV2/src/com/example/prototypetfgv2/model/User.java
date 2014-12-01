package com.example.prototypetfgv2.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
	
	String id;
	String username;
	String profilePicture;
	private int friendsNumber;
	private int photosNumber;
	private int albumsNumber;
	
	public User(String id, String username, String profilePicture,int friendsNumber,int photosNumber,int albumsNumber) {
		super();
		this.id = id;
		this.username = username;
		this.profilePicture = profilePicture;
		this.friendsNumber = friendsNumber;
		this.photosNumber = photosNumber;
		this.albumsNumber = albumsNumber;
	}
	
	public User(Parcel in) {
		readFromParcel(in);
	}
	
	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		public User createFromParcel(Parcel in) {
		    return new User(in);
		}
		
		public User[] newArray(int size) {
		    return new User[size];
		}
	};

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	@Override
	public int describeContents() {
		return 0;
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

	public int getAlbumsNumber() {
		return albumsNumber;
	}

	public void setAlbumsNumber(int albumsNumber) {
		this.albumsNumber = albumsNumber;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(username);
		dest.writeString(profilePicture);
		dest.writeInt(friendsNumber);
		dest.writeInt(albumsNumber);
		dest.writeInt(photosNumber);
	}
	
	private void readFromParcel(Parcel in) {
        id = in .readString();
        username = in .readString();
        profilePicture = in .readString();
        friendsNumber = in.readInt();
        albumsNumber = in.readInt();
        photosNumber = in.readInt();
    }
} 
