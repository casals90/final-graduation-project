package com.example.prototypetfgv2.model;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Album implements Parcelable {
	
	private String id;
	private String albumCover;
	private String albumTitle;
	private List<String> photos;
	private List<String> members;
		
	public Album(String id,String albumCover, String albumTitle,List<String> photos, List<String> members) {
		super();
		this.id = id;
		this.albumCover = albumCover;
		this.albumTitle = albumTitle;
		this.photos = photos;
		this.members = members;
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

	public List<String> getPhotos() {
		return photos;
	}

	public void setPhotos(List<String> photos) {
		this.photos = photos;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}

	public List<String> getMembers() {
		return members;
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
		dest.writeStringList(photos);
		dest.writeStringList(members);
	}
	
	public void readFromParcel(Parcel in) {
		id = in.readString();
		albumCover = in.readString();
		albumTitle = in.readString();
		in.readStringList(photos);
		in.readStringList(members);
	}
	
	
	

}
