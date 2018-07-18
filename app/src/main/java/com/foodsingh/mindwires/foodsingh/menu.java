package com.foodsingh.mindwires.foodsingh;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.DisplayMetrics;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,ViewPager.OnPageChangeListener, AppBarLayout.OnOffsetChangedListener {



    ProgressDialog progress;
    RecyclerView recylerView;
    RecyclerView.LayoutManager layoutmanager;
    List<String> categories=new ArrayList<>();

    Thread t;


    public static TextView noitem;

    List<String> images=new ArrayList<>();
    categoryAdapter adapter;
    TextView drinks;
    BroadcastReceiver broadcastReceiver;

    localdatabase local;
    ImageButton next, back;

    ImageView ad1, ad2;

    int counter_button=0;
    int counter_button2=0;
    int counter_button3=0;
    int pageritem = 0;
    int counter_button4=0;
    boolean nav=true;
    TextView attack;
    private GcmNetworkManager gcmNetworkManager;
    pagerAdapter pageradater;
    NavigationView navigationView;
    TextView location;
    ViewPagerCustomDuration pager;
    static int[] dotPositions = {R.id.view_pager_1, R.id.view_pager_2,R.id.view_pager_3,R.id.view_pager_4,R.id.view_pager_5,R.id.view_pager_6};
    AppBarLayout appBarLayout;
    public static int width;
    public static TextView cartitemcount1;
    View actionView;
    boolean swipe = false;
    Handler handler;
    Runnable runnable;
    RecyclerView.ItemDecoration itemDecoration;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences shared;

    TextView cuisine_btn;
    TextView time_btn;
    TextView combo_btn;


     int c=0;


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        if(localdatabase.loaded==false){
            RestoreResponse.getResponse(this);

        }
    }

    private void load(final String url){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        RemoveTop();

        setContentView(R.layout.activity_menu);

        if(localdatabase.loaded==false){
            RestoreResponse.getResponse(this);

        }

        AddressFetchingService.startActionFoo(this,localdatabase.deliveryLocation.getLatitude()+"",localdatabase.deliveryLocation.getLongitude()+"");

        noitem = (TextView)findViewById(R.id.noitem);
        //Display(versionStatus());

      //

//FOR NAVIGATION DRAWERo
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

        ///////////////////////////////////////////////////////////////////////////////////////
         manipulatenavigationdrawer();

        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            applyFontToMenuItem(mi);
        }

        nav=true;

        attack = (TextView)findViewById(R.id.attack);
        handler = new Handler();
        initialize();

        Typeface tf1 = Typeface.createFromAsset(getAssets(),"fonts/OratorStd.otf");

        attack.setTypeface(tf1);

        next = (ImageButton) findViewById(R.id.right_arrow);
        back = (ImageButton) findViewById(R.id.left_arrow);
        width = getScreenWidth();
        if(checking_net_permission())
        {
            getting_categories();

        }
        else {
            nav=false;
            setContentView(R.layout.no_internet);
            Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/android.ttf");
            TextView calm = (TextView)findViewById(R.id.calm);
            final TextView retry = (TextView)findViewById(R.id.menu);
            calm.setTypeface(tf);
            retry.setTypeface(tf);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recreate();
                }
            });

        }
        local = new localdatabase();
        pageradater = new pagerAdapter(local);
        pager.setAdapter(pageradater);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = pager.getCurrentItem() + 1;
                if(current == localdatabase.BannerUrls.size()) {
                    pager.setCurrentItem(0);
                    getPositionsForDots(0);
                }else{
                    pager.setCurrentItem(current);
                    getPositionsForDots(current%dotPositions.length);
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = pager.getCurrentItem() - 1;
                if (current >= 0) {
                    pager.setCurrentItem(current);
                    getPositionsForDots(current % dotPositions.length);
                }
                else{
                    pager.setCurrentItem(localdatabase.BannerUrls.size()-1);
                    getPositionsForDots(dotPositions.length-1);
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe = true;
                recylerView.removeItemDecoration(itemDecoration);
                categories.clear();
                images.clear();
                getting_categories();

            }
        });

        cuisine_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter_button++;
                counter_button2=0;
                counter_button3=0;
                counter_button4=0;

                if((counter_button)%2==0)
                {
                    //ununselected
                    cuisine_btn.setBackgroundResource(R.drawable.sort_back_gray);
                    categories.clear();
                    images.clear();
                    send_to_adapter();
                    getting_categories();

                }
                else
                {
                    //selected
                    cuisine_btn.setBackgroundResource(R.drawable.sort_back_green);
                    drinks.setBackgroundResource(R.drawable.sort_back_gray);
                    combo_btn.setBackgroundResource(R.drawable.sort_back_gray);
                    time_btn.setBackgroundResource(R.drawable.sort_back_gray);


                    categories.clear();
                    images.clear();

                    send_to_adapter();

                    for(int i=0;i<localdatabase.masterList.size();i++)
                    {
                        MasterMenuItems a=localdatabase.masterList.get(i);
                        if(a.getCuisine().equals("1"))
                        {
                            if(categories.contains(a.getName()) & images.contains(a.getImage()))
                            {

                            }
                            else
                            {
                                categories.add(a.getName());
                                images.add(a.getImage());

                            }

                        }
                    }
                    send_to_adapter();



                }//     cuisine_btn.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.cuisine));
            }
        });
        time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                counter_button2++;
                counter_button=0;
                counter_button4=0;
                counter_button3=0;
                if((counter_button2)%2==0)
                {
                    //unelected
                    time_btn.setBackgroundResource(R.drawable.sort_back_gray);
                    categories.clear();
                    images.clear();
                    send_to_adapter();
                    getting_categories();

                }
                else
                {
                    //selected
                    cuisine_btn.setBackgroundResource(R.drawable.sort_back_gray);
                    drinks.setBackgroundResource(R.drawable.sort_back_gray);
                    combo_btn.setBackgroundResource(R.drawable.sort_back_gray);
                    time_btn.setBackgroundResource(R.drawable.sort_back_green);


                    categories.clear();
                    images.clear();

                    send_to_adapter();
                    for(int i=0;i<localdatabase.masterList.size();i++)
                    {
                        MasterMenuItems a=localdatabase.masterList.get(i);
                        if(a.getTime().equals("1"))
                        {
                            if(categories.contains(a.getName()) & images.contains(a.getImage()))
                            {

                            }
                            else
                            {
                                categories.add(a.getName());
                                images.add(a.getImage());

                            }
                        }
                    }
                    send_to_adapter();


                }
            }
        });

        drinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  cuisine_btn.setBackgroundResource(R.drawable.menu_button);
                counter_button4++;
                counter_button=0;
                counter_button2=0;
                counter_button3=0;
                if((counter_button4)%2==0)
                {
                    //unselected
                    drinks.setBackgroundResource(R.drawable.sort_back_gray);
                    categories.clear();
                    images.clear();
                    send_to_adapter();
                    getting_categories();
                }
                else
                {
                    cuisine_btn.setBackgroundResource(R.drawable.sort_back_gray);
                    drinks.setBackgroundResource(R.drawable.sort_back_green);
                    combo_btn.setBackgroundResource(R.drawable.sort_back_gray);
                    time_btn.setBackgroundResource(R.drawable.sort_back_gray);


                    categories.clear();
                    categories.clear();
                    images.clear();
                    send_to_adapter();

                    for(int i=0;i<localdatabase.masterList.size();i++)
                    {
                        MasterMenuItems a=localdatabase.masterList.get(i);
                        if(a.getDrinks().equals("1"))
                        {
                            if(categories.contains(a.getName()) & images.contains(a.getImage()))
                            {

                            }
                            else
                            {
                                categories.add(a.getName());
                                images.add(a.getImage());

                            }}
                    }
                    send_to_adapter();


                }

            }
        });

        combo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  cuisine_btn.setBackgroundResource(R.drawable.menu_button);
                counter_button4=0;
                counter_button=0;
                counter_button2=0;
                counter_button3++;
                if((counter_button3)%2==0)
                {
                    //unselected
                    combo_btn.setBackgroundResource(R.drawable.sort_back_gray);
                    categories.clear();
                    images.clear();
                    send_to_adapter();
                    getting_categories();
                }
                else
                {
                    cuisine_btn.setBackgroundResource(R.drawable.sort_back_gray);
                    drinks.setBackgroundResource(R.drawable.sort_back_gray);
                    combo_btn.setBackgroundResource(R.drawable.sort_back_green);
                    time_btn.setBackgroundResource(R.drawable.sort_back_gray);


                    categories.clear();
                    categories.clear();
                    images.clear();
                    send_to_adapter();

                    for(int i=0;i<localdatabase.masterList.size();i++)
                    {
                        MasterMenuItems a=localdatabase.masterList.get(i);
                        if(a.getCombo().equals("1"))
                        {
                            if(categories.contains(a.getName()) & images.contains(a.getImage()))
                            {

                            }
                            else
                            {
                                categories.add(a.getName());
                                images.add(a.getImage());

                            }}
                    }
                    send_to_adapter();


                }

            }
        });

      //  setUpJobScheduler();

        SetupBroadcastReceiver();

            setupGcmNetWorkManager();


        if(localdatabase.couponClassList.size()>0){
          //  LoadBitmaps();
        }

        ad1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String h= String.valueOf(localdatabase.couponClassList.get(0).getCouponCode());
           //     Toast.makeText(menu.this, h, Toast.LENGTH_SHORT).show();
                if(h.equalsIgnoreCase(""))
                {
                    //nothing to do
                }
                else
                {
                    setClipboard(getApplicationContext(),h);
                    Toast.makeText(menu.this, "Coupon Code copied to clipboard", Toast.LENGTH_SHORT).show();
                }

            }
        });

        ad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String h= String.valueOf(localdatabase.couponClassList.get(1).getCouponCode());
            //    Toast.makeText(menu.this, h, Toast.LENGTH_SHORT).show();
                if(h.equalsIgnoreCase(""))
                {
                    //nothing
                }
                else
                {
                    setClipboard(getApplicationContext(),h);
                    Toast.makeText(menu.this, "Coupon Code copied to clipboard", Toast.LENGTH_SHORT).show();

                }
            }
        });


        infiniteViewPager();


    }

    private void infiniteViewPager() {
    }



    Runnable run;
    private void SetupBroadcastReceiver() {

        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(constants.broaadcastReceiverMenu);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if(intent.getAction().equals(constants.broaadcastReceiverMenu)){

                        localdatabase.notifmount = (TextView)actionView.findViewById(R.id.notification_badge);
                        localdatabase.notifmount.setVisibility(View.VISIBLE);
                        localdatabase.notifmount.setText(localdatabase.notifications+"");


                    Log.i("broadcastreceiver1menu", localdatabase.notifications+"");
                }else if(intent.getAction().equals(constants.menu2BroadcastReceiver)){
                    localdatabase.notifmount = (TextView)actionView.findViewById(R.id.notification_badge);
                    localdatabase.notifmount.setVisibility(View.INVISIBLE);
                    Log.i("broadcastreceiver1menu2", localdatabase.notifications+"");
                }else if(intent.getAction().equals(constants.menugetcitybroadcast)){

                    SharedPreferences sh=getSharedPreferences(localdatabase.shared_location_key,MODE_PRIVATE);
                    String city=sh.getString("location","No Location Found");
                    location.setText(localdatabase.sublocality);

                    Log.i("broadcastreceiver1menu3", city+"");
                }else if(intent.getAction().equals(constants.kitchenStatusBroadcast)) {
                    Log.i("broadcastreceiver1menu4", localdatabase.metaData.getservice() + "");

                    if (local.metaData != null) {
                        if (local.metaData.getservice().equals("true")) {
                            // Toast.makeText(this, "Kitchen is Open", Toast.LENGTH_SHORT).show();
                            attack.setText("KITCHEN IS OPEN");
                            attack.setBackgroundColor(Color.parseColor("#7ee591"));
                            //showDialog(this,"Kitchen is Closed\nPlease come back from 6 to 10",R.drawable.store);
                        }else{
                            attack.setText("KITCHEN IS CLOSED");
                            attack.setBackgroundColor(Color.parseColor("#56FF0000"));
                        }

                    }
                }
            }


        };

        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(constants.menu2BroadcastReceiver);

        IntentFilter intentFilter3 = new IntentFilter();
        intentFilter3.addAction(constants.menugetcitybroadcast);

        IntentFilter serviceFilter = new IntentFilter();
        serviceFilter.addAction(constants.kitchenStatusBroadcast);

        registerReceiver(broadcastReceiver,intentFilter);
        registerReceiver(broadcastReceiver,intentFilter2);
        registerReceiver(broadcastReceiver,intentFilter3);
        registerReceiver(broadcastReceiver,serviceFilter);
    }


    private void RemoveTop(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    private String versionStatus(){
        SharedPreferences sharedPreferences = getSharedPreferences(constants.foodsingh, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        return sharedPreferences.getString("version","null");
    }

    private void manipulatenavigationdrawer() {
      //  final Handler h = new Handler();

        View v = navigationView.getHeaderView(0);
        Typeface tp = Typeface.createFromAsset(getAssets(), "fonts/COPRGTB.TTF");
        TextView t = (TextView) v.findViewById(R.id.welcome);

        t.setTypeface(tp);
        location = (TextView)v.findViewById(R.id.location);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/OratorStd.otf");
        location.setTypeface(tf);

        SharedPreferences sh=getSharedPreferences(localdatabase.shared_location_key,MODE_PRIVATE);
        String city=(sh.getString("location","No Location Found"));
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
            name.replace(" ","\n");
            t.setText("Hello, "+name);
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/android.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.i("getting unregistered","stopped");
    }

    private void setupGcmNetWorkManager(){
        gcmNetworkManager = GcmNetworkManager.getInstance(this);
        Task task = new PeriodicTask.Builder()
                .setService(KitchenServiceLollipop.class)
                .setPeriod(5)
                .setFlex(10)
                .setTag(constants.kitchenStatusBroadcast)
                .setPersisted(true)
                .build();

        gcmNetworkManager.schedule(task);
    }

    private void initialize() {
        ad1  = (ImageView)findViewById(R.id.advertisement1);
        ad2 = (ImageView)findViewById(R.id.advertisement2);

        if(localdatabase.couponClassList.size()>0)
        Glide.with(this).load(localdatabase.couponClassList.get(0).getUrl()).into(ad1);
        if(localdatabase.couponClassList.size()>1)
        Glide.with(this).load(localdatabase.couponClassList.get(1).getUrl()).into(ad2);
        pager = (ViewPagerCustomDuration) findViewById(R.id.view_pager);
        pager.addOnPageChangeListener(this);
        appBarLayout = (AppBarLayout)findViewById(R.id.appbar);
        drinks = (TextView) findViewById(R.id.drinks);
        if(localdatabase.drinks.equals("false")){
            drinks.setVisibility(View.GONE);
            drinks.setClickable(false);
        }
        progress=new ProgressDialog(this);
        progress.setCancelable(false);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);
        //swipeRefreshLayout.canScrollVertically()
        recylerView=(RecyclerView) findViewById(R.id.recyclerView);
        recylerView.setFocusable(false);
        layoutmanager=new LinearLayoutManager(this);
        recylerView.setLayoutManager(layoutmanager);
        recylerView.setNestedScrollingEnabled(false);
        recylerView.setItemAnimator(new DefaultItemAnimator());

        setTypeface();
        shared=getSharedPreferences(constants.foodsingh,MODE_PRIVATE);

        cuisine_btn=(TextView) findViewById(R.id.cuisine);
        time_btn=(TextView) findViewById(R.id.time);
        combo_btn=(TextView) findViewById(R.id.combo);

        if(localdatabase.superCategoriesList.size()>3) {

            cuisine_btn.setText(localdatabase.superCategoriesList.get(0).getName());
            time_btn.setText(localdatabase.superCategoriesList.get(1).getName());
            combo_btn.setText(localdatabase.superCategoriesList.get(2).getName());
            drinks.setText(localdatabase.superCategoriesList.get(3).getName());
        }else{
            Intent i = new Intent(this, login_page.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }
        pager.setScrollDurationFactor(3.5);
        pager.setCurrentItem(pageritem);
        getPositionsForDots(pageritem);

        runnable = new Runnable() {
            @Override
            public void run() {
                pageritem++;
                if(pageritem==6){
                    local = new localdatabase();
                    pageradater = new pagerAdapter(local);
                    pager.setAdapter(pageradater);
                    pageritem=0;
                }
                pager.setCurrentItem(pageritem);
                getPositionsForDots(pageritem);
                handler.postDelayed(runnable,5000);
            }
        };

    handler.postDelayed(runnable,2500);

    }



    public void showDialog(Activity activity, String msg,int pic, String cancel){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);
        // used to show the dialog box at bottom

//        Window window = dialog.getWindow();
//        WindowManager.LayoutParams wlp = window.getAttributes();
//        wlp.gravity = Gravity.BOTTOM;
//        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        window.setAttributes(wlp);
//
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/OratorStd.otf");
        text.setTypeface(tf);
        TextView cancel11 = (TextView) dialog.findViewById(R.id.cancel);
        cancel11.setText(cancel);
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

    private void getPositionsForDots(int position){

        for (int i=0; i<dotPositions.length; i++){
            ImageView temp = (ImageView)findViewById(dotPositions[i]);
            temp.setAlpha(0.5f);
        }
        ImageView temp = (ImageView)findViewById(dotPositions[position]);
        temp.setAlpha(1f);

    }

    private void setTypeface(){
        ImageView t1 = (ImageView) findViewById(R.id.new_location);
        TextView t2 = (TextView)findViewById(R.id.attack);
        Typeface t = Typeface.createFromAsset(getAssets(), "fonts/android.ttf");
       // t1.setTypeface(t);
        //t2.setTypeface(t);
    }

    public int getScreenWidth() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        return width;
    }

    public static int showRandomInteger(int aStart, int aEnd, Random aRandom) {
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = (long) aEnd - (long) aStart + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * aRandom.nextDouble());
        int randomNumber = (int) (fraction + aStart);
        return randomNumber;
    }

    private void getting_categories() {
        if(c==0)
        {
            getting_service_status();
            c=1;
        }

        if (!swipeRefreshLayout.isRefreshing())
        {
            progress.setMessage("Fetching Data.....");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.show();
        }

                if (progress.isShowing()) {
                    progress.dismiss();
                }

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
        int i;
        categories.clear();
        images.clear();
        for (i = 0; i < localdatabase.masterList.size(); i++) {
            MasterMenuItems a=localdatabase.masterList.get(i);
            String name=a.getName();
            String img=a.getImage();
            if((categories.contains(a.getName())) & (images.contains(a.getImage())) )
            {
                //nothing should be added
            }
            else
            {
                if(a.getDefault())
                {
                    categories.add(name);
                    images.add(img);
                }
                else{
                    Display("No Items to Display");
                }
            }
        }
        send_to_adapter();

    }



    private void getting_service_status() {

        if(localdatabase.delivery.equals("NA")){
            drinks.setVisibility(View.GONE);
            cuisine_btn.setVisibility(View.GONE);
            time_btn.setVisibility(View.GONE);
            combo_btn.setVisibility(View.GONE);
            ad1.setVisibility(View.GONE);
            ad2.setVisibility(View.GONE);
            attack.setVisibility(View.GONE);
            View view_pager2 = (View) this.findViewById(R.id.dummypage);
            view_pager2.setVisibility(View.GONE);
            View view_pager1 = (View) this.findViewById(R.id.appbar);
            view_pager1.setVisibility(View.GONE);
            View notavailable = (View) this.findViewById(R.id.notavailable);
            }

        else {

            View notavailable = (View) this.findViewById(R.id.notavailable);
            notavailable.setVisibility(View.GONE);
        }

        if(progress.isShowing())
        {
            progress.dismiss();
        }
        else
        {

            if(!swipeRefreshLayout.isRefreshing()) {
                progress.setMessage("Getting Kitchen Status...");
                progress.show();
            }
        }
        if(swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
if(local.metaData!=null) {
    if (local.metaData.getservice().equals("true")) {
       // Toast.makeText(this, "Kitchen is Open", Toast.LENGTH_SHORT).show();
        attack.setText("KITCHEN IS OPEN");
        attack.setBackgroundColor(Color.parseColor("#7ee591"));
        //showDialog(this,"Kitchen is Closed\nPlease come back from 6 to 10",R.drawable.store);
    } else {
        if(!localdatabase.delivery.equals("NA")) {
           if(localdatabase.kitchen==0){
               showDialog(this, localdatabase.kitchenClosedText, R.drawable.store, "Browse Menu");
               localdatabase.kitchen++;
           }
        }
        //Toast.makeText(this, "Kitchen is closed", Toast.LENGTH_SHORT).show();
        attack.setText("KITCHEN IS CLOSED");
        attack.setBackgroundColor(Color.parseColor("#56FF0000"));
    }
}
    }


    private Boolean checking_net_permission() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void Display(String s)
    {

        Log.i("view pager", s);
    }

    private void send_to_adapter()
    {
        adapter=new categoryAdapter(this,categories,images);
        adapter.notifyDataSetChanged();
        recylerView.setAdapter(adapter);
        recylerView.setNestedScrollingEnabled(false);
//        recylerView.addItemDecoration(itemDecoration);
        recylerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Display(position+" page selected1");

        getPositionsForDots(position%dotPositions.length);
       // Display("Called at "+position);

    }

    @Override
    public void onPageSelected(int position) {
//       Display("Page selected at "+position);
        Display(position+" page selected");
        getPositionsForDots(position%dotPositions.length);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

  //      getPositionsForDots(3);


    }

    @Override
    public void onBackPressed() {
        if(nav){
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }else{

        }
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
                startActivity(new Intent(menu.this, NotificationActivity.class));
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

    private  void updateUI(int d){
        if(localdatabase.notifmount!=null)
        localdatabase.notifmount.setText(d+"");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu) {
            // Handle the camera action
        } else if (id == R.id.cart) {

            Intent a=new Intent(getApplicationContext(),cart.class);
            a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);


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

    @Override
    protected void onRestart() {
        super.onRestart();
        //recreate();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            swipeRefreshLayout.setEnabled(true);
        } else {
            swipeRefreshLayout.setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(appBarLayout!=null)
        appBarLayout.addOnOffsetChangedListener(this);



    }

    @Override
    protected void onPause() {
        super.onPause();
        appBarLayout.removeOnOffsetChangedListener(this);
       // Intent i = new Intent(this, AddressFetchingService.class);
        //stopService(i);
    }

    @Override

    protected void onDestroy(){
        super.onDestroy();
        if(broadcastReceiver!=null){
            unregisterReceiver(broadcastReceiver);
            Log.i("getting unregistered","");
        }



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

    @Override
    public void onStart(){
        super.onStart();

    }



}
