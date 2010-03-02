package bravo.kguide.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bravo.kguide.control.RouteList;
import bravo.kguide.control.Routes;

public class Toolbox {
  
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