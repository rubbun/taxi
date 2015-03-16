package com.black.taxi.app;

import android.app.Application;

public class MyApplication extends Application{

	public UserInfo userInfo;

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
}
