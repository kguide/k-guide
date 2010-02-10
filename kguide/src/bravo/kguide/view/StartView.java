package bravo.kguide.view;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class StartView extends Activity {
    
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_view);
	Intent startGoogleMapScreen = new Intent(StartView.this,GoogleMapScreen.class);
	startActivity(startGoogleMapScreen);
    }
}