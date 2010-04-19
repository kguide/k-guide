package bravo.kguide.view;


import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import bravo.kguide.control.Controller;
import bravo.kguide.data.MediaHandler;

public class StartView extends Activity {
	Controller controller = Controller.getInstance();
	Context context = this;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_v);
        
	    ImageButton continuePrevious = (ImageButton)findViewById(R.id.v_main_b01_continue);
	    continuePrevious.setOnClickListener(new ImageButton.OnClickListener(){
	    	public void onClick(View v){
	    		controller.debugPrintRouteList();
	    		if(controller.getRouteListCount() == 0 & controller.hasRoutesOnPhone(context)){
	    			controller.loadInitialMapInfo(context);
    		Intent startGoogleMapScreen = new Intent(StartView.this,GoogleMapScreen.class);
		    		startActivity(startGoogleMapScreen);    				    		
	    		}
	    		else if(controller.getRouteListCount() > 0){
		    		Intent startGoogleMapScreen = new Intent(StartView.this,GoogleMapScreen.class);
		    		startActivity(startGoogleMapScreen);    			
	    		}
	    	}
	    });
	    
	    ImageButton selectRoute = (ImageButton)findViewById(R.id.v_main_b02_selectroute);
	    selectRoute.setOnClickListener(new ImageButton.OnClickListener(){
	    	public void onClick(View v){
		    		Intent startSelectRouteScreen = new Intent(StartView.this,RouteSelection.class);
		    		startActivity(startSelectRouteScreen);
	    	}
	    });
	    
	    ImageButton showMap = (ImageButton)findViewById(R.id.v_main_b03_showmap);
	    showMap.setOnClickListener(new ImageButton.OnClickListener(){
	    	public void onClick(View v){
	    		MediaHandler mh = new MediaHandler();
	    		File fh = mh.getPhotoDirectory(8, 0);
	    		if(fh != null)
	    			Log.v("StartView","File is: "+fh.getPath());
	    		else
	    			Log.v("StartView","file is null");
	    		
	    		/*
	    		boolean result = mh.storeFile("http://www.hgphoto.net/kguide/test/p2.jpg", MediaHandler.HOME_DIRECTORY_SD, MediaHandler.AUDIO_MEDIA_DIR, 8, 1);
	    		if(result) {
	    			Log.v("StartView","File downloaded and stored");
	    		}
	    		else
	    			Log.v("Startview","Failed downloading file");
	    		*/
	    	}
	    });
	    
	    ImageButton settings = (ImageButton)findViewById(R.id.v_main_b04_settings);
	    settings.setOnClickListener(new ImageButton.OnClickListener(){
	    	public void onClick(View v){
		    //	Intent startSettingsScreen = new Intent(StartView.this,SettingsScreen.class);
		    //	startActivity(startSettingsScreen);
	    	}
	    });
	    
	    ImageButton help = (ImageButton)findViewById(R.id.v_main_b05_help);
	    help.setOnClickListener(new ImageButton.OnClickListener(){
	    	public void onClick(View v){
	    		Intent helpScreen = new Intent(StartView.this,HelpGuide.class);
	    		startActivity(helpScreen);
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