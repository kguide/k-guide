package bravo.kguide.data;

import android.content.Context;
import android.util.Log;
import bravo.kguide.control.Routes;

public class DataAccess {
	private static RouteDB routeDB;
	private static ServerConnection server;
	
	public DataAccess(Context context) {
		Log.v("DAL","Initializing routeDB");
		routeDB = new RouteDB(context);
		Log.v("DAL","Initializing routeDB done");
	}
	
	public void insertRoute(int routeId,String routeJson,boolean storeContent){
		routeDB.insertRoute(routeId, routeJson, storeContent);
		
	}
	
	public boolean existsRoute(int routeId){
		return routeDB.existsRoute(routeId);
	}
	
	
	public String getRouteJsonFromDB(int routeId){
		return routeDB.getRouteJSON(routeId);
	}
	
    /**
     *  Returns a route object from the android database, if it does not exist in database
     *  then search for it on the online server
     * @param routeId : The returned games routeId.
     * @param storeMedia : If true all media content will be fetched and put on the phone file system
     * @return : A route object of the selected route's routeId. 
     */
	public Routes getRoute(int routeId,boolean storeMedia){
    	String jsonReply;
		if(!this.existsRoute(routeId)){ 
			Log.v("DAL","Get route from server");
    		jsonReply = ServerConnection.getRouteFromServer(routeId,storeMedia);
    		Log.v("DAL",jsonReply);
    		this.insertRoute(routeId, jsonReply, storeMedia);
    		return Toolbox.createRouteFromJSON(routeId, jsonReply);
    	}
		else{
			Log.v("DAL","Get route from DB");
			jsonReply = this.getRouteJsonFromDB(routeId);
			return Toolbox.createRouteFromJSON(routeId, jsonReply);
		}
	}
}
