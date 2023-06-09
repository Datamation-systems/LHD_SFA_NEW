package com.lankahardwared.lankahw.control;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.lankahardwared.lankahw.model.Result;

import java.util.List;
import java.util.Locale;

public class AddressAyncTask extends AsyncTask<Double, Integer, String> {

    Result result = null;
    double lat, lon;
    Context context;
    GPSTracker gps;
    String strAdd;

    DefaultTaskListener taskListener;

    public AddressAyncTask(Context context, DefaultTaskListener defaultTaskListener) {
        super();

        this.taskListener = defaultTaskListener;
        this.context = context;
        this.strAdd = "No Address";

        this.gps = new GPSTracker(context);
    }

    @Override
    protected String doInBackground(Double... doubles) {
        // Getting your doubles
        lat = gps.getLatitude();
        lon = gps.getLongitude();

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses != null && addresses.size() > 0) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(" ");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
            return "No Address";
        }

        return strAdd;
    }


    protected void onPostExecute(String result) {
        taskListener.onTaskCompleted(strAdd);
    }
}	