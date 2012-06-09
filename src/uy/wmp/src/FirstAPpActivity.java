package uy.wmp.src;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class FirstAPpActivity extends Activity {
	
	public static final String PREFS_NAME = "WMPPrefsFile";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  
        
        
        
        setContentView(R.layout.main);       
        loadContent();
        Button button = (Button)this.findViewById(R.id.button_guardar);
        Button buttonKillService = (Button)this.findViewById(R.id.button_stopService);
     
        buttonKillService.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				killService();				
			}
		});
        
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
            	//Almacenar data
            	
            	EditText startTokenEd = (EditText)findViewById(R.id.editText_startToken);
                EditText stopTokenEd = (EditText)findViewById(R.id.editText_stopToken);
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                
                Log.d("DEBUG_ACTIVITY", "Almacenando startToken como "+startTokenEd.getText().toString());
                Log.d("DEBUG_ACTIVITY", "Almacenando stopToken como "+stopTokenEd.getText().toString());
                editor.putString("startToken",startTokenEd.getText().toString());
                editor.putString("stopToken",stopTokenEd.getText().toString());
            	editor.commit();
            	
            	 Toast.makeText(FirstAPpActivity.this, "Configuración almacenada", Toast.LENGTH_SHORT).show();
                /* Create an Intent to start * MySecondActivity. 
            	Intent i = new Intent( FirstAPpActivity.this, MySecondActivity.class);
            	/* Send intent to the OS to make 
            	 * 
            	 * * it aware that we want to start * MySecondActivity as a SubActivity.  startSubActivity(i, 0x1337);
            	 */
            }
        });
    }

	private void loadContent() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String startToken = settings.getString("startToken", "");
        String stopToken = settings.getString("stopToken", "");
        
        EditText startTokenEd = (EditText)this.findViewById(R.id.editText_startToken);
        EditText stopTokenEd = (EditText)this.findViewById(R.id.editText_stopToken);
        
        startTokenEd.setText(startToken);
        stopTokenEd.setText(stopToken);
        
		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d("DEBUG_ACTIVITY", "ACTIVITY INITIATED");
		
	}
	
	private void killService(){
		
		Intent nintent = new Intent(this, LocService.class);
		Boolean result = stopService(nintent);
		Toast.makeText(FirstAPpActivity.this, "Servicio eliminado ? "+result.toString(), Toast.LENGTH_SHORT).show();
		
	}
	
	
    
    
} 