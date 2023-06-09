package com.lankahardwared.lankahw.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lankahardwared.lankahw.model.Tour;
import com.lankahardwared.lankahw.model.mapper.TourMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TourDS {

    public static final String SETTINGS = "SETTINGS";
    public static SharedPreferences localSP;
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "TourDS";

    public TourDS(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public void InsertUpdateTourData(Tour tour) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FTOUR + " WHERE " + DatabaseHelper.FTOUR_DATE + " = '" + tour.getFTOUR_DATE() + "'";

            Cursor cursor = dB.rawQuery(selectQuery, null);

            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.FTOUR_REFNO, tour.getFTOUR_REFNO());
            values.put(DatabaseHelper.FTOUR_DATE, tour.getFTOUR_DATE());
            values.put(DatabaseHelper.FTOUR_F_KM, tour.getFTOUR_F_KM());
            values.put(DatabaseHelper.FTOUR_F_TIME, tour.getFTOUR_F_TIME());
            values.put(DatabaseHelper.FTOUR_ROUTE, tour.getFTOUR_ROUTE());
            values.put(DatabaseHelper.FTOUR_S_KM, tour.getFTOUR_S_KM());
            values.put(DatabaseHelper.FTOUR_S_TIME, tour.getFTOUR_S_TIME());
            values.put(DatabaseHelper.FTOUR_VEHICLE, tour.getFTOUR_VEHICLE());
            values.put(DatabaseHelper.FTOUR_DISTANCE, tour.getFTOUR_DISTANCE());
            values.put(DatabaseHelper.FTOUR_IS_SYNCED, tour.getFTOUR_IS_SYNCED());
            values.put(DatabaseHelper.FTOUR_REPCODE, tour.getFTOUR_REPCODE());
            values.put(DatabaseHelper.FTOUR_MAC, tour.getFTOUR_MAC());
            values.put(DatabaseHelper.FTOUR_DRIVER, tour.getFTOUR_DRIVER());
            values.put(DatabaseHelper.FTOUR_ASSIST, tour.getFTOUR_ASSIST());
//            values.put(DatabaseHelper.FTOUR_LOCCODE, tour.getFTOUR_LOC_CODE());
//            values.put(DatabaseHelper.FTOUR_TERRITORY, tour.getFTOUR_TERRITORY());
//            values.put(DatabaseHelper.FTOUR_COST_CENTER, tour.getFTOUR_COST_CENTER());


            if (cursor.getCount() > 0) {
                dB.update(DatabaseHelper.TABLE_FTOUR, values, DatabaseHelper.FTOUR_DATE + " =?", new String[]{String.valueOf(tour.getFTOUR_DATE())});
            } else {
                dB.insert(DatabaseHelper.TABLE_FTOUR, null, values);
            }

            cursor.close();

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public Tour getIncompleteRecord() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FTOUR + " WHERE " + DatabaseHelper.FTOUR_F_TIME + " IS NULL AND " + DatabaseHelper.FTOUR_F_KM + " IS NULL AND " + DatabaseHelper.FTOUR_DATE + " IS NOT NULL";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        Tour tour = new Tour();
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    tour.setFTOUR_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_DATE)));
                    tour.setFTOUR_F_KM(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_F_KM)));
                    tour.setFTOUR_F_TIME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_F_TIME)));
                    tour.setFTOUR_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_ID)));
                    tour.setFTOUR_ROUTE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_ROUTE)));
                    tour.setFTOUR_S_KM(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_S_KM)));
                    tour.setFTOUR_S_TIME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_S_TIME)));
                    tour.setFTOUR_VEHICLE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_VEHICLE)));
                    tour.setFTOUR_DISTANCE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_DISTANCE)));
                    tour.setFTOUR_DRIVER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_DRIVER)));
                    tour.setFTOUR_ASSIST(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_ASSIST)));
//                    tour.setFTOUR_REPCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_REPCODE)));
//                    tour.setFTOUR_LOC_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_LOCCODE)));
//                    tour.setFTOUR_TERRITORY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_TERRITORY)));
//                    tour.setFTOUR_COST_CENTER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_COST_CENTER)));
                }

                return tour;
            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }
        return null;
    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public boolean hasTodayRecord() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_FTOUR + " WHERE " + DatabaseHelper.FTOUR_F_TIME + " IS NOT NULL AND " + DatabaseHelper.FTOUR_F_KM + " IS NOT NULL AND " + DatabaseHelper.FTOUR_DATE + "='" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "' AND " + DatabaseHelper.FTOUR_ROUTE + " IS NOT NULL AND " + DatabaseHelper.FTOUR_S_KM + " IS NOT NULL AND " + DatabaseHelper.FTOUR_S_TIME + " IS NOT NULL AND " + DatabaseHelper.FTOUR_VEHICLE + " IS NOT NULL";

            Cursor cursor = dB.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0)
                return true;

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }

        return false;

    }

	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public int updateIsSynced(TourMapper mapper) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {
            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.FTOUR_IS_SYNCED, "1");

            if (mapper.isSynced()) {
                count = dB.update(DatabaseHelper.TABLE_FTOUR, values, DatabaseHelper.FTOUR_DATE + " =?", new String[]{String.valueOf(mapper.getFTOUR_DATE())});
            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return count;

    }
	
	/*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*/

    public ArrayList<TourMapper> getUnsyncedTourData() {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<TourMapper> list = new ArrayList<TourMapper>();
        //localSP = context.getSharedPreferences(SETTINGS, Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
        localSP = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE + Context.MODE_PRIVATE);


        try {
            String s = "SELECT * FROM " + DatabaseHelper.TABLE_FTOUR + " WHERE " + DatabaseHelper.FTOUR_IS_SYNCED + "='0'";

            Cursor cursor = dB.rawQuery(s, null);

            while (cursor.moveToNext()) {

                TourMapper mapper = new TourMapper();
                mapper.setConsoleDB(localSP.getString("Console_DB", "").toString());
                mapper.setFTOUR_DATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_DATE)));
                mapper.setFTOUR_F_KM(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_F_KM)));
                mapper.setFTOUR_F_TIME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_F_TIME)));
                mapper.setFTOUR_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_ID)));
                mapper.setFTOUR_ROUTE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_ROUTE)));
                mapper.setFTOUR_S_KM(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_S_KM)));
                mapper.setFTOUR_S_TIME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_S_TIME)));
                mapper.setFTOUR_VEHICLE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_VEHICLE)));
                mapper.setFTOUR_DISTANCE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_DISTANCE)));
                mapper.setFTOUR_IS_SYNCED(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_IS_SYNCED)));
                mapper.setFTOUR_REPCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_REPCODE)));
                mapper.setFTOUR_MAC(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_MAC)));
                mapper.setfTOUR_DRIVER(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_DRIVER)));
                mapper.setFTOUR_ASSIST(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOUR_ASSIST)));

                list.add(mapper);

            }

        } catch (Exception e) {

            Log.v(TAG + " Exception", e.toString());

        } finally {
            dB.close();
        }
        return list;

    }

}


