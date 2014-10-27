package com.example.prototypetfgv2.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Comment implements Parcelable {
	
	private String idUser;
	private String comment;
	
	public Comment(String idUser, String comment) {
		super();
		this.idUser = idUser;
		this.comment = comment;
	}
	
	public Comment(Parcel in) {
		readParcel(in);
	}
	
	public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
		public Comment createFromParcel(Parcel in) {
		    return new Comment(in);
		}
		
		public Comment[] newArray(int size) {
		    return new Comment[size];
		}
	};

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(idUser);
		dest.writeString(comment);
	}
	
	private void readParcel(Parcel in) {
		// TODO Auto-generated method stub
		idUser = in.readString();
		comment = in.readString();
	}
}
