package com.pathway.pathway;

import android.database.Cursor;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Path;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.support.customtabs.CustomTabsIntent.KEY_ID;

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

    //routes and report key information
    private static final String KEY_ID = "id";
    private static final String KEY_PID = "pid";
    private static final String KEY_RID = "rid";
    private static final String KEY_JSON = "json_str";

    //user information
    private static final String KEY_TOT_DIST = "totalDistance";
    private static final String KEY_TOT_TIME = "totalTime";
    private static final String KEY_NUM_ROUT = "numRoutes";
    private static final String KEY_NUM_RUNS = "numRuns";

    //achievements information
    private static final String KEY_ACH_SET = "isSet";
    private static final String KEY_ACH_NAME = "Name";

    public DeviceDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db1) {


    }

    /**
     * Generates the tables for the application's local side components
     */
    public void createTables(){
        //creates databases if they do not exist

        SQLiteDatabase db = this.getWritableDatabase();
        //creates routes table
        String CREATE_ROUTES_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s " +
                "(%s INTEGER PRIMARY " + "KEY AUTOINCREMENT, " +
                "%s INTEGER," +
                "%s INTEGER," +
                "%s TEXT);", TABLE_ROUTES, KEY_ID, KEY_PID, KEY_RID, KEY_JSON);

        db.execSQL(CREATE_ROUTES_TABLE);

        //creates reports table
        String CREATE_report_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s " +
                "(%s INTEGER PRIMARY " + "KEY AUTOINCREMENT, " +
                "%s INTEGER," +
                "%s INTEGER," +
                " %s TEXT" +
                ");", TABLE_REPORTS, KEY_ID, KEY_PID, KEY_RID,  KEY_JSON);
        db.execSQL(CREATE_report_TABLE);

        //creates user table(holds profile reports)
        String create_tbl_user = String.format("CREATE TABLE IF NOT EXISTS %s " +
                "(%s INTEGER PRIMARY " + "KEY AUTOINCREMENT, " +
                "%s REAL," + // total distance
                "%s REAL," + // total runtime
                "%s REAL," + // # of routes
                "%s REAL," + // # of runs
                "%s TEXT" +
                ");", TABLE_USER, KEY_ID, KEY_TOT_DIST, KEY_TOT_TIME, KEY_NUM_ROUT, KEY_NUM_RUNS, KEY_JSON);
        db.execSQL(create_tbl_user);

        //creates achievements table(holds boolean values for when a user qualifies for achievements)
        String create_tbl_achievements = String.format("CREATE TABLE IF NOT EXISTS %s " +
                "(%s INTEGER PRIMARY " + "KEY AUTOINCREMENT, " +
                "%s TEXT," +
                "%s INTEGER" +
                ");", TABLE_ACHIEVEMENTS, KEY_ID, KEY_ACH_NAME, KEY_ACH_SET);
        db.execSQL(create_tbl_achievements);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_ROUTES));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_ACHIEVEMENTS));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_REPORTS));
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_USER));
        this.createTables();
    }

    /**
     * Adds a New Route to the Table
     * Will set the pid to highestpid+1 and rid to 0
     * This route will be the parent of the route.
     * @param path
     * @return
     */
    public long addNewRoute(Route path) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //get count of parent routes to set new pid
        String selectQuery = String.format("SELECT %s FROM %s WHERE %s = %d", KEY_PID, TABLE_ROUTES, KEY_RID, 0);
        Cursor cursor = db.rawQuery(selectQuery, null);
        int pid = cursor.getCount();

        values.put(KEY_JSON, path.toString());
        values.put(KEY_PID, pid);
        values.put(KEY_RID, 0);

        long id = db.insert(TABLE_ROUTES, null, values);

        BasicReport r = PathwayStats.generateBasicReport(path);
        addBasicReport(r);
        db.close();
        return id;
    }

    /**
     * Adds a new Run of a currently existing route to the local Database.
     * **Requirements: path variable needs to have the pid set to some value(int)
     * @param path
     * @return
     */
    public long addRun(Route path) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //get count of parent routes to set new pid
        String selectQuery = String.format("SELECT %s FROM %s WHERE %s = %d", KEY_PID, TABLE_ROUTES, KEY_PID, path.getPID());
        Cursor cursor = db.rawQuery(selectQuery, null);
        int rid = cursor.getCount();

        values.put(KEY_JSON, path.toString());
        values.put(KEY_PID, path.getPID());
        values.put(KEY_RID, rid);

        long id = db.insert(TABLE_ROUTES, null, values);

        // Create report of run path
        BasicReport report = PathwayStats.generateBasicReport(path);

        this.addBasicReport(report);

        db.close();
        return id;
    }


    public String getRoute(int id) {
        String selectQuery = String.format("SELECT %s FROM %s WHERE %s = %d", KEY_JSON, TABLE_ROUTES, KEY_ID, id);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return (cursor.moveToFirst() == true) ? cursor.getString(0) : null;
    }


    public List<String> getParentRoutes(){
        List<String> routeList = new ArrayList<String>();
        // Select All Query
        String selectQuery = String.format("SELECT %s FROM %s where rid=0", KEY_JSON, TABLE_ROUTES);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                routeList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return route list
        return routeList;
    }

    /**
     * Returns all Routes in the Database as a List of Strings.
     * If there are none in the Database it will return an empty List
     * @return
     */
    public List<String> getUserRoutes() {
        List<String> routeList = new ArrayList<String>();
        // Select All Query
        String selectQuery = String.format("SELECT %s FROM %s", KEY_JSON, TABLE_ROUTES);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                routeList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return route list
        return routeList;
    }

    /**
     * Gets all of the Users reports from the User Table and returns them as a list of Strings
     * If there are non in the database it will return an empty list
     * @return
     */
    public List<String> getUserReports(){
        List<String> reportList = new ArrayList<String>();
        // Select All Query
        String selectQuery = String.format("SELECT %s FROM %s", "*", TABLE_USER);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = new JSONObject();
                for(int idx = 0; idx < cursor.getColumnCount(); idx++) {
                    try {
                        jsonObject.put(cursor.getColumnName(idx), cursor.getDouble(idx));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                reportList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return route list
        return reportList;
    }

    /**
     * Adds an achievement to the database and sets its value.
     *
     * The Achievement parameter is the name of the achievement
     * The set parameter will represent a boolean: 0 or less is off, 1 or more is on
     *
     * @param Achievement
     * @param set
     * @return
     */
    public boolean addAchievement(String Achievement, int set){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_ACH_NAME,Achievement);
        values.put(KEY_ACH_SET, set);
        return false;
    }

    public List<String> getAchievements(){
        List<String> reportList = new ArrayList<String>();
        // Select All Query
        String selectQuery = String.format("SELECT %s FROM %s", "*", TABLE_ACHIEVEMENTS);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = new JSONObject();
                for(int idx = 0; idx < cursor.getColumnCount(); idx++) {
                    try {
                        jsonObject.put(cursor.getColumnName(idx), cursor.getDouble(idx));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                reportList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return route list
        return reportList;
    }

    /**
     * Retrieves the latest user report from the local database as a JSON string.
     *
     * **The JSON string can be passed to the UserReport through
     * its constructor to allow easier access to the contained fields
     * @return
     */
    public String getLatestUserReport(){
        String query = String.format("SELECT TOP 1 %s FROM %s ORDER BY %s DESC", KEY_JSON, TABLE_USER, KEY_ID );
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        return null;
    }

    public boolean addUserReport(UserReport r){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_JSON, r.toString());
        values.put(KEY_TOT_DIST, r.getTotalDistance());
        values.put(KEY_TOT_TIME, r.getTotalTime());
        values.put(KEY_NUM_ROUT, r.getNumRoutes());
        values.put(KEY_NUM_RUNS, r.getNumRuns());

        long id = db.insert(TABLE_USER, null, values);

        db.close();
        return (id >= 0);
    }

    public boolean addBasicReport(BasicReport r){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_JSON, r.toString());
        values.put(KEY_PID, r.getPid());
        values.put(KEY_RID, r.getRid());
        long id = db.insert(TABLE_USER, null, values);

        db.close();
        return (id >= 0);
    }

    public List<String> getRouteReports(){
        List<String> reportList = new ArrayList<String>();
        // Select All Query
        String selectQuery = String.format("SELECT %s FROM %s", KEY_JSON, TABLE_REPORTS);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                reportList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return route list
        return reportList;
    }

    public String getLastRoute(){
        String query = String.format("SELECT TOP 1 %s FROM %s ORDER BY %s DESC", KEY_JSON, TABLE_ROUTES, KEY_ID );
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }
        return null;
    }

}

