package bravo.kguide.control;

import bravo.kguide.control.Routes;
import java.util.ArrayList;

/**
 * 
 *
 * Created: 
 *
 * @author Team Bravo
 * @since 2010-02-09
 */


public class RouteList {
    public ArrayList<Routes> routes = new ArrayList<Routes>();
    public int routeIndex = 0;
    public Routes current = null;
    
    public RouteList(){
	
    }

    public Routes nextRoute(){
		if (routes.isEmpty())  {
		    return null;
		}
		routeIndex = ++routeIndex % routes.size();
		current = routes.get(routeIndex);
		return current;
    }
    
    public void prevRoute(){
		if (routes.isEmpty())  {
		    current = null;
		    return;
		}
		routeIndex = routeIndex == 0 ? routes.size()-1 : --routeIndex % routes.size();
		current = routes.get(routeIndex);
    }

    public void removeRoute(){
		if (routes.isEmpty())  {
		    current = null;
		    return;
		}
		routes.remove(routeIndex);
    }

    public void addRoute(Routes route){
		routes.add(route);
		current = routes.get(routeIndex);
    }

    public void lastRoute(){
	if (routes.isEmpty())  {
	    current = null;
	}
	routeIndex = routes.size()-1;
	current = routes.get(routeIndex);
    }

   public void getCurrent(){
	if (routes == null) {
	    return;
	}
    	current = routes.get(routeIndex);
    }
    
    public ArrayList<Routes> getArrayList(){
    	return routes;
    }
    
    public String toString(){
    	String info = "";
	if (routes != null) {
	    for(int i=0;i<routes.size();i++){
    		info += " Has route: "+routes.get(i).routeId+" ";
	    }
	}    	
    	return info;
    }
}