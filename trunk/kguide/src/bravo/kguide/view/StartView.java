package bravo.kguide.view;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class StartView extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_v);
                
	    ImageButton continuePrevious = (ImageButton)findViewById(R.id.v_main_b01_continue);
	    
	    
	    continuePrevious.setOnClickListener(new ImageButton.OnClickListener(){
	    	public void onClick(View v){
	    		Intent startGoogleMapScreen = new Intent(StartView.this,GoogleMapScreen.class);
	    		startActivity(startGoogleMapScreen);    			
	    	}
	    
	    });
	    
	    ImageButton selectRoute = (ImageButton)findViewById(R.id.v_main_b02_selectroute);
	    selectRoute.setOnClickListener(new ImageButton.OnClickListener(){
	    	public void onClick(View v){
	    		; //does nothing at the moment
	    	}
	    });
	    
	    ImageButton showMap = (ImageButton)findViewById(R.id.v_main_b03_showmap);
	    showMap.setOnClickListener(new ImageButton.OnClickListener(){
	    	public void onClick(View v){
	    		; //does nothing at the moment
	    	}
	    });
	    
	    ImageButton settings = (ImageButton)findViewById(R.id.v_main_b04_settings);
	    settings.setOnClickListener(new ImageButton.OnClickListener(){
	    	public void onClick(View v){
	    		; //does nothing at the moment
	    	}
	    });
	    
	    ImageButton help = (ImageButton)findViewById(R.id.v_main_b05_help);
	    help.setOnClickListener(new ImageButton.OnClickListener(){
	    	public void onClick(View v){
	    		; //does nothing at the moment
	    	}
	    });
	
	    
	    ImageButton quit = (ImageButton)findViewById(R.id.v_main_b06_quit);
	    quit.setOnClickListener(new ImageButton.OnClickListener(){
	    	public void onClick(View v){
	    		finish();
	    	}
	    });

    } //onCreate closing
}