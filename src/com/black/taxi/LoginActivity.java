package com.black.taxi;

import org.json.JSONException;
import org.json.JSONObject;

import com.black.taxi.network.HttpClient;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends BaseActivity {
	
	private EditText et_email, et_password;
	private Button btn_login;
	private TextView tv_reg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		et_email = (EditText)findViewById(R.id.et_email);
		et_password = (EditText)findViewById(R.id.et_password);
		btn_login = (Button)findViewById(R.id.btn_login);
		tv_reg = (TextView)findViewById(R.id.tv_reg);
		btn_login.setOnClickListener(this);
		tv_reg.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_login:
			if(isvalid()){
				new LoginAsynctak().execute();
			}
			
			break;
		case R.id.tv_reg:
			Intent intent  = new Intent(getApplicationContext(), RegistrationActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	public boolean isvalid(){
		boolean flag = true;
		if(et_email.getText().toString().trim().length()==0){
			et_email.setError("Please enter email");
			flag = false;
		}else if(et_password.getText().toString().trim().length()==0){
			et_password.setError("Please enter password");
			flag = false;
		}
		return flag;
	}
	
	public class LoginAsynctak extends AsyncTask<Void, Void, Boolean>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			doShowLoading();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			JSONObject obj = new JSONObject();
			try {
				obj.put("email", et_email.getText().toString().trim());
				obj.put("password", et_password.getText().toString().trim());
			
				String response = HttpClient.SendHttpPost("", obj.toString());
				if(response != null){
					JSONObject ob = new JSONObject(response);
					return ob.getBoolean("status");
				}
			
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			doRemoveLoading();
			if(result){
				
			}
		}
	}
}
