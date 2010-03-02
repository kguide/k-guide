package bravo.kguide.control;

import android.content.Context;
import android.util.Log;
import bravo.kguide.data.DataAccess;
import bravo.kguide.data.ServerConnection;


public class Controller {
    public RouteList routeList = new RouteList();
    public ServerConnection server;
    public DataAccess dal;
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
    
    // lack the case when items have already been loaded
    public void loadInitialMapInfo(Context context){
    	if(routeList.routes.size()==0){
	    	dal = new DataAccess(context);
	    	int intArray[] = dal.getLocalRouteIdArray();
	    	Log.v("Controller", "loadMapInfo idArray size "+intArray.length);
	    	for(int id : intArray){
	    		routeList.addRoute(dal.getRoute(id));
	    	}
	    	Log.v("Controller",routeList.toString());
	    	dal.closeDatabase();
    	}
    }
    
    public void addRouteToList(Context context,int routeId){
    	this.loadInitialMapInfo(context);
    	dal = new DataAccess(context);
    	routeList.addRoute(dal.getRoute(routeId));
    	Log.v("Controller", "addRouteToList now routelist has:"+routeList.routes.size());
    }
    
    public RouteList getRouteSelectionList(int limit,int offset){
    	return DataAccess.getRouteSelectionList(limit,offset);    	
    }
}
