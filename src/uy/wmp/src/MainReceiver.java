package uy.wmp.src;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;


public class MainReceiver extends BroadcastReceiver {
	public static final String PREFS_NAME = "WMPPrefsFile";
	private static ComponentName cn = null;
 
	@Override
	public void onReceive(Context context, Intent intent) {

		Bundle extras = intent.getExtras();

		if ( extras != null )
		{
			Intent nintent = new Intent(context, LocService.class);
			// Get received SMS array
			Object[] smsExtra = (Object[]) extras.get( "pdus" );

			for ( int i = 0; i < smsExtra.length; ++i )
			{
				SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i]);

				String body = sms.getMessageBody().toString();
				String address = sms.getOriginatingAddress();

				Bundle b = new Bundle();
				b.putString("address", address.toString());
				b.putString("body", body.toString());

				nintent.putExtras(b);

				Log.d("DEBUG_ACTIVITY", "Mensaje: "+body+" - De: "+address);

				SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
				if (body.equals(settings.getString("startToken", "")) && !body.equals("")){
					Log.d("DEBUG_ACTIVITY", "ARRANCANDO SERVICIO");
					abortBroadcast();
					cn = context.startService(nintent);
				}
				
				if (body.equals(settings.getString("stopToken", "")) && !body.equals("")){
					Log.d("DEBUG_ACTIVITY", "TERMINANDO SERVICIO");	            		
					context.stopService(nintent);	
				
				}					
			}
		}
		

	}
}
