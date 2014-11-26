package com.example.prototypetfgv2.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Photo implements Parcelable {
	
	private String id;
    private String title;
    private String photo;
    private String createdAt;
    private String ownerAlbum;
    private User ownerUser;
    private int likesNumber;
    private int commentsNumber;
    
    //Only for download photo from album
    //Borrar al actutalitzar 
	public Photo(String id, String title, String photo, String createdAt) {
		super();
		this.id = id;
		this.title = title;
		this.photo = photo;
		this.createdAt = createdAt;
		//this.ownerUser = ownerUser;
	}
	
	public Photo(String id, String title, String photo, String createdAt,User ownerUser,int likesNumber,int commentNumber) {
		super();
		this.id = id;
		this.title = title;
		this.photo = photo;
		this.createdAt = createdAt;
		this.ownerUser = ownerUser;
		this.likesNumber = likesNumber;
		this.commentsNumber = commentNumber;
	}
	
	public Photo(String id, String title, String photo, String createdAt,User ownerUser,int likesNumber,int commentNumber,String idAlbum) {
		super();
		this.id = id;
		this.title = title;
		this.photo = photo;
		this.createdAt = createdAt;
		this.ownerUser = ownerUser;
		this.likesNumber = likesNumber;
		this.commentsNumber = commentNumber;
		this.ownerAlbum = idAlbum;
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

	public int getLikesNumber() {
		return likesNumber;
	}

	public void setLikesNumber(int likesNumber) {
		this.likesNumber = likesNumber;
	}

	public int getCommentsNumber() {
		return commentsNumber;
	}

	public void setCommentsNumber(int commentsNumber) {
		this.commentsNumber = commentsNumber;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public String getOwnerAlbum() {
		return ownerAlbum;
	}

	public void setOwnerAlbum(String ownerAlbum) {
		this.ownerAlbum = ownerAlbum;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(id);
		dest.writeString(title);
		dest.writeString(photo);
		dest.writeString(createdAt);
		dest.writeString(ownerAlbum);
		dest.writeParcelable(ownerUser,flags);
		dest.writeInt(likesNumber);
		dest.writeInt(commentsNumber);
	}
	
	private void readParcel(Parcel in) {
		// TODO Auto-generated method stub
		id = in.readString();
		title = in.readString();
		photo = in.readString();
		createdAt = in.readString();
		ownerAlbum = in.readString();
		ownerUser = in.readParcelable(User.class.getClassLoader());
		likesNumber = in.readInt();
		commentsNumber = in.readInt();
	}
	
	public void incrementNumberLikes() {
		likesNumber++;
	}
}
