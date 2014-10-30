package com.example.prototypetfgv2.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Comment implements Parcelable {
	
	private User user;
	private String comment;
	private String date;
	
	public Comment(User user, String comment, String date) {
		super();
		this.user = user;
		this.comment = comment;
		this.date = date;
	}
	
	public Comment(Parcel in) {
		readToParcel(in);
	}
	
	public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
		public Comment createFromParcel(Parcel in) {
		    return new Comment(in);
		}
		
		public Comment[] newArray(int size) {
		    return new Comment[size];
		}
	};

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void readToParcel(Parcel in) {
		user = in.readParcelable(User.class.getClassLoader());
		comment = in.readString();
		date = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(user,flags);
		dest.writeString(comment);
		dest.writeString(date);
		
	}	
}
