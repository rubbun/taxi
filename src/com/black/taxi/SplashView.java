package com.black.taxi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashView extends BaseActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_splash);
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(app.getUserInfo().isLogin()){
			Intent i = new Intent(SplashView.this,DashBoard.class);
			startActivity(i);
			finish();
		}else{
			stall();
		}
	}

	public void stall() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(intent);
				finish();
			}
		}, 4000);
	}
}
