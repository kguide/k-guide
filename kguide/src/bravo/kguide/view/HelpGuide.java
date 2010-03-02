package bravo.kguide.view;



import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class HelpGuide extends Activity {
	WebView webview;
	//private Object readOutput;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_guide);
        
        webview = (WebView) findViewById(R.id.myWebview);
	    webview.getSettings().setJavaScriptEnabled(true);
	    Log.v("HelpGuide", "Before loading file");
	    webview.loadUrl("file:///android_asset/helpguide.html" );
	   
	   
    } 
}