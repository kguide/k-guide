package bravo.kguide.view;

import java.io.BufferedInputStream; 
import java.io.IOException; 
import java.io.InputStream; 
import java.io.File;
import java.net.URL; 
import java.net.URLConnection; 

import java.util.ArrayList;
import java.util.List;



import android.os.Bundle;
import android.app.Activity; 
import android.content.Context; 
import android.graphics.Bitmap; 
import android.graphics.BitmapFactory; 
import android.os.Bundle; 
import android.util.Log; 
import android.view.View; 
import android.view.ViewGroup; 
import android.widget.BaseAdapter; 
import android.widget.Gallery; 
import android.widget.ImageView; 

public class PhotoViewer extends BaseAdapter { 
    /** The parent context */ 
    private Context myContext; 
    public List<String> fileList;
    public Bitmap myBitmap;
    
    public PhotoViewer(Context c, List<String> fList) {  
	this.myContext = c; 
        fileList = fList;  
	myBitmap = null;
    }  
        


    public int getCount() { return this.fileList.size(); } 
    public Object getItem(int position) { return position; } 
    public long getItemId(int position) { return position; } 
    
    /** Returns a new ImageView to 
     * be displayed, depending on 
     * the position passed. */ 
    public View getView(int position, View convertView, ViewGroup parent) { 
	ImageView i = new ImageView(this.myContext); 
	
	myBitmap = BitmapFactory.decodeFile(fileList.get(position).toString());
	i.setImageBitmap(myBitmap);
	i.setAdjustViewBounds(true);
	i.setLayoutParams(new Gallery.LayoutParams(70, 75));
        i.setScaleType(ImageView.ScaleType.FIT_XY);
	return i; 
    } 
    
    /** Returns the size (0.0f to 1.0f) of the views 
     * depending on the 'offset' to the center. */ 
    public float getScale(boolean focused, int offset) { 
	/* Formula: 1 / (2 ^ offset) */ 
	return Math.max(0, 1.0f / (float)Math.pow(2, Math.abs(offset))); 
    } 
} 
