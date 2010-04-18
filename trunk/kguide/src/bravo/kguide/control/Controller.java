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


	ArrayList<GeoPoint> museumOverlay = new ArrayList<GeoPoint>();
    	museumOverlay.clear();
	museumOverlay.add(new GeoPoint((int) (1E6*64.16035919662922),(int) (1E6*-22.00664520263672)));
	museumOverlay.add(new GeoPoint((int) (1E6*64.15796487639479),(int) (1E6*-22.006473541259766))); 
	museumOverlay.add(new GeoPoint((int) (1E6*64.1506684081135 ),(int) (1E6*-21.960554122924805))); 
	museumOverlay.add(new GeoPoint((int) (1E6*64.15362464435712),(int) (1E6*-21.947250366210938))); 
	museumOverlay.add(new GeoPoint((int) (1E6*64.13928957439484),(int) (1E6*-21.951026916503906))); 
	museumOverlay.add(new GeoPoint((int) (1E6*64.14415609372006),(int) (1E6*-21.94570541381836)));  
	museumOverlay.add(new GeoPoint((int) (1E6*64.14703816840174),(int) (1E6*-21.943817138671875))); 
	museumOverlay.add(new GeoPoint((int) (1E6*64.14902176026892),(int) (1E6*-21.940813064575195))); 
	museumOverlay.add(new GeoPoint((int) (1E6*64.1489843353191 ),(int) (1E6*-21.929826736450195))); 
	museumOverlay.add(new GeoPoint((int) (1E6*64.14666388989184),(int) (1E6*-21.93523406982422)));  
	museumOverlay.add(new GeoPoint((int) (1E6*64.14602760484519),(int) (1E6*-21.929655075073242))); 
	museumOverlay.add(new GeoPoint((int) (1E6*64.1443058192523 ),(int) (1E6*-21.938581466674805))); 
	museumOverlay.add(new GeoPoint((int) (1E6*64.14086192774185),(int) (1E6*-21.92819595336914)));  
	museumOverlay.add(new GeoPoint((int) (1E6*64.14569074215227),(int) (1E6*-21.91875457763672)));  
	museumOverlay.add(new GeoPoint((int) (1E6*64.13794177209914),(int) (1E6*-21.9136905670166)));	  
	museumOverlay.add(new GeoPoint((int) (1E6*64.12884239566569),(int) (1E6*-21.91969871520996)));  
	museumOverlay.add(new GeoPoint((int) (1E6*64.14134859049841),(int) (1E6*-21.885623931884766))); 
	museumOverlay.add(new GeoPoint((int) (1E6*64.11966510946355),(int) (1E6*-21.836271286010742))); 
	museumOverlay.add(new GeoPoint((int) (1E6*64.11865354966504),(int) (1E6*-21.818246841430664))); 
	museumOverlay.add(new GeoPoint((int) (1E6*64.11936539187936),(int) (1E6*-21.81438446044922)));               
	ol.addOverlay(museumOverlay, R.drawable.o_museum,12);
	

	ArrayList<GeoPoint> policeOverlay = new ArrayList<GeoPoint>();
    	policeOverlay.clear();
	policeOverlay.add(new GeoPoint((int) (1E6*64.15029417854385),(int) (1E6*-21.984329223632812)));
	policeOverlay.add(new GeoPoint((int) (1E6*64.14823582572716),(int) (1E6*-21.938238143920900)));
	policeOverlay.add(new GeoPoint((int) (1E6*64.14381920832055),(int) (1E6*-21.913776397705078)));
	policeOverlay.add(new GeoPoint((int) (1E6*64.10579479147960),(int) (1E6*-21.867084503173828)));
	policeOverlay.add(new GeoPoint((int) (1E6*64.12130850351774),(int) (1E6*-21.789665222167970))); 
	ol.addOverlay(policeOverlay, R.drawable.o_police,12);


	ArrayList<GeoPoint> guesthouseOverlay = new ArrayList<GeoPoint>();
	guesthouseOverlay.clear();
	//guesthouseOverlay.add(new GeoPoint((int) (1E6*64.14823582572716),(int) (1E6*-21.95591926574707)));   
	guesthouseOverlay.add(new GeoPoint((int) (1E6*64.14952200218738),(int) (1E6*-21.94496512413025)));   
	//guesthouseOverlay.add(new GeoPoint((int) (1E6*64.14946118771415),(int) (1E6*-21.944600343704224)));  
	guesthouseOverlay.add(new GeoPoint((int) (1E6*64.14945183162952),(int) (1E6*-21.945812702178955)));  
	//guesthouseOverlay.add(new GeoPoint((int) (1E6*64.14713547993074),(int) (1E6*-21.94246530532837)));   
	guesthouseOverlay.add(new GeoPoint((int) (1E6*64.14751910882234),(int) (1E6*-21.942615509033203)));  
	//guesthouseOverlay.add(new GeoPoint((int) (1E6*64.14819278575492),(int) (1E6*-21.93847417831421)));   
	guesthouseOverlay.add(new GeoPoint((int) (1E6*64.1481460031628),(int) (1E6*-21.940770149230957)));  
	//guesthouseOverlay.add(new GeoPoint((int) (1E6*64.1467518457381),(int) (1E6*-21.935362815856934)));  
	guesthouseOverlay.add(new GeoPoint((int) (1E6*64.14720097792164),(int) (1E6*-21.933302879333496)));  
	//guesthouseOverlay.add(new GeoPoint((int) (1E6*64.14912842104205),(int) (1E6*-21.930663585662842)));  
	guesthouseOverlay.add(new GeoPoint((int) (1E6*64.14581613035281),(int) (1E6*-21.930792331695557)));  
	//guesthouseOverlay.add(new GeoPoint((int) (1E6*64.14567577032506),(int) (1E6*-21.93021297454834)));   
	guesthouseOverlay.add(new GeoPoint((int) (1E6*64.14546990766746),(int) (1E6*-21.928861141204834)));  
	//guesthouseOverlay.add(new GeoPoint((int) (1E6*64.14619042029122),(int) (1E6*-21.927037239074707)));  
	guesthouseOverlay.add(new GeoPoint((int) (1E6*64.14421598397271),(int) (1E6*-21.923818588256836)));  
	//guesthouseOverlay.add(new GeoPoint((int) (1E6*64.14291521224261),(int) (1E6*-21.928517818450928)));  
	guesthouseOverlay.add(new GeoPoint((int) (1E6*64.1449084562979),(int) (1E6*-21.91819667816162)));   
	//guesthouseOverlay.add(new GeoPoint((int) (1E6*64.14331761576214),(int) (1E6*-21.915857791900635)));  
	guesthouseOverlay.add(new GeoPoint((int) (1E6*64.13942435096995),(int) (1E6*-21.936371326446533)));  
	//guesthouseOverlay.add(new GeoPoint((int) (1E6*64.13569903381993),(int) (1E6*-21.919312477111816)));  
	guesthouseOverlay.add(new GeoPoint((int) (1E6*64.1397051335198),(int) (1E6*-21.91972017288208)));   
	//guesthouseOverlay.add(new GeoPoint((int) (1E6*64.14094990196836),(int) (1E6*-21.913626194000244)));  
	guesthouseOverlay.add(new GeoPoint((int) (1E6*64.14297136192101),(int) (1E6*-21.911051273345947)));  
	//guesthouseOverlay.add(new GeoPoint((int) (1E6*64.14131489867907),(int) (1E6*-21.904120445251465)));  
	guesthouseOverlay.add(new GeoPoint((int) (1E6*64.14612491991625),(int) (1E6*-21.89375638961792)));   
	//guesthouseOverlay.add(new GeoPoint((int) (1E6*64.14583484496958),(int) (1E6*-21.882705688476562)));  
	guesthouseOverlay.add(new GeoPoint((int) (1E6*64.13162682169124),(int) (1E6*-21.87448740005493)));   
	//guesthouseOverlay.add(new GeoPoint((int) (1E6*64.13028801008561),(int) (1E6*-21.867084503173828)));  
	guesthouseOverlay.add(new GeoPoint((int) (1E6*64.12886486687539),(int) (1E6*-21.86204195022583)));   
	//guesthouseOverlay.add(new GeoPoint((int) (1E6*64.12727310719328),(int) (1E6*-21.87877893447876)));   
	guesthouseOverlay.add(new GeoPoint((int) (1E6*64.12160757565269),(int) (1E6*-21.899807453155518)));  
	//guesthouseOverlay.add(new GeoPoint((int) (1E6*64.13083103235986),(int) (1E6*-21.93199396133423)));   
	guesthouseOverlay.add(new GeoPoint((int) (1E6*64.11286696988958),(int) (1E6*-21.847128868103027)));  
	ol.addOverlay(guesthouseOverlay, R.drawable.o_guesthouse,12);

	ArrayList<GeoPoint> golfOverlay = new ArrayList<GeoPoint>();
    	golfOverlay.clear();
    	golfOverlay.add(new GeoPoint((int)(1E6*64.152367),(int)(1E6*-22.030383)));
    	golfOverlay.add(new GeoPoint((int)(1E6*64.088467),(int)(1E6*-21.924967)));
    	golfOverlay.add(new GeoPoint((int)(1E6*64.092567),(int)(1E6*-21.898233)));
    	golfOverlay.add(new GeoPoint((int)(1E6*64.084150),(int)(1E6*-21.882700)));
    	golfOverlay.add(new GeoPoint((int)(1E6*64.082217),(int)(1E6*-21.892950)));
    	golfOverlay.add(new GeoPoint((int)(1E6*64.069267),(int)(1E6*-21.925650)));
    	golfOverlay.add(new GeoPoint((int)(1E6*64.059750),(int)(1E6*-21.896967)));
    	golfOverlay.add(new GeoPoint((int)(1E6*64.058933),(int)(1E6*-21.991950)));
    	golfOverlay.add(new GeoPoint((int)(1E6*64.123133),(int)(1E6*-21.772533)));
    	golfOverlay.add(new GeoPoint((int)(1E6*64.151950),(int)(1E6*-21.762650)));
    	golfOverlay.add(new GeoPoint((int)(1E6*64.171750),(int)(1E6*-21.727050)));
    	ol.addOverlay(golfOverlay, R.drawable.o_golf,16);


    	ArrayList<GeoPoint> hospitalOverlay = new ArrayList<GeoPoint>();
    	hospitalOverlay.add(new GeoPoint((int)(1E6*64.151017),(int)(1E6*-21.993483)));
    	hospitalOverlay.add(new GeoPoint((int)(1E6*64.149533),(int)(1E6*-21.942533)));
    	hospitalOverlay.add(new GeoPoint((int)(1E6*64.138067),(int)(1E6*-21.923400)));
    	hospitalOverlay.add(new GeoPoint((int)(1E6*64.141100),(int)(1E6*-21.914967)));
    	hospitalOverlay.add(new GeoPoint((int)(1E6*64.133250),(int)(1E6*-21.913667)));
    	hospitalOverlay.add(new GeoPoint((int)(1E6*64.148633),(int)(1E6*-21.876900)));
    	hospitalOverlay.add(new GeoPoint((int)(1E6*64.127100),(int)(1E6*-21.890900)));
    	hospitalOverlay.add(new GeoPoint((int)(1E6*64.139233),(int)(1E6*-21.891983)));
    	hospitalOverlay.add(new GeoPoint((int)(1E6*64.122467),(int)(1E6*-21.888517)));
    	hospitalOverlay.add(new GeoPoint((int)(1E6*64.101500),(int)(1E6*-21.886883)));
    	hospitalOverlay.add(new GeoPoint((int)(1E6*64.098183),(int)(1E6*-21.887333)));
    	hospitalOverlay.add(new GeoPoint((int)(1E6*64.088217),(int)(1E6*-21.918567)));
    	hospitalOverlay.add(new GeoPoint((int)(1E6*64.111883),(int)(1E6*-21.907350)));
    	hospitalOverlay.add(new GeoPoint((int)(1E6*64.106933),(int)(1E6*-21.915083)));
    	hospitalOverlay.add(new GeoPoint((int)(1E6*64.096183),(int)(1E6*-21.876217)));
    	hospitalOverlay.add(new GeoPoint((int)(1E6*64.093667),(int)(1E6*-21.859117)));
    	hospitalOverlay.add(new GeoPoint((int)(1E6*64.108700),(int)(1E6*-21.842583)));
    	hospitalOverlay.add(new GeoPoint((int)(1E6*64.109100),(int)(1E6*-21.842483)));
    	hospitalOverlay.add(new GeoPoint((int)(1E6*64.109150),(int)(1E6*-21.843333)));
    	hospitalOverlay.add(new GeoPoint((int)(1E6*64.104983),(int)(1E6*-21.814533)));
    	hospitalOverlay.add(new GeoPoint((int)(1E6*64.118600),(int)(1E6*-21.800050)));
    	hospitalOverlay.add(new GeoPoint((int)(1E6*64.133600),(int)(1E6*-21.868483)));
    	hospitalOverlay.add(new GeoPoint((int)(1E6*64.150433),(int)(1E6*-21.785333)));
    	hospitalOverlay.add(new GeoPoint((int)(1E6*64.167267),(int)(1E6*-21.696833)));
    	ol.addOverlay(hospitalOverlay, R.drawable.o_hospital,12);


    	return ol;
    	
    }
    
}
