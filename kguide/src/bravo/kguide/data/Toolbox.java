package bravo.kguide.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bravo.kguide.control.RouteList;
import bravo.kguide.control.Routes;

public class Toolbox {

    public static Routes createRouteFromJSON(int routeId, String jsonString){
		try {
		    
		    JSONObject jsonObject = new JSONObject(jsonString);
		    
		    Routes route = new Routes(routeId, jsonObject.getString("routeName"));
		    
		    JSONArray lngArray = jsonObject.getJSONArray("lng");
		    JSONArray latArray = jsonObject.getJSONArray("lat");

		    for (int i = 0; i < latArray.length(); i++) {
		
			double currentLatitude = Double.parseDouble(latArray.getString(i));
			double currentLongitude = Double.parseDouble(lngArray.getString(i));
			route.addCoordinate(currentLatitude,currentLongitude);
		    }		    
		    return route;		    
		} catch (JSONException e) {
		    e.printStackTrace();
		    return null;
		}	
    }
    
    public static RouteList createRouteSelectionList(String jsonString){
    	RouteList routeList = new RouteList();
    	try{
	    	JSONObject jsonObject = new JSONObject(jsonString);
	    	JSONArray routeIdArray = jsonObject.getJSONArray("routeId");
	    	JSONArray routeNameArray = jsonObject.getJSONArray("name");
	    	for(int i=0; i<routeIdArray.length();i++){
	    		Routes route = new Routes(routeIdArray.getInt(i),routeNameArray.getString(i));
	    		routeList.addRoute(route);
	    	}
		} catch (JSONException e) {
		    e.printStackTrace();
		    return null;
		}
    	return routeList;    	
    }
}