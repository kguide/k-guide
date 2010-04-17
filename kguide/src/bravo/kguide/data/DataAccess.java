package bravo.kguide.data;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import bravo.kguide.control.OverlayList;
import bravo.kguide.control.RouteList;
import bravo.kguide.control.Routes;

import com.google.android.maps.Overlay;

public class DataAccess {
	private static RouteDB routeDB;
	
	public DataAccess(Context context) {
		Log.v("DAL","Initializing routeDB");
		routeDB = new RouteDB(context);
		Log.v("DAL","Initializing routeDB done");
	}
	
	private void insertRoute(int routeId,String routeJson) {
		routeDB.insertRoute(routeId, routeJson);
	}
	
	public boolean existsRoute(int routeId) {
		return routeDB.existsRoute(routeId);
	}
	
	public void closeDatabase() {
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
	public Routes getRoute(int routeId, boolean storeMedia, String storeLocation){
    	String jsonReply;
		if(!this.existsRoute(routeId)){
			Log.v("DAL","Get route from server");
    		jsonReply = ServerConnection.getRouteFromServer(routeId);
    		Log.v("DAL","Route has been retrived");
    		this.insertRoute(routeId, jsonReply);
    		Routes route = constructRoute(routeId);
    		if(storeMedia){
    			downloadMedia(route, storeLocation);
    		}
    		return route;
    	}
		else{
			Log.v("DAL","Route exists locally calling constructRoute");
			return this.constructRoute(routeId);
		}
	}

	/**
	 * Download all files needed for the route
	 * @param route : Routes object of the route containing all information about the route
	 * @param storeLocation : MediaHandler constant to set store location to be either cache, sd or local
	 * @return
	 */
	private boolean downloadMedia(Routes route, String storeLocation){
		int routeId = route.routeId;
		MediaHandler mh = new MediaHandler();
    	for(int i=0;i<route.routePath.size();i++){
	    	//Now we want to download all audio and photos
    			String[] media = route.routePath.get(i).mediaArray;
	    		if(media[RouteDB.MEDIA_ARRAY_AUDIO] != null){
	    			mh.storeFile(media[RouteDB.MEDIA_ARRAY_AUDIO], storeLocation, MediaHandler.AUDIO_MEDIA_DIR, routeId, i);
	    		}
	    		
	    		if(media[RouteDB.MEDIA_ARRAY_PHOTO] != null){
	    			mh.storeFile(media[RouteDB.MEDIA_ARRAY_PHOTO], storeLocation, MediaHandler.PHOTO_MEDIA_DIR, routeId, i);
	    		}
    	}
    	return true;
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
    	
    	// Construct the route media array, first indexing the coordinate id and then the media array
    	// And download media to phone if requested
    	String media[][] = new String[c.getCount()][];
    	
    	for(int i=0;i<c.getCount();i++){
    		media[i] = routeDB.getRouteCoordinateMediaArray(c.getInt(0));
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

	/*
	private boolean downloadRouteContents(Routes route,String storeLocation){
		MediaHandler mh = new MediaHandler();
		route.
		route.hasMediaAtCoordinate(i)
		return true;
	}
	*/
	
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
	
	
	//public static OverlayList[] getoverlayList(){
	//	OverlayList list[] = new OverlayList[5];
	//	Overlay test = new Overlay();
		//list[0].addRoute(OverlayItem.)
	//	return list;
	//}
}