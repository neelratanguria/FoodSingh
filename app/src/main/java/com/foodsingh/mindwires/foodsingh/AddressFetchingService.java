package com.foodsingh.mindwires.foodsingh;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class AddressFetchingService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.foodsingh.mindwires.foodsingh.action.FOO";
    private static final String ACTION_BAZ = "com.foodsingh.mindwires.foodsingh.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.foodsingh.mindwires.foodsingh.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.foodsingh.mindwires.foodsingh.extra.PARAM2";

    public AddressFetchingService() {
        super("AddressFetchingService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, AddressFetchingService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, AddressFetchingService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);

                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
       // throw new UnsupportedOperationException("Not yet implemented");
        double latitude = Double.parseDouble(param1);
        double longitude = Double.parseDouble(param2);

        String city_temp = getCity(latitude,longitude);
        if((!(city_temp.equalsIgnoreCase("Location Not Found"))) || (city_temp!=null))
        {
            localdatabase.city=city_temp;
            SharedPreferences sharedPreferences=getSharedPreferences(localdatabase.shared_location_key,MODE_PRIVATE);
            SharedPreferences.Editor edit=sharedPreferences.edit();
            edit.putString("location",city_temp);
            edit.commit();
        }
        else
        {
            SharedPreferences sharedPreferences=getSharedPreferences(localdatabase.shared_location_key,MODE_PRIVATE);
            localdatabase.city=sharedPreferences.getString("location","Bhubaneswar");

        }

        Intent i = new Intent();
        i.setAction(constants.menugetcitybroadcast);
        sendBroadcast(i);

        Log.i("servicecity",localdatabase.city);
    }


    private String getCity(double latitude, double longitude){
        Geocoder geocoder;
        List<Address> addresses = new ArrayList<>();
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
            //Display(e.toString());
        }
        String address, city;
        if(addresses.size()>0) {
            address = addresses.get(0).getAddressLine(0)+" , , "; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            Log.i("addressmine",addresses.get(0).getAdminArea()+", "+addresses.get(0).getFeatureName()+" , "+addresses.get(0).getLocality()+", "+address);
            localdatabase.lane = address;
            localdatabase.sublocality = addresses.get(0).getSubLocality();

            int comma = 0;
            localdatabase.lane2 = "";
            for (int i =0; comma != 2; i++ ){
                if(address.charAt(i) == ','){
                    comma++;
                }
                if(comma == 2){
                    break;
                }
                localdatabase.lane2 += address.charAt(i);
            }


            return city;
        }

        return "Location Not Found";
    }
    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
