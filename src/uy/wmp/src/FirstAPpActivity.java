package uy.wmp.src;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


public class FirstAPpActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d("DEBUG_ACTIVITY", "ACTIVITY INITIATED");
		
	}
	
	
    
    
} 