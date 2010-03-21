package bravo.kguide.control;

import android.content.Context;
import android.util.Log;
import bravo.kguide.data.DataAccess;
import bravo.kguide.data.MediaHandler;
import bravo.kguide.data.ServerConnection;

import bravo.kguide.view.AudioPlay;


public class Controller {

    
    public RouteList routeList = new RouteList();
    public ServerConnection server;
    public DataAccess dal;

    public AudioPlay ourPlayer;
    public boolean storeMedia = true;
    public String storeLocation = MediaHandler.HOME_DIRECTORY_SD;
    //Singleton creation of control
    protected Controller() {}  // private so other classes can't instantiate 
    static private Controller INSTANCE = null;
    
    /**
     * @return The unique instance of this class.
     */
    static public Controller getInstance() {
		if(null == INSTANCE) {
		  
		    INSTANCE = new Controller();
		}		
		return INSTANCE;
    }
    //Singleton part ends
    public void initData(Context context) {
    	// Need to load every route locally stored (but not if it has already been done)
	ourPlayer = new AudioPlay();
	this.loadInitialMapInfo(context);
    }    

    /**
     * Checks if the phone has any routes stored
     * @param context
     * @return
     */
    public boolean hasRoutesOnPhone(Context context){
    	dal = new DataAccess(context);
    	boolean exists = dal.hasRoutesOnPhone();
    	dal.closeDatabase();
    	return exists;

    }
    
    public int getRouteListCount(){
    	return routeList.routes.size();
    }
    // lack the case when items have already been loaded
    public void loadInitialMapInfo(Context context){
    	if(routeList.routes.size()==0){
	    	dal = new DataAccess(context);
	    	int intArray[] = dal.getLocalRouteIdArray();
	    	Log.v("Controller", "loadMapInfo idArray size "+intArray.length);
	    	for(int id : intArray){
	    		routeList.addRoute(dal.getRoute(id,false,null));
	    	}
	    	Log.v("Controller",routeList.toString());
	    	dal.closeDatabase();
    	}
    }
    
    /**
     * Gets a route with a given routeId and context
     * @param context
     * @param routeId 
     */
    public Routes getRoute(Context context, int routeId){
    	Log.v("Controller","getting route");
    	dal = new DataAccess(context);
    	return dal.getRoute(routeId,storeMedia,storeLocation);
    }
    
    public void addRouteToList(Context context,int routeId){
    	//this.loadInitialMapInfo(context);
    	dal = new DataAccess(context);
    	routeList.addRoute(dal.getRoute(routeId,storeMedia,storeLocation));
    	Log.v("Controller", "addRouteToList now routelist has:"+routeList.routes.size());
    }
    
    public RouteList getRouteSelectionList(int limit,int offset){
    	return DataAccess.getRouteSelectionList(limit,offset);    	
    }
    
    public void debugPrintRouteList(){
    	if(routeList.routes.size()>0)
    		Log.v("Controller", "Routelist: " + routeList.toString());
    }

    public boolean isRouteListEmpty() {
    	return routeList.current == null;
    }
}
