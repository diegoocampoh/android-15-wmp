/**
 * 
 */
package uy.wmp.src;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.gsm.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.util.Log;

/**
 * @author diego
 *
 */
public class LocService extends Service {

	public static final String PREFS_NAME = "WMPPrefsFile";
	Location last = null;
	SmsManager smsManager = SmsManager.getDefault();
	String address,body = null;
	LocationListener locationListener;
	LocationListener networklocationListener;
	
	LocationManager locationManager;

	/* (non-Javadoc)
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent arg0) {
		Log.d("DEBUG_ACTIVITY", "onbind");
		return null;
	}
	
	@Override
	public void onCreate(){
		Log.d("DEBUG_ACTIVITY", "onCreate");
		
	}

	@Override
	public void onDestroy() {
		locationManager.removeUpdates(locationListener);
		locationListener = null;
		locationManager = null;
		
		Log.d("DEBUG_ACTIVITY", "Destroing locservice");

	}

	@Override
	public boolean stopService(Intent name) {

		locationListener = null;
		this.locationListener = null;
		Log.d("DEBUG_ACTIVITY", "LOCSERVICE STOPPED");
		return super.stopService(name);
	}



	@Override
	public boolean onUnbind(Intent intent) {
		Log.d("DEBUG_ACTIVITY", "onUnBind");
		return super.onUnbind(intent);
	}
	
	

	@Override
	public void unbindService(ServiceConnection conn) {
		// TODO Auto-generated method stub
		Log.d("DEBUG_ACTIVITY", "unbindService");
		super.unbindService(conn);
	}
	
	

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub

		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		//Log.d("DEBUG_ACTIVITY", new Integer(intent.getExtras().size()).toString());

		//Log.d("DEBUG_ACTIVITY", intent.getExtras().getString("address"));
		address = intent.getExtras().getString("address");
		body = intent.getExtras().getString("body");
		

		// Acquire a reference to the system Location Manager
		

		//Obtengo la ultima localización del gps o 3g
		Location lastGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Location lastNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
		if (lastNetwork != null && lastGPS != null){
			long diff = lastNetwork.getTime() - lastGPS.getTime();
			
			if (diff > 0){
				//es mas reciente la localización de network
				sendLocationSMS(address, lastNetwork);
			}else{
				sendLocationSMS(address, lastGPS);
			}
		}else{
			//alguna es null, no se pueden comparar tiempos
			if (lastGPS != null){
				sendLocationSMS(address, lastGPS);	
			}else{			
				if (lastNetwork != null){
					sendLocationSMS(address, lastNetwork);	
				}
			}
		}
		
		



		// Define a listener that responds to location updates
		locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				manageObtainedLocation(location);

			}

			@Override
			public void onProviderDisabled(String arg0) {
				Log.d("DEBUG_ACTIVITY", "PROVIDER DISABLED");

			}

			@Override
			public void onProviderEnabled(String arg0) {
				Log.d("DEBUG_ACTIVITY", "PROVIDER ENABLED");

			}

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				Log.d("DEBUG_ACTIVITY", "STATUS CHANGE: "+arg0+" ; "+arg1+" ; "+arg2.size());				
			}
		};

		networklocationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				manageObtainedLocation(location);

			}

			@Override
			public void onProviderDisabled(String arg0) {
				Log.d("DEBUG_ACTIVITY", "PROVIDER DISABLED");

			}

			@Override
			public void onProviderEnabled(String arg0) {
				Log.d("DEBUG_ACTIVITY", "PROVIDER ENABLED");

			}

			@Override
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				Log.d("DEBUG_ACTIVITY", "STATUS CHANGE: "+arg0+" ; "+arg1+" ; "+arg2.size());				
			}
		};


		
		
		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 10, locationListener);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 10, networklocationListener);

	}

	private void sendLocationSMS(String address, Location loc){
		try {
			String url = "http://maps.google.com/?q="+loc.getLatitude() + "," + loc.getLongitude();
			sendSMS(address, url );
		} catch (Exception e) {
			Log.d("DEBUG_ACTIVITY", "Error al enviar mensaje: "+e.getMessage());
		}		
	}

	private void sendSMS(String address, String body){
		try {
			smsManager.sendTextMessage(address, null, body, null, null);
			Log.d("DEBUG_ACTIVITY", "Mensaje enviado: " + address + " : "
					+ body);
		} catch (Exception e) {
			Log.d("DEBUG_ACTIVITY", "Error al enviar mensaje: "+e.getMessage());
		}
	}
	
	private void manageObtainedLocation(Location location){
		
		last = location;
		if (last != null){
			Log.d("DEBUG_ACTIVITY", "LOCATION OBTAINED: "+last.getLatitude()+" ; "+ last.getLongitude());
			sendLocationSMS(address,last);
		}else{
			Log.d("DEBUG_ACTIVITY", "COULD NOT OBTAIN LOCATION");					
		}		
	}



}
