package bravo.kguide.view;

import bravo.kguide.control.Controller;

import java.util.ArrayList;
import java.util.List;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.Paint; 
import android.graphics.Bitmap; 
import android.graphics.BitmapFactory; 
import android.view.View;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.widget.*;

import android.os.Handler;
 
public class GoogleMapScreen extends MapActivity 
{    

    public static final int PLAYER_POS_ID = Menu.FIRST;
    public static final int HELP_ID = Menu.FIRST+1;
    public static final int CURRENT_POS_ID = Menu.FIRST+2;

    public static final int CURRENT_ZOOM_INFO = 18;
    public static final int ZOOM_WAIT = 2200;
    
    
    
  
    Controller ctrl = Controller.getInstance();
    
    Context context = this;

    double currLat;
    double currLong;
	
    private OurOverlay ourOverlay;
    private OurOverlay infoPoint;
    private MapOverlay playerPos;

    private MapWidgets myWidget;
    
    private LocationManager locationManager=null;
    private LocationListener locationListener=null;

    private MapView mapView; 
    private MapController mapController;
    private GeoPoint p;
    private GeoPoint p2;
    private GeoPoint playerPosition;

    private GeoPoint oldLocation;
    private int oldZoom;

    public boolean panAndZoom(GeoPoint curr) {
	oldLocation = mapView.getMapCenter();
	oldZoom = mapView.getZoomLevel();
	mapController.animateTo(curr);
		
	while (mapView.getZoomLevel() != CURRENT_ZOOM_INFO) {
	    if (mapView.getZoomLevel() < CURRENT_ZOOM_INFO) {
		mapController.zoomIn();
	    } 
	    else {
		mapController.zoomOut();
	    }
	}
	return true;
    }
    
    public  boolean restorePanAndZoom() {
	//	oldLocation;
	//oldZoom;
	while (mapView.getZoomLevel() != oldZoom) {
	    if (mapView.getZoomLevel() < oldZoom) {
		mapController.zoomIn();
	    } 
	    else {
		mapController.zoomOut();
	    }
	}
	
	mapController.animateTo(oldLocation);
	return true;
    }


    private class OurOverlay extends ItemizedOverlay<OverlayItem> {
	
	private List<OverlayItem> items;
	private Drawable marker;
	
	public OurOverlay(Drawable myMarker) {
	    super(myMarker);
	    items = new ArrayList<OverlayItem>();
	    marker = myMarker;
	    populate();
	}
	
	@Override
	protected OverlayItem createItem(int i) {
	    return(items.get(i));
	    
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
	    super.draw(canvas, mapView, shadow);
	    boundCenterBottom(marker);
	}
	
	@Override
	protected boolean onTap(int pIndex) {    
	    if (items.get(pIndex).getTitle().equals("clickable")) {
		
		panAndZoom(items.get(pIndex).getPoint());
		
		
		AlertDialog.Builder infoDialog = new AlertDialog.Builder(context);
		infoDialog.setMessage(items.get(pIndex).getSnippet())
		    .setCancelable(false)
		    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int id) {
			        restorePanAndZoom();
				dialog.cancel();
			    }
			});
		infoDialog.show();            
            }
	    return true;
	}
	
	@Override
	public int size() {
	    return(items.size());
	}

	public void addItem(OverlayItem item) {
	    items.clear();
	    items.add(item);
	    setLastFocusedIndex(-1); 
	    populate();
	}
	
	public void addItemNotDelete(OverlayItem item) {
	    items.add(item);
	    setLastFocusedIndex(-1); 
	    populate();
	}

	
    }



    class MapOverlay extends com.google.android.maps.Overlay
    {
	private List<Bitmap> items;
	private int animcounter;
	private long deltaTimer;
	private Bitmap walker = BitmapFactory.decodeResource(getResources(), R.drawable.overlay_user_walk);
	Point screenPts;


	public MapOverlay() {
	    animcounter = 0;
	    deltaTimer = 0;
	    screenPts = new Point();

	}
        @Override
	public void draw(Canvas canvas, MapView mapView, 
			 boolean shadow) 
        {
	    super.draw(canvas, mapView, shadow);
	    myWidget.drawComplex(canvas,mapView);
	    mapView.getProjection().toPixels(playerPosition, screenPts);
	    canvas.drawBitmap(walker, screenPts.x-8, screenPts.y-8, null); 
        }
    }

    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	ctrl.initData(context);
	// Begin Display Map  
	//----------------------------------------------------------------------------------------------------------
	setContentView(R.layout.google_maps);
	
	mapView = (MapView) findViewById(R.id.googleMapsMapview);
	mapView.setBuiltInZoomControls(true);   	
	mapController = mapView.getController();
	
	// Get the first coordinate and move the map to it.
	//******************************************************************************************************
	// Central reykjavik.
	playerPosition = new GeoPoint((int) (64.13674367070412 * 1E6), 
				      (int) (-21.923303604125977 * 1E6));
	
	mapController.animateTo(playerPosition);
	mapController.setZoom(15);
	//******************************************************************************************************
	//----------------------------------------------------------------------------------------------------------
	
	
	// Add icon for first coordinate location 
	//----------------------------------------------------------------------------------------------------------
	playerPos = new MapOverlay();
	
	// ourOverlay.addItem(new OverlayItem(p,"hint",controller.game.getCurrentHintText()));
	
	myWidget = new MapWidgets();
	List<Overlay> listOfOverlays = mapView.getOverlays();
	listOfOverlays.clear();
	// listOfOverlays.add(ourOverlay);
	
	listOfOverlays.add(playerPos);
	
	Drawable exclaim = getResources().getDrawable(R.drawable.exclaim);
	exclaim.setBounds(0, 0, exclaim.getIntrinsicWidth(), exclaim.getIntrinsicHeight());
	
	infoPoint = new OurOverlay(exclaim);
	
	if (!ctrl.isRouteListEmpty()) {
	    for (int u = 0; u < ctrl.routeList.current.routePath.size();u++) {
		if (ctrl.routeList.current.hasMediaAtCoordinate(u)) {
		    infoPoint.addItemNotDelete(new OverlayItem(ctrl.routeList.current.routePath.get(u).p,"clickable",ctrl.routeList.current.routePath.get(u).mediaArray[0]));
		}
	    }
	    
	}
	
	listOfOverlays.add(infoPoint);
	mapView.invalidate();
	
	//End Display Map           
	
	// Set up the location listener to listen for new GPS locations
	locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	locationListener = new MyLocationListener();
	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 100,locationListener);
    }
    
    private class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location location) {
		    currLat = location.getLatitude();
		    currLong = location.getLongitude();
	
		    playerPosition = new GeoPoint((int) (currLat * 1E6), 
						  (int) (currLong * 1E6));
			    
		}
		
		
		public void onProviderDisabled(String provider) {
		}	
		public void onProviderEnabled(String provider) {
		}
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
    }
    
    @Override
    protected boolean isRouteDisplayed() {
	return false;
    }
    @Override 
	public boolean onCreateOptionsMenu(Menu menu){
	boolean result = super.onCreateOptionsMenu(menu);
	menu.add(0,HELP_ID,0,"Help");
	menu.add(0,PLAYER_POS_ID,0,"Show player");
    	menu.add(0,CURRENT_POS_ID,0,"Show next route");
    	return result;
    }


    
   
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case PLAYER_POS_ID:
	    mapController.animateTo(playerPosition);
	    System.out.println(mapView.getZoomLevel());
	    break;
	case CURRENT_POS_ID:
	    if (ctrl.isRouteListEmpty()) {
		String helpString = "No routes have been added.  Goto Select Route to get new routes";
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(helpString)
		    .setCancelable(false)
		    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			    }
			});
		AlertDialog alert = builder.create();
		alert.show();
		break;
	    }
	    mapController.animateTo(ctrl.routeList.nextRoute().routePath.get(0).p);
	    mapController.setZoom(15);
	    this.mapView.invalidate();
	    break;
	    
	case HELP_ID:
	    String helpString = "Welcome to k-guide. ";
		
	    AlertDialog.Builder builder = new AlertDialog.Builder(context);
	    builder.setMessage(helpString)
		.setCancelable(false)
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			    dialog.cancel();
			}
			});
	    AlertDialog alert = builder.create();
	    alert.show();
	    break;
	default : 
	    return super.onOptionsItemSelected(item);
	}
	return true; /* true means: "we handled the event". */
    } 
        
}
