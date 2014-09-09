package com.example.prototypetfgv1.controller;

import android.app.Application;

import com.example.prototypetfgv1.model.User;
import com.example.prototypetfgv1.view.Utils;
import com.parse.Parse;
import com.parse.ParseUser;
 
public class ApplicationClass extends Application {
	
	private User user;
 
    @Override
    public void onCreate() {
        super.onCreate();
 
        Parse.initialize(this, "Pz2ope2OFVDLDypgpdFMpeZiXhnPjm62tDv40b35", "ISRt37kcr6frHkhzvJ3Y9cxhvZxyocO7bP795y4c");
         
       /* ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
 
        // If you would like all objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);
 
        ParseACL.setDefaultACL(defaultACL, true);*/
    }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
    
	public void newUser(ParseUser user, String password) {
    	this.user = new User(user.getObjectId(),user.getUsername(),password,Utils.dateToString(user.getUpdatedAt()));

    }
	
	public void downloadUser(ParseUser user) {
		this.user = new User(user.getObjectId(),user.getUsername(),"pass",user.getJSONArray("photos"),Utils.dateToString(user.getUpdatedAt()));
	}
}
