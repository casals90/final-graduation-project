package com.example.prototypetfgv2.model;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * Class that define a comment
 * @author jordi
 *
 */
public class Comment implements Parcelable {
	
	private User user;
	private String comment;
	private String date;
	/**
	 * Constructor of comment
	 * @param user user that he has wrote comment
	 * @param comment comment text
	 * @param date date of created comment
	 */
	public Comment(User user, String comment, String date) {
		super();
		this.user = user;
		this.comment = comment;
		this.date = date;
	}
	/**
	 * Parcel constructor
	 * @param in parcel data
	 */
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
	/*
	 * (non-Javadoc)
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		return 0;
	}
	/**
	 * Method that read Parcel data
	 * @param in param to read data
	 */
	public void readToParcel(Parcel in) {
		user = in.readParcelable(User.class.getClassLoader());
		comment = in.readString();
		date = in.readString();
	}
	/*
	 * Method that write date that after recupered to Parcel interface
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(user,flags);
		dest.writeString(comment);
		dest.writeString(date);
	}	
}
