/**
 * A handler class to retrieve files from the web and store on the phone, either on the
 * phone sd card or it's internal hard drive
 */
package bravo.kguide.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.os.Environment;
import android.util.Log;

public class MediaHandler {
	private String storeDirectory, fileName;
	private URL url;
	public final static String HOME_DIRECTORY_PHONE = Environment.getDataDirectory().getAbsolutePath()+"/data/bravo.kguide.view/files/";
	public final static String HOME_DIRECTORY_SD = Environment.getExternalStorageDirectory()+"/bravo.kguide.view/";
	public final static String HOME_CACHE_DIR = Environment.getDownloadCacheDirectory()+"/bravo.kguide.view/";
	public final static String AUDIO_MEDIA_DIR = "audio/";
	public final static String PHOTO_MEDIA_DIR = "photo/";
	
	
	public MediaHandler(){
	}
	
	/**
	 * Create directory
	 * @param dir: the path to the directory
	 * @param location: the path to 
	 * @return
	 */
	private boolean makeDirectories(String directoryPath){
		File directory = new File(directoryPath);
		if(directory.exists() || directory.mkdirs()){
			Log.i("MediaHandler", "Dirs created");
			return true;
		}
		Log.e("MediaHandler", "Dirs not created");
		return false;
	}
	
	private boolean startCopy(String connectionString){
		try {
			URLConnection urlCon = url.openConnection();
			InputStream in = urlCon.getInputStream();
			
			byte[] buffer = new byte[1024];
			int length;
			File destinationFile = new File(storeDirectory + fileName);
			OutputStream out = new FileOutputStream(destinationFile);
			while( (length=in.read(buffer))>0){
				out.write(buffer,0,length);
				
			}
			out.close();
			in.close();
		}
		catch(IOException e){
			Log.e("MediaHandler", "IOException ocurred");
			return false;
		}
		return true;
	}
	
	/**
	 * Retrive and store a file from the web on the phone 
	 * @param connectionString : A http url pointing to the file
	 * @param basePathConst : Where to store the file on phone, should be one of the constants
	 * HOME_DIRECTORY_PHONE, HOME_DIRECTORY_SD, HOME_CACHE_DIR
	 * @param mediaTypeConst : The type of media, should be one of the constants AUDIO_MEDIA_DIR or PHOTO_MEDIA_DIR
	 * @param routeId : The id of the route
	 * @param coordinateId : The coordinate having the media
	 * @return
	 */
	public boolean storeFile(String connectionString, String basePathConst, String mediaTypeConst,int routeId, int coordinateId){		
		//validates the url
		try {
			url = new URL(connectionString);
		}
		catch(MalformedURLException e){
			Log.e("MediaHandler", "Malformed url");
			return false;
		}
		
		this.storeDirectory = basePathConst+mediaTypeConst+routeId+"/"+coordinateId+"/";
		
		//return immediately if the file already exists
		File destination = new File(this.storeDirectory+this.fileName);
		if(destination.exists()){
			return true;
		}

		//Check if we are using the sdcard and if it is write-able
		if(basePathConst.equals(MediaHandler.HOME_DIRECTORY_SD)){
			if(!Environment.getExternalStorageDirectory().canWrite()){
				Log.i("MediaHandler", "SD card not writeable");
				return false;
			}
		}
		
		if(!this.makeDirectories(this.storeDirectory)){
			return false;
		}

		// Extract the filename from the url
		
		fileName = extractFilenameFromUrl(connectionString);
		/*
		fileName = new StringBuffer(connectionString).reverse().toString();
		fileName = fileName.substring(0,fileName.indexOf("/"));
		fileName = new StringBuffer(fileName).reverse().toString();
		*/
		
		Log.i("MediaHandler", this.storeDirectory+fileName);
		return startCopy(connectionString);
	}

	/**
	 * Returns a File object if the file exists in any of the three store folders
	 * @param fileName : name of the file
	 * @param routeId : id of the route
	 * @param coordinateId : id of the route's coordinate 
	 * @param mediaType : the type of media
	 * @return
	 */
	public File getFile(String url, int routeId, int coordinateId, String mediaType){
		//Try finding it in any of the three store locations
		String filename = extractFilenameFromUrl(url);
		String relPath = mediaType+routeId+"/"+coordinateId+"/"+filename;
		Log.i("MediaHandler","Retr. file at path: "+relPath);
		File file = new File(HOME_DIRECTORY_SD+relPath);
		if(file.exists()){
			return file;
		}
		
		file = new File(HOME_DIRECTORY_PHONE+relPath);
		if(file.exists()){
			return file;
		}
		
		file = new File(HOME_CACHE_DIR+relPath);
		if(file.exists()){
			return file;
		}
		return null;
	}
	
	private String extractFilenameFromUrl(String url){
		String fn;
		fn = new StringBuffer(url).reverse().toString();
		fn = fn.substring(0,fn.indexOf("/"));
		fn = new StringBuffer(fn).reverse().toString();		
		return fn;
	}
}