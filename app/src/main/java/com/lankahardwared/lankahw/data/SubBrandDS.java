package com.lankahardwared.lankahw.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lankahardwared.lankahw.model.SubBrand;

import java.util.ArrayList;

public class SubBrandDS {
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "swadeshi";


    public SubBrandDS(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    @SuppressWarnings("static-access")
    public int createOrUpdateSubBrand(ArrayList<SubBrand> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (SubBrand subBrand : list) {

                ContentValues values = new ContentValues();

                values.put(dbHelper.FSUBBRAND_ADD_DATE, subBrand.getFSUBBRAND_ADD_DATE());
                values.put(dbHelper.FSUBBRAND_ADD_MACH, subBrand.getFSUBBRAND_ADD_MACH());
                values.put(dbHelper.FSUBBRAND_ADD_USER, subBrand.getFSUBBRAND_ADD_USER());
                values.put(dbHelper.FSUBBRAND_RECORDID, subBrand.getFSUBBRAND_RECORDID());
                values.put(dbHelper.FSUBBRAND_CODE, subBrand.getFSUBBRAND_CODE());
                values.put(dbHelper.FSUBBRAND_NAME, subBrand.getFSUBBRAND_NAME());

                count = (int) dB.insert(dbHelper.TABLE_FSUBBRAND, null, values);

            }
        } catch (Exception e) {

            Log.v("Exception", e.toString());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return count;

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

            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FSUBBRAND, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(dbHelper.TABLE_FSUBBRAND, null, null);
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
