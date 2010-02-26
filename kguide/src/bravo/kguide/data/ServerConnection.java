package bravo.kguide.data;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import bravo.kguide.control.Routes;

public class ServerConnection {
    private static String domainString = "http://hgphoto.net/kguide/index.php/";
    
    public ServerConnection(Context context){}
    /**
     * Handles calling the server given a connection string.
     * 
     * @param connectionString : The selected connection string.
     * @return : String containing the servers reply.
     */
    private static String serverReply(String connectionString){
	URL url;
	try {
	    url = new URL(connectionString);
	    URLConnection connection = url.openConnection();
	    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	    String replyString = reader.readLine();
	    return replyString;
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	    return "";
	}		
	catch (IOException e) {
	    e.printStackTrace();
	    return "";
	}
	
	
    }
    
    /**
     * Returns a route object from the online server corresponding to the selected routeId.
     * 
     * @param routeId : The returned games routeId.
     * @return : A route object corresponding to the selected routeId. Note that if a game was not found that corresponded to the 
     * selected routeId then this returned object will be null.
     */
    public static String getRouteFromServer(int routeId, boolean downloadAllContent) {
		String connectionString = domainString + "route/getRouteJson/" + routeId;
		return serverReply(connectionString);
    }
    
}
