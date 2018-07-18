package com.foodsingh.mindwires.foodsingh;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    private EditText etComments, etLane1, etLane2, etLocality;
    private Button btnPlaceOrder, btnHome, btnWork, btnCash, btnOnline;
    private String statusAddress = "HOME", statusPayment = "CASH";
    private TextView tvTotalAmount;
    FoodItem getItems;
    private int totalAmount=-1, totalRAmount=-1, discount=-1;
    public static TextView cartitemcount1;
    BroadcastReceiver broadcastReceiver;
    View actionView;
    private ProgressDialog progress;
    int check;
    private String items = "";
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        if(localdatabase.loaded==false){
            RestoreResponse.getResponse(this);
        }

        addBottomToolbar();

        etComments = (EditText) findViewById(R.id.et_comments);
        etLane1 = (EditText) findViewById(R.id.et_lane1);
        etLane2 = (EditText) findViewById(R.id.et_lane2);
        etLocality = (EditText) findViewById(R.id.et_locality);
        btnPlaceOrder = (Button) findViewById(R.id.btn_place_order);
        btnHome = (Button) findViewById(R.id.btn_add_home);
        btnWork = (Button) findViewById(R.id.btn_add_work);
        btnCash = (Button) findViewById(R.id.btn_cash);
        btnOnline = (Button) findViewById(R.id.btn_online);
        tvTotalAmount = (TextView) findViewById(R.id.total_amount);
        sp = getSharedPreferences(constants.foodsingh, MODE_PRIVATE);

        Intent intent = getIntent();
        if(intent != null) {
             check = intent.getIntExtra("key",0);
            if(check==0){
                //Display("not here");
            }else{
                //Display("here");
            }
            if(check==0&&localdatabase.loaded) {
                Bundle aa = intent.getExtras();
                totalAmount = aa.getInt("total_amount");
                totalRAmount = aa.getInt("total_r_amount");
                discount = aa.getInt("discount");
                tvTotalAmount.setText("₹" + totalAmount);
            }else if(localdatabase.loaded){
                items = intent.getStringExtra("items");
                totalAmount = Integer.parseInt(intent.getStringExtra("price"));
                totalRAmount = 0;
                discount = 0;
                tvTotalAmount.setText("₹" + totalAmount);

            }else{
                SharedPreferences sharedPreferences =getSharedPreferences(constants.foodsingh,Context.MODE_PRIVATE);
                totalAmount = sharedPreferences.getInt("total_amount",-1);
                totalRAmount = sharedPreferences.getInt("total_r_amount",0);
                discount = sharedPreferences.getInt("discount",0);
                items = sharedPreferences.getString("items","");
                sharedPreferences.edit().remove("total_amount");
                sharedPreferences.edit().apply();
            }
        }
        else {
            finish();
        }
        updateUI();

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!statusAddress.equals("HOME")) {
                    String lane1 = etLane1.getText().toString();
                    String lane2 = etLane2.getText().toString();
                    String locality = etLocality.getText().toString();
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(statusAddress + "_lane1", lane1);
                    //editor.putString(statusAddress + "_lane2", lane2);
                    //editor.putString(statusAddress + "_locality", locality);
                    editor.commit();
                }

                statusAddress = "HOME";
                updateUI();

            }
        });

        btnWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!statusAddress.equals("WORK")) {
                    String lane1 = etLane1.getText().toString();
                    String lane2 = etLane2.getText().toString();
                    String locality = etLocality.getText().toString();
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(statusAddress + "_lane1", lane1);
                    //editor.putString(statusAddress + "_lane2", lane2);
                    //editor.putString(statusAddress + "_locality", locality);
                    editor.commit();
                }

                statusAddress = "WORK";
                updateUI();
            }
        });

        btnCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusPayment = "CASH";
                updatePaymentUI();
            }
        });

        btnOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusPayment = "ONLINE";
                updatePaymentUI();
            }
        });


        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkForErrors(v);
            }
        });

        SetupBroadcastReceiver();
    }

    private void SetupBroadcastReceiver() {

        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(constants.broaadcastReceiverMenu);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                localdatabase.notifmount = (TextView)actionView.findViewById(R.id.notification_badge);
                if(intent.getAction().equals(constants.broaadcastReceiverMenu)){


                    localdatabase.notifmount.setVisibility(View.VISIBLE);
                    localdatabase.notifmount.setText(localdatabase.notifications+"");


                    Log.i("broadcastreceiver1", localdatabase.notifications+"");
                }else if(intent.getAction().equals(constants.menu2BroadcastReceiver)){
                    localdatabase.notifmount.setVisibility(View.INVISIBLE);
                }

            }


        };

        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(constants.menu2BroadcastReceiver);

        registerReceiver(broadcastReceiver,intentFilter);
        registerReceiver(broadcastReceiver,intentFilter2);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem=menu.findItem(R.id.action_cart);
        View actionView= MenuItemCompat.getActionView(menuItem);
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
                startActivity(new Intent(CheckoutActivity.this, NotificationActivity.class));
            }
        });

        localdatabase.notifmount = (TextView)actionView.findViewById(R.id.notification_badge);
        if(localdatabase.notifications==0){
            localdatabase.notifmount.setVisibility(View.INVISIBLE);
        }else {
            localdatabase.notifmount.setVisibility(View.VISIBLE);
            localdatabase.notifmount.setText(localdatabase.notifications+"");
        }

        return true;
    }

    private void checkForErrors(View v) {
        if(TextUtils.isEmpty(etLane1.getText().toString()) || TextUtils.isEmpty(etLane2.getText().toString())
                || TextUtils.isEmpty(etLocality.getText().toString())){
            Snackbar.make(v, "Please enter your full address", Snackbar.LENGTH_LONG).show();
        }
        else {
            placeOrder();
        }
    }

    private void placeOrder() {
        String lane1 = etLane1.getText().toString();
        String lane2 = etLane2.getText().toString();
        String locality = etLocality.getText().toString();

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(statusAddress+"_lane1", lane1);
        //editor.putString(statusAddress+"_lane2", lane2);
        //editor.putString(statusAddress+"_locality", locality);
        editor.commit();

        final String address = lane1+", "+lane2+", "+locality;
        final String comments = etComments.getText().toString();
        final String mobile = sp.getString("mobile", "123");
        if(mobile.equals("123"))
        {
            Toast.makeText(this, "Number is missing, kindly logout and log in again", Toast.LENGTH_SHORT).show();
            finish();
        }


    if(check!=1) {
        items="";
        for (int i = 0; i < localdatabase.cartList.size(); i++) {
            MenuItems item = localdatabase.cartList.get(i);
            items += item.getName() + "-" + item.getQuantity();
            if (i != localdatabase.cartList.size() - 1) {
                items += ", ";
            }
        }


    }


        Log.d("DATA23432", "Address = "+address);
        Log.d("DATA23432", "Comments = "+comments);
        Log.d("DATA23432", "Mobile = "+mobile);
        Log.d("DATA23432", "Items = "+items);
        Log.d("DATA23432", "Total Amount = "+totalAmount);
        progress = new ProgressDialog(this);
        progress.setMessage("Sending Order...");
        progress.setCancelable(false);
        progress.show();

        //    Toast.makeText(this, addy+"\n"+String.valueOf(final_am)+"\n"+local_list+"\n"+mobile_number, Toast.LENGTH_SHORT).show();
        StringRequest str=new StringRequest(Request.Method.POST, constants.send_to_debian, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(progress.isShowing())
                {
                    progress.dismiss();
                }
                try {
                    JSONObject object = new JSONObject(response);
                    Log.d("DATA","response: "+response);
                    String main_status = object.getString("message");
                    String id = object.getString("id");
                    Log.d("DATA","ID -"+id);
                    if(main_status.equals("SUCCESS")){
                        Toast.makeText(CheckoutActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                      //  localdatabase.cartList.clear();
                        Intent intent = new Intent(CheckoutActivity.this, OrderPlacedActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("amount", ""+totalAmount);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        FoodItem myItem = new FoodItem(id,items,totalAmount+"",address,"Just Now");
                        Bundle b = new Bundle();
                        b.putParcelable("object",myItem);
                        intent.putExtras(b);

                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(CheckoutActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(progress.isShowing())
                {
                    progress.dismiss();
                }
                Toast.makeText(CheckoutActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> maps=new HashMap<>();
                maps.put("item",items);
                maps.put("amount", String.valueOf(totalAmount));
                maps.put("mobile",mobile);
                maps.put("address",address);
                maps.put("comments",comments);
                maps.put("kitchen_price",String.valueOf(totalRAmount));
                maps.put("discount",String.valueOf(discount));
                maps.put("location_id",localdatabase.location_id);

                return maps;
            }
        };
        RequestQueue re= Volley.newRequestQueue(this);

        if(totalRAmount<0||totalAmount<0||discount<0||items.equals("")){
            Display("Some Problem is There. Please restart the app.");
        }else
        re.add(str);
        str.setRetryPolicy(new DefaultRetryPolicy(0,  DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }


    private void Display(String s){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }


    void updateUI(){

        if(statusAddress.equals("HOME")){
            btnHome.setBackgroundResource(R.drawable.card_addition_edittext);
            btnWork.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        else if(statusAddress.equals("WORK")){
            btnWork.setBackgroundResource(R.drawable.card_addition_edittext);
            btnHome.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }

        //etLane1.setText(sp.getString(statusAddress+"_lane1",""));
       // etLane2.setText(sp.getString(statusAddress+"_lane2",""));
        //etLocality.setText(sp.getString(statusAddress+"_locality",""));
        etLocality.setText(localdatabase.sublocality);
        etLane2.setText(localdatabase.lane2);

    }

    void updatePaymentUI(){
        if(statusPayment.equals("CASH")){
            btnCash.setBackgroundResource(R.drawable.card_addition_edittext);
            btnOnline.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        else if(statusPayment.equals("ONLINE")){
            btnOnline.setBackgroundResource(R.drawable.card_addition_edittext);
            btnCash.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
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
                Intent intent = new Intent(CheckoutActivity.this, menu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckoutActivity.this, order_history.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckoutActivity.this, Support.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


    }

    @Override

    public void onPause(){
        super.onPause();
       SharedPreferences sharedPreferences =getSharedPreferences(constants.foodsingh,Context.MODE_PRIVATE);

        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("total_amount",totalAmount);
        edit.putInt("total_r_amount",totalRAmount);
        edit.putInt("discount",discount);
        edit.putString("items",items);
        edit.apply();
    }

}
