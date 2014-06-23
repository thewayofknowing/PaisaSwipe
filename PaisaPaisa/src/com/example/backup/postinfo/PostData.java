package com.example.backup.postinfo;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.example.backup.constants.Constants;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PostData implements Constants {

	public static void post(Activity context) {
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = lm.getBestProvider(criteria, false);
		if (provider == null) Log.d(TAG,"Provider null");
		//Location location = lm.getLastKnownLocation(provider);
		
		//double latitude = location.getLatitude();
		//double longitude = location.getLongitude();
		String cityName = "Dummy";
		Geocoder gcd = new Geocoder(context,Locale.getDefault());
		List<Address> addresses;
		//try {
		//	addresses = gcd.getFromLocation(latitude, longitude, 1);
		//	cityName = addresses.get(0).getLocality();
		//} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String operator = tm.getSimOperatorName();
		String pNumber = tm.getLine1Number();
		//Log.d(TAG,"Number:" + pNumber);
		
		Post p = new Post(context);
		p.execute(cityName,operator,pNumber);
	}
	
}
