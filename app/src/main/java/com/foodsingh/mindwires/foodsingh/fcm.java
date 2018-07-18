package com.foodsingh.mindwires.foodsingh;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;
import java.util.Map;

//Class extending FirebaseInstanceIdService
public class fcm extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    String mobile = "";


    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);
       sendRegistrationToServer(refreshedToken,constants.token_url);

    }

    private void sendRegistrationToServer(final String refreshedToken, String url) {

        SharedPreferences sharedPreferences = getSharedPreferences(constants.foodsingh,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();


        mobile = sharedPreferences.getString("mobile","");

        edit.putString("token",refreshedToken);
        edit.apply();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               Log.i("service", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("service", error.toString());
            }
        }){
            @Override
            public Map getParams(){

                Map<String, String> map = new HashMap<>();
                map.put("token",refreshedToken);
                map.put("mobile", mobile);
                return map;

            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue q =Volley.newRequestQueue(this);
        q.add(stringRequest);
    }


}
