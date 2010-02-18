package bravo.kguide.view;

import bravo.kguide.control.Controller;
import java.util.ArrayList;

import android.graphics.Point;
import android.graphics.Canvas;
import android.graphics.Paint; 
import android.graphics.Paint.Style; 
import android.graphics.Color;

import com.google.android.maps.MapView;

interface WidgetFrame {
    public void run();
}

public class MapWidgets {

    protected Canvas ourCanvas;
    protected MapView ourMap;

    
    public ArrayList widgetList = new ArrayList<WidgetFrame>();
    

    Controller ctrl = Controller.getInstance();


    public void addWidget(WidgetFrame wid) {
	widgetList.add(wid);
    }


    public void drawComplex(Canvas ourC, MapView ourM) {
	ourCanvas = ourC;
	ourMap = ourM;
	for (int i = 0; i<widgetList.size(); i++) {
	    ((WidgetFrame)widgetList.get(i)).run();
	}

    }
    
    public MapWidgets() {
	
	addWidget( new WidgetFrame() {
		Point mp1 = new Point();
		Point mp2 = new Point();
		Paint myPaint = new Paint();
		
		@Override
		public void run() {
		    myPaint.setStyle(Paint.Style.STROKE); 
		    myPaint.setARGB(255,0,0,0); 
		    myPaint.setAntiAlias(true);
		    myPaint.setTextSize(22); 
		    myPaint.setStyle(Style.FILL);
		    
		    for (int u = 1; u < ctrl.routeList.current.routePath.size();u++) {
			ourMap.getProjection().toPixels(ctrl.routeList.current.routePath.get(u-1).p, mp1);		
			ourMap.getProjection().toPixels(ctrl.routeList.current.routePath.get(u).p, mp2);
			myPaint.setColor(Color.BLUE);
			myPaint.setStrokeWidth(1); 
			ourCanvas.drawCircle(mp2.x,mp2.y, 2, myPaint); 
			ourCanvas.drawCircle(mp2.x,mp2.y, 1, myPaint); 
			
			myPaint.setStrokeWidth(6); 
			ourCanvas.drawLine(mp1.x,mp1.y,mp2.x,mp2.y,myPaint);
		    }
		    
		    for (int u = 1; u < ctrl.routeList.current.routePath.size();u++) {
			ourMap.getProjection().toPixels(ctrl.routeList.current.routePath.get(u-1).p, mp1);		
			ourMap.getProjection().toPixels(ctrl.routeList.current.routePath.get(u).p, mp2);
			myPaint.setStrokeWidth(2); 
			myPaint.setColor(Color.YELLOW);
			ourCanvas.drawLine(mp1.x,mp1.y,mp2.x,mp2.y,myPaint);
		    }
		    
		    
		    
		    myPaint.setARGB(160,200,250,35); 
		    ourCanvas.drawText(ctrl.routeList.current.routeName, 10,20, myPaint);
		    myPaint.setARGB(200,0,0,35); 
		    ourCanvas.drawText(ctrl.routeList.current.routeName, 12,22, myPaint); 
		    
		    
		}
	    });

	

	//
	// Add new widget by editing the code within the curly brackets below.  
	// run() has to be implemented and that is the method that is executed.
	// new methods can be added by changing the interface for
	// WidgetFrame 
	//
	// addWidget( new WidgetFrame() {
	// 	@Override 
	// 	public void run(){}
	//     }

    }	    
    
}