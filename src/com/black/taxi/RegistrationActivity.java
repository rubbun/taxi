package com.black.taxi;

import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.black.taxi.constants.Constant;
import com.black.taxi.network.HttpClient;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends BaseActivity implements LocationListener {

	public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private EditText et_email, et_phone, et_forename, et_surname, et_password, et_confirm_password;
	private Button btn_register;
	private LocationManager lm;
	private double latitude,longitude;
	public boolean isFirstTime = true;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_register);
		et_email = (EditText) findViewById(R.id.et_email);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_forename = (EditText) findViewById(R.id.et_forename);
		et_surname = (EditText) findViewById(R.id.et_surname);
		et_password = (EditText) findViewById(R.id.et_password);
		et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);

		btn_register = (Button) findViewById(R.id.btn_register);
		btn_register.setOnClickListener(this);
		
		if(isConnectingToInternet()){
			lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0000,000, this);
		}
	}
	
	public boolean isvalid() {
		boolean flag = true;
		if (et_email.getText().toString().trim().length() == 0) {
			et_email.setError("Pleaswe enter email");
			flag = false;
		} else if (!isvalidMailid(et_email.getText().toString().trim())) {
			et_email.setError("In valid email id");
			flag = false;
		}else if (et_phone.getText().toString().trim().length() == 0) {
			et_phone.setError("Pleaswe enter phone");
			flag = false;
		} else if (!(et_phone.getText().toString().trim().length() >=10)) {
			et_phone.setError("In valid phone number");
			flag = false;
		} else if (et_phone.getText().toString().trim().length() == 0) {
			et_phone.setError("Pleaswe enter phone");
			flag = false;
		} else if (et_forename.getText().toString().trim().length() == 0) {
			et_forename.setError("Pleaswe enter forename");
			flag = false;
		} else if (et_surname.getText().toString().trim().length() == 0) {
			et_surname.setError("Pleaswe enter surname");
			flag = false;
		} else if (et_password.getText().toString().trim().length() == 0) {
			et_password.setError("Pleaswe enter password");
			flag = false;
		} else if (et_password.getText().toString().trim().length() < 6) {
			et_password.setError("Password must br atleast 6 digit");
			flag = false;
		} else if (et_confirm_password.getText().toString().trim().length() == 0) {
			et_confirm_password.setError("Pleaswe enter password");
			flag = false;
		} else if (!et_confirm_password.getText().toString().trim().equalsIgnoreCase(et_password.getText().toString().trim())) {
			Toast.makeText(RegistrationActivity.this, "Password mismatch...", Toast.LENGTH_LONG).show();
			flag = false;
		}
		return flag;
	}

	public boolean isvalidMailid(String mail) {
		return Pattern.compile(EMAIL_PATTERN).matcher(mail).matches();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_register:
			if (isvalid()) {
				new SendUserRegDataToserver().execute();
			}
			break;
		}
	}

	public class SendUserRegDataToserver extends AsyncTask<Void, Void, Boolean> {

		String message = "";
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
				obj.put("phone", et_phone.getText().toString().trim());
				obj.put("forename", et_forename.getText().toString().trim());
				obj.put("surname", et_surname.getText().toString().trim());
				obj.put("password", et_password.getText().toString().trim());
				obj.put("latitude", ""+latitude);
				obj.put("longitude", ""+longitude);
				
				String response = HttpClient.SendHttpPost(Constant.REGISTRATION, obj.toString());
				if(response != null){
					JSONObject ob = new JSONObject(response);
					if(ob.has("message")){
						message = ob.getString("message");
					}
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
				Toast.makeText(RegistrationActivity.this, "You have successfully registered..", Toast.LENGTH_LONG).show();
				finish();
			}else{
				if(message.length()>0){
					Toast.makeText(RegistrationActivity.this, message, Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(RegistrationActivity.this, "Error occured ..please try again", Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		if (isFirstTime) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			isFirstTime = false;
		}
	}

	@Override
	public void onProviderDisabled(String arg0) {
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
	
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		lm.removeUpdates(this);
	}
}
