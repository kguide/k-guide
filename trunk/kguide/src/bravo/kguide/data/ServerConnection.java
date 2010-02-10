package bravo.kguide.data;


import bravo.kguide.control.Routes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class ServerConnection {
    private static String domainString = "http://hgphoto.net/kguide/index.php/";
    
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
    public static Routes getRouteOnServer(int routeId) {
	
	String connectionString = domainString + "route/getRouteJson/" + routeId;
	String JSONReplyString = serverReply(connectionString);	
	
	
	try {
	    
	    JSONObject jsonObject = new JSONObject(JSONReplyString);
	    
	    Routes route = new Routes(routeId, jsonObject.getString("routeName"));
	    
	    JSONArray lngArray = jsonObject.getJSONArray("lng");
	    JSONArray latArray = jsonObject.getJSONArray("lat");

	    for (int i = 0; i < latArray.length(); i++) {
	
		double currentLatitude = Double.parseDouble(latArray.getString(i));
		double currentLongitude = Double.parseDouble(lngArray.getString(i));
		route.addCoordinate(currentLatitude,currentLongitude);
	    }
	    
	    return route;
	    
	} catch (JSONException e) {
	    e.printStackTrace();
	    return null;
	}	
    }
}
