package com.black.taxi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.black.taxi.constants.Constant;
import com.black.taxi.network.HttpClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FindtaxiActivity extends BaseActivity implements LocationListener {

	private GoogleMap googleMap;
	private LinearLayout ll_details;
	private TextView tv_name,tv_address,tv_postcode;
	private Button btn_request_online,btn_call;
	
	private LocationManager lm;
	private double latitude,longitude;
	public boolean isFirstTime = true;
	public JSONArray jarray;
	int pos = -1;;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_map);

		try {
			initilizeMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ll_details = (LinearLayout)findViewById(R.id.ll_details);
		ll_details.setVisibility(View.INVISIBLE);
		
		tv_name = (TextView)findViewById(R.id.tv_name);
		tv_address = (TextView)findViewById(R.id.tv_address);
		tv_postcode = (TextView)findViewById(R.id.tv_postcode);
		
		btn_request_online = (Button)findViewById(R.id.btn_request_online);
		btn_request_online.setOnClickListener(this);
		
		btn_call = (Button)findViewById(R.id.btn_call);
		btn_call.setOnClickListener(this);
		
		if(isConnectingToInternet()){
			lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0000,000, this);
		}
		getAllTaxiLocation();
		
		googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				for(int i= 0 ; i<jarray.length();i++){
					JSONObject c;
					try {
						c = jarray.getJSONObject(i);
						if(marker.getTitle().equalsIgnoreCase(c.getString("name"))){
							pos = i;
							ll_details.setVisibility(View.VISIBLE);
							tv_name.setText("Name: "+c.getString("name"));
							tv_address.setText("Address: "+c.getString("address"));
							tv_postcode.setText("Postcode: "+c.getString("postcode"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				return false;
			}
		});
	}
	private void getAllTaxiLocation() {
		new CallServerForTaxiLocation().execute();
	}
	
	public class CallServerForTaxiLocation extends AsyncTask<Void, Void, JSONArray>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			doShowLoading();
		}
		
		@Override
		protected JSONArray doInBackground(Void... params) {
			JSONObject ob = new JSONObject();
			try {
				ob.put("userID", app.getUserInfo().getUser_id());
				String response = HttpClient.SendHttpPost(Constant.FINDTAXI, ob.toString());
				if(response != null){
					JSONObject obj = new JSONObject(response);
					if(obj.getBoolean("status")){
						JSONArray arr = obj.getJSONArray("taxies");
						return arr;
					}else{
						return null;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(JSONArray result) {
			super.onPostExecute(result);
			doRemoveLoading();
			jarray = result;
			if(result.length() > 0){
				for(int i = 0; i < result.length(); i++){
					try {
						JSONObject object = result.getJSONObject(i);
						MarkerOptions marker;
						marker = new MarkerOptions().position(new LatLng(Double.parseDouble(object.getString("latitude")), Double.parseDouble(object.getString("longitude")))).title(object.getString("name"));
						googleMap.addMarker(marker);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
			CameraPosition cameraPosition = new CameraPosition.Builder().target(
	                new LatLng(latitude, longitude)).zoom(12).build();
			googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		}
	}
	
	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

			/*MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude,longitude)).title("");
			googleMap.addMarker(marker);
			
			CameraPosition cameraPosition = new CameraPosition.Builder().target(
	                new LatLng(latitude, longitude)).zoom(12).build();
			googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
			
			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_call:
			if(pos >=0){
				 String number;
				try {
					number = "tel:" +jarray.getJSONObject(pos).getString("phone");
					 Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number)); 
				     startActivity(callIntent);	
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			break;

		case R.id.btn_request_online:
			
			break;
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
}
