package com.pathway.pathway;

import android.database.Cursor;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johnny on 11/5/2017.
 * Edited by Eric West on 11/13/2017
 */

public class DeviceDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "device_DB";

    private static final String TABLE_ROUTES = "tbl_routes";
    private static final String TABLE_REPORTS = "tbl_reports";
    private static final String TABLE_USER = "tbl_user";
    private static final String TABLE_ACHIEVEMENTS = "tbl_achievements";

    //routes information
    private static final String KEY_ID = "id";
    private static final String KEY_JSON = "json_str";

    //user information
    private static final String KEY_TOT_DIST = "Total_Distance";
    private static final String KEY_TOT_TIME = "Total_Runtime";
    private static final String KEY_NUM_ROUT = "Number_Routes";
    private static final String KEY_NUM_RUNS = "Number_Runs";

    //achievements information

    public DeviceDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db1) {


    }

    /**
     * Generates the tables for the application's local side components
     */
    public void createTables(){
        //creates databases if they do not exist
        //
        SQLiteDatabase db = this.getWritableDatabase();
        //creates routes table
        String CREATE_ROUTES_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s " +
                "(%s INTEGER PRIMARY " + "KEY AUTOINCREMENT, " +
                "%s TEXT);", TABLE_ROUTES, KEY_ID, KEY_JSON);
        db.execSQL(CREATE_ROUTES_TABLE);

        //creates reports table
        String CREATE_report_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s " +
                "(%s INTEGER PRIMARY " + "KEY AUTOINCREMENT, " +
                "%s TEXT);", TABLE_REPORTS, KEY_ID, KEY_JSON);
        db.execSQL(CREATE_report_TABLE);

        //creates user table(holds profile reports)
        String create_tbl_user = String.format("CREATE TABLE IF NOT EXISTS %s " +
                "(%s INTEGER PRIMARY " + "KEY AUTOINCREMENT, " +
                "%s REAL" + // total distance
                "%s REAL" + // total runtime
                "%s REAL" + // # of routes
                "%s REAL" + // # of runs
                ");", TABLE_USER, KEY_ID, KEY_TOT_DIST, KEY_TOT_TIME, KEY_NUM_ROUT, KEY_NUM_RUNS);
        db.execSQL(create_tbl_user);

        //creates achievements table(holds boolean values for when a user qualifies for achievements)
        /*String create_tbl_achievements = String.format("CREATE TABLE IF NOT EXISTS %s " +
                "(%s INTEGER PRIMARY " + "KEY AUTOINCREMENT, " +
                "%s TEXT);", TABLE_ACHIEVEMENTS, KEY_ID, KEY_JSON);
        db.execSQL(create_tbl_achievements);
        */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_ROUTES));
        this.onCreate(db);
    }

    public long addRoute(Route path) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_JSON, path.toString());

        long id = db.insert(TABLE_ROUTES, null, values);

        db.close();
        return id;
    }

    public void delRoute(int id) {
        //may not need.
        //allows user to remove route from local db.
    }

    public String getRoute(int id) {
    //    SQLiteDatabase db = this.getReadableDatabase();
    //    create query request and return string
    //    Cursor cursor = db.query(TABLE_ROUTES, )
        return null;
    }

    public List<String> getUserRoutes() {
        //get and return all routes stored on device.
        //user specific, so no need for a parameter
        return null;
    }

}

