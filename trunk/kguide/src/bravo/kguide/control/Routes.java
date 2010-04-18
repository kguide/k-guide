package bravo.kguide.control;

import java.util.ArrayList;

import bravo.kguide.data.DataAccess;

import com.google.android.maps.GeoPoint;

public class Routes {
	
    public final static int MEDIA_TEXT = 0;
    public final static int MEDIA_URL = 1;
    public final static int MEDIA_PHOTO = 2;
    public final static int MEDIA_AUDIO = 3;
    public final static int NUMBER_OF_MEDIAS = 4;
    
    public int routeId;
    public int routeTypeId;
    public double routeLength;
    public String routeName;
    public String extraInfo;
    public ArrayList<Coordinate> routePath = new ArrayList<Coordinate>();
    public DataAccess dal;
    
    /**
     * Initializes a route with all information about it
     * @param routeId : The id of the route.
     */
    public Routes(int routeId){
     	this.routeId = routeId;
    }
    
    public Routes(int id, String name) {
	String[] splits;
	splits = name.split ("\\$\\#");
	this.routeId = id;
	if (splits.length == 2) {
	    this.routeName = splits[0];
	    this.extraInfo = splits[1];
	}
	else {
	    this.routeName = name;
	    this.extraInfo = "No info available\n";
	}
      }

    /**
     * Adds coordinate to the coordinate list
     * @param currentLatitude : the latitude value of the coordinate
     * @param currentLongitude : the longitude value of the coordinate
     * @param mediaArr : array of each media
     */
    public void addCoordinate(double currentLatitude, double currentLongitude, String[] mediaArr) {
    	routePath.add(new Coordinate(currentLatitude, currentLongitude, mediaArr));
    }
    
    /**
     * To check if given coordinate has some media at location
     * @param coordinateId : The id of the coordinate
     * @return true if it has some media
     */
    public boolean hasMediaAtCoordinate(int coordinateId){
    	return routePath.get(coordinateId).hasMedia();
    }

    
    public class Coordinate {	
    	public GeoPoint p;	
    	public String mediaArray[] = new String[4];

    	public Coordinate(double latitude, double longitude,String[] mediaArr){
		    p = new GeoPoint((int) (latitude * 1E6),(int) (longitude * 1E6));
		    mediaArray = mediaArr;
    	}
    	
    	/**
    	 * Checks if this coordinate has any media
    	 * @return true if it has any media
    	 */
    	public boolean hasMedia(){
    		for(String item : mediaArray){
    			if(item != null){
    				return true;
    			}
    		}
    		return false;
    	}
    }
    
    public String toString(){
    	return routeName;
    }
}