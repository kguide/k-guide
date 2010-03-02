package bravo.kguide.data;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import bravo.kguide.control.RouteList;
import bravo.kguide.control.Routes;
import bravo.kguide.control.Routes.Coordinate;

public class DataAccess {
	private static RouteDB routeDB;
	private static ServerConnection server;
	
	public DataAccess(Context context) {
		Log.v("DAL","Initializing routeDB");
		routeDB = new RouteDB(context);
		Log.v("DAL","Initializing routeDB done");
	}
	
	private void insertRoute(int routeId,String routeJson){
		routeDB.insertRoute(routeId, routeJson);
	}
	
	public boolean existsRoute(int routeId){
		return routeDB.existsRoute(routeId);
	}
	
	public void closeDatabase(){
		routeDB.closeDatabase();
	}
	
	public int[] getLocalRouteIdArray(){
		return routeDB.getRouteIdArray();
	}
	
	public boolean hasRoutesOnPhone(){
		return routeDB.hasRoutes();
	}
	
    /**
     *  Returns a route object from the android database, if it does not exist in database
     *  then search for it on the online server
     * @param routeId : The returned games routeId.
     * @param storeMedia : If true all media content will be fetched and put on the phone file system
     * @return : A route object of the selected route's routeId. 
     */
	public Routes getRoute(int routeId){
    	String jsonReply;
		if(!this.existsRoute(routeId)){
			Log.v("DAL","Get route from server");
    		jsonReply = ServerConnection.getRouteFromServer(routeId);
    		Log.v("DAL","Route has been retrived");
    		this.insertRoute(routeId, jsonReply);
    		return this.constructRoute(routeId);
    	}
		else{
			Log.v("DAL","Route exists locally calling constructRoute");
			return this.constructRoute(routeId);
		}
	}
	
	private Routes constructRoute(int routeId){
     	Routes r = new Routes(routeId);
    	// get the basic route info as a cursor and set the class variables
    	Cursor c;
    	c = routeDB.getRouteBaseInfo(routeId);
    	r.routeName = c.getString(2);
    	r.routeTypeId = c.getInt(1);
    	r.routeLength = c.getDouble(3);
    	Log.v("DAL", "constructRoute: routeBaseInfo collected");
    	c.close();
    	Log.v("DAL", "cursor closed");
    	// get the lat/lng cursor
    	Log.v("DAL", "constructRoute: get routeLatLng info");
    	c = routeDB.getRouteLatLng(routeId);
    	Log.v("DAL", "constructRoute: routeLatLng collected");
    	// get the route media array, first indexing the coordinate id and then the media array
    	String media[][] = new String[c.getCount()][];
    	for(int i=0;i<c.getCount();i++){
    		media[i] = routeDB.getRouteMediaInfo(c.getInt(0));
    		c.moveToNext();
    	}
    	
    	// initialize the coordinate class
    	c.moveToFirst();
    	for(int i=0;i<c.getCount();i++){    		
    		r.addCoordinate(c.getDouble(1), c.getDouble(2), media[i]);
    		c.moveToNext();
    	}
    	c.close();
		return r;
	}
	
	
	/**
	 * @param start: The 
	 * @param offset: The offset of the items to be fetched
	 * @return Arraylist containing Routes with name and id info
	 */
	public static RouteList getRouteSelectionList(int limit,int offset){
		String jsonRouteList = ServerConnection.getRouteListFromServer(limit, offset);
		Log.v("DAL",jsonRouteList);
		RouteList r = Toolbox.createRouteSelectionList(jsonRouteList);
		Log.v("DAL","Number of items in sel.list"+r.routes.size());
		return r;
	}
}