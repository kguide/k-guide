package bravo.kguide.view;

import android.util.Log;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import android.net.Uri;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Paint;

import android.graphics.Paint.Style; 
import android.graphics.Color;


import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Criteria;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;
import bravo.kguide.control.Controller;
import bravo.kguide.control.OverlayList;
import bravo.kguide.control.Routes;


import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

class Mp3Filter implements FilenameFilter {
    public boolean accept(File dir, String name) {
        return (name.endsWith(".mp3"));
    }
}
 
public class GoogleMapScreen extends MapActivity implements ViewFactory
{    
    public View makeView() {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundColor(0xFF000000);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setLayoutParams(new 
                ImageSwitcher.LayoutParams(
                        LayoutParams.FILL_PARENT,
                        LayoutParams.FILL_PARENT));
        return imageView;
    }

    public static final int PLAYER_POS_ID = Menu.FIRST;
    public static final int HELP_ID = Menu.FIRST+1;
    public static final int CURRENT_POS_ID = Menu.FIRST+2;

    public static final int CURRENT_ZOOM_INFO = 16;
    public static final int ZOOM_WAIT = 2200;
    
    
    public AbsoluteLayout myGal;

    public PhotoViewer myViewer ;
    public ImageSwitcher imageSwitcher;


    public ImageButton textButt;
    public ImageButton audioButt;
    public ImageButton homeButt;
    public ImageButton urlButt;
    public ImageButton photoButt;

    public ImageButton playButt;
    public ImageButton stopButt;


    public AbsoluteLayout playOut;
    public AbsoluteLayout topPanel;
    public SeekBar progressBar;
    public TextView elapsedTime;
    public TextView totalTime;
    public TextView routeName;
    public int myIndex;


    public ImageButton btnHelp;
    public ImageButton btnLandscape;
    public ImageButton btnOverlay;
    public ImageButton btnSettings;
    public ImageButton btnZoomOut;
    public ImageButton btnZoomIn;
    public ImageButton btnNextRoute;


    public OverlayList ol;

    public Button homePhoto;
	

    public List<Overlay> listOfOverlays;
    public String showText;
    public AlertDialog.Builder infoDialog;


    public Gallery myGallery;

    public boolean isZoom = false; 

    public int hideNumberofblingoverlays = 0;
  
    Controller ctrl = Controller.getInstance();
    
    public Context context = this;
   

    public double currLat;
    public double currLong;
	

    
    private OurOverlay ourOverlay;
    private OurOverlay infoPoint;
    private MapOverlay playerPos;
    private int blinkCounter;

    private MapWidgets myWidget;
    
    private LocationManager locationManager=null;
    private LocationListener locationListener=null;
    private String myProvider;

    private MapView mapView; 
    private MapController mapController;
    private GeoPoint p;
    private GeoPoint p2;
    private GeoPoint playerPosition;

    private GeoPoint oldLocation;
    private int oldZoom;




    private final Handler handler = new Handler();


    public void activateButtons() {
	textButt.setEnabled(true);
	textButt.setVisibility(1);
	mapView.setEnabled(false);
	audioButt.setEnabled(true);
	audioButt.setVisibility(1);
	homeButt.setEnabled(true);
	homeButt.setVisibility(1);
	urlButt.setEnabled(true);
	urlButt.setVisibility(1);
	photoButt.setEnabled(true);
	photoButt.setVisibility(1);
    }

    public void disableButtons() {
	textButt.setEnabled(false);
	mapView.setEnabled(true);
	textButt.setVisibility(8);
	audioButt.setEnabled(true);
	audioButt.setVisibility(8);
	homeButt.setEnabled(false);
	homeButt.setVisibility(8);
	urlButt.setEnabled(false);
	urlButt.setVisibility(8);
	photoButt.setEnabled(false);
	photoButt.setVisibility(8);
	
    }

    public void activatePlayerButtons() {
	playButt.setEnabled(true);
	playButt.setVisibility(1);
	stopButt.setEnabled(true);
	stopButt.setVisibility(1);
	
	playButt.setOnClickListener(new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
		    ctrl.ourPlayer.togglePausePlay();
		    
		    if (ctrl.ourPlayer.playing) {
			playButt.setImageResource(R.drawable.btn_pause);
		    }
		    else {
			playButt.setImageResource(R.drawable.btn_play);
		    }
		
		}
	    });

	stopButt.setOnClickListener(new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
		    ctrl.ourPlayer.mp.stop();
		    Animation down = AnimationUtils.loadAnimation(context, R.anim.push_down_out);
		    
		    down.setAnimationListener(new Animation.AnimationListener() 
			{
			    @Override
			    public void onAnimationEnd(Animation animation) {
				playOut.setVisibility(8);
			    }
			    @Override
			    public void onAnimationStart(Animation animation) {
			    }
			    @Override
			    public void onAnimationRepeat(Animation animation) {
			    }
			});
		    playOut.startAnimation(down);
		}
	    });
	
    }

    public void initTopPanel() {

	
	
	btnHelp = (ImageButton) findViewById(R.id.help);
	btnLandscape = (ImageButton) findViewById(R.id.landscape);
	btnOverlay = (ImageButton) findViewById(R.id.overlay);
	btnSettings = (ImageButton) findViewById(R.id.settings);
	btnZoomIn = (ImageButton) findViewById(R.id.zoomin);
	btnZoomOut = (ImageButton) findViewById(R.id.zoomout);
	btnNextRoute = (ImageButton) findViewById(R.id.next_route);
	
	
	btnSettings.setEnabled(false);

	btnOverlay.setOnClickListener(new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
		    if (hideNumberofblingoverlays ==  ol.size()) {
			hideNumberofblingoverlays = 0;
		    }
		    else {
			hideNumberofblingoverlays++;
		    }
		    redrawOverlays();
		}
	    });


	btnSettings.setOnClickListener(new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
		    Intent startSettingsScreen = new Intent(GoogleMapScreen.this,SettingsScreen.class);
		    startActivity(startSettingsScreen);
		}
	    });
	
	btnHelp.setOnClickListener(new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
		    Intent helpScreen = new Intent(GoogleMapScreen.this,HelpGuide.class);
		    startActivity(helpScreen);
		}
	    });
	
	btnLandscape.setOnClickListener(new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
		    if (mapView.isSatellite()) {
			mapView.setSatellite(false);
			mapView.setStreetView(true);
			mapView.invalidate();
			
		    }
		    else {
			mapView.setStreetView(false);
			mapView.setSatellite(true);
			mapView.invalidate();
		    }
		}
	    });

	btnZoomIn.setOnClickListener(new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
		    mapController.zoomIn();
		    redrawOverlays();
		}
	    });

	
	btnZoomOut.setOnClickListener(new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
		    mapController.zoomOut();
		    redrawOverlays();
		}
	    });
	
	btnNextRoute.setOnClickListener(new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
		    if (isZoom) {
			disableButtons();
			restorePanAndZoom();
		    }
		    
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
		    }
		    else {
			mapController.animateTo(ctrl.routeList.nextRoute().routePath.get(0).p);
			blinkCounter = 0;
			redrawOverlays();
			routeName.setText(ctrl.routeList.current.routeName);
			mapController.setZoom(15);
			mapView.invalidate();
		    }
		}
	    });
	
    }
    
    



    public void disablePlayerButtons() {
	playButt.setEnabled(false);
	playButt.setVisibility(8);
    }

    public void initPlayer() {
	playOut = (AbsoluteLayout) findViewById(R.id.playerlayout);
	topPanel = (AbsoluteLayout) findViewById(R.id.toppanel);


	playButt = (ImageButton) findViewById(R.id.audio_play);
	stopButt = (ImageButton) findViewById(R.id.audio_stop);
	
	progressBar = (SeekBar) findViewById(R.id.progressbar);
	elapsedTime = (TextView) findViewById(R.id.elapsedTime);
	totalTime = (TextView) findViewById(R.id.totalTime);
	playOut.setEnabled(true);
	playOut.setVisibility(8);
	activatePlayerButtons();	
	Animation down = AnimationUtils.loadAnimation(context, R.anim.push_down_out);
	playOut.startAnimation(down);
	
	
    }
	

    public boolean panAndZoom(GeoPoint curr) {

	    isZoom = true;
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
	isZoom = false;
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
    

    private class OurItem extends OverlayItem {
	
	protected String audio = null;
	protected String photo = null;
	protected String url   = null;
	protected int coordinateID;
	

	public OurItem(GeoPoint point, String title, String snippet, String photo, String url, String audio,int coordinateID)  {

	    super( point,  title, snippet);

	    if (audio != null && audio.length() != 0) {
		this.audio = audio;
	    }
	    else {
		this.audio = null;
	    }

	    if (photo != null && photo.length() != 0) {
		this.photo = photo;
	    }
	    else {
		this.photo = null;
	    }


	    if (url != null && url.length() != 0) {
		this.url = url;
	    }
	    else {
		this.url = null;
	    }
	    
	    
	    this.coordinateID = coordinateID;
	}
	
	protected boolean hasAudio() {
	    if  (this.audio == null) {return false;}
	    return this.audio.length() > 4;
	}
	
	protected boolean hasUrl() {
	    if  (this.url == "" || this.url == null) {	    
		Log.v("is NULL ", ""+this.url);
		return false;
	    }
	    return this.url.length() > 4;
	    
	}
	
	protected boolean hasPhoto() {
	    if  (this.photo == null) {return false;}
	    return this.photo.length() > 4;
	}
	
	protected boolean hasText() {
	    if  ( this.getSnippet() == null) {return false;}
	    return this.getSnippet().length() > 1;
	}

	
    }
    
    private class OurOverlay extends ItemizedOverlay<OurItem> {

	private List<OurItem> items;
	private Drawable marker;
	
	public OurOverlay(Drawable myMarker) {
	    super(myMarker);
	    items = new ArrayList<OurItem>();
	    marker = myMarker;
	    populate();
	}
	
	@Override
	protected OurItem createItem(int i) {
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
		Dialog dialog = new Dialog(context);
		myIndex = pIndex;
		activateButtons();
		if (!items.get(pIndex).hasAudio()) {audioButt.setEnabled(false);audioButt.setVisibility(8);}
		if (!items.get(pIndex).hasPhoto()) {photoButt.setEnabled(false);photoButt.setVisibility(8);}
		if (!items.get(pIndex).hasUrl()) {urlButt.setEnabled(false);urlButt.setVisibility(8);}
		if (!items.get(pIndex).hasText()) {textButt.setEnabled(false);textButt.setVisibility(8);}


		
		
		homeButt.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
			    disableButtons();
			    restorePanAndZoom();
			}
		    });
		

		urlButt.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
			    try {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(items.get(myIndex).url))); 
			    } catch (Exception e) {
				
			    }
			    
			}
		    });
		

		
		audioButt.setOnClickListener(new Button.OnClickListener() {
			@Override
                        public void onClick(View v) {
			    ctrl.ourPlayer.mp.setOnCompletionListener(new OnCompletionListener() {
				    public void onCompletion(MediaPlayer arg0) {
				
					Runnable update = new Runnable() {
						public void run() {
						    Animation down = AnimationUtils.loadAnimation(context, R.anim.push_down_out);
						    
						    down.setAnimationListener(new Animation.AnimationListener() 
							{
							    @Override
							    public void onAnimationEnd(Animation animation) {
								playOut.setVisibility(8);
							    }
							    @Override
							    public void onAnimationStart(Animation animation) {
							    }
							    @Override
							    public void onAnimationRepeat(Animation animation) {
							    }
							});
						    playOut.startAnimation(down);
						}
					    };
					handler.postDelayed(update,6);
				    }
				    
				});
			    
			    
			    Animation up = AnimationUtils.loadAnimation(context, R.anim.push_up_in);
			    				    up.setAnimationListener(new Animation.AnimationListener() 
				{
				    @Override
				    public void onAnimationStart(Animation animation) {
					playOut.setVisibility(1);
				    }
				    @Override
				    public void onAnimationEnd(Animation animation) {
				    }
				    @Override
				    public void onAnimationRepeat(Animation animation) {
				    }
				});


							    playOut.startAnimation(up);
							    playOut.setVisibility(1);
							    
							    File temp = ctrl.myMedia.getAudioFile(ctrl.routeList.current.routeId, items.get(myIndex).coordinateID);
							    Log.v("Audio file: ", ""+temp);
							    if (items.get(myIndex).audio != null && temp != null) {
								ctrl.ourPlayer.playAudio(temp,progressBar,elapsedTime,totalTime);
							    }
								
			}
		    });
		

		photoButt.setOnClickListener(new Button.OnClickListener() {
			@Override
                        public void onClick(View v) {
			    File temp = ctrl.myMedia.getPhotoDirectory(ctrl.routeList.current.routeId, items.get(myIndex).coordinateID);
			    
			    
			    if (items.get(myIndex).photo != null && temp != null) {
				List<String> tFileList = new ArrayList<String>();
				Log.v("Photo check", ""+items.get(myIndex).photo);
				File[] files=temp.listFiles();
				Log.v("photos : ", ""+files.length);
				if (files.length != 0) {
				    for(int i=0; i<files.length; i++) {
					File file = files[i];
					tFileList.add(file.getPath());
				    }
				    
				    myViewer = new PhotoViewer(context,tFileList);
				    
				    myGallery.setAdapter(myViewer);
				    myGal.setVisibility(1);
				    myGal.setEnabled(true);
				    imageSwitcher.setImageDrawable(null);
				    myGallery.setOnItemClickListener(new OnItemClickListener() 
					{
					    public void onItemClick(AdapterView parent, 
								    View v, int position, long id) { 
						Log.i("drawable name : " , (new BitmapDrawable(myViewer.myBitmap)).toString());
						imageSwitcher.setEnabled(true);
						imageSwitcher.setVisibility(1);
						imageSwitcher.setImageDrawable(new BitmapDrawable(myViewer.fileList.get(position).toString()));
					    }
					});
				}
			    }
			}
		    });
		



		infoDialog = new AlertDialog.Builder(context);
		infoDialog.setMessage(items.get(pIndex).getSnippet())
				.setCancelable(false)
				.setPositiveButton("Close", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					    dialog.cancel();
					}
				    });
		textButt.setOnClickListener(new Button.OnClickListener() {
			
                        @Override
                        public void onClick(View v) {
			    infoDialog.show();       
                        }
		    });
	    }
	    return true;
	}
	
	@Override
	public int size() {
	    return(items.size());
	}

	public void addItem(OurItem item) {
	    items.clear();
	    items.add(item);
	    setLastFocusedIndex(-1); 
	    populate();
	}
	
	public void addItemNotDelete(OurItem item) {
	    items.add(item);
	    setLastFocusedIndex(-1); 
	    populate();
	}

	public void clear() {
	    items.clear();
	    populate();
	}
        
	
    }



  
    class MapOverlay extends com.google.android.maps.Overlay {
	private List<Bitmap> items;
	private int animcounter;
	private long deltaTimer;
	private Bitmap walker = BitmapFactory.decodeResource(getResources(), R.drawable.overlay_user_walk);
	Point screenPts;
	
	Paint myPaint = new Paint();
	
	public MapOverlay() {
	    animcounter = 0;
	    deltaTimer = 0;
	    screenPts = new Point();
	}
        @Override
	public boolean draw(Canvas canvas, MapView mapView, 
			    boolean shadow, long when)  {
            super.draw(canvas, mapView, shadow);

	    if (blinkCounter < 50 && animcounter < 5) {
		blinkCounter++;
	    }
	    else {
		myWidget.draw(canvas, mapView,shadow);
	    }

	    myPaint.setStrokeWidth(2); 
	    myPaint.setStyle(Paint.Style.STROKE); 
	    mapView.getProjection().toPixels(playerPosition, screenPts);
	    myPaint.setAntiAlias(true);
	    for (int i = 0; i<3; i++) {
		myPaint.setARGB(255-i*50,255,animcounter*15,animcounter*15); 

		canvas.drawCircle(screenPts.x, screenPts.y, (animcounter*i*2), myPaint); 
	    }
            if (deltaTimer > when ) return true;
            animcounter++;
            animcounter = animcounter % 9;
            deltaTimer = when + 80;
            return true;
        }
    }
    

    public void redrawOverlays() {
	infoPoint.clear();
	listOfOverlays.clear();

	Log.v("Zoom-level :" ,""+  mapView.getZoomLevel()) ;
	if ( mapView.getZoomLevel() > 10) {
	    ol = ctrl.getSimpleOverlays(context);
	    for (int i = hideNumberofblingoverlays; i < ol.size(); i++) {
		listOfOverlays.add(ol.get(i));
	    }
	}

	if (!ctrl.isRouteListEmpty()) {
	    for (int u = 0; u < ctrl.routeList.current.routePath.size();u++) {
		if (ctrl.routeList.current.hasMediaAtCoordinate(u)) {
		    infoPoint.addItemNotDelete(new OurItem(ctrl.routeList.current.routePath.get(u).p,"clickable",
							   ctrl.routeList.current.routePath.get(u).mediaArray[Routes.MEDIA_TEXT],
							   ctrl.routeList.current.routePath.get(u).mediaArray[Routes.MEDIA_PHOTO],
							   ctrl.routeList.current.routePath.get(u).mediaArray[Routes.MEDIA_URL],
							   ctrl.routeList.current.routePath.get(u).mediaArray[Routes.MEDIA_AUDIO],
							   u));

		}
	    }
	    listOfOverlays.add(infoPoint);
	}
	


	listOfOverlays.add(playerPos);
	mapView.invalidate();
    }



    public void initAlbum() {

	imageSwitcher = (ImageSwitcher) findViewById(R.id.imswitch);
	imageSwitcher.setFactory(this);
	imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
								  android.R.anim.fade_in));
	imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
								   android.R.anim.fade_out));
	
	
	
	myGallery = (Gallery) findViewById(R.id.gallery);
	homePhoto = (Button) findViewById(R.id.photo_home);
	homePhoto.setOnClickListener(new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
		    myGal.setVisibility(8);
		    myGal.setEnabled(false);
		}
	    });
	
	myGal = (AbsoluteLayout) findViewById(R.id.mygal);
	myGal.setVisibility(8);
	myGal.setEnabled(false);
	
	
    }


    public GeoPoint initGPS() {
	locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	
	// List providers
	List<String> providers = locationManager.getAllProviders();
	for (String provider : providers) {
	    Log.v("Providers",provider);
	}
	
	Criteria criteria = new Criteria();
	myProvider = locationManager.getBestProvider(criteria, false);
	locationListener = new MyLocationListener();
	Location location = locationManager.getLastKnownLocation(myProvider);
	
	locationManager.requestLocationUpdates(myProvider, 15000, 10,locationListener );
	return (new GeoPoint((int) (location.getLatitude() * 1E6), (int) ( location.getLongitude()* 1E6)));
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
	//mapView.setBuiltInZoomControls(true);   	
	mapController = mapView.getController();
	
	textButt = (ImageButton) findViewById(R.id.text);
	audioButt = (ImageButton) findViewById(R.id.audio);
	homeButt = (ImageButton) findViewById(R.id.home);
	urlButt = (ImageButton) findViewById(R.id.url);
	photoButt = (ImageButton) findViewById(R.id.photo);
	photoButt = (ImageButton) findViewById(R.id.photo);
	disableButtons();
	routeName = (TextView) findViewById(R.id.routename);
	routeName.setText(ctrl.routeList.current.routeName);
	
	
	
	
	initAlbum();
	initTopPanel();
    	initPlayer();
	
	// Get the first coordinate and move the map to it.
	//******************************************************************************************************
	// Nord, endurmennt
	try {
	    playerPosition = initGPS();
	} catch (Exception e) {
	    playerPosition =(new GeoPoint((int) ( 64.13976253758364  * 1E6),  (int) ( -21.95570468902588 * 1E6)));
	}
	
	

	
	



	


	
	
	
	mapController.animateTo(playerPosition);
	mapController.setZoom(15);
	//******************************************************************************************************
	//----------------------------------------------------------------------------------------------------------
	
	
	// Add icon for first coordinate location 
	//----------------------------------------------------------------------------------------------------------
	playerPos = new MapOverlay();
	
	myWidget = new MapWidgets();
	listOfOverlays = mapView.getOverlays();
	listOfOverlays.clear();

	
	Drawable exclaim = getResources().getDrawable(R.drawable.exclaim);
	exclaim.setBounds(0, 0, exclaim.getIntrinsicWidth(), exclaim.getIntrinsicHeight());

	infoPoint = new OurOverlay(exclaim);
	//ctrl.routeList.lastRoute(); //Select the route that was selected last
	redrawOverlays();
	routeName.setText(ctrl.routeList.current.routeName);
	mapView.invalidate();
	//End Display Map           
	

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
	menu.add(0,PLAYER_POS_ID,0,"Where am I?");
    	menu.add(0,CURRENT_POS_ID,0,"Show next route");
    	return result;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case PLAYER_POS_ID:
	    if (isZoom) {
		disableButtons();
		restorePanAndZoom();
	    }
	    mapController.animateTo(playerPosition);
	    break;
	case CURRENT_POS_ID:
	    if (isZoom) {
		disableButtons();
		restorePanAndZoom();
	    }

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
	    blinkCounter = 0;
	    redrawOverlays();
	    routeName.setText(ctrl.routeList.current.routeName);
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
