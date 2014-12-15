package com.example.prototypetfgv2.model;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Album implements Parcelable {
	
	private String id;
	private String albumCover;
	private String albumTitle;
	private List<String> members;
	private String idAdmin;
	private String createdAt;
	private int photosNumber;
	private int membersNumber;
		
	public Album(String id,String albumCover, String albumTitle, List<String> members,int photosNumber,int membersNumber,String idAdmin,String createdAt) {
		super();
		this.id = id;
		this.albumCover = albumCover;
		this.albumTitle = albumTitle;
		this.members = members;
		this.idAdmin = idAdmin;
		this.photosNumber = photosNumber;
		this.membersNumber = membersNumber;
		this.createdAt = createdAt;
	}
	
	public Album(Parcel in) {
		readFromParcel(in);
	}
	
	public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() {
		public Album createFromParcel(Parcel in) {
		    return new Album(in);
		}
		
		public Album[] newArray(int size) {
		    return new Album[size];
		}
	};
	
	public String getAlbumCover() {
		return albumCover;
	}

	public void setAlbumCover(String albumCover) {
		this.albumCover = albumCover;
	}

	public String getAlbumTitle() {
		return albumTitle;
	}

	public void setAlbumTitle(String albumTitle) {
		this.albumTitle = albumTitle;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}

	public List<String> getMembers() {
		return members;
	}
	
	public String getIdAdmin() {
		return idAdmin;
	}

	public void setIdAdmin(String idAdmin) {
		this.idAdmin = idAdmin;
	}

	public int getPhotosNumber() {
		return photosNumber;
	}

	public void setPhotosNumber(int photosNumber) {
		this.photosNumber = photosNumber;
	}

	public int getMembersNumber() {
		return membersNumber;
	}

	public void setMembersNumber(int membersNumber) {
		this.membersNumber = membersNumber;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(albumCover);
		dest.writeString(albumTitle);
		dest.writeStringList(members);
		dest.writeInt(photosNumber);
		dest.writeInt(membersNumber);
		dest.writeString(idAdmin);
		dest.writeString(createdAt);
	}
	
	public void readFromParcel(Parcel in) {
		id = in.readString();
		albumCover = in.readString();
		albumTitle = in.readString();
		in.readStringList(members);
		photosNumber = in.readInt();
		membersNumber = in.readInt();
		idAdmin = in.readString();
		createdAt = in.readString();
	}
}
