package bravo.kguide.control;

import bravo.kguide.control.RouteList;
import bravo.kguide.data.ServerConnection;
import java.util.ArrayList;


public class Controller {
    public RouteList routeList = new RouteList();
    public ServerConnection server;
    
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

    public void initData() {
	routeList.addRoute(server.getRouteOnServer(3));
	routeList.addRoute(server.getRouteOnServer(4));
	routeList.addRoute(server.getRouteOnServer(5));
	routeList.addRoute(server.getRouteOnServer(6));
	
    }

}
