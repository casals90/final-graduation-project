package com.example.prototypetfgv2.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {
	
	private String id;
    private String title;
    private String photo;
    private String createdAt;
    private User ownerUser;
    
	public Photo(String id, String title, String photo, String createdAt,User ownerUser) {
		super();
		this.id = id;
		this.title = title;
		this.photo = photo;
		this.createdAt = createdAt;
		this.ownerUser = ownerUser;
	}
	
	public Photo(Parcel in) {
		readParcel(in);
	}
	
	public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
		public Photo createFromParcel(Parcel in) {
		    return new Photo(in);
		}
		
		public Photo[] newArray(int size) {
		    return new Photo[size];
		}
	};

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	
	public User getOwnerUser() {
		return ownerUser;
	}

	public void setOwnerUser(User ownerUser) {
		this.ownerUser = ownerUser;
	}

	public ArrayList<String>  jsonArrayToArrayListLike(JSONArray likes) {
		ArrayList<String> l = new ArrayList<String>();
		for(int i = 0; i < likes.length(); i++) {
			try {
				l.add(likes.getString(i));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return l;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(id);
		dest.writeString(title);
		dest.writeString(photo);
		dest.writeString(createdAt);
		dest.writeParcelable(ownerUser,flags);
	}
	
	private void readParcel(Parcel in) {
		// TODO Auto-generated method stub
		id = in.readString();
		title = in.readString();
		photo = in.readString();
		createdAt = in.readString();
		ownerUser = in.readParcelable(User.class.getClassLoader());
	}
}
