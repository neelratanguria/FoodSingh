package com.foodsingh.mindwires.foodsingh;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tanmay on 01-11-2017.
 */

public class KitchenServiceLollipop extends GcmTaskService {

    @Override
    public int onRunTask(TaskParams taskParams) {
        if(localdatabase.metaData!=null)
        getKitchenStatus(constants.service_status_url);

      return 0;
    }

    private void getKitchenStatus(String url) {
        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("service");
                    Intent i = new Intent();
                    Log.i("kitchenservice",status);
                    localdatabase.metaData.setService(status);
                    i.setAction(constants.kitchenStatusBroadcast);
                    sendBroadcast(i);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("kitchenservice",e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("kitchenservice",error.toString());
            }
        }){
            @Override
            public Map getParams(){
                Map<String,String> map = new HashMap<>();
                map.put("location_id",localdatabase.location_id);
                return map;
            }
        };

        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(sr);
    }
}
