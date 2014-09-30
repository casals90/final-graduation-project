package com.example.prototypetfgv1.controller;

import android.app.Application;

import com.example.prototypetfgv1.model.User;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
 
public class ApplicationClass extends Application {
	
	private User user;
 
    @Override
    public void onCreate() {
        super.onCreate();
 
        Parse.initialize(this, "Pz2ope2OFVDLDypgpdFMpeZiXhnPjm62tDv40b35", "ISRt37kcr6frHkhzvJ3Y9cxhvZxyocO7bP795y4c"); 
       
        //ParseFacebookUtils.initialize("826968380688405");
    }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
    
	public void newUser(ParseUser user) {
    	this.user = new User(user.getObjectId(),user.getUsername(),null,0);

    }
	
	public void downloadUser(ParseUser user) {
		this.user = new User(user.getObjectId(),user.getUsername(),null,0);
	}
	
	public void deleteUser() {
		user = null;
	}
}
