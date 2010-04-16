package bravo.kguide.control;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import bravo.kguide.view.R;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class OverlayList  {
	
	public ArrayList<Overlay> overlays;
	public ListIterator<Overlay> overlayIterator;
	private Context context;
	private Overlay test;
	public int overlayIndex;

	
	public OverlayList(Context context){
		overlays = new ArrayList<Overlay>();
		overlayIterator = overlays.listIterator();
		overlayIndex = 0;
		this.context = context;
	}
	
	public void addOverlay(ArrayList<GeoPoint> locations, int bitmapResourceId, int powerOfTwoSize){
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), bitmapResourceId); 
		InfoOverlay overlay = new InfoOverlay(locations,bitmap,powerOfTwoSize);
		overlays.add(overlay);
		overlayIndex++;
	}
	
	public Overlay next(){
		if(overlays.isEmpty()){
			return null;
		}
		if(overlayIndex >= overlays.size()-1){
			overlayIndex=0;
		}
		else{
			overlayIndex += 1;
		}
		return overlays.get(overlayIndex);		
	}
	
	public boolean hasNext(){
		if(overlayIndex == overlays.size()-1){
			return false;
		}
		return true;
	}
	
	public void gotoBegining(){
		overlayIndex = 0;
	}
    private class InfoOverlay extends com.google.android.maps.Overlay {
		private Bitmap overlayBitmap;
		private GeoPoint playerloc;
		private Point screenPts;
		private ArrayList<GeoPoint> locations;
		private int pixelSize;
		
		public InfoOverlay(ArrayList<GeoPoint> locations, Bitmap bitmap,int powerOfTwoSize) {
		    
			screenPts = new Point();
			this.locations = locations;
			overlayBitmap = bitmap;
		    pixelSize = powerOfTwoSize;
		    
		}
		
	    @Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		    super.draw(canvas, mapView, shadow);
		    for(int i=0;i<locations.size();i++){
		    	playerloc = locations.get(i);
		    	mapView.getProjection().toPixels(playerloc, screenPts);
		    	canvas.drawBitmap(overlayBitmap, screenPts.x-pixelSize, screenPts.y-pixelSize, null);
		    }
	    }
    }
}