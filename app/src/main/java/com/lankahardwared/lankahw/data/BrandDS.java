package com.lankahardwared.lankahw.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lankahardwared.lankahw.model.Brand;

import java.util.ArrayList;

public class BrandDS {
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "swadeshi";


    public BrandDS(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    @SuppressWarnings("static-access")
    public int createOrUpdateBrand(ArrayList<Brand> list) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            for (Brand brand : list) {

                ContentValues values = new ContentValues();

                values.put(dbHelper.FBRAND_ADD_MACH, brand.getFBRAND_ADD_MACH());
                values.put(dbHelper.FBRAND_ADD_USER, brand.getFBRAND_ADD_USER());
                values.put(dbHelper.FBRAND_CODE, brand.getFBRAND_CODE());
                values.put(dbHelper.FBRAND_NAME, brand.getFBRAND_NAME());
                values.put(dbHelper.FBRAND_RECORDID, brand.getFBRAND_RECORDID());

                count = (int) dB.insert(dbHelper.TABLE_FBRAND, null, values);

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


    @SuppressWarnings("static-access")
    public int deleteAll() {
        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        try {

            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FBRAND, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(dbHelper.TABLE_FBRAND, null, null);
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
