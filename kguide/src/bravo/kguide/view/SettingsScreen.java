package bravo.kguide.view;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsScreen extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_v);
	}
	
}
