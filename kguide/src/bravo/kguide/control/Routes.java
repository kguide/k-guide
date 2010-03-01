package bravo.kguide.control;

import java.util.ArrayList;
import com.google.android.maps.GeoPoint;

public class Routes {
	
	public final int MEDIA_TEXT = 1;
	public final int MEDIA_PHOTO = 2;
	public final int MEDIA_URL = 3;
	public final int MEDIA_AUDIO = 4;
	
	
    public int routeId;
    public String routeName;
    public ArrayList<Coordinate> routePath = new ArrayList<Coordinate>();
    
    public Routes(int id, String name) {
		this.routeId = id;
		this.routeName = name;
    }
    
    public void addCoordinate(double currentLatitude, double currentLongitude) {
    	routePath.add(new Coordinate(currentLatitude, currentLongitude));
    }

    public class Coordinate {	
    	public GeoPoint p;	
    	
    	public Coordinate(double latitude, double longitude){
		    p = new GeoPoint((int) (latitude * 1E6),(int) (longitude * 1E6));
    	}
    }
    
    public String toString(){
    	return routeName;
    }
}


