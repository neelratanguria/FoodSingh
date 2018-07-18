package com.foodsingh.mindwires.foodsingh;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class cart extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recycler,sides;
    RecyclerView.LayoutManager layout;
    public  static Sides_Adapter sidesAdapter;
    ProgressDialog couponDilog;
    Toolbar toolbar;
    Dialog dialog;
    static Button checkout;
    SharedPreferences shared;


    NavigationView navigationView;
    public static CartItemAdapter adapter;
    ProgressDialog progress;
    View actionView;
    public static TextView cartitemcount1,notifmount;
    BroadcastReceiver broadcastReceiver;
    static RelativeLayout bottomBar;
    static EditText coupon;
    static Button enterCoupon;
    //TextView tvCouponCode;
    static TextView tvDisAmt;

    static View sideView;

    static TextView tvDeliveryCharge, tvTotalAmount, tvTotalAmount2, tvTax_amount;

    static int totalAmount;
    static int delivery_charges;
    static int totalRAmount;
    static int subtotalAmount;
    static int stax_amount;
    static int amount_for_free_delivery;
    static int free_item_alert;
    static int amount_for_free_item;
    static int discountAmount = 0;
    static int discountPercent = 0;
    static int taxamount = 0;
    LinearLayout availableOptions;


    //this part is to be added and removed according to the meta deta,
    RelativeLayout img_temp_relative;
    ImageView img_temp;
/////////////////////////////////////////

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //////////////////////////////////



//Toast.makeText(this, constants.item_quant_deb.size()+"",Toast.LENGTH_LONG).show();

        //////////////////////


        setContentView(R.layout.activity_cart);



        Typeface t = Typeface.createFromAsset(getAssets(), "fonts/android.ttf");
        TextView toolbarText = (TextView) findViewById(R.id.toolbarText);
        toolbarText.setTypeface(t);
        ///////////////////////////////////////////////////////////////////////////////
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        progress = new ProgressDialog(this);
        addBottomToolbar();
        ////////////////////////////////////////////////////////////////

        //CODING CODING CODING
        initialize();

//        tvCouponCode.setText(localdatabase.couponCode);
        send_to_adapter();

        if (localdatabase.sidesList.size() == 0){
            getSides();
        }

        if(localdatabase.metaData!=null) {

            if (localdatabase.metaData.getservice().equals("true")) {
                checkout.setClickable(false);
            }
        }else{
            Intent i = new Intent(this, login_page.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    Toast.makeText(cart.this, "Order place krna  hai yahan se", Toast.LENGTH_SHORT).show();
       /*         int sum=0;
                for(int i=0;i<constants.items_price.size();i++) {
                    sum= sum+Integer.parseInt((constants.items_price.get(i)).substring(3));
                } */

                //    Toast.makeText(cart.this,String.valueOf(sum), Toast.LENGTH_SHORT).show();

                if(localdatabase.amount==0)
                {
                    Display("Sorry, no item in your cart, you cant proceed");
                }
                else if(localdatabase.metaData.getservice().equals("false")){
                    showDialog(cart.this, localdatabase.kitchenClosedText, R.drawable.store);
                }else if(totalAmount < Integer.parseInt(localdatabase.metaData.getMin_Order())){
                    showDialog2(cart.this, "Sorry, minimum order amount is ₹"+localdatabase.metaData.getMin_Order());
                }
                else
                {
                    Intent aas=new Intent(getApplicationContext(),CheckoutActivity.class);
                    aas.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    aas.putExtra("key",0);
                    Bundle a=new Bundle();
                    a.putInt("total_amount", totalAmount);
                    a.putInt("total_r_amount", totalRAmount);
                    a.putInt("discount", discountAmount);

                    aas.putExtras(a);
                    startActivity(aas);

                }


            }
        });

        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);

        }
        manipulatenavigationdrawer();

        enterCoupon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(localdatabase.metaData.getservice().equals("false")){
                    showDialog(cart.this, localdatabase.kitchenClosedText, R.drawable.store);
                }else {

                    couponDilog = new ProgressDialog(v.getContext());
                    couponDilog.setMessage("Validating your coupon.");
                    getDiscount(constants.coupon_url);
                }


               /* if(coupon.getText().toString().equals(localdatabase.couponCode) && localdatabase.cartList.size()>0){
                    discountPercent = localdatabase.discount;
                    calculateTotal();

                    Toast.makeText(cart.this, "Congratulations. "+discountPercent+"% discount applied.", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(cart.this, "Invalid coupon code. Please try again.", Toast.LENGTH_LONG).show();
                }*/
            }
        });

        availableOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(cart.this, NotificationActivity.class));
            }
        });

        SetupBroadcastReceiver();
    }

    public static void updateCartIcon(){
        cartitemcount1.setText(String.valueOf(localdatabase.cartList.size()));
    }

    private void getDiscount(String url){

        if(progress.isShowing())
        {
            progress.dismiss();
        }
        progress.setMessage("Validating Coupon Code..");
        progress.setCancelable(false);
        progress.show();

        final SharedPreferences sharedPreferences = getSharedPreferences(constants.foodsingh, Context.MODE_PRIVATE);

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(progress.isShowing())
                {
                    progress.dismiss();
                }

                if(!couponDilog.isShowing()){
                    couponDilog.dismiss();
                }

                if(localdatabase.cartList.size()>0){


                    try {
                        JSONObject obj = new JSONObject(response);
                        String result = obj.getString("result");

                        if(result.equals("true")){
                            //    showDialog2(cart.this, "Sorry, you have entered an\ninvalid coupon.");
                            String messg=obj.getString("message");

                            String dis= obj.getString("new_total_discount");
                            String deli=obj.getString("new_delivery_charge");
                            String total=obj.getString("new_total_amount");

                            showDialog_coupon(cart.this,messg,dis,deli,total);

                        }else if(result.equals("false")){
                            String mess=obj.getString("message");
                            showDialog2(cart.this, mess);

                        }
                        else if(result.equals("0"))
                        {
                            String messg=obj.getString("message");
                            showDialog2(cart.this,messg);
                        }


                    } catch (JSONException e) {
                        if(!couponDilog.isShowing()){
                            couponDilog.dismiss();
                        }
                        e.printStackTrace();
                        showDialog2(cart.this, "Some error occured, may be due to server or bad internet connection");
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(progress.isShowing())
                {
                    progress.dismiss();
                }

                if(!couponDilog.isShowing()){
                    couponDilog.dismiss();
                }
                showDialog2(cart.this,"Some error occured, may be due to server or bad internet connection");
            }
        }){
            @Override
            public Map getParams(){
                Map<String, String> map = new HashMap<>();
                map.put("mobile",sharedPreferences.getString("mobile",""));
                map.put("coupon",coupon.getText().toString());
                map.put("total_amount", String.valueOf(totalAmount));
                map.put("delivery_charge", String.valueOf(localdatabase.deliveryCharge));
                map.put("location_id",localdatabase.location_id);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(sr);

    }

    private void Display(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void SetupBroadcastReceiver() {

        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(constants.broaadcastReceiverMenu2);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(constants.broaadcastReceiverMenu2)) {


                    notifmount.setVisibility(View.VISIBLE);
                    notifmount.setText(localdatabase.notifications + "");


                    Log.i("broadcastreceiver1", localdatabase.notifications + "");
                } else if (intent.getAction().equals(constants.broadCastReceiverNotification2)) {
                    notifmount.setVisibility(View.INVISIBLE);
                }

            }


        };

        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(constants.broadCastReceiverNotification2);

        registerReceiver(broadcastReceiver,intentFilter);
        registerReceiver(broadcastReceiver,intentFilter2);
    }

    public void showDialog2(Context activity, String msg){
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
                dialog.dismiss();
            }
        });


        image.setText("Done");
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });

        dialog.show();

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

        ImageView notif = (ImageView)actionView.findViewById(R.id.notif);

        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(cart.this, NotificationActivity.class));
            }
        });

        notifmount = (TextView)actionView.findViewById(R.id.notification_badge);
        if(localdatabase.notifications==0){
            notifmount.setVisibility(View.INVISIBLE);
        }else {
            notifmount.setVisibility(View.VISIBLE);
            notifmount.setText(localdatabase.notifications+"");
        }

        return true;
    }



    private void getSides() {
        progress.setMessage("Please wait...");
        progress.show();
        StringRequest str=new StringRequest(Request.Method.POST, constants.get_sides, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(cart.this, response, Toast.LENGTH_LONG).show();
                if(progress.isShowing())
                {
                    progress.dismiss();
                }
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject jo = jsonArray.getJSONObject(i);
                        String id = jo.getString("id");
                        String name = jo.getString("name");
                        String category = jo.getString("category");
                        String price = jo.getString("price");
                        String image = jo.getString("image");
                        //String status = jo.getString("status");
                        String status = "live";
                        String detail = "NA";
                        MenuItems item = new MenuItems(id, name, category, price, image, status, detail, price,localdatabase.location_id);
                        localdatabase.sidesList.add(item);

                    }

                    //Toast.makeText(cart.this, ""+localdatabase.sidesList.size(), Toast.LENGTH_LONG).show();
                    //Toast.makeText(cart.this, ""+localdatabase.sidesList.get(2).getName(), Toast.LENGTH_LONG).show();
                    sidesAdapter.notifyDataSetChanged();

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

            }
        }){
            @Override
            public Map<String,String> getParams(){
                Map<String,String> map = new HashMap<>();
                map.put("location_id",localdatabase.location_id);
                return map;
            }
        };

        RequestQueue re= Volley.newRequestQueue(this);
        re.add(str);

    }

    public static void calculateTotal(){

        totalAmount=0;
        totalRAmount = 0;
        discountAmount = 0;
        stax_amount = 0;
        delivery_charges = 0;
        subtotalAmount = 0;

        if(localdatabase.cartList.size()>0) {
            checkout.setClickable(true);
            checkout.setBackgroundResource(R.drawable.back_checkout);
            for (int i = 0; i < localdatabase.cartList.size(); i++) {
                MenuItems item = localdatabase.cartList.get(i);
                totalAmount += item.getQuantity() * Integer.parseInt(item.getPrice());
                subtotalAmount += item.getQuantity() * Integer.parseInt(item.getPrice());
                totalRAmount += item.getQuantity() * Integer.parseInt(item.getR_price());
                stax_amount = localdatabase.stax_amount;
                taxamount =(int)(subtotalAmount*stax_amount/100);
                totalAmount = subtotalAmount + taxamount;
            }

            if(totalAmount >= amount_for_free_item){
                //free item msg
            }

            if(localdatabase.deliveryCharge == 0 || totalAmount == 0 || totalAmount >= amount_for_free_delivery) {
                tvDeliveryCharge.setText("Free delivery");
                tvDeliveryCharge.setBackgroundColor(Color.parseColor("#E329F599"));
                delivery_charges = 0;
            }
            else {
                tvDeliveryCharge.setText("Delivery charge ₹ " + localdatabase.deliveryCharge);
                tvDeliveryCharge.setBackgroundColor(Color.parseColor("#E329F599"));
                delivery_charges = localdatabase.deliveryCharge;
            }
            amount_for_free_delivery = localdatabase.metaData.getamount_for_free_delivery();
            totalAmount = totalAmount + delivery_charges;
            localdatabase.amount = totalAmount;
            discountAmount =(int)(localdatabase.amount*((float)discountPercent/100));
            totalAmount = localdatabase.amount - discountAmount;


            Log.d("sdsd","discount -"+discountAmount);
            Log.d("sdsd","discount % "+discountPercent);
            Log.d("sdsd","totalRAmount :"+totalRAmount);
            Log.d("sdsd","totalAmount :"+totalAmount);

        }
        else {
            checkout.setClickable(false);
            checkout.setBackgroundResource(R.drawable.back_checkout_grey);
        }

        if(totalAmount == 0) {
            tvTax_amount.setText(" ₹ 0");
            tvDeliveryCharge.setText("Your cart is empty.");
            tvDeliveryCharge.setBackgroundColor(Color.parseColor("#ff6861"));
        }
        else {
            tvTax_amount.setText(" ₹ " + taxamount);
        }

        tvTotalAmount.setText(" ₹ " + subtotalAmount);
        tvTotalAmount2.setText("₹" + totalAmount);

        if (discountAmount == 0 || totalAmount == 0){
            tvDisAmt.setText(" ₹ 0");
        }
        else {
            tvDisAmt.setText(" ₹ "+(int)discountAmount);
        }


    }



    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/android.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(broadcastReceiver!=null)
            unregisterReceiver(broadcastReceiver);
    }

    private void manipulatenavigationdrawer() {
        View v = navigationView.getHeaderView(0);
        Typeface tp = Typeface.createFromAsset(getAssets(), "fonts/COPRGTB.TTF");
        TextView t = (TextView) v.findViewById(R.id.welcome);
        t.setTypeface(tp);
        TextView location = (TextView)v.findViewById(R.id.location);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/OratorStd.otf");
        location.setTypeface(tf);

        SharedPreferences sh=getSharedPreferences(localdatabase.shared_location_key,MODE_PRIVATE);
        String city=sh.getString("location","No Location Found");
        location.setText(city);

        ImageView back = (ImageView)v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawers();
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences(constants.foodsingh, Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name","_");
        if(!name.equals("_")){
            t.setText("Hello, "+name);
        }
    }

    private void initialize() {
        Typeface t = Typeface.createFromAsset(getAssets(), "fonts/android.ttf");
        checkout=(Button)findViewById(R.id.btn_checkout);
        checkout.setTypeface(t);

        recycler=(RecyclerView)findViewById(R.id.recyclerView_cart);
        sides=(RecyclerView)findViewById(R.id.sides_recycler);
        layout=new GridLayoutManager(this,1);
        recycler.setLayoutManager(layout);

        sides.setNestedScrollingEnabled(true);
        recycler.setNestedScrollingEnabled(true);
        shared=getSharedPreferences(constants.foodsingh,MODE_PRIVATE);
        tvTotalAmount = (TextView) findViewById(R.id.total_amount);
        tvTotalAmount2 = (TextView) findViewById(R.id.total_amount_2);
        tvDeliveryCharge = (TextView)findViewById(R.id.delivery_charge);
        tvTax_amount = (TextView)findViewById(R.id.tax_amount);

        bottomBar = (RelativeLayout) findViewById(R.id.place_order);
        coupon = (EditText) findViewById(R.id.code);
        enterCoupon = (Button) findViewById(R.id.enter);
        //tvCouponCode = (TextView) findViewById(R.id.txt_coupon_code);
        tvDisAmt = (TextView) findViewById(R.id.dis_amt);
        availableOptions = (LinearLayout) findViewById(R.id.grp_available_options);
        calculateTotal();

        //intialization has been done here for these both
        img_temp_relative=(RelativeLayout)findViewById(R.id.img_rev);
        img_temp=(ImageView)findViewById(R.id.img_temp);
        //setting the image between th views in CART

        Boolean status=true;

        if(localdatabase.cartCoupon.equalsIgnoreCase("true"))
        {
            status=true;
        }
        else
        {
            status=false;
        }

        if(status)
        {
            img_temp_relative.setVisibility(View.VISIBLE);
            //now set the image in the imageview here.
            String urk=localdatabase.couponClassList.get(2).getUrl();
            Glide.with(this).load(urk).into(img_temp);
            img_temp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String cc=localdatabase.couponClassList.get(2).getCouponCode();

                    //receive the text from localdatabase
                    setClipboard(getApplicationContext(),cc);
                    Display("Coupon copied to ClipBoard");
                }
            });

        }
        else
        {
            img_temp_relative.setVisibility(View.GONE);
        }
        //////////////////////////////////////////////////////////

    }

    private void send_to_adapter()
    {
        adapter = new CartItemAdapter(this, localdatabase.cartList);
        recycler.setAdapter(adapter);
        sidesAdapter = new Sides_Adapter(this, localdatabase.sidesList);
        sides.setAdapter(sidesAdapter);
        sidesAdapter.notifyDataSetChanged();
        sides.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        sides.setItemAnimator(new DefaultItemAnimator());
        recycler.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void onPause(){
        super.onPause();
        Carerestore restore = new Carerestore(localdatabase.cartList,localdatabase.sidesList);
        Gson gson = new Gson();
        String temp = gson.toJson(restore);
        SharedPreferences sharedPreferences = getSharedPreferences(constants.foodsingh,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(constants.cartrestore,temp);
        edit.apply();
    }




    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //writing function for Categories



    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //   if (id == R.id.action_settings) {
        //     return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        SharedPreferences shared=getSharedPreferences(constants.foodsingh,MODE_PRIVATE);
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu) {
            Intent a=new Intent(getApplicationContext(),menu.class);
            a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
        } else if (id == R.id.cart) {


        } else if (id == R.id.orders) {
            Intent a=new Intent(getApplicationContext(),order_history.class);
            a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);


        } else if (id == R.id.SignOut) {

            shared.edit().remove("address").apply();
            shared.edit().remove("password").apply();
            shared.edit().remove("mobile").apply();

            this.finish();
            Intent intent=new Intent(getApplicationContext(),login_page.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();


        }
        else if(id==R.id.details)
        {
            Intent a=new Intent(getApplicationContext(),details.class);
            a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);

        }else if(id==R.id.notifications){
            final Intent a=new Intent(getApplicationContext(),NotificationActivity.class);
            a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(a);
                }
            },1000);

        }else if(id==R.id.favNav){
            Intent as=new Intent(this,menu_category_wise.class);
            Bundle a=new Bundle();
            a.putString("category","Favourites");
            a.putInt("position", -1);
            as.putExtras(a);
            as.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(as);
        } else if (id == R.id.AboutUs) {
            Intent a = new Intent(getApplicationContext(), about_us.class);
            a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
        }
        else if (id == R.id.Support) {
            Intent a = new Intent(getApplicationContext(), Support.class);
            a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
        }
        else if(id==R.id.share)
        {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "FoodSingh");
                String sAux = "\n"+localdatabase.share_text+"\n\n";
                sAux = sAux + localdatabase.share_url+"\n\n";
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                Intent intent = new Intent(cart.this, menu.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cart.this, order_history.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });


        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cart.this, Support.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });


    }


    public void showDialog(Activity activity, String msg,int pic){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/OratorStd.otf");

        text.setTypeface(tf);

        ImageView image = (ImageView) dialog.findViewById(R.id.btn_dialog);
        image.setImageBitmap(BitmapFactory.decodeResource(getResources(),pic));
        TextView dialogButton = (TextView)dialog.findViewById(R.id.cancel);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void setClipboard(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }


    public void showDialog_coupon(final Activity activity, String msg, final String disc, String deliv, final String tota){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialogcoupon);

        TextView dis = (TextView) dialog.findViewById(R.id.discount);
        TextView dilv = (TextView) dialog.findViewById(R.id.delivery);
        TextView total = (TextView) dialog.findViewById(R.id.total);
        TextView mss=(TextView) dialog.findViewById(R.id.text_dialog);

        dis.setText("Discount is Rs."+disc);
        dilv.setText("Delivery charge is Rs."+deliv);
        total.setText("Total Amount is Rs."+tota);
        mss.setText(msg);

        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/OratorStd.otf");
        dis.setTypeface(tf);
        dilv.setTypeface(tf);
        total.setTypeface(tf);
        mss.setTypeface(tf);

        TextView dialogButton = (TextView)dialog.findViewById(R.id.cancel);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //      Toast.makeText(getApplicationContext(), coupon.getText().toString(), Toast.LENGTH_SHORT).show();

                final SharedPreferences sharedPreferences=getSharedPreferences(constants.foodsingh,MODE_PRIVATE);
                if(progress.isShowing())
                {
                    progress.dismiss();
                }
                progress.setMessage("Wait for a while.....");
                progress.setCancelable(false);
                progress.show();

                StringRequest str=new StringRequest(Request.Method.POST, constants.remove_coupon, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(progress.isShowing())
                        {
                            progress.dismiss();
                        }

                        try {
                            JSONObject a=new JSONObject(response);
                            String s=a.getString("result");
                            if(s.equalsIgnoreCase("true"))
                            {
                                dialog.dismiss();
                            }
                            else
                            {
                                Display("Some error occured, kindly close it again.");
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
                        Display("Unsuccessfull operation, please cancel it again");
                    }
                }){

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String,String> maps=new HashMap<>();
                        maps.put("mobile",sharedPreferences.getString("mobile","38245"));
                        maps.put("coupon",coupon.getText().toString());
                        return maps;
                    }
                };

                RequestQueue r=Volley.newRequestQueue(getApplicationContext());
                r.add(str);

            }
        });

        TextView btn_proceed=(TextView) dialog.findViewById(R.id.btn_dialog);

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent aas=new Intent(getApplicationContext(),CheckoutActivity.class);
                aas.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                aas.putExtra("key",0);
                Bundle a=new Bundle();
                a.putInt("total_amount", Integer.parseInt(tota));
                a.putInt("total_r_amount", totalRAmount);
                a.putInt("discount", Integer.parseInt(disc));
                aas.putExtras(a);
                startActivity(aas);
                finish();
            }
        });


        dialog.show();

    }
}
