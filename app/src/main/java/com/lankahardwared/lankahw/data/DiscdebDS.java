package com.lankahardwared.lankahw.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lankahardwared.lankahw.model.Discdeb;

import java.util.ArrayList;

public class DiscdebDS {
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "swadeshi";

    public DiscdebDS(Context context) {

        this.context = context;
        dbHelper = new DatabaseHelper(context);

    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    @SuppressWarnings("static-access")
    public int createOrUpdateDiscdeb(ArrayList<Discdeb> list) {
        int count = 0;
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;
        Cursor cursor_ini = null;

        try {

            cursor_ini = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FDISCDEB, null);

            for (Discdeb discdeb : list) {

                ContentValues values = new ContentValues();

                values.put(dbHelper.FDISCDEB_REF_NO, discdeb.getFDISCDEB_REF_NO());
                values.put(dbHelper.FDISCDEB_DEB_CODE, discdeb.getFDISCDEB_DEB_CODE());
                values.put(dbHelper.FDISCDEB_RECORD_ID, discdeb.getFDISCDEB_RECORD_ID());
                values.put(dbHelper.FDISCDEB_TIEMSTAMP_COLUMN, discdeb.getFDISCDEB_TIEMSTAMP_COLUMN());

                if (cursor_ini.getCount() > 0) {
                    String selectQuery = "SELECT * FROM " + dbHelper.TABLE_FDISCDEB + " WHERE " + dbHelper.FDISCDEB_REF_NO + "='" + discdeb.getFDISCDEB_REF_NO() + "' AND " + dbHelper.FDISCDEB_DEB_CODE + " = '" + discdeb.getFDISCDEB_DEB_CODE() + "'";
                    cursor = dB.rawQuery(selectQuery, null);

                    if (cursor.getCount() > 0) {
                        count = (int) dB.update(dbHelper.TABLE_FDISCDEB, values, dbHelper.FDISCDEB_REF_NO + "='" + discdeb.getFDISCDEB_REF_NO() + "' AND " + dbHelper.FDISCDEB_DEB_CODE + " = '" + discdeb.getFDISCDEB_DEB_CODE() + "'", null);
                    } else {
                        count = (int) dB.insert(dbHelper.TABLE_FDISCDEB, null, values);
                    }

                } else {
                    count = (int) dB.insert(dbHelper.TABLE_FDISCDEB, null, values);
                }

            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (cursor_ini != null) {
                cursor_ini.close();
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

            cursor = dB.rawQuery("SELECT * FROM " + dbHelper.TABLE_FDISCDEB, null);
            count = cursor.getCount();
            if (count > 0) {
                int success = dB.delete(dbHelper.TABLE_FDISCDEB, null, null);
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
