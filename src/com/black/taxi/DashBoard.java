package com.black.taxi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DashBoard extends BaseActivity {
	private TextView tv_user_name;
	private ImageView iv_find_taxi;
	private Button btn_logout,btn_status;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_dashboard);
		
		tv_user_name = (TextView)findViewById(R.id.tv_user);
		tv_user_name.setText("Welcome "+app.getUserInfo().getForename());
		
		iv_find_taxi = (ImageView)findViewById(R.id.iv_find_taxi);
		iv_find_taxi.setOnClickListener(this);
		
		btn_status = (Button)findViewById(R.id.btn_order_status);
		btn_status.setOnClickListener(this);
		
		btn_logout = (Button)findViewById(R.id.btn_logout);
		btn_logout.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_order_status:
			
			break;

		case R.id.btn_logout:
			app.getUserInfo().setLogin(false);
			Intent i = new Intent(DashBoard.this,LoginActivity.class);
			startActivity(i);
			finish();
			break;
			
		case R.id.iv_find_taxi:
			
			break;
		}
	}
}
