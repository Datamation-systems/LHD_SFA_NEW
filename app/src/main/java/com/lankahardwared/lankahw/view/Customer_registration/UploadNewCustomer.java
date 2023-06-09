package com.lankahardwared.lankahw.view.Customer_registration;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.lankahardwared.lankahw.control.TaskType;
import com.lankahardwared.lankahw.data.NewCustomerDS;
import com.lankahardwared.lankahw.data.SharedPreferencesClass;
import com.lankahardwared.lankahw.model.NewCustomer;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Sathiyaraja on 7/3/2018.
 */

public class UploadNewCustomer extends AsyncTask<ArrayList<NewCustomer>, Integer, ArrayList<NewCustomer>> {

    Context context;
    ProgressDialog dialog;

    AsyncTaskListener taskListener;
    TaskType taskType;
    ProgressDialog pDialog;
    int totalRecords;
    ArrayList<NewCustomer> fNewCustomerslist = new ArrayList<>();
    public static SharedPreferences localSP;
    private String customerID;
    NewCustomerDS newCustomerDS;

    public UploadNewCustomer(Context context, AsyncTaskListener taskListener, TaskType taskType, ArrayList<NewCustomer> ordList) {

        this.context = context;
        this.taskListener = taskListener;
        this.taskType = taskType;
        fNewCustomerslist.addAll(ordList);
        newCustomerDS = new NewCustomerDS(context);

//        localSP = context.getSharedPreferences(SharedPreferencesClass.SETTINGS, Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
        localSP = context.getSharedPreferences(SharedPreferencesClass.SETTINGS, 0);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Uploading data...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<NewCustomer> fNewCustomers) {
        super.onPostExecute(fNewCustomers);
        pDialog.dismiss();
        taskListener.onTaskCompleted(taskType);


    }

    @Override
    protected ArrayList<NewCustomer> doInBackground(ArrayList<NewCustomer>... arrayLists) {

        int recordCount = 0;
        publishProgress(recordCount);

        // ArrayList<fNewCustomer> fNewCustomersList = arrayLists[0];
        totalRecords = fNewCustomerslist.size();

        final String sp_url = localSP.getString("URL", "").toString();
        String URL = "http://" + sp_url;
        Log.v("## Json ##", URL.toString());

        //create json list
        for (NewCustomer fnc : fNewCustomerslist) {

            customerID = fnc.getCUSTOMER_ID();
            ArrayList<String> jsonlist = new ArrayList<>();
            String sJsonHed = new Gson().toJson(fnc);

            jsonlist.add(sJsonHed);

            Log.v("## Json ##", jsonlist.toString());
//
            try {
                // PDADBWebServiceMO
                HttpPost requestfDam = new HttpPost(URL+"/KFDWebServices/KFDWebServicesRest.svc/NewCustomerPDAUpload");
                StringEntity entityfDam = new StringEntity(jsonlist.toString(), "UTF-8");
                entityfDam.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                entityfDam.setContentType("application/json");
                requestfDam.setEntity(entityfDam);
                // Send request to WCF service
                DefaultHttpClient httpClientfDamRec = new DefaultHttpClient();


                HttpResponse responsefDamRec = httpClientfDamRec.execute(requestfDam);
                HttpEntity entityfDamEntity = responsefDamRec.getEntity();
                InputStream is = entityfDamEntity.getContent();

                //StatusLine statusLine = responsefDamRec.getStatusLine();
                //int statusCode = statusLine.getStatusCode();

                if (is != null) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }

                    is.close();
//---
                    String result = sb.toString();
                    String result_fDamRec = result.replace("\"", "");

                    Log.e("response", "connect:" + result_fDamRec);

                    if (result_fDamRec.trim().equalsIgnoreCase("200")) {

                        // Toast.makeText(context, "New Customer Saved", Toast.LENGTH_SHORT).show();
                        //update sync status

                        newCustomerDS.updateIsSynced(customerID, "1");

                        pDialog.dismiss();

                    } else {
                     //   fnc.setIS_SYNCED(false);
                        newCustomerDS.updateIsSynced(customerID, "0");
                    }


                }
            } catch (Exception e) {

                e.getStackTrace();
            }

//            ++recordCount;
//            publishProgress(recordCount);

        }

        return null;
    }
}

