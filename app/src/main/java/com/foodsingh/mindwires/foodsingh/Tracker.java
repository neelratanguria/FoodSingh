package com.foodsingh.mindwires.foodsingh;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tracker extends AppCompatActivity {

    static TextView order_no, repeat_order,price,date,
            fooditems,foodqt,driverinfo,driver_number, items,logistics,issue,notifamount;
    boolean canheorder = true;
    final String prompt = "Sorry. But you are too far from the restaurant.";
    final String promp2 = "Sorry, but this kitchen is closed right now.";
    ImageView trackimage;
    int discount = 0;
    Typeface tf,tf1;

    TextView toolbarText;
    View actionView;
    TextView cartitemcount1;
    String itemsString="", itemnames = "";
    BroadcastReceiver broadcastReceiver;
    String drivern, driverm;
    boolean megacheck = false;

    String status;

    Intent i;
    FoodItem item, newItem;
    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RemoveTop();
        setContentView(R.layout.orders);

        if(localdatabase.loaded==false){
            RestoreResponse.getResponse(this);
        }
        addBottomToolbar();
        tf = Typeface.createFromAsset(getAssets(),"fonts/OratorStd.otf");
        tf1 = Typeface.createFromAsset(getAssets(),"fonts/COPRGTB.TTF");
        toolbarText = (TextView)findViewById(R.id.toolbarText);
        issue = (TextView)findViewById(R.id.issue);
        toolbarText.setTypeface(tf);
        toolbarText.setText("Orders");
        order_no = setTextId(this.order_no,R.id.order_number);
        repeat_order = setTextId(this.repeat_order,R.id.repeatorder);
        repeat_order = setTextId(this.repeat_order,R.id.repeatorder);
        price = setTextId(this.price,R.id.price);
        date = setTextId(this.date,R.id.date);
        fooditems=setTextId(this.fooditems,R.id.foodname);
        foodqt = setTextId(this.foodqt,R.id.foodqt);
        driver_number = setTextId(this.driver_number,R.id.number_info);
        driverinfo = setTextId(this.driver_number,R.id.info);
        logistics = setTextId(this.driver_number,R.id.logisticinfo);
        trackimage = (ImageView)findViewById(R.id.trackimage);
        items = (TextView)findViewById(R.id.items);
        img_back=(ImageView) findViewById(R.id.back);
        i = getIntent();
        order_no.setTypeface(tf);
        repeat_order.setTypeface(tf);
        if(localdatabase.location_id.equals("NA")||localdatabase.metaData.getservice().equals("false")){
            repeat_order.setClickable(false);
        }
        items.setTypeface(tf1);
        price.setTypeface(tf1);
        date.setTypeface(tf);
        fooditems.setTypeface(tf);
        foodqt.setTypeface(tf);
        driver_number.setTypeface(tf);
        driverinfo.setTypeface(tf);
        logistics.setTypeface(tf1);

        if(i!=null){
            item = i.getExtras().getParcelable("object");
           //processFoodNames(item.getItem());
            //checking the activity
            boolean check = i.getExtras().getBoolean("getter");

            if(check){
                repeat_order.setVisibility(View.INVISIBLE);
            }else {

                //repeat_order.setVisibility(View.VISIBLE);
                repeat_order.setVisibility(View.INVISIBLE);

            }
            if(item!=null) {
                  test(item.getItem());
                   price.setText(item.getAmount());
                date.setText(item.getDate());
                order_no.setText(item.getId());
                trackimage.setImageResource(R.drawable.orderplaced);
                //item.getItem();
            }
        }else{
            Display("i is null");
        }

        getResponse(constants.order_details);

        repeat_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(localdatabase.metaData!=null) {
                    if (localdatabase.metaData.getservice().equals("true")) {
                        if (canheorder) {
                            if (!megacheck) {
                                Intent i = new Intent(view.getContext(), CheckoutActivity.class);
                                i.putExtra("items", item.getItem());
                                i.putExtra("key", 1);
                                int amount = Integer.parseInt(item.getAmount()) + discount;
                                i.putExtra("price", amount + "");
                                Log.i("tracker_price", itemsString.substring(0, itemsString.length() - 1) + "," + item.getAmount());
                                startActivity(i);
                            } else {
                                String temp = itemsString;
                                temp.replace(",", "\n");
                                showDialog2(Tracker.this, "Sorry!Only the following items are available right now. \n\n" + temp + "\n");
                            }
                        } else {
                            showDialog2(Tracker.this, prompt);
                        }
                    }else{
                        showDialog2(Tracker.this, promp2);
                    }
                }
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a=new Intent(getApplicationContext(),order_history.class);
                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(a);
            }
        });

        issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), Support.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        
        SetupBroadcastReceiver();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem=menu.findItem(R.id.action_cart);
        actionView= MenuItemCompat.getActionView(menuItem);
        cartitemcount1=(TextView) actionView.findViewById(R.id.cart_badge);

        cartitemcount1.setText(String.valueOf(localdatabase.cartList.size()));
        ImageView cart = (ImageView)actionView.findViewById(R.id.cartimage);

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ssd=new Intent(getApplicationContext(),cart.class);
                startActivity(ssd);
            }
        });

        ImageView notif = (ImageView)actionView.findViewById(R.id.notif);

        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Tracker.this, NotificationActivity.class));
            }
        });



        notifamount = (TextView)actionView.findViewById(R.id.notification_badge);
        if(localdatabase.notifications==0){
            notifamount.setVisibility(View.INVISIBLE);
        }else {
            notifamount.setVisibility(View.VISIBLE);
            notifamount.setText(localdatabase.notifications+"");
        }

        return true;
    }



    public void showDialog2(Context activity, final String msg){
        final Dialog dialog = new Dialog(activity);
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
                dialog.dismiss();
            }
        });

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

       if(msg.equals(prompt)||msg.equals(promp2)){
           image.setText("Okay");
       }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(!(msg.equals(prompt)||msg.equals(promp2))){
                   Intent i = new Intent(view.getContext(), CheckoutActivity.class);
                   i.putExtra("items",itemsString);
                   i.putExtra("key",1);

                   i.putExtra("price", newItem.getAmount());
                   Log.i("tracker_price",itemsString.substring(0,itemsString.length()-1)+","+item.getAmount());
                   startActivity(i);
               }else{
                   dialog.dismiss();
               }
            }
        });

        dialog.show();

    }
    private void RemoveTop(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

 

    private void test(String foods1){
       // String foods1 = "DRAGON CHICKEN (B\\/L) x1, HUNAN CHICKEN (B\\/L) x1, Coke 750ml x1";

        fooditems.append(" ");
        String[] foods = foods1.split(",");



        Pattern p1 = Pattern.compile("-\\d+");
        for (int i=0; i<foods.length; i++){
            Log.i("trackerfood",foods[i]);
            foods[i].replace("\n"," ");
            Matcher m = p1.matcher(foods[i]);
            //foods[i].replace("(B\\/L)","k");
            if(m.find()) {
                // Display(foods[i]+" 2here");
                String qt =m.group(0);
                qt = qt.substring(1);
                String name = foods[i].substring(0, foods[i].length() - qt.length() - 1);

                fooditems.append(name+"\n");
                foodqt.append(qt+"\n");
                boolean uChecker = false;
                for(int j=0; j<localdatabase.unavailableItemsList.size(); j++){
                    uChecker = false;

                    if(localdatabase.unavailableItemsList.get(j).getName().equals(name.trim())){
                       // Display("Detected");
                        uChecker = true;
                        megacheck= true;
                        String new_Amount = String.valueOf(
                                Integer.parseInt(item.getAmount())-Integer.parseInt(qt.trim())*localdatabase.unavailableItemsList.get(j).getPrice());
                        newItem = new FoodItem(item.getId(),item.getItem(),new_Amount,item.getAddress(),item.getDate());
                      itemnames+=name+",";

                        break;
                    }else{
                        uChecker = false;
                       // Log.i("tracker2323",localdatabase.unavailableItemsList.get(i).getName()+",");
                    }
                }

                if(!uChecker){
                    itemsString += name + "-" + qt+", ";
                }

            }else{

            }
        }
    }

    private void Display(String s){
        //Toast.makeText(this, s, Toast.LENGTH_LONG).show();
        Log.i("android23235616",s);

    }


    private void SetupBroadcastReceiver() {

        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(constants.broaadcastReceiverMenu);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                notifamount = (TextView)actionView.findViewById(R.id.notification_badge);
                if(intent.getAction().equals(constants.broaadcastReceiverMenu)){


                    notifamount.setVisibility(View.VISIBLE);
                    notifamount.setText(localdatabase.notifications+"");


                    Log.i("broadcastreceiver1", localdatabase.notifications+"");
                }else if(intent.getAction().equals(constants.menu2BroadcastReceiver)){
                    notifamount.setVisibility(View.INVISIBLE);
                }

            }


        };

        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(constants.menu2BroadcastReceiver);

        registerReceiver(broadcastReceiver,intentFilter);
        registerReceiver(broadcastReceiver,intentFilter2);
    }
    private TextView setTextId(TextView t, int id){
        t = (TextView)findViewById(id);
        return t;
    }

    private void getResponse(String url){

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    status = jsonObject.getString("status");
                    discount = Integer.parseInt(jsonObject.getString("discount"));
                    TextView name = (TextView) findViewById(R.id.info);
                    TextView mob = (TextView) findViewById(R.id.number_info);

                    drivern = jsonObject.getString("delivery_boy");
                    driverm = jsonObject.getString("delivery_boy_mobile");


                    if(status.equals("0")){
                        trackimage.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.orderplaced));
                        name.setText("NOT AVAILABLE");
                        mob.setText("NOT AVAILABLE");
                        driver_number.setText("NOT AVAILABLE");
                        driverinfo.setText("NOT AVAILABLE");

                    }else if(status.equals("1")){
                        trackimage.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.processing));
                        name.setText("NOT AVAILABLE");
                        mob.setText("NOT AVAILABLE");
                        driver_number.setText("NOT AVAILABLE");
                        driverinfo.setText("NOT AVAILABLE");
                    }else if(status.equals("2")){
                        trackimage.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.outfordelivery));
                        name.setText(drivern);
                        mob.setText(driverm);
                        driver_number.setText(driverm);
                        driverinfo.setText(drivern);
                    }else if(status.equals("3")){
                        trackimage.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.delivered));
                        name.setText("NOT AVAILABLE");
                        mob.setText("NOT AVAILABLE");

                    }else if(status.equals("NA")){
                        trackimage.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.orderplaced));
                        name.setText("NOT AVAILABLE");
                        mob.setText("NOT AVAILABLE");
                    }
                    else {
                        trackimage.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.orderplaced));
                        name.setText("NOT AVAILABLE");
                        mob.setText("NOT AVAILABLE");
                    }

                    if(date.getText().toString().equalsIgnoreCase("just now")){
                        trackimage.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.orderplaced));
                        name.setText("NOT AVAILABLE");
                        mob.setText("NOT AVAILABLE");
                    }

                    String location_id = jsonObject.getString("location_id");

                    if(location_id.equals(localdatabase.location_id)){
                        canheorder = true;
                    }else{
                        canheorder = false;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Display(e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Display(error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //
                Map<String, String> map = new HashMap<>();
                map.put("id",item.getId().substring(2));

                return  map;
            }
        };

        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(sr);


    }

    void addBottomToolbar(){
        TextView tvHome,tvOrders, tvSupport;
        LinearLayout home, orders, support;

        tvHome = (TextView) findViewById(R.id.txt_home);
        tvOrders = (TextView) findViewById(R.id.txt_orders);
        tvSupport = (TextView) findViewById(R.id.txt_support);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/COPRGTL.TTF");
        tvHome.setTypeface(tf);
        tvOrders.setTypeface(tf);
        tvSupport.setTypeface(tf);

        home = (LinearLayout) findViewById(R.id.btm_home);
        orders = (LinearLayout) findViewById(R.id.btm_orders);
        support = (LinearLayout) findViewById(R.id.btm_support);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tracker.this, menu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         //       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tracker.this, order_history.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           //     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Tracker.this, Support.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(broadcastReceiver!=null){
            unregisterReceiver(broadcastReceiver);
        }
    }

}
