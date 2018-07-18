package com.foodsingh.mindwires.foodsingh;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import java.util.Collections;
import java.util.List;




public class NotificationActivity extends AppCompatActivity {
    Gson gson;
    NotificationLists notificationLists;
    SharedPreferences sharedPreferences;
    Dialog dialog;
    public static int read = 0;
    BroadcastReceiver broadcastReceiver;
    RecyclerView recyclerView;
    NotificationAdapter notificationAdapter;
    android.support.design.widget.FloatingActionButton floatingActionButton;
    String text = "Are you sure that you want to remove all the notifications?";
    static TextView clear, notifamount;
    // static TextView localdatabase.no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RemoveTop();

        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        clear = (TextView) findViewById(R.id.text);
        floatingActionButton = (android.support.design.widget.FloatingActionButton) findViewById(R.id.new_clear);
        sharedPreferences = getSharedPreferences(constants.foodsingh, Context.MODE_PRIVATE);
        recyclerView = (RecyclerView) findViewById(R.id.notificationRecycler);
        gson = new Gson();
        String tempJson = sharedPreferences.getString(constants.foodsinghNotif, "");

        if (tempJson.equals("")) {
            if (read == 0) {
                showDialog(this, "You have no notifications.", R.drawable.notification);
            }
        } else {
            notificationLists = gson.fromJson(tempJson, NotificationLists.class);
            List<NotificationItem> shallowCopy = notificationLists.getNotification().subList(0, notificationLists.getNotification().size());
            Collections.reverse(shallowCopy);
            notificationAdapter = new NotificationAdapter(shallowCopy, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setAdapter(notificationAdapter);

            notificationAdapter.notifyDataSetChanged();

        }

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //edit.putInt(constants.notifAmount,0);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog2(NotificationActivity.this, text);

            }
        });


        setUpReceiver();

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(NotificationActivity.this, "Pressed", Toast.LENGTH_SHORT).show();
                if (localdatabase.masterList.size() > 0) {
                    Intent a = new Intent(NotificationActivity.this, menu.class);
                    a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(a);
                } else if (localdatabase.masterList.size() == 0 && localdatabase.delivery.equals("NA")) {
                    Intent a = new Intent(NotificationActivity.this, menu.class);
                    a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(a);

                } else {
                    Intent a = new Intent(NotificationActivity.this, Splash.class);
                    a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(a);

                }
            }
        });

    }

    private void clearNotification() {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(constants.foodsinghNotif, "");
        edit.putInt(constants.notifAmount, 0);
        localdatabase.notifications = 0;
        // notifamount.setVisibility(View.INVISIBLE);
        edit.apply();
        Intent i = new Intent();
        i.setAction(constants.menu2BroadcastReceiver);

        sendBroadcast(i);
        Intent q1 = new Intent();
        q1.setAction(constants.broadCastReceiverNotification2);
        sendBroadcast(q1);
        recreate();
    }

    private void setUpReceiver() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(constants.broadCastReceiverNotification);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        UpdateUi();
                    }
                }, 1000);
                Log.i("checkerbroadacast", "called");

            }
        };
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void UpdateUi() {


        //recreate();
        String tempJson = sharedPreferences.getString(constants.foodsinghNotif, "");

        if (tempJson.equals("")) {
            if (read == 0) {
                showDialog(this, "You have no notifications.\nStay tuned for more coupons.", R.drawable.notification);
            }

        } else {
            notificationLists = gson.fromJson(tempJson, NotificationLists.class);
            List<NotificationItem> shallowCopy = notificationLists.getNotification().subList(0, notificationLists.getNotification().size());
            Collections.reverse(shallowCopy);
            notificationAdapter = new NotificationAdapter(shallowCopy, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setAdapter(notificationAdapter);

            notificationAdapter.notifyDataSetChanged();

        }

//        int diff = notificationLists.getNotification().size() - localdatabase.cache;


    }

    private void RemoveTop() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_cart);
        View actionView = MenuItemCompat.getActionView(menuItem);
        TextView cartitemcount1 = (TextView) actionView.findViewById(R.id.cart_badge);

        cartitemcount1.setText(String.valueOf(localdatabase.cartList.size()));
        ImageView cart = (ImageView) actionView.findViewById(R.id.cartimage);

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (localdatabase.masterList.size() > 0) {
                    startActivity(new Intent(NotificationActivity.this, cart.class));
                } else {
                    startActivity(new Intent(NotificationActivity.this, Splash.class));
                    finish();
                }
            }
        });

        ImageView notif = (ImageView) actionView.findViewById(R.id.notif);


        notifamount = (TextView) actionView.findViewById(R.id.notification_badge);
        notifamount.setVisibility(View.INVISIBLE);
        if (localdatabase.notifications == 0) {
            notifamount.setVisibility(View.INVISIBLE);
        } else {
            // notifamount.setVisibility(View.VISIBLE);
            //notifamount.setVisibility(View.VISIBLE);
            notifamount.setText(localdatabase.notifications + "");
        }

        return true;
    }

    private void Display(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    public void showDialog(Activity activity, String msg, int pic) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/OratorStd.otf");

        text.setTypeface(tf);

        ImageView image = (ImageView) dialog.findViewById(R.id.btn_dialog);
        image.setImageBitmap(BitmapFactory.decodeResource(getResources(), pic));
        TextView dialogButton = (TextView) dialog.findViewById(R.id.cancel);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void showDialog2(Context activity, final String msg) {
        Log.i("mainresponse", "called");
        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog2);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/OratorStd.otf");

        text.setTypeface(tf);

        TextView image = (TextView) dialog.findViewById(R.id.btn_dialog);
        // Glide.with(activity).load(pic).into(image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearNotification();
                dialog.dismiss();
            }
        });

        TextView dialogButton = (TextView) dialog.findViewById(R.id.cancel);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (msg.equals("Please go to app store and download the latest version.")) {
                    finish();
                } else {
                    dialog.dismiss();
                }

            }
        });

    dialog.show();
    }
}


