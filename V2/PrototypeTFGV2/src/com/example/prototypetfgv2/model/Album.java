package com.example.prototypetfgv2.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * Class that represented album in a model 
 * @author jordi
 *
 */
public class Album implements Parcelable {
	
	private String id;
	private String albumCover;
	private String albumTitle;
	private List<String> members;
	private String idAdmin;
	private String createdAt;
	private int photosNumber;
	private int membersNumber;
	/**
	 * Constructor of Album	
	 * @param id id of album
	 * @param albumCover url of cover photo
	 * @param albumTitle album title
	 * @param members list of album members
	 * @param photosNumber photos number of album
	 * @param membersNumber members number of album
	 * @param idAdmin id of user admin
	 * @param createdAt string that contains date of created album
	 */
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
	/**
	 * Parcel constructor
	 * @param in parcel data
	 */
	public Album(Parcel in) {
		readFromParcel(in);
	}
	/**
	 * Method that creates a object from Parcel interface
	 */
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
	/*
	 * (non-Javadoc)
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		return 0;
	}
	/*
	 * Method that write date that after recupered to Parcel interface
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
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
	/**
	 * Method that read Parcel data
	 * @param in param to read data
	 */
	public void readFromParcel(Parcel in) {
		id = in.readString();
		albumCover = in.readString();
		albumTitle = in.readString();
		members = new ArrayList<String>();
		in.readStringList(members);
		photosNumber = in.readInt();
		membersNumber = in.readInt();
		idAdmin = in.readString();
		createdAt = in.readString();
	}
}
