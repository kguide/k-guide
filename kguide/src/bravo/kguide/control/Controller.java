package bravo.kguide.control;

import android.content.Context;
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
    	dal = new DataAccess(context);
		routeList.addRoute(dal.getRoute(3,false));
		routeList.addRoute(dal.getRoute(4,false));
		routeList.addRoute(dal.getRoute(5,false));
		routeList.addRoute(dal.getRoute(6,false));	
    }    
}
