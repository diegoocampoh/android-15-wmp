package uy.wmp.src;

import src.main.R;
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

				nintent.putExtras(b);

				Log.d("DEBUG_ACTIVITY", "Mensaje: "+body+" - De: "+address);


				if (body.equals(context.getString(R.string.token))){
					Log.d("DEBUG_ACTIVITY", "ARRANCANDO SERVICIO");
					cn = context.startService(nintent);
					//eliminamos el mensaje
					deleteMessage(context, sms);


				}
				if (body.equals(context.getString(R.string.stopToken))){
					Log.d("DEBUG_ACTIVITY", "TERMINANDO SERVICIO");	            		
					context.stopService(new Intent(context,LocService.class));
					//eliminamos el mensaje
					deleteMessage(context, sms);
				}

				

				
			}
		}

	}
	
	private int deleteMessage(Context context, SmsMessage msg) {
		Log.d("DEBUG_ACTIVITY", "INTENTANDO ELIMINAR MENSAJE");	     
	    Uri deleteUri = Uri.parse("content://sms");
	    int count = 0;
	    Cursor c = context.getContentResolver().query(deleteUri, null, null,
	            null, null);
	    while (c.moveToNext()) {
	        try {
	            // Delete the SMS
	            String pid = c.getString(0); // Get id;
	            String uri = "content://sms/" + pid;
	            count = context.getContentResolver().delete(Uri.parse(uri),
	                    null, null);
	            abortBroadcast();
	        } catch (Exception e) {
	        	Log.d("DEBUG_ACTIVITY", "ERROR ELIMINANDO MENSAJE "+e.getMessage());	 
	        }
	    }
	    return count;
	}

}
