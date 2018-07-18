package com.foodsingh.mindwires.foodsingh;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class details extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

  private   Button button_save_change;
    private  EditText name,email,old_password,new_password;
    private ProgressDialog progress;
    String old_password_check,mobile_old;
    BroadcastReceiver broadcastReceiver;
    View actionView;
    NavigationView navigationView;
    SharedPreferences shared;
    public static TextView cartitemcount1,notifamount;
    
    TextView tvName, tvEmail, tvMobile;

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        if(localdatabase.loaded==false){
            RestoreResponse.getResponse(this);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(localdatabase.loaded==false){
            RestoreResponse.getResponse(this);
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        

        setContentView(R.layout.activity_details);
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
    /////////////////////////////////////////////////////////////////////////////
        //CODING CODING CODING

        initialize();

        getting_setting_details();

        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/gadugi.ttf");
        TextView tvDetails, tvProfile, tvName, tvEmail,tvPassword, tvOld, tvNew;
        tvDetails = (TextView) findViewById(R.id.txt_details);
        tvDetails.setTypeface(tf);

        tvProfile = (TextView) findViewById(R.id.txt_profile);
        tvProfile.setTypeface(tf);

        tvName = (TextView) findViewById(R.id.txt_name);
        tvName.setTypeface(tf);

        tvEmail = (TextView) findViewById(R.id.txt_email);
        tvEmail.setTypeface(tf);

        tvOld = (TextView) findViewById(R.id.txt_old);
        tvOld.setTypeface(tf);

        tvNew = (TextView) findViewById(R.id.txt_new);
        tvNew.setTypeface(tf);

        tvPassword = (TextView) findViewById(R.id.txt_password);
        tvPassword.setTypeface(tf);


        button_save_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           String old_pa=old_password.getText().toString();

                   if(old_pa.equals(old_password_check))
                   {
                           if(checking_net_permission())
                           {
                               String n=name.getText().toString();
                               String e=email.getText().toString();
                               String np=new_password.getText().toString();
                               if(isEmailValid(e)) {
                                   if ((np.length() < 4 & np.length() > 0) || e.length() <= 4 || n.length() <= 1) {
                                       Display("Kindly give proper details");
                                   }
                                   if (np.length() == 0 & e.length() >= 4 & n.length() >= 1) {
                                       np = old_password_check;
                                       Display("Your password will be same");
                                       upudate_to_deb(n, e, np);
                                   }
                                   if (np.length() != 0 & np.length() >= 4 & e.length() >= 4 & n.length() >= 1) {
                                       upudate_to_deb(n, e, np);
                                   }
                               }
                               else {
                                   Display("Please enter a valid email address");
                               }

                           }
                           else
                           {
                               Display("You dont have Internet connection ,cant make changes");
                           }
                   }
                   else
                   {
                       Display("Your Current Password is Incorrect");
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
        SetupBroadcastReceiver();
    }

    private void SetupBroadcastReceiver() {

        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(constants.broaadcastReceiverMenu);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

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
                startActivity(new Intent(details.this, NotificationActivity.class));
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

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/android.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);

    }



    @Override
    public void onDestroy(){
        super.onDestroy();
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

    private void upudate_to_deb( final String n, final String e, final String np) {
        SharedPreferences sh=getSharedPreferences(constants.foodsingh, Context.MODE_PRIVATE);
          mobile_old=sh.getString("mobile","000");
        if(mobile_old.equals("000"))
        {
            Display("Something went wrong,kindly signout and then sign in");
            finish();
        }

        if(progress.isShowing())
        {
            progress.dismiss();
        }
        else
        {
            progress.setMessage("Updating the details....");
            progress.show();
        }
        StringRequest strq=new StringRequest(Request.Method.POST, constants.update_details, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(progress.isShowing())
                {
                    progress.dismiss();
                }
                try {
                    JSONObject obj=new JSONObject(response);
                    String m=obj.getString("message");
                    String r=obj.getString("result");

                    if(m.equals("SUCCESS") && r.equals("true"))
                    {
                        SharedPreferences sg=getSharedPreferences(constants.foodsingh,Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit=sg.edit();
                        edit.putString("password",np);
                        edit.putString("name",n);
                        edit.putString("email",e);
                        edit.apply();
                        Display("Details Has been Updated....");
                        finish();
                    }
                    else
                    {
                        Display("Details has not been updated,try again later");
                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(progress.isShowing())
                {
                    progress.dismiss();
                }
                Display("Some error Occured may be due to bad internet connection or Server issue");
                Log.i("checkerror",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> maps=new HashMap<>();
                maps.put("name",n);
                maps.put("email",e);
                maps.put("password",np);
                maps.put("mobile",mobile_old);
                return maps;
            }
        };

        RequestQueue re=Volley.newRequestQueue(this);
        re.add(strq);

    }

    private void getting_setting_details() {
        if(checking_net_permission())
        {
            SharedPreferences pref=getSharedPreferences(constants.foodsingh,MODE_PRIVATE);
            String mobile=pref.getString("mobile","000");
            if(mobile.equals("000"))
            {
                Display("Some Error is there,Kindly log out and then log in again");
            }
            else
            {
                get_data_deb(mobile);
            }

        }
        else {
            Display("You need Internet connection,to see and edit your details");
        }
    }

    private void get_data_deb(final String mobile) {
        if(progress.isShowing())
        {
            progress.dismiss();
        }
        else
        {
            progress.setMessage("Getting your details from server.....");
            progress.show();
        }

        StringRequest str=new StringRequest(Request.Method.POST, constants.get_details, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(progress.isShowing())
                {
                    progress.dismiss();
                }

                try {
                    JSONArray array=new JSONArray(response);
                    JSONObject obj=array.getJSONObject(0);
                    tvName.setText(""+obj.getString("name"));
                    name.setText(""+obj.getString("name"));
                    tvEmail.setText(""+obj.getString("email"));
                    email.setText(""+obj.getString("email"));
                    tvMobile.setText(""+shared.getString("mobile", "NA"));
                    old_password_check=obj.getString("password");

                    Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/gadugi.ttf");
                    tvName.setTypeface(tf);
                    tvMobile.setTypeface(tf);
                    tvEmail.setTypeface(tf);
                    name.setTypeface(tf);
                    email.setTypeface(tf);
                    button_save_change.setTypeface(tf);

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
                Display("Some Error occured,may be due to bad internet connection or server problem");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> maps=new HashMap<>();
                maps.put("mobile",mobile);
                return maps;
            }
        };
        RequestQueue r= Volley.newRequestQueue(this);
        r.add(str);
    }

    private void Display(String s)
    {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void initialize()
    {
        name =(EditText)findViewById(R.id.text_name);
        email =(EditText)findViewById(R.id.text_email);
        old_password =(EditText)findViewById(R.id.text_cur_pass);
        new_password =(EditText)findViewById(R.id.text_new_pass);
        button_save_change=(Button)findViewById(R.id.button_save_change);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvEmail = (TextView)findViewById(R.id.tv_email);
        tvMobile = (TextView) findViewById(R.id.tv_mobile);

        shared=getSharedPreferences(constants.foodsingh,MODE_PRIVATE);
        progress=new ProgressDialog(this);
        progress.setCancelable(false);
    }


    private Boolean checking_net_permission() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
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


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        SharedPreferences shared=getSharedPreferences(constants.foodsingh,MODE_PRIVATE);
        // Handle navigation view item clicks here.Fmani
        int id = item.getItemId();

        if (id == R.id.menu) {
            // Handle the camera action
            Intent a=new Intent(getApplicationContext(),menu.class);
            a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
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


    boolean isEmailValid(CharSequence emai) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emai).matches();
    }
}
