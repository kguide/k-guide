package bravo.kguide.view;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;
import bravo.kguide.control.Controller;
import bravo.kguide.control.Routes;

public class RouteSelection extends ListActivity {
    Controller controller = Controller.getInstance();
    Context context = this;
    
    private ListView listView;
    private int limit=10,offset=0;
    public ArrayList<Routes> routeList;
    
    //### Indexes for the Options menu buttons ####/
    final int QUIT_OB = 3;
    final int FILTER_OB = 2;
    final int NEXT_OB = 1;
    final int PREV_OB = 0;
    
    //### Indexes for the Context menu buttons ####/
    final int SELECT_ROUTE_CM = 0;
    
    int longSelectedRoute;
    private MenuItem prev,more,filter,back;
    
    private TextView myText;


    @Override
    public void onContentChanged () {
	super.onContentChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.route_selection_v);
	myText = (TextView) findViewById(R.id.descriptionView);
	Log.v("RouteSelection","onCreate called");
	listView = this.getListView();
	listView.setDrawingCacheEnabled(false);
	listView.setOnItemLongClickListener(new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					       int arg2, long arg3) {
		    longSelectedRoute = arg2;
		    Log.v("RouteSelection", ""+arg2);
		    return false;
		}
	    });
	listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		public void onItemSelected(AdapterView parentView, View childView, int position, long id) {
		    Log.v("lol id = ",""+routeList.get((int)id).extraInfo);
		    myText.setText(routeList.get((int)id).extraInfo);
		}
		public void onNothingSelected(AdapterView parentView) {
		}
	    });		
	
	Log.v("registerForContextMenu","");
	registerForContextMenu(listView);
	Log.v("populateview","");
	populateListView();
    }
	
    /**
     * Populates the listview with information from the server
     */
    private void populateListView(){
	try{
	    routeList = controller.getRouteSelectionList(limit,offset).getArrayList();
	    // Load items into the listview
	    if(routeList.size() > 0){
		setListAdapter(new ArrayAdapter<Routes>(this,
							R.layout.list_item,R.id.label,routeList));
	    }
	}
	catch(NullPointerException e){
	    Log.v("RouteSelection", "routeList null");
	}
    }
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	super.onCreateOptionsMenu(menu);
	MenuInflater inflater = new MenuInflater(context);
	inflater.inflate(R.drawable.route_selection_om, menu);

	//####### Options menu on click listeners
	more = menu.getItem(this.NEXT_OB);
	prev = menu.getItem(this.PREV_OB);
	filter = menu.getItem(this.FILTER_OB);
	back = menu.getItem(this.QUIT_OB);
	prev.setEnabled(false);
	if(routeList.size()<limit){
	    more.setEnabled(false);
	}
		
	more.setOnMenuItemClickListener(new OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(MenuItem arg0) {
		    if(routeList.size() == offset){
			limit +=limit;
			populateListView();
			// allow user to browse back
			prev.setEnabled(true);
			// no more items to display
			if(routeList.size() < limit){
			    more.setEnabled(false);
			}
		    }
				
		    // TODO Auto-generated method stub
		    return false;
		}
	    });
		
		
	prev.setOnMenuItemClickListener(new OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(MenuItem arg0) {
		    if(offset > 0){
			offset -= limit;
			populateListView();
			//allow user to browse back current list
			more.setEnabled(true);
			//can't advance previous if already at beginning
			if(offset == 0){
			    prev.setEnabled(false);
			}
		    }
		    return false;
		}
	    });

	filter.setOnMenuItemClickListener(new OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(MenuItem arg0) {
		    // TODO Auto-generated method stub
		    return false;
		}
	    });
		
		
	back.setOnMenuItemClickListener(new OnMenuItemClickListener() {			
		@Override
		public boolean onMenuItemClick(MenuItem arg0) {
		    finish();
		    return false;
		}
	    });
	
	return true;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
	super.onCreateContextMenu(menu, v, menuInfo);
		
	MenuInflater inflater = new MenuInflater(context);
	inflater.inflate(R.drawable.route_select_cm, menu);
	MenuItem selectRoute = menu.getItem(this.SELECT_ROUTE_CM);
	Intent startRoute = new Intent(RouteSelection.this,GoogleMapScreen.class);
	selectRoute.setIntent(startRoute);
	selectRoute.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
		@Override
		public boolean onMenuItemClick(MenuItem item) {
		    Log.v("RouteSelection", "Item selected: "+longSelectedRoute);
		    int routeId = routeList.get(longSelectedRoute).routeId;

		    // If the intial data hasn't been loaded, we need to load it now
		    if(controller.hasRoutesOnPhone(context)){
			controller.loadInitialMapInfo(context);
		    }
		    controller.addRouteToList(context, routeId);
		    return false;
		}
	    });
    }
	
}