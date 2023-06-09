package com.lankahardwared.lankahw.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


import com.lankahardwared.lankahw.R;
import com.lankahardwared.lankahw.model.NewCustomer;

import java.util.ArrayList;

/**
 * Created by Dhanushika on 4/5/2018.
 */

public class NewCustomerDS {
    Context context;
    private SQLiteDatabase dB;
    private DatabaseHelper dbHelper;
    private String TAG = "Finac New";
    public static final String SETTINGS = "SETTINGS";
    public static SharedPreferences localSP;

    public NewCustomerDS(Context context) {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        dB = dbHelper.getWritableDatabase();
    }

    public int createOrUpdateCustomer(ArrayList<NewCustomer> list)  {

        int serverdbID = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = null;
        try {

            for (NewCustomer fN : list) {

                ContentValues contentValues = new ContentValues();
                String selectQuery = "SELECT * FROM " + dbHelper.TABLE_NEW_CUSTOMER + " WHERE " + dbHelper.CUSTOMER_ID + " = '" + fN.getCUSTOMER_ID() + "'";
                cursor = dB.rawQuery(selectQuery, null);
                contentValues.put(dbHelper.CUSTOMER_ID, fN.getCUSTOMER_ID());
                contentValues.put(dbHelper.CUSTOMER_OTHER_CODE, fN.getC_OTHERCODE());
                contentValues.put(dbHelper.COMPANY_REG_CODE, fN.getC_REGNUM());
                contentValues.put(dbHelper.NAME, fN.getNAME());
                contentValues.put(dbHelper.NIC, fN.getCUSTOMER_NIC());
                contentValues.put(dbHelper.ADDRESS1, fN.getADDRESS1());
                contentValues.put(dbHelper.ADDRESS2, fN.getADDRESS2());
                contentValues.put(dbHelper.CITY, fN.getCITY());
                contentValues.put(dbHelper.PHONE, fN.getPHONE());
                contentValues.put(dbHelper.MOBILE, fN.getMOBILE());
                contentValues.put(dbHelper.FAX, fN.getFAX());
                contentValues.put(dbHelper.E_MAIL, fN.getE_MAIL());
                contentValues.put(dbHelper.C_TOWN, fN.getC_TOWN());
                contentValues.put(dbHelper.ROUTE_ID, fN.getROUTE_ID());
                contentValues.put(dbHelper.DISTRICT, fN.getDISTRICT());
                contentValues.put(dbHelper.OLD_CODE, fN.getOLD_CODE());
                contentValues.put(dbHelper.C_IMAGE, fN.getC_IMAGE());
                contentValues.put(dbHelper.C_IMAGE1, fN.getC_IMAGE1());
                contentValues.put(dbHelper.C_IMAGE2, fN.getC_IMAGE2());
                contentValues.put(dbHelper.C_IMAGE3, fN.getC_IMAGE3());
                contentValues.put(dbHelper.C_LONGITUDE, fN.getC_LONGITUDE());
                contentValues.put(dbHelper.C_LATITUDE, fN.getC_LATITUDE());
                contentValues.put(dbHelper.C_ADD_DATE, fN.getC_ADDDATE());
                contentValues.put(dbHelper.C_ADD_MACH, fN.getAddMac());
                contentValues.put(dbHelper.C_IS_SYNCED, fN.getC_SYNCSTATE());

                int cn = cursor.getCount();
                if (cn > 0) {
                    serverdbID = dB.update(dbHelper.TABLE_NEW_CUSTOMER, contentValues, dbHelper.CUSTOMER_ID + " = '" + fN.getCUSTOMER_ID() + "'", null);
                } else {
                    serverdbID = (int) dB.insert(dbHelper.TABLE_NEW_CUSTOMER, null, contentValues);
                }

            }
        } catch (Exception e) {

           e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            dB.close();
        }
        return serverdbID;


    }
//----------------------------------------------------------------------------------------------------------------

    public ArrayList<NewCustomer> getAllNewCusDetails(String newTExt, String uploaded) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<NewCustomer> list = new ArrayList<NewCustomer>();
        String selectQuery;
        if (uploaded.equals("U"))
            selectQuery= "select * from " + DatabaseHelper.TABLE_NEW_CUSTOMER + " WHERE isSynced='1' and  Name LIKE '%" + newTExt + "%'";
        else
       selectQuery = "select * from " + DatabaseHelper.TABLE_NEW_CUSTOMER + " WHERE  isSynced='0' and Name LIKE '%" + newTExt + "%'";

        Cursor cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            NewCustomer fCustomer = new NewCustomer();
            fCustomer.setCUSTOMER_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CUSTOMER_ID)));
            fCustomer.setC_ADDDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.C_ADD_DATE)));
            fCustomer.setNAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME)));
            fCustomer.setC_SYNCSTATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.C_IS_SYNCED)));
            list.add(fCustomer);
        }

        return list;
    }

//---------------------get all Customer for edit------------------------------------------------------------------------

    public ArrayList<NewCustomer> getAllNewCusDetailsForEdit(String newTExt) {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        ArrayList<NewCustomer> list = new ArrayList<NewCustomer>();
        String selectQuery = "select * from " + DatabaseHelper.TABLE_NEW_CUSTOMER + " WHERE Name LIKE '%" + newTExt + "%' and isSynced=0";
        Cursor cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            NewCustomer fCustomer = new NewCustomer();
            fCustomer.setCUSTOMER_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CUSTOMER_ID)));
            fCustomer.setC_OTHERCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CUSTOMER_OTHER_CODE)));
            fCustomer.setC_REGNUM(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COMPANY_REG_CODE)));
            fCustomer.setNAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME)));
            fCustomer.setCUSTOMER_NIC(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NIC)));
            fCustomer.setADDRESS1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ADDRESS1)));
            fCustomer.setADDRESS2(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ADDRESS2)));
            fCustomer.setCITY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CITY)));
            fCustomer.setPHONE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PHONE)));
            fCustomer.setMOBILE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MOBILE)));
            fCustomer.setFAX(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FAX)));
            fCustomer.setE_MAIL(cursor.getString(cursor.getColumnIndex(DatabaseHelper.E_MAIL)));
            fCustomer.setC_TOWN(cursor.getString(cursor.getColumnIndex(DatabaseHelper.C_TOWN)));
            fCustomer.setROUTE_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ROUTE_ID)));
            fCustomer.setDISTRICT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DISTRICT)));
            fCustomer.setC_SYNCSTATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.C_IS_SYNCED)));
            fCustomer.setC_IMAGE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.C_IMAGE)));
            fCustomer.setC_IMAGE1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.C_IMAGE1)));
            fCustomer.setC_IMAGE2(cursor.getString(cursor.getColumnIndex(DatabaseHelper.C_IMAGE2)));
            fCustomer.setC_IMAGE3(cursor.getString(cursor.getColumnIndex(DatabaseHelper.C_IMAGE3)));

            list.add(fCustomer);
        }

        return list;
    }
    public int updateIsSynced(String customerID, String res) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.C_IS_SYNCED, "1");

            if (res.equalsIgnoreCase("1")) {
                count = dB.update(DatabaseHelper.TABLE_NEW_CUSTOMER, values, DatabaseHelper.CUSTOMER_ID + " =?", new String[]{String.valueOf(customerID)});
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

    public int updateIsSynced(NewCustomer mapper) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.C_IS_SYNCED, "1");
            if (mapper.isIS_SYNCED()) {
                count = dB.update(DatabaseHelper.TABLE_NEW_CUSTOMER, values, DatabaseHelper.CUSTOMER_ID + " =?", new String[]{String.valueOf(mapper.getCUSTOMER_ID())});
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


    public boolean isEntrySynced(String Refno) {

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }

        Cursor cursor = dB.rawQuery("select isSynced from NewCustomer where customerID ='" + Refno + "'", null);

        while (cursor.moveToNext()) {

            String result = cursor.getString(cursor.getColumnIndex(DatabaseHelper.C_IS_SYNCED));

            if (result.equals("1"))
                return true;

        }
        cursor.close();
        dB.close();
        return false;

    }
    public int deleteRecord(String refno) {

        int count = 0;

        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        Cursor cursor = null;

        try {

            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NEW_CUSTOMER + " WHERE " + DatabaseHelper.CUSTOMER_ID + " = '" + refno + "'";
            cursor = dB.rawQuery(selectQuery, null);
            int cn = cursor.getCount();

            if (cn > 0) {
                count = dB.delete(DatabaseHelper.TABLE_NEW_CUSTOMER, DatabaseHelper.CUSTOMER_ID + " ='" + refno + "'", null);

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


    public ArrayList<NewCustomer>  getUnsyncRecord() {
        if (dB == null) {
            open();
        } else if (!dB.isOpen()) {
            open();
        }
        CompanyBranchDS branchDS = new CompanyBranchDS(context);
        SalRepDS fSalRe = new SalRepDS(context);
        ArrayList<NewCustomer> list = new ArrayList<NewCustomer>();
        String selectQuery = "select * from " + DatabaseHelper.TABLE_NEW_CUSTOMER + " WHERE  isSynced='0'";
        localSP = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE + Context.MODE_PRIVATE);
        Cursor cursor = dB.rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            NewCustomer fCustomer = new NewCustomer();
            fCustomer.setCUSTOMER_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CUSTOMER_ID)));
            fCustomer.setC_ADDDATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.C_ADD_DATE)));
            fCustomer.setNAME(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME)));
            fCustomer.setC_SYNCSTATE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.C_IS_SYNCED)));
            fCustomer.setnNumVal(branchDS.getCurrentNextNumVal(context.getResources().getString(R.string.newCusVal)));
            fCustomer.setCONSOLE_DB(localSP.getString("Console_DB", "").toString());
            fCustomer.setC_REPCODE(fSalRe.getCurrentRepCode());
            fCustomer.setC_OTHERCODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CUSTOMER_OTHER_CODE)));
            fCustomer.setC_REGNUM(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COMPANY_REG_CODE)));
            fCustomer.setCUSTOMER_NIC(cursor.getString(cursor.getColumnIndex(DatabaseHelper.NIC)));
            fCustomer.setFAX(cursor.getString(cursor.getColumnIndex(DatabaseHelper.FAX)));
            fCustomer.setC_TOWN(cursor.getString(cursor.getColumnIndex(DatabaseHelper.C_TOWN)));
            fCustomer.setROUTE_ID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ROUTE_ID)));
            fCustomer.setDISTRICT(cursor.getString(cursor.getColumnIndex(DatabaseHelper.DISTRICT)));
            fCustomer.setADDRESS1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ADDRESS1)));
            fCustomer.setADDRESS2(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ADDRESS2)));
            fCustomer.setCITY(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CITY)));
            fCustomer.setMOBILE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MOBILE)));
            fCustomer.setE_MAIL(cursor.getString(cursor.getColumnIndex(DatabaseHelper.E_MAIL)));
            fCustomer.setC_IMAGE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.C_IMAGE)));
            fCustomer.setC_IMAGE1(cursor.getString(cursor.getColumnIndex(DatabaseHelper.C_IMAGE1)));
            fCustomer.setC_IMAGE2(cursor.getString(cursor.getColumnIndex(DatabaseHelper.C_IMAGE2)));
            fCustomer.setC_IMAGE3(cursor.getString(cursor.getColumnIndex(DatabaseHelper.C_IMAGE3)));
            fCustomer.setC_LATITUDE("0.000");
            fCustomer.setC_LONGITUDE("0.000");
            fCustomer.setAddMac("0");
            fCustomer.setPHONE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PHONE)));
            fCustomer.setOLD_CODE(cursor.getString(cursor.getColumnIndex(DatabaseHelper.OLD_CODE)));


            list.add(fCustomer);
        }
        return  list;
    }
}
