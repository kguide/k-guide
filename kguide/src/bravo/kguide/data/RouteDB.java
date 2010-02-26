package bravo.kguide.data;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RouteDB {

    public static final String DB_NAME = "kGuideDB";
    public static final String DB_TABLE_ROUTES = "routesTable";
    public static final String DB_TABLE_ROUTE_TYPES = "routeTypes";
    public static final int DB_VERSION = 3;

    private static final String[] ROUTE_COLS = new String[] { "routeId", "jsonString", "contentOnPhone"};
    private static final String[] ROUTE_TYPES_COLS = new String[] {"typeId","description"};
    
    private SQLiteDatabase db;
    private final DBhelper dbOpenHelper;

    private static class DBhelper extends SQLiteOpenHelper {

        private static final String DB_CREATE_ROUTES = "CREATE TABLE "
            + RouteDB.DB_TABLE_ROUTES
            + " (routeId INTEGER PRIMARY KEY, jsonString TEXT UNIQUE NOT NULL, contentOnPhone INTEGER);";

        private static final String DB_CREATE_ROUTE_TYPES = "CREATE TABLE "
            + RouteDB.DB_TABLE_ROUTE_TYPES
            + " (typeId INTEGER PRIMARY KEY, description TEXT UNIQUE NOT NULL);";
        
        
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
                db.execSQL(DBhelper.DB_CREATE_ROUTES);
                db.execSQL(DBhelper.DB_CREATE_ROUTE_TYPES);
            } catch (SQLException e) {
//                Log.e(Constants.LOGTAG, DBHelper.CLASSNAME, e);
            }
        }

        @Override
        public void onOpen(final SQLiteDatabase db) {
            super.onOpen(db);
        }

        @Override
        public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + RouteDB.DB_TABLE_ROUTES);
            db.execSQL("DROP TABLE IF EXISTS " + RouteDB.DB_TABLE_ROUTE_TYPES);
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
     * 
     * @param route
     * @param routeId
     * @param jsonRoute
     * @param contentOnPhone
     */
    public void insertRoute(final int routeId, final String jsonRoute, final boolean contentOnPhone) {
        ContentValues values = new ContentValues();
        values.put("routeId", routeId);
        values.put("jsonString", jsonRoute);
        values.put("contentOnPhone", contentOnPhone);
        this.db.insert(RouteDB.DB_TABLE_ROUTES, null, values);
    }

    /**
     * Retrive the JSON representation of a route
     * @param routeId : The id of the route to be retrieved
     * @return JSON string containing information about the route
     */
    public String getRouteJSON(int routeId){
    	Cursor c = null;
    	String routeJSON = null;
    	try {
    		c = this.db.query(true, RouteDB.DB_TABLE_ROUTES, RouteDB.ROUTE_COLS, "routeId="+routeId, null, null, null, null, null);
    		c.moveToFirst();
    		Log.v("database", "getting json string");
    		routeJSON = c.getString(1);
    		Log.v("database", routeJSON);
    	}
    	catch (SQLException e){
          Log.v("SQLException", e.getMessage());
    	}
	    finally {
	        if (c != null && !c.isClosed()) {
	            c.close();
	        }
	    }
    	return routeJSON;
    }	
    
    public void deleteRoute(final int routeId) {
    	try{
    		this.db.delete(RouteDB.DB_TABLE_ROUTES, "routeId=" + routeId, null);
    	}catch (SQLException e) {
    		e.printStackTrace();
    	}
    }
    
    public boolean existsRoute(final int routeId) {
    	Log.v("database", "In existsRoute");
        Cursor c = null;
        try {
        	Log.v("database", "initializing cursor");
            c = this.db.query(true, RouteDB.DB_TABLE_ROUTES, RouteDB.ROUTE_COLS, "routeId = '" + routeId + "'", null, null, null, null,
                null);
            Log.v("database", "after initializing cursor");
            if (c.getCount() > 0) {
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
