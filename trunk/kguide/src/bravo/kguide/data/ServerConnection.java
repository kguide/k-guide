package bravo.kguide.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;

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
     * @return : A json string for the given route 
     * selected routeId then this returned object will be null.
     */
    public static String getRouteFromServer(int routeId, boolean downloadAllContent) {
		String connectionString = domainString + "route/getRouteJson/" + routeId;
		return serverReply(connectionString);
    }

    public static String getRouteListFromServer(int limit,int offset){    	
    	String connectionString = domainString + "route/getRouteListJson/" + limit + "/" + offset;
    	return serverReply(connectionString);
    }
}
