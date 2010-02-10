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
	current = routes.remove(routeIndex);
    }

    public void addRoute(Routes route){
	routes.add(route);
	current = routes.get(routeIndex);
    }

    public void getCurrent(){
	current = routes.get(routeIndex);
    }

} // RouteList