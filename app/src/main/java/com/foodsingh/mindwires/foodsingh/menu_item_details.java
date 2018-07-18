package com.foodsingh.mindwires.foodsingh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class menu_item_details extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView plus,minus,item_image;
    int p;
    TextView item_name,item_price,quantity,cartitemcount1,unav,notifamout;
    String name,price,image,item_quantity,des,id, status, category;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TextView description;
    ImageView fav;
    Button addFav;
    MenuItems mainItem;
    int isAvailable;
    BroadcastReceiver broadcastReceiver;
    boolean isFav = false;
    Gson gson;

    FavouritesList myList;

    List<MenuItems> favList = new ArrayList<>();
    NavigationView navigationView;


    View actionView;

    MenuItems item;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RemoveTop();
        
        setContentView(R.layout.activity_menu_item_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gson = new Gson();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



         navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        initialize();
        getting_intents();

        if(isAvailable==1){


        }else{
            unav.setVisibility(View.INVISIBLE);
        }

       refreshList();

        if(isFav) {

            fav.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_fav));
            addFav.setText("REMOVE FROM FAVOURITES");
        }else{
            fav.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_not_fav));
            addFav.setText("ADD TO FAVOURITES");
        }

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(isAvailable!=1){
                  int check=0;
                  //Toast.makeText(menu_item_details.this, "plus", Toast.LENGTH_SHORT).show();
                  int quantiti = item.getQuantity();
                  quantiti=quantiti+1;
                  quantity.setText(String.valueOf(quantiti));
                  item.setQuantity(quantiti);
                  for(int i=0;i<localdatabase.cartList.size();i++)
                  {
                      if(name.equalsIgnoreCase(localdatabase.cartList.get(i).getName()))
                      {
                          //Toast.makeText(menu_item_details.this, "Got it", Toast.LENGTH_SHORT).show();
                          localdatabase.cartList.remove(i);
                          localdatabase.cartList.add(item);
                          check=1;

                      }

                  }
                  if(check!=1) {
                      localdatabase.cartList.add(item);
                  }

                  cartitemcount1.setText(localdatabase.cartList.size()+"");
              }

                if(menu_category_wise.cartitemcount1!=null){
                    menu_category_wise.updateCartIcon();
                }

            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isAvailable!=1){

                 //   Toast.makeText(menu_item_details.this, "minus", Toast.LENGTH_SHORT).show();
                    int quantiti = item.getQuantity();
                    quantiti=quantiti-1;
                    if(quantiti==0)
                    {
                     //cart se hat jayega
                        quantity.setText(String.valueOf(quantiti));
                        item.setQuantity(quantiti);
                        for(int i=0;i<localdatabase.cartList.size();i++)
                        {
                            if(name.equalsIgnoreCase(localdatabase.cartList.get(i).getName()))
                            {
                                //Toast.makeText(menu_item_details.this, "Got it", Toast.LENGTH_SHORT).show();
                                localdatabase.cartList.remove(localdatabase.cartList.get(i).getName());
                                break;
                            }

                        }
                    }
                    else if(quantiti>0)
                    {
                        quantity.setText(String.valueOf(quantiti));
                        item.setQuantity(quantiti);
                        for(int i=0;i<localdatabase.cartList.size();i++)
                        {
                            if(name.equalsIgnoreCase(localdatabase.cartList.get(i).getName()))
                            {
                                //Toast.makeText(menu_item_details.this, "Got it", Toast.LENGTH_SHORT).show();
                                localdatabase.cartList.remove(i);
                                localdatabase.cartList.add(item);
                                break;
                            }

                        }
                        cartitemcount1.setText(localdatabase.cartList.size()+"");
                    }
                    else
                    {
                        Toast.makeText(menu_item_details.this, "Item is not present", Toast.LENGTH_SHORT).show();
                    }


                }
                if(menu_category_wise.cartitemcount1!=null){
                    menu_category_wise.updateCartIcon();
                }
            }
        });

        addFav.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                if (isAvailable != 1) {
                    if (isFav) {
                        isFav = false;

                        fav.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_not_fav));
                        addFav.setText("ADD TO FAVOURITES");
                        favList.remove(Exists(item));

                    } else {
                        isFav = true;
                        addFav.setText("REMOVE FROM FAVOURITES");
                        fav.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_fav));
                        favList.add(mainItem);

                    }

                    save();
                }
            }
        });

        SetupBroadcastReceiver();
        navigationView.setNavigationItemSelectedListener(this);
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
    }


    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/android.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);

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

    private void SetupBroadcastReceiver() {

        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(constants.broaadcastReceiverMenu);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

               // notifamout = (TextView)actionView.findViewById(R.id.notification_badge);
                if(intent.getAction().equals(constants.broaadcastReceiverMenu)){


                    notifamout.setVisibility(View.VISIBLE);
                    notifamout.setText(localdatabase.notifications+"");


                    Log.i("broadcastreceiver1", localdatabase.notifications+"");
                }else if(intent.getAction().equals(constants.menu2BroadcastReceiver)){
                    notifamout.setVisibility(View.INVISIBLE);
                }

            }


        };

        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(constants.menu2BroadcastReceiver);

        registerReceiver(broadcastReceiver,intentFilter);
        registerReceiver(broadcastReceiver,intentFilter2);
    }



    private void RemoveTop(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void refreshList(){
        myList = getMyList();
        favList = myList.getFavouriteList();
    }

    private void save() {

        DisplayList(favList);

        FavouritesList temp = new FavouritesList(favList);
        String tempJson = gson.toJson(temp);
        editor.putString(constants.fav,tempJson);
        editor.apply();

    }

    private int Exists(MenuItems item){

        for(int i=0; i<favList.size(); i++){
            if(favList.get(i).getId().equals(item.getId())
                    &&favList.get(i).getName().equals(item.getName())
                    ){
                return i;
            }
        }
        return -1;


    }

    private void DisplayList(List<MenuItems> item){
        for (int i=0; i<item.size(); i++){
            Log.i("details Fav "+i, item.get(i).getName());
        }
    }

    private void getting_intents() {
        Bundle b=getIntent().getExtras();
        name=b.getString("item_name");
        price=b.getString("item_price");
        image=b.getString("item_image");
        //item=b.getParcelable("object");
        des = b.getString("desc");
        description.setText(des);
        //item=b.getParcelable("object");
        item_quantity=b.getString("item_quantity");
        id = b.getString("id");
        status = b.getString("status");
        category = b.getString("category");
        position=b.getInt("position");
        isFav = b.getBoolean("isfav");
        mainItem = b.getParcelable("mainobject");
        isAvailable = b.getInt("avail");
        p = b.getInt("mainposition");

        if(!status.equals("live")){
            unav.setVisibility(View.VISIBLE);
        }
        else {
            unav.setVisibility(View.INVISIBLE);
        }

        item = new MenuItems(id, name, category, price, image, status, des, mainItem.getR_price(),localdatabase.location_id);

        if (item_quantity.equals("")){
            quantity.setText("0");
        }
        else
        {
            quantity.setText(item_quantity);
        }

        Glide.with(getApplicationContext()).load(image).skipMemoryCache(true).thumbnail(0.05f)
                .diskCacheStrategy(DiskCacheStrategy.RESULT).centerCrop().into(item_image);
        item_price.setText("₹"+price);
        item_name.setText(name);

    }

    private void initialize() {
        plus=(ImageView)findViewById(R.id.plus_slide);
        minus=(ImageView)findViewById(R.id.minus_slide);
        quantity=(TextView)findViewById(R.id.item_quantity_slide);
        item_name=(TextView) findViewById(R.id.item_name);
        item_image=(ImageView)findViewById(R.id.item_image);
        item_price=(TextView) findViewById(R.id.item_price);

        description = (TextView)findViewById(R.id.descrption);
        Typeface tf11 = Typeface.createFromAsset(getAssets(), "fonts/MTCORSVA.TTF");
        description.setTypeface(tf11);
        fav = (ImageView)findViewById(R.id.fav);
        unav = (TextView)findViewById(R.id.txt_unavailable);
        Typeface tf111 = Typeface.createFromAsset(getAssets(), "fonts/OratorStd.otf");
        unav.setTypeface(tf111, Typeface.BOLD);
        addFav = (Button)findViewById(R.id.add_to_fav);

        sp = getSharedPreferences(constants.foodsingh, Context.MODE_PRIVATE);
        Typeface tf1 = Typeface.createFromAsset(getAssets(),"fonts/vijaya.ttf");
        Typeface tf2 = Typeface.createFromAsset(getAssets(),"fonts/tahoma.ttf");
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/tahoma.ttf");
        Typeface t = Typeface.createFromAsset(getAssets(), "fonts/Alisandra Demo.ttf");
        item_name.setTypeface(t);
        item_price.setTypeface(tf,Typeface.BOLD);



        editor = sp.edit();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }

       /* Intent as=new Intent(this,menu_category_wise.class);
        Bundle a=new Bundle();
        a.putString("category",item.getCategory());
        a.putInt("position", p);
        as.putExtras(a);
        startActivity(as);
        finish();*/
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
                finish();
            }
        });

        ImageView notif = (ImageView)actionView.findViewById(R.id.notif);

        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(menu_item_details.this, NotificationActivity.class));
            }
        });



        notifamout = (TextView)actionView.findViewById(R.id.notification_badge);
        if(localdatabase.notifications==0){
            notifamout.setVisibility(View.INVISIBLE);
        }else {
            notifamout.setVisibility(View.VISIBLE);
            notifamout.setText(localdatabase.notifications+"");
        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        SharedPreferences shared=getSharedPreferences(constants.foodsingh,MODE_PRIVATE);
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu) {
            Intent a=new Intent(getApplicationContext(),menu.class);
            a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
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


    private void Display(String s){
        Toast.makeText(this,s, Toast.LENGTH_LONG).show();
    }

    public FavouritesList getMyList() {

        String tempJson = sp.getString(constants.fav, "");

        if(tempJson.equals("")){
            List<MenuItems> tempList = new ArrayList<>();
           return new FavouritesList(tempList);
        }else{
            FavouritesList f = gson.fromJson(tempJson, FavouritesList.class);
            return f;
        }

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(broadcastReceiver!=null)
        unregisterReceiver(broadcastReceiver);
    }
}
