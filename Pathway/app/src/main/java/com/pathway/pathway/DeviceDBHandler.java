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
 */

public class DeviceDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "device_DB";
    private static final String TABLE_ROUTES = "tbl_routes";
    private static final String TABLE_RUNS = "tbl_runs";

    //routes information
    private static final String KEY_PID = "pid";
    private static final String KEY_RID = "rid";
    private static final String KEY_JSON = "json_str";

    public DeviceDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ROUTES_TABLE = String.format("CREATE TABLE %s " +
                "(%s INTEGER PRIMARY " + "KEY AUTOINCREMENT, " +
                "%s INTEGER, " +
                "%s TEXT);", KEY_PID, KEY_RID, KEY_JSON);
        db.execSQL(CREATE_ROUTES_TABLE);
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
        values.put(KEY_RID, path.getRID());

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

