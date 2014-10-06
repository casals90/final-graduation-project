package com.example.prototypetfgv2.controller;


import android.app.Application;

import com.example.prototypetfgv2.model.User;
import com.example.prototypetfgv2.view.InitActivity;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.PushService;
 
public class ApplicationClass extends Application {
	
	private User user;
 
    @Override
    public void onCreate() {
        super.onCreate();
 
        Parse.initialize(this, "Pz2ope2OFVDLDypgpdFMpeZiXhnPjm62tDv40b35", "ISRt37kcr6frHkhzvJ3Y9cxhvZxyocO7bP795y4c");
        ParseTwitterUtils.initialize("1LRilPY6fB23EKrqq6LkD6DPN", "oOsUsmOcRihiBpdy8ILSvjX4lcKTyb2Dnqaz9ChaQado7ZFyFj");
        //ParseFacebookUtils.initialize("key");
        PushService.setDefaultPushCallback(this, InitActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        
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
