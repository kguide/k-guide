package bravo.kguide.view;



import android.app.Activity;
import android.os.Bundle;
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
	   webview.loadUrl("file:///android_asset/helpguide.html" );
	   
	   
    } 
}