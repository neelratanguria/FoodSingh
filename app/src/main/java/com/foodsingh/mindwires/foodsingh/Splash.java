package com.foodsingh.mindwires.foodsingh;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;

import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.v4.app.ActivityCompat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Splash extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RequestQueue req;
    boolean temp = false;
    Thread t;
    ProgressBar progressBar;
    StringRequest sr;
    Handler h;
    Dialog dialog;
    int i=0;
    Context ctx;
    boolean rec = false;

    TextView header;
    TextView retry_menu;

    ProgressBar progressbar;
    private boolean dataLoaded = false,locationisthere = false;
    GoogleApiClient apiClient;
    boolean redundent = false;
    private boolean LocationChecked = false, LocationPermission = false;
    boolean checker = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checker = false;
        rec = false;
        ctx = this;
        // req = Volley.newRequestQueue(this);
        h = new Handler();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (!InternetConnection_check()) {
            setContentView(R.layout.no_internet);
            retry_menu= (TextView) findViewById(R.id.menu_retry);
            retry_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recreate();
                }
            });
        } else {
            setContentView(R.layout.splash);

            progressBar = (ProgressBar) findViewById(R.id.real_progressbar);
            sharedPreferences = getSharedPreferences(constants.foodsingh, Context.MODE_PRIVATE);
            localdatabase.notifications = sharedPreferences.getInt(constants.notifAmount, 0);
            header = (TextView) findViewById(R.id.metadata);
            Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/android.ttf");
            header.setTypeface(tf);

            // Glide.with(this).load(R.drawable.signin).into(AnimationTarget);
            //Display("Loading! Please Wait");


            editor = sharedPreferences.edit();


            if (!checkPermission()) {
                // requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);

                LocationPermission = false;
            } else {
                LocationPermission = true;
                if (!isLocationEnabled(this)) {
                    GoToLocations();
                } else {
                    //  Initiate_Meta_Data();
                }
            }

        }

    }




    private void New_Details(String name, String number, final String main_url) {

        i++;



        RequestQueue re = Volley.newRequestQueue(this, new OkHttpStack());

        sr = new StringRequest(Request.Method.POST, main_url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                localdatabase.loaded = true;
                progressBar.setVisibility(View.INVISIBLE);
                Log.i("mainresponse" + i, response);

                if(!temp) {
                    startThread(response);
                    temp = true;

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Display(error.toString());
                Log.i("mainresponse", "1"+error.toString());
//                Display("Please start the app again");
                Log.i("mainresponse","2"+error.toString());
                temp = false;
                //finish();

            }
        })
        {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed =1; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 1; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));

                    return Response.success(jsonString, cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (Exception e) {
                    return Response.error(new ParseError(e));
                }
            }



            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }


//        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);


            @Override
            public Map getParams(){
                Map<String,String> map = new HashMap<>();
                map.put("latitude",localdatabase.deliveryLocation.getLatitude()+"");
                map.put("longitude",localdatabase.deliveryLocation.getLongitude()+"");
                String mobile = sharedPreferences.getString("mobile", null);
                map.put("mobile", mobile);
                map.put("app_version",""+BuildConfig.VERSION_CODE);
                return map;
            }
        };

        sr.setShouldCache(false);
        // RequestQueue queue = new RequestQueue(new NoCache(),new BasicNetwork(new HurlStack()));

        sr.setRetryPolicy(new DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        if(i==1)
            re.add(sr);

    }

    private void startThread(String response){
        dataLoaded = true;


        try {
            JSONObject mainObject = new JSONObject(response);
            localdatabase.about_text =mainObject.getString("about_text");
            localdatabase.cartCoupon = mainObject.getString("cart_ad");
            localdatabase.about_img = mainObject.getString("about_image");
            localdatabase.metaData = new MetaData(
                    mainObject.getString("stax_amount"),
                    mainObject.getString("latest_version_new"),
                    mainObject.getString("service"),
                    mainObject.getString("min_order"),
                    mainObject.getString("msg_api"),
                    mainObject.getInt("amount_for_free_delivery")
            );
            localdatabase.aboutText = mainObject.getString("about_text");
            localdatabase.aboutImage = mainObject.getString("about_image");
            localdatabase.delivery = mainObject.getString("location");
            localdatabase.drinks = mainObject.getString("drinks");
            localdatabase.share_text =mainObject.getString("share_text");
            localdatabase.share_url = mainObject.getString("share_url");
            localdatabase.deliveryCharge = Integer.parseInt(mainObject.getString("delivery_charge"));
            localdatabase.stax_amount = Integer.parseInt(mainObject.getString("stax_amount"));
            localdatabase.kitchenClosedText = mainObject.getString("kitchen_closed_text");
            localdatabase.location_id = mainObject.getString("location_id");
            Log.i("mainresponse","location_id:"+localdatabase.location_id);

            if(Integer.parseInt(localdatabase.metaData.getLatest_version())>BuildConfig.VERSION_CODE){
                showDialog2(Splash.this,"You are using an older version of this app. Please go to playstore and download the latest version.");
                return;
            }else{

            }

            JSONArray Categories = mainObject.getJSONArray("categories");
            for (int i=0; i<Categories.length(); i++){
                String name,image,cuisine, time, combo,default_no;
                boolean default_bool;
                List<MenuItems> menuItemsList = new ArrayList<>();
                JSONObject tempObject = Categories.getJSONObject(i);
                name = tempObject.getString("name");
                image = tempObject.getString("image");
                cuisine = tempObject.getString("cuisine");
                time = tempObject.getString("time");

                default_no=tempObject.getString("default");
                if(default_no.equals("1"))
                {
                    default_bool=true;
                }
                else
                {
                    default_bool=false;
                }

                combo = tempObject.getString("combo");

                JSONArray miniMenu = tempObject.getJSONArray("menu");

                for(int j=0; j<miniMenu.length(); j++){
                    String id, name_, category, price, image_, status, detail, r_price;

                    JSONObject miniTempObject = miniMenu.getJSONObject(j);
                    id = miniTempObject.getString("id");
                    name_ = miniTempObject.getString("name");
                    category = miniTempObject.getString("category");
                    price = miniTempObject.getString("price");
                    image_ = miniTempObject.getString("image");
                    status = miniTempObject.getString("status");
                    detail = miniTempObject.getString("detail");
                    r_price = miniTempObject.getString("r_price");


                    MenuItems menuItems = new MenuItems(id,name_,category,price,image_, status, detail, r_price,localdatabase.location_id);
                    menuItemsList.add(menuItems);
                    String available = miniTempObject.getString("status");

                    if(available.equals("NA")){
                        UnavailableItems ii = new UnavailableItems(name_,Integer.parseInt(price));
                        localdatabase.unavailableItemsList.add(ii);
                    }
                }
                MasterMenuItems menuItemsObject = new MasterMenuItems(name,image,cuisine, combo,menuItemsList,time,tempObject.getString("drinks"), tempObject.getString("detail"),default_bool);
                Log.i("checking details", tempObject.getString("drinks"));
                localdatabase.masterList.add(menuItemsObject);
            }
            JSONArray BannerImages = mainObject.getJSONArray("home_images");

            for(int i=0; i<BannerImages.length(); i++){
                localdatabase.BannerUrls.add(BannerImages.getJSONObject(i).getString("url"));
            }

            JSONArray getAdImages = mainObject.getJSONArray("ad_bars");

            for(int i=0; i<getAdImages.length(); i++){
                JSONObject tempJson =getAdImages.getJSONObject(i);
                CouponClass temp = new CouponClass(tempJson.getString("id"),tempJson.getString("coupon"),tempJson.getString("image"));
                localdatabase.couponClassList.add(temp);
            }

            JSONArray superCategories = mainObject.getJSONArray("super_categories");

            for(int i=0; i<superCategories.length(); i++){
                SuperCategories su = new SuperCategories(superCategories.getJSONObject(i).getString("id"),
                        superCategories.getJSONObject(i).getString("name"));
                localdatabase.superCategoriesList.add(su);
                Log.i("super_categories",localdatabase.superCategoriesList.get(i).getName());
            }

            if(LocationChecked&&dataLoaded) {
                SharedPreferences  sharedPreferences = getSharedPreferences(constants.foodsingh,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(constants.mainresponsesharedpreferences,response);
                editor.apply();
                if(!redundent) {
                    redundent = true;

                    //   h.postDelayed(new Runnable() {
                    //@Override
                    //public void run() {
                    if(isCoarseLocationEnabled(Splash.this)) {
                        startActivity(new Intent(Splash.this, menu.class));
                        finish();
                    }
                    // }
                    //},500);


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
//            Display("Please start the app again");
            Log.i("mainresponse","3"+e.toString());
            // finish();
        }finally {

        }

    }


    private void Initiate_Meta_Data() {

        if (!checkPermission()) {
            // requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);

            LocationPermission = false;
        } else {
            LocationPermission = true;
            New_Details("","",constants.main_url);
        }

    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    private boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        //return (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED));
    }

    @Override
    public void onStart() {
        super.onStart();
        apiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).
                addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();

        if(checkPermission()){
            apiClient.connect();
        }

        Log.i("called now", "called now");

    }

    @Override
    public void onStop() {
        super.onStop();
        apiClient.disconnect();
        if(req!=null){
            req.cancelAll(sr);
            Log.i("23235616","called on stop");
        }

    }





    private void Display(final String s) {

        h.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Splash.this,s,Toast.LENGTH_LONG).show();
            }
        });
    }




    @Override
    public void onPause() {
        super.onPause();
        if(req!=null){
            req.cancelAll(sr);
            Log.i("23235616","called on stop");
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(req!=null){
            req.cancelAll(sr);
            Log.i("23235616","called on stop");
        }

    }



    @Override

    public void onResume() {
        super.onResume();
        if(!isLocationEnabled(this)){
            GoToLocations();

        }else{
            if(dialog!=null){
                dialog.dismiss();

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        if (grantResults.length > 0) {
            if(grantResults[1]!=PackageManager.PERMISSION_GRANTED){
                Display("Permission Denied");
                finish();
            }else{
                apiClient.connect();
                if(!isLocationEnabled(this)){
                    GoToLocations();
                }
            }
        }

    }

    public void showDialog2(Context activity,final String msg){
        Log.i("mainresponse","called");
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog2);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/OratorStd.otf");

        text.setTypeface(tf);

        TextView image = (TextView) dialog.findViewById(R.id.btn_dialog);
        // Glide.with(activity).load(pic).into(image);
        TextView dialogButton = (TextView)dialog.findViewById(R.id.cancel);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(msg.equals("You are using an older version of this app. Please go to playstore and download the latest version.")){
                    finish();
                }else{
                    dialog.dismiss();
                }

            }
        });



        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Integer.parseInt(localdatabase.metaData.getLatest_version())> BuildConfig.VERSION_CODE){
                    String url = localdatabase.share_url;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                    finish();
                }else{
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }

            }
        });

        dialog.show();

    }
    private void GoToLocations(){
       /* LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            showDialog2(this,"You Need To Enable\n Your loction to use this Application.");

        }else{
           // Initiate_Meta_Data();
        }*/
    }

    private void showLog(String s) {
        Log.i(constants.logTag, s);
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if (resultCode != RESULT_OK) {
                locationisthere = false;
                Display("You Won't be able to use this application right now.");
                finish();
            } else if (temp) {
                locationisthere = true;

                if (isCoarseLocationEnabled(Splash.this)) {
                    temp = false;
                    startActivity(new Intent(Splash.this, menu.class));
                    finish();
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        showLog("Connected Api Client");
        if (apiClient != null) {
            LocationRequest locationRequest = LocationRequest.create().setInterval(200000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 0);

                LocationPermission = false;


            } else {


                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
                builder.setAlwaysShow(true);
                final String TAG = "LOCATION PERMISSION";
                PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(apiClient, builder.build());
                result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                                             @Override
                                             public void onResult(LocationSettingsResult result) {

                                                 final Status status = result.getStatus();

                                                 if (!isCoarseLocationEnabled(Splash.this)) {
                                                     locationisthere = false;
                                                     try {
                                                         status.startResolutionForResult(Splash.this, 2);
                                                     } catch (IntentSender.SendIntentException e) {
                                                         e.printStackTrace();
                                                         Display(e.toString());
                                                     }
                                                 }else{
                                                     locationisthere = true;
                                                 }
                                                 // switch (status.getStatusCode()) {
                         /*   case LocationSettingsStatusCodes.SUCCESS:
                                Log.i(TAG, "All location settings are satisfied.");
                                Display("here1");
                                locationisthere = true;
                                break;
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                locationisthere = false;
                                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                                Display("here");

                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the result
                                    // in onActivityResult().
                                    status.startResolutionForResult(Splash.this, 2);

                                } catch (IntentSender.SendIntentException e) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                Display("here3");
                                locationisthere = false;
                                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");

                                break;

                            default:
                                Display("here4");*/
                                             }
                                         }
                );


                LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, Splash.this);
                LocationPermission = true;
                if (!isLocationEnabled(this)) {
                    GoToLocations();
                } else {
                    // Initiate_Meta_Data();
                }
            }

        }
    }

    public Boolean InternetConnection_check()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    @Override
    public void onConnectionSuspended(int i) {
        showLog("Connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        showLog("Connection Failed");

    }

    public boolean isCoarseLocationEnabled(Context context) {
        if (context != null) {
            LocationManager manager = (LocationManager) context.getSystemService(android.content.Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                return false;
            } else
                return true;

        }

        return false;
    }

    @Override
    public void onLocationChanged(final Location location) {

        showLog("Location at "+location.getLongitude()+", "+location.getLongitude());
        localdatabase.deliveryLocation = location;


        //  localdatabase.city  = getCity(location.getLatitude(),location.getLongitude());
        LocationChecked = true;


        doit();

    }

    private void doit(){
        if(i==0) {
            //  AddressFetchingService.startActionFoo(this,location.getLatitude()+"",location.getLongitude()+"");

            SharedPreferences sharedPreferences = getSharedPreferences(constants.foodsingh, Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putFloat("latitude", (float) localdatabase.deliveryLocation.getLatitude());
            edit.putFloat("longitude", (float) localdatabase.deliveryLocation.getLongitude());

            edit.apply();

            Initiate_Meta_Data();


        }
    }
}
