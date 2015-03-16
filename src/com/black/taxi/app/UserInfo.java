package com.black.taxi.app;

import com.black.taxi.constants.Constant;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserInfo {

	public String user_id, email, phone, forename, surname;
	public boolean isLogin;
	public SharedPreferences pref;

	public UserInfo(Context context) {

		pref = context.getSharedPreferences(Constant.Values.USER_INFO.name(), Context.MODE_PRIVATE);
		isLogin = pref.getBoolean(Constant.Values.LOGIN_STATUS.name(), false);
		user_id = pref.getString(Constant.Values.USERID.name(), null);
		email = pref.getString(Constant.Values.EMAIL.name(), null);
		phone = pref.getString(Constant.Values.PHONE.name(), null);
		forename = pref.getString(Constant.Values.FORENAME.name(), null);
		surname = pref.getString(Constant.Values.SURNAME.name(), null);
	}

	public void setValues(Boolean isLogin,String userid, String email, String phone, String forename, String surname) {
		this.user_id = userid;
		this.email = email;
		this.phone = phone;
		this.forename = forename;
		this.surname = surname;
		this.isLogin = isLogin;
		
		Editor edit = pref.edit();
		edit.putBoolean(Constant.Values.LOGIN_STATUS.name(), isLogin);
		edit.putString(Constant.Values.USERID.name(), user_id);
		edit.putString(Constant.Values.EMAIL.name(), email);
		edit.putString(Constant.Values.PHONE.name(), phone);
		edit.putString(Constant.Values.FORENAME.name(), forename);
		edit.putString(Constant.Values.SURNAME.name(), surname);
		edit.commit();
	}

	public String getUser_id() {
		return user_id;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public String getForename() {
		return forename;
	}

	public String getSurname() {
		return surname;
	}

	public SharedPreferences getPref() {
		return pref;
	}

	public boolean isLogin() {
		return pref.getBoolean(Constant.Values.LOGIN_STATUS.name(), false);
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
		Editor edit = pref.edit();
		edit.putBoolean(Constant.Values.LOGIN_STATUS.name(), isLogin);
		edit.commit();
	}
}
