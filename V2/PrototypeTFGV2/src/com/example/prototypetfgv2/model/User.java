package com.example.prototypetfgv2.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
	
	String id;
	String username;
	String profilePicture;
	private int followersNumber;
	private int followingNumber;
	private int photosNumber;
	private int albumsNumber;
	
	public User(String id, String username, String profilePicture,int followersNumber,int followingNumber,int photosNumber,int albumsNumber) {
		super();
		this.id = id;
		this.username = username;
		this.profilePicture = profilePicture;
		this.followersNumber = followersNumber;
		this.followingNumber = followingNumber;
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

	public int getFollowersNumber() {
		return followersNumber;
	}

	public void setFollowersNumber(int followersNumber) {
		this.followersNumber = followersNumber;
	}

	public int getFollowingNumber() {
		return followingNumber;
	}

	public void setFollowingNumber(int followingNumber) {
		this.followingNumber = followingNumber;
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
	
	public void incrementFollowersNumber() {
		followersNumber ++;
	}
	
	public void decrementFollowersNumber() {
		if(followersNumber > 0)
			followersNumber --;
		else
			followersNumber = 0;
	}
	
	public void decrementPhotosNumber() {
		if(photosNumber > 0)
			photosNumber--;
		else
			photosNumber = 0;
	}
	
	public void decrementAlbumsNumber() {
		if(albumsNumber > 0)
			albumsNumber--;
		else
			albumsNumber = 0;
	}
	
	public void incrementPhotosNumber() {
		photosNumber++;
	}

	public void incrementAlbumsNumber() {
		albumsNumber++;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(username);
		dest.writeString(profilePicture);
		dest.writeInt(followersNumber);
		dest.writeInt(followingNumber);
		dest.writeInt(albumsNumber);
		dest.writeInt(photosNumber);
	}
	
	private void readFromParcel(Parcel in) {
        id = in .readString();
        username = in .readString();
        profilePicture = in .readString();
        followersNumber = in.readInt();
        followingNumber = in.readInt();
        albumsNumber = in.readInt();
        photosNumber = in.readInt();
    }
} 
