package bravo.kguide.data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RouteDB {

	public static final int MEDIA_ARRAY_TEXT = 0;
	public static final int MEDIA_ARRAY_LINK = 1;
	public static final int MEDIA_ARRAY_PHOTO = 2;
	public static final int MEDIA_ARRAY_AUDIO = 3;
	
    public static final String DB_NAME = "kGuideDB";
    public static final String DB_TABLE_ROUTES = "routesTable";
    public static final String DB_TABLE_ROUTE_TYPES = "routeTypes";
    public static final String DB_TABLE_ROUTE_COORDINATES = "route_coordinates";
    public static final String DB_TABLE_MEDIA_LINKS = "media_links";
    public static final String DB_TABLE_MEDIA_TEXTS = "media_texts";
    public static final String DB_TABLE_MEDIA_AUDIO = "media_audio";
    public static final String DB_TABLE_MEDIA_PHOTOS = "media_photos";
    
    public static final int DB_VERSION = 9;

    // Table column names
    private static final String[] ROUTE_COLS = new String[] { "routeId", "typeId", "name","route_length"};
    private static final String[] ROUTE_TYPES_COLS = new String[] {"typeId","description"};
    private static final String[] ROUTE_COORDINATES_COLS = new String[] {"id","latitude","longitude","routeId"};
    private static final String[] MEDIA_LINKS_COLS = new String[] {"coordinate_id","url"};
    private static final String[] MEDIA_TEXTS_COLS = new String[] {"coordinate_id","information"};
    private static final String[] MEDIA_AUDIO_COLS = new String[] {"coordinate_id","url"};
    private static final String[] MEDIA_PHOTOS_COLS = new String[] {"coordinate_id","url"};    
   
    private SQLiteDatabase db;
    private final DBhelper dbOpenHelper;

    private static class DBhelper extends SQLiteOpenHelper {

    	/* ############ Create table strings ############*/
        private static final String DB_CREATE_ROUTES = "CREATE TABLE "
            + RouteDB.DB_TABLE_ROUTES
            + " (routeId INTEGER PRIMARY KEY,typeId INTEGER NOT NULL, name TEXT NOT NULL, route_length double NOT NULL);";

        private static final String DB_CREATE_ROUTE_TYPES = "CREATE TABLE "
            + RouteDB.DB_TABLE_ROUTE_TYPES
            + " (typeId INTEGER PRIMARY KEY, description TEXT UNIQUE NOT NULL);";

        private static final String DB_CREATE_ROUTE_COORDINATES = "CREATE TABLE "
            + RouteDB.DB_TABLE_ROUTE_COORDINATES
            + " (id INTEGER PRIMARY KEY AUTOINCREMENT, latitude FLOAT NOT NULL, longitude FLOAT NOT NULL, routeId INTEGER NOT NULL);";
        
        private static final String DB_CREATE_MEDIA_LINKS = "CREATE TABLE "
            + RouteDB.DB_TABLE_MEDIA_LINKS
            + " (coordinate_id INTEGER PRIMARY KEY, url TEXT NOT NULL);";
        
        private static final String DB_CREATE_MEDIA_TEXTS = "CREATE TABLE "
            + RouteDB.DB_TABLE_MEDIA_TEXTS
            + " (coordinate_id INTEGER PRIMARY KEY, information TEXT NOT NULL);";
        
    	private static final String DB_CREATE_MEDIA_AUDIO = "CREATE TABLE "
            + RouteDB.DB_TABLE_MEDIA_AUDIO
            + " (coordinate_id INTEGER PRIMARY KEY, url TEXT NOT NULL);";
    	
		private static final String DB_CREATE_MEDIA_PHOTOS = "CREATE TABLE "
            + RouteDB.DB_TABLE_MEDIA_PHOTOS
            + " (coordinate_id INTEGER PRIMARY KEY, url TEXT NOT NULL);";
		/* ############ End of create table strings ############*/
		
		
        public DBhelper(final Context context) {
            super(context, RouteDB.DB_NAME, null, RouteDB.DB_VERSION);
        }

        /*
         * Creates the database tables needed
         * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
         */
        @Override      
        public void onCreate(final SQLiteDatabase db) {
            try {
            	Log.v("RouteDB", "Creating tables");
                db.execSQL(DBhelper.DB_CREATE_ROUTES);
                db.execSQL(DBhelper.DB_CREATE_ROUTE_TYPES);
                db.execSQL(DBhelper.DB_CREATE_ROUTE_COORDINATES);
                db.execSQL(DBhelper.DB_CREATE_MEDIA_LINKS);
                db.execSQL(DBhelper.DB_CREATE_MEDIA_TEXTS);
                db.execSQL(DBhelper.DB_CREATE_MEDIA_AUDIO);
                db.execSQL(DBhelper.DB_CREATE_MEDIA_PHOTOS);
            } catch (SQLException e) {
            	Log.e("RouteDB", "SQLException");
            }
        }
        
        @Override
        public void onOpen(final SQLiteDatabase db) {
            super.onOpen(db);
        }
        
        @Override
        public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
            Log.v("RouteDB", "Onupgrade dropping tables");
        	db.execSQL("DROP TABLE IF EXISTS " + RouteDB.DB_TABLE_ROUTES);
            db.execSQL("DROP TABLE IF EXISTS " + RouteDB.DB_TABLE_ROUTE_TYPES);
            db.execSQL("DROP TABLE IF EXISTS " + RouteDB.DB_TABLE_ROUTE_COORDINATES);
            db.execSQL("DROP TABLE IF EXISTS " + RouteDB.DB_TABLE_MEDIA_AUDIO);
            db.execSQL("DROP TABLE IF EXISTS " + RouteDB.DB_TABLE_MEDIA_LINKS);
            db.execSQL("DROP TABLE IF EXISTS " + RouteDB.DB_TABLE_MEDIA_PHOTOS);
            db.execSQL("DROP TABLE IF EXISTS " + RouteDB.DB_TABLE_MEDIA_TEXTS);
            onCreate(db);
        }
    }

    public RouteDB(final Context context) {
        this.dbOpenHelper = new DBhelper(context);
        establishDb();
    }

    private void establishDb() {
    	Log.v("database","Establishing database");
        if (this.db == null) {
        	Log.v("database","db not existent");
            this.db = this.dbOpenHelper.getWritableDatabase();
        }
    }

    public void cleanup() {
        if (this.db != null) {
            this.db.close();
            this.db = null;
        }
    }

    /**
     * Inserts a route into the database
     * 
     * @param routeId
     * @param jsonRoute
     * @param contentOnPhone
     */
    //private static final String[] ROUTE_COLS = new String[] { "routeId", "typeId", "name","route_length"};
    public void insertRoute(final int routeId,final String jsonRoute){
		try {
		    
		    JSONObject jsonObject = new JSONObject(jsonRoute);
		    
		    String routeName = jsonObject.getString("routeName");
		    int routeType = Integer.parseInt(jsonObject.getString("routeType"));
		    insertRoute(routeId, routeType, routeName, 0);
		    //By pre-condition all arrays are of equal length
		    JSONArray textArray = jsonObject.getJSONArray("routeText");
		    JSONArray photoArray = jsonObject.getJSONArray("routePhotoUrl");
		    JSONArray audioArray = jsonObject.getJSONArray("routeAudioUrl");
		    JSONArray linkArray = jsonObject.getJSONArray("routeWebUrl");
		    JSONArray lngArray = jsonObject.getJSONArray("lng");
		    JSONArray latArray = jsonObject.getJSONArray("lat");
		    
		    // Insert the coordinate and media for this route
		    String media = "";
		    for (int i = 0; i < latArray.length(); i++) {
				double lat = Double.parseDouble(latArray.getString(i));
				double lng = Double.parseDouble(lngArray.getString(i));
				this.insertCoordinate(lat, lng, routeId);
				
				int coordinateId = this.getLastInsertedCoordinateId();
				Log.v("RouteDB", "Got coordinateId "+coordinateId);
				media = textArray.getString(i);
				if(media.equals("")){
					;
				} else {
					insertMediaText(coordinateId, media);
				}
				
				media = photoArray.getString(i);
				if(media.equals("")){
					;
				} else {
					insertMediaPhoto(coordinateId, media);
				}

				media = audioArray.getString(i);
				if(media.equals("")){
					;
				} else {
					insertMediaAudio(coordinateId, media);
				}

				media = linkArray.getString(i);
				if(media.equals("")){
					;
				} else {
					insertMediaLink(coordinateId, media);
				}
				
		    }
		} catch (JSONException e) {
		    e.printStackTrace();
		}	
    }
    
    private void insertRoute(final int routeId, final int typeId, final String name, final double routeLength) {
        ContentValues values = new ContentValues();
	String[] splits;
        values.put("routeId", routeId);
        values.put("typeId", typeId);

	
	splits = name.split ("\\$\\#");
	if (splits.length == 2) {
	    values.put("name", splits[0]);
	}
	else {
	    values.put("name", name);
	}
        
        values.put("route_length",routeLength);
        this.db.insert(RouteDB.DB_TABLE_ROUTES, null, values);
    }

    /**
     * Retrieve information about a given route as a Cursor object having one row
     * @param routeId : The id of the route to be retrieved
     * @return Cursor positioned at it's first row, having columns
     * 			routeId | type_id | name | route_length | created | active
     */
    public Cursor getRouteBaseInfo(int routeId){
    	Cursor c = null;
    	try {
    		c = this.db.query(true, RouteDB.DB_TABLE_ROUTES, RouteDB.ROUTE_COLS, "routeId="+routeId, null, null, null, null, null);
    		c.moveToFirst();
    	}
    	catch (SQLException e){
          Log.v("RouteDB","SQLException"+e.getMessage());
    	}/*
	    finally {
	        if (c != null && !c.isClosed()) {
	            c.close();
	        }
    	}*/
    	return c;
    }	

 
    /**
     * Retrieve lat/lng information about a given route as a Cursor object positioned at first row
     * @param routeId : The id of the route to be retrieved
	 * @return Cursor containing all the coordinate rows for this route.
	 * Having columns: coordinateId | latitude | longitute | routeId
     */
    public Cursor getRouteLatLng(int routeId){
    	Cursor c = null;
    	try {
    		c = this.db.query(true, RouteDB.DB_TABLE_ROUTE_COORDINATES, RouteDB.ROUTE_COORDINATES_COLS, "routeId="+routeId, null, null, null, null, null);
    		c.moveToFirst();
    	}
    	catch (SQLException e){
          Log.v("RouteDB","SQLException"+e.getMessage());
    	}/*
	    finally {
	        if (c != null && !c.isClosed()) {
	            c.close();
	        }
	    }*/
    	return c;
    }
 
    /**
     * Delete a route from the local database
     * @param routeId : The id of the route to be delete
     */
    public void deleteRoute(final int routeId) {
    	try{
    		if(existsRoute(routeId)){
    			this.db.delete(RouteDB.DB_TABLE_ROUTES, "routeId=" + routeId, null);
    		}
    	}catch (SQLException e) {
    		e.printStackTrace();
    	}
    }

    /**
	 * Checks if route exists     
	 * @param routeId: The routeId to look for
	 * @return true if route exists, false otherwise
	 */
    public boolean existsRoute(final int routeId) {
    	Log.v("RouteDB", "In existsRoute");
        Cursor c = null;
        try {
            c = this.db.query(true, RouteDB.DB_TABLE_ROUTES, RouteDB.ROUTE_COLS, "routeId = '" + routeId + "'", null, null, null, null,
                null);
            c.moveToFirst();
            if (c.getCount() > 0) {
            	Log.v("RouteDB", "Cursor count > 0 route exists");
            	c.close();
            	return true;
            }
            return false;
        } catch (SQLException e) {
        	e.printStackTrace();
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }
        return false;
    }

    
    private void insertCoordinate(double lat, double lng, int routeId) {
        ContentValues values = new ContentValues();
        values.put(ROUTE_COORDINATES_COLS[1], lat);
        values.put(ROUTE_COORDINATES_COLS[2], lng);
        values.put(ROUTE_COORDINATES_COLS[3], routeId);
        this.db.insert(RouteDB.DB_TABLE_ROUTE_COORDINATES, null, values);
    }
    
    /**
     * Inserts a route into the database
     * 
     * @param routeId
     * @param jsonRoute
     * @param contentOnPhone
     */
    private void insertMediaLink(final int coordinateId, final String url) {
        ContentValues values = new ContentValues();
        values.put(MEDIA_LINKS_COLS[0], coordinateId);
        values.put(MEDIA_LINKS_COLS[1], url);
        this.db.insert(RouteDB.DB_TABLE_MEDIA_LINKS, null, values);
    }
    
    private void insertMediaText(final int coordinateId, final String text){
        ContentValues values = new ContentValues();
        values.put(MEDIA_TEXTS_COLS[0], coordinateId);
        values.put(MEDIA_TEXTS_COLS[1], text);
        this.db.insert(RouteDB.DB_TABLE_MEDIA_TEXTS, null, values);    	
    }

    private void insertMediaAudio(final int coordinateId, final String url) {
        ContentValues values = new ContentValues();
        values.put(MEDIA_AUDIO_COLS[0], coordinateId);
        values.put(MEDIA_AUDIO_COLS[1], url);
        this.db.insert(RouteDB.DB_TABLE_MEDIA_AUDIO, null, values);
    }
    
    private void insertMediaPhoto(final int coordinateId, final String url) {
        ContentValues values = new ContentValues();
        values.put(MEDIA_PHOTOS_COLS[0], coordinateId);
        values.put(MEDIA_PHOTOS_COLS[1], url);
        this.db.insert(RouteDB.DB_TABLE_MEDIA_PHOTOS, null, values);
    }
    
    /**
     * Returns the http string containing the media at given coordinateId
     * @param coordinateId
     * @return
     */
    public String[] getRouteCoordinateMediaArray(final int coordinateId){
    	Cursor c = null;
    	String routeMedia[] = new String[4];
    	
    	try {
    		//Get text information
    		c = this.db.query(true, RouteDB.DB_TABLE_MEDIA_TEXTS, RouteDB.MEDIA_TEXTS_COLS, MEDIA_TEXTS_COLS[0]+"="+coordinateId, null, null, null, null, null);
    		if(c.getCount() > 0){
	    		c.moveToFirst();
	    		routeMedia[RouteDB.MEDIA_ARRAY_TEXT] = c.getString(1);
    		}
	    	c.close();
	    	
	    	//Get link information
    		c = this.db.query(true, RouteDB.DB_TABLE_MEDIA_LINKS, RouteDB.MEDIA_LINKS_COLS, MEDIA_LINKS_COLS[0]+"="+coordinateId, null, null, null, null, null);
    		if(c.getCount() > 0){
	    		c.moveToFirst();
	    		routeMedia[RouteDB.MEDIA_ARRAY_LINK] = c.getString(1);
    		}
    		c.close();
    		//Get photo information
    		c = this.db.query(true, RouteDB.DB_TABLE_MEDIA_PHOTOS, RouteDB.MEDIA_PHOTOS_COLS, MEDIA_PHOTOS_COLS[0]+"="+coordinateId, null, null, null, null, null);
    		if(c.getCount() > 0){
	    		c.moveToFirst();
	    		routeMedia[RouteDB.MEDIA_ARRAY_PHOTO] = c.getString(1);
    		}
    		c.close();
    		
    		//Get audio information
    		c = this.db.query(true, RouteDB.DB_TABLE_MEDIA_AUDIO, RouteDB.MEDIA_AUDIO_COLS, MEDIA_AUDIO_COLS[0]+"="+coordinateId, null, null, null, null, null);
    		if(c.getCount() > 0){
	    		c.moveToFirst();
	    		routeMedia[RouteDB.MEDIA_ARRAY_AUDIO] = c.getString(1);
    		}
    		c.close();
    	}
    	catch (SQLException e){
          Log.v("SQLException", e.getMessage());
    	}
	    finally {
	        if (c != null && !c.isClosed()) {
	            c.close();
	        }
	    }
    	return routeMedia;
    }
    
    private int getLastInsertedCoordinateId(){
    	Cursor c;
    	String orderBy = "id DESC";
    	c = this.db.query(true, DB_TABLE_ROUTE_COORDINATES, ROUTE_COORDINATES_COLS, null, null, null, null, orderBy, "1");
    	c.moveToFirst();
    	return c.getInt(0);
    }
    
    
    public boolean hasRoutes(){
    	Cursor c;
    	try{
	    	c = this.db.query(DB_TABLE_ROUTES, ROUTE_COLS, null, null, null, null, null,"1");
	    	if(c.getCount() > 0){
	    		return true;
	    	}
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    }
    	return false;
    }
    
    /**
     * Usedto get array of all routeId's in the database
     * @return a array of all routeId's
     */
    public int[] getRouteIdArray(){
    	Cursor c;
    	int[] routeIdArray; 
    	try{
	    	c = this.db.query(DB_TABLE_ROUTES, ROUTE_COLS, null, null, null, null, null);
	    	c.moveToFirst();
	    	routeIdArray = new int[c.getCount()];
	    	for(int i=0 ; i<c.getCount();i++){
	    		routeIdArray[i] = c.getInt(0);
	    		c.moveToNext();
	    	}
	    	return routeIdArray;
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    }
    	return null;    	
    }
    
    public void closeDatabase(){
    	this.db.close();
    }
    
    /*
    public Game getGame(final int gameId) {
        Cursor cGame = null;
        Cursor cCoordinate = null;
        Cursor cHint = null;
        Game game = new Game();
        try {
            cGame = this.db.query(true, GameDB.DB_TABLE_GAME, GameDB.GAME_COLS, "gameId = '" + gameId + "'", null, null, null, null,
                null);
            if (cGame.getCount() > 0) {
                cGame.moveToFirst();
                game.setGameId(cGame.getInt(0));
                game.setGameName(cGame.getString(1));
                game.setCurrentCoordinateId(cGame.getInt(2));
                game.setGameFinished(1 == cGame.getInt(3)); // the game is finished if gameFinished == 1
            }
            
            cCoordinate = this.db.query(true, GameDB.DB_TABLE_COORDINATE, GameDB.COORDINATE_COLS, "gameId = '" + gameId + "'", null, null, null, null,
                    null);
            int numRows = cCoordinate.getCount();
            cCoordinate.moveToFirst();
            for (int i = 0; i < numRows; ++i) {
                int coordinateId = cCoordinate.getInt(1);
                float latitude = cCoordinate.getFloat(2);
                float longitude = cCoordinate.getFloat(3);
                game.addCoordinate(gameId, coordinateId, latitude, longitude);
                cCoordinate.moveToNext();
            }     
            
            cHint = this.db.query(true, GameDB.DB_TABLE_HINT, GameDB.HINT_COLS, "gameId = '" + gameId + "'", null, null, null, null,
                    null);
            int numRows2 = cHint.getCount();
            cHint.moveToFirst();
            for (int i = 0; i < numRows2; ++i) {
                int coordinateId = cHint.getInt(1);
                int hintId = cHint.getInt(2);
                String hintText = cHint.getString(3);
                game.addHint(gameId, coordinateId, hintId, hintText);
                cHint.moveToNext();
            }  
 
        } catch (SQLException e) {
        	e.printStackTrace();
        } finally {
            if (cGame != null && !cGame.isClosed()) {
                cGame.close();
            }
            if (cCoordinate != null && !cCoordinate.isClosed()) {
                cCoordinate.close();
            }
            if (cHint != null && !cHint.isClosed()) {
                cHint.close();
            }
        }
        return game;
    }
   */
}
