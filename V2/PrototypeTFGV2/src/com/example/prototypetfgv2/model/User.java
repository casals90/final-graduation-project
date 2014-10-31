package com.example.prototypetfgv2.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
	
	String id;
	String username;
	String profilePicture;
	int commonFriends;
	
	public User(String id, String username, String profilePicture,int commonFriends) {
		super();
		this.id = id;
		this.username = username;
		this.profilePicture = profilePicture;
		this.commonFriends = commonFriends;
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

	public int getCommonFriends() {
		return commonFriends;
	}

	public void setCommonFriends(int commonFriends) {
		this.commonFriends = commonFriends;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(id);
		dest.writeString(username);
		dest.writeString(profilePicture);
		dest.writeInt(commonFriends);
	}
	
	private void readFromParcel(Parcel in) {
        id = in .readString();
        username = in .readString();
        profilePicture = in .readString();
        commonFriends = in .readInt();
    }
} 
