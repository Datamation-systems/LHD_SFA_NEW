package com.lankahardwared.lankahw.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lankahardwared.lankahw.model.Town;

import java.util.ArrayList;

public class TownDS {

    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "swadeshi";


    public TownDS(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }


    @SuppressWarnings("static-access")
    public int createOrUpdateTown(ArrayList<Town> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (Town town : list) {

                ContentValues values = new ContentValues();

                values.put(dbHelper.FTOWN_RECORDID, town.getFTOWN_RECORDID());
                values.put(dbHelper.FTOWN_CODE, town.getFTOWN_CODE());
                values.put(dbHelper.FTOWN_NAME, town.getFTOWN_NAME());
                values.put(dbHelper.FTOWN_DISTR_CODE, town.getFTOWN_DISTR_CODE());
                values.put(dbHelper.FTOWN_ADDDATE, town.getFTOWN_ADDDATE());
                values.put(dbHelper.FTOWN_ADD_MACH, town.getFTOWN_ADD_MACH());
                values.put(dbHelper.FTOWN_ADD_USER, town.getFTOWN_ADD_USER());

                count = (int) dB.insert(dbHelper.TABLE_FTOWN, null, values);
            }
        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return count;

    }

//----------------------------get Town detalis by district code---------------------------------------
    public ArrayList<Town> getTown(String DistricCode) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<Town> list = new ArrayList<Town>();

        String selectQuery = "select * from fTown where " + DatabaseHelper.FTOWN_DISTR_CODE + " = '" + DistricCode + "'";
        Cursor cursor=null;
        try {
            cursor = dB.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                Town town = new Town();
                town.setFTOWN_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOWN_CODE)));
                town.setFTOWN_NAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOWN_NAME)));

                list.add(town);

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }
    public String getCode(String Name)  {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        String Code = null;
        Cursor cursor = null;
        try {
            String selectQuery = "select * from fTown where TownName = '" + Name.trim() + "'";
            cursor = dB.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                Code = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOWN_CODE));
            }
        } catch (Exception e) {
           e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return Code;
    }

    public String getName(String code)  {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        String Code = null;
        Cursor cursor = null;
        try {
            String selectQuery = "select * from fTown where TownCode = '" + code.trim() + "'";
            cursor = dB.rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                Code = cursor.getString(cursor.getColumnIndex(DatabaseHelper.FTOWN_NAME));
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return Code;
    }



    public int deleteAll() {
        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {
            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FTOWN, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(dbHelper.TABLE_FTOWN, null, null);
                Log.v("Success", success + "");
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


}
