package bravo.kguide.control;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import bravo.kguide.data.DataAccess;
import bravo.kguide.data.MediaHandler;
import bravo.kguide.data.ServerConnection;
import bravo.kguide.view.AudioPlay;
import bravo.kguide.view.R;

import com.google.android.maps.GeoPoint;


public class Controller {

    
    public RouteList routeList = new RouteList();
    public ServerConnection server;
    public DataAccess dal;
    
    public MediaHandler myMedia;

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
	myMedia = new MediaHandler();
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
    
    public OverlayList getSimpleOverlays(Context context){
    	OverlayList ol = new OverlayList(context);
    	ArrayList<GeoPoint> parkOverlay = new ArrayList<GeoPoint>();
    	
    	parkOverlay.clear();
    	parkOverlay.add(new GeoPoint((int) (1E6*64.15261432054969),(int) (1E6*-22.02956199645996)));
    	parkOverlay.add(new GeoPoint((int) (1E6*64.15351238797243),(int) (1E6*-21.99488639831543)));
    	parkOverlay.add(new GeoPoint((int) (1E6*64.14741244186601),(int) (1E6*-21.947851181030273)));
    	parkOverlay.add(new GeoPoint((int) (1E6*64.14823582572716),(int) (1E6*-21.933517456054688)));
    	parkOverlay.add(new GeoPoint((int) (1E6*64.14127371986018),(int) (1E6*-21.941242218017578)));
    	parkOverlay.add(new GeoPoint((int) (1E6*64.14011319915525),(int) (1E6*-21.947765350341797)));
    	parkOverlay.add(new GeoPoint((int) (1E6*64.12880494351393),(int) (1E6*-21.919612884521484)));
    	parkOverlay.add(new GeoPoint((int) (1E6*64.13686098700536),(int) (1E6*-21.913433074951172)));
    	parkOverlay.add(new GeoPoint((int) (1E6*64.13906494522097),(int) (1E6*-21.871376037597656)));
    	parkOverlay.add(new GeoPoint((int) (1E6*64.13730528716792),(int) (1E6*-21.868200302124023)));
    	parkOverlay.add(new GeoPoint((int) (1E6*64.12734427021864),(int) (1E6*-21.873435974121094)));
    	parkOverlay.add(new GeoPoint((int) (1E6*64.10624958371393),(int) (1E6*-21.906909942626953)));
    	parkOverlay.add(new GeoPoint((int) (1E6*64.11224614939255),(int) (1E6*-21.913089752197266)));
    	parkOverlay.add(new GeoPoint((int) (1E6*64.10951037669146),(int) (1E6*-21.91695213317871)));
    	parkOverlay.add(new GeoPoint((int) (1E6*64.11827888854619),(int) (1E6*-21.840648651123047)));
    	parkOverlay.add(new GeoPoint((int) (1E6*64.09005239660539),(int) (1E6*-21.898584365844727)));
    	parkOverlay.add(new GeoPoint((int) (1E6*64.07527173897235),(int) (1E6*-21.963300704956055)));
    	parkOverlay.add(new GeoPoint((int) (1E6*64.07136893652486),(int) (1E6*-21.958494186401367)));
    	parkOverlay.add(new GeoPoint((int) (1E6*64.06814120603406),(int) (1E6*-21.94416046142578)));
    	parkOverlay.add(new GeoPoint((int) (1E6*64.05891133665301),(int) (1E6*-21.96587562561035)));
    	parkOverlay.add(new GeoPoint((int) (1E6*64.07403340896307),(int) (1E6*-21.824254989624023)));
    	ol.addOverlay(parkOverlay, R.drawable.o_park,8);
    	
    	
    	ArrayList<GeoPoint> swimmingOverlay = new ArrayList<GeoPoint>();
    	swimmingOverlay.clear();
    	swimmingOverlay.add(new GeoPoint((int) (1E6*64.11267148159958),(int) (1E6*-21.795941591262817)));
    	swimmingOverlay.add(new GeoPoint((int) (1E6*64.10526533625024),(int) (1E6*-21.817726492881775)));
    	swimmingOverlay.add(new GeoPoint((int) (1E6*64.13904591398806),(int) (1E6*-21.786012053489685)));
    	swimmingOverlay.add(new GeoPoint((int) (1E6*64.14617732028570),(int) (1E6*-21.878113746643066)));
    	swimmingOverlay.add(new GeoPoint((int) (1E6*64.14221208389853),(int) (1E6*-21.919720172882080)));
    	swimmingOverlay.add(new GeoPoint((int) (1E6*64.14484295289857),(int) (1E6*-21.962946653366090)));
    	swimmingOverlay.add(new GeoPoint((int) (1E6*64.14678927369758),(int) (1E6*-21.953333616256714)));
    	swimmingOverlay.add(new GeoPoint((int) (1E6*64.15057984082826),(int) (1E6*-21.992011070251465)));
    	swimmingOverlay.add(new GeoPoint((int) (1E6*64.13037789042454),(int) (1E6*-21.931929588317870)));
    	swimmingOverlay.add(new GeoPoint((int) (1E6*64.11078710407457),(int) (1E6*-21.915986537933350)));
    	swimmingOverlay.add(new GeoPoint((int) (1E6*64.10422794992706),(int) (1E6*-21.883628368377686)));
    	swimmingOverlay.add(new GeoPoint((int) (1E6*64.09200630734225),(int) (1E6*-21.856323480606080)));
    	swimmingOverlay.add(new GeoPoint((int) (1E6*64.10453845141570),(int) (1E6*-22.018221616744995)));
    	swimmingOverlay.add(new GeoPoint((int) (1E6*64.07308961436877),(int) (1E6*-21.968439817428590)));
    	swimmingOverlay.add(new GeoPoint((int) (1E6*64.06015033893178),(int) (1E6*-21.961691379547120)));
    	swimmingOverlay.add(new GeoPoint((int) (1E6*64.05222728359249),(int) (1E6*-21.975359916687010)));
    	ol.addOverlay(swimmingOverlay, R.drawable.o_swimming,12);
    	
    	return ol;
    	
    }
    
}
