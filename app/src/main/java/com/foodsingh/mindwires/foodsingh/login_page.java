package com.foodsingh.mindwires.foodsingh;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login_page extends AppCompatActivity {
    EditText mobile,password;
    Button login,new_user;
    ProgressDialog progress;
    TextView forgot_password;

    Typeface tf;
    int sta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_page);


        tf = Typeface.createFromAsset(getAssets(), "fonts/OratorStd.otf");

        initialize();

        setTypeFace();

        if(check_if_logged_in())
        {
            Intent as1=new Intent(getApplicationContext(),Splash.class);
            startActivity(as1);
            finish();
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num=mobile.getText().toString();
                String pass=password.getText().toString();



                if(checking_net_permission())
                {
                    if(num.length()==10 & pass.length()>=4)

                    {
                        if(!progress.isShowing()){
                            progress.setMessage("Fetching Details. Please Wait");
                            progress.setCancelable(false);
                            //login.setClickable(false);
                            progress.show();
                        }
                        num="91"+num;
                        check_login_details(num,pass);
                    }
                    else
                    {
                        Display("Enter 10 Digit mobile number & password should have atleast 4 characters");
                    }

                }
                else
                {
                    // Display("No internet connection,kindly have it to proceed");
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
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobile_number=mobile.getText().toString();
                mobile_number="91"+mobile_number;

                if(mobile.length()==10)
                {
                    if(checking_net_permission())
                    {
                        get_mobile_confirmation(mobile_number);

                        //    send_forgot_password(mobile_number);
                    }
                    else
                    {
                        Display("No Internet Connection,Kindly have Connection to proceed");
                    }

                }
                else
                {
                    Display("Please enter your 10 Digit Mobile number, and then press this");
                }

            }
        });

        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a=new Intent(getApplicationContext(),Signup_OTP.class);
                startActivity(a);
                finish();
            }
        });

    }

    private void get_mobile_confirmation(final String mob) {

        if(!progress.isShowing())
        {
            progress.setMessage("Loading,Please Wait...");
            progress.setCancelable(false);
            progress.show();
        }
        StringRequest str=new StringRequest(Request.Method.POST, constants.check_mobile, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(progress.isShowing())
                {
                    progress.dismiss();
                }
                try {
                    JSONObject a=new JSONObject(response);
                    String t=a.getString("result");
                    if(t.equalsIgnoreCase("true"))
                    {
                        send_forgot_password(mob);
                    }
                    else
                    {
                        Toast.makeText(login_page.this, "This number does not exist..", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Display("Some Error Occured..");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(progress.isShowing())
                {
                    progress.dismiss();
                }
                Display("Some Error Occured...");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> maps=new HashMap<>();
                maps.put("mobile",mob);
                return maps;
            }
        };

        RequestQueue que=Volley.newRequestQueue(this);
        que.add(str);

    }

    private void sendRegistrationToServer(String url) {
        //You can implement this method to store the token on your server
        //Not required for current project
//        Toast.makeText(this,token,Toast.LENGTH_LONG).show();


        SharedPreferences sharedPreferences = getSharedPreferences(constants.foodsingh, Context.MODE_PRIVATE);

        final String token = sharedPreferences.getString("token","token");

        final String mobile = sharedPreferences.getString("mobile","");

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(progress.isShowing())
                {
                    progress.dismiss();
                }

                Log.d("fcmnotification",response);

                Intent f=new Intent(getApplicationContext(),Splash.class);
                startActivity(f);
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("fcmnotification",error.toString());

            }
        }){
            @Override

            public Map getParams(){
                Map<String,String> map = new HashMap<>();
                map.put("mobile",mobile);
                map.put("token",token);

                Log.d("fcmnotification",map.toString());

                return map;
            }
        };

        RequestQueue v = Volley.newRequestQueue(this);
        sr.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        v.add(sr);

    }


    private void send_forgot_password(final String mobile_number) {
        if(progress.isShowing())
        {
            progress.dismiss();
        }
        else
        {
            progress.setMessage("Sending Password to Your Registered Mobile Number");
            progress.show();
            progress.setCancelable(false);
        }

        String Url="https://control.msg91.com/api/sendotp.php?authkey=112452AB1seNQy572e2e51&mobile="+mobile_number+"&message=Your%20OTP%20is%20%23%23OTP%23%23%20.%20It%20is%20Valid%20for%203%20minutes%20only.&sender=FoodSingh&otp_expiry=3";

        StringRequest str=new StringRequest(Request.Method.GET,Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(progress.isShowing())
                {
                    progress.dismiss();
                }
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String responseString = jsonObject.getString("type");
                    if(responseString.equalsIgnoreCase("SUCCESS")){
                        Display("Password sent! Check Your Registered Mobile");
                        Bundle a =new Bundle();
                        a.putString("mob",mobile_number);
                        a.putString("key","1000");
                        Intent b=new Intent(getApplicationContext(),verifying_otp.class);
                        b.putExtras(a);
                        startActivity(b);
                        finish();
                    }else{
                        Display("Some Error Occured! Please check your number.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Display(e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(progress.isShowing())
                {
                    progress.dismiss();
                }
                Display("Some Error Occured :"+error.toString());
            }
        });

        RequestQueue re=Volley.newRequestQueue(this);
        re.add(str);

    }

    private void setTypeFace() {
        Typeface t = Typeface.createFromAsset(getAssets(), "fonts/android.ttf");
        mobile.setTypeface(t);
        password.setTypeface(t);
        login.setTypeface(tf);
        new_user.setTypeface(tf);
        Typeface freescpt = Typeface.createFromAsset(getAssets(), "fonts/FREESCPT.TTF");
        TextView loginpage = (TextView)findViewById(R.id.logintofoodsingh);
        loginpage.setTypeface(freescpt);
        forgot_password.setTypeface(tf);

    }

    private void getting_setting_details() {
        if(!progress.isShowing()){
            progress.setMessage("Fetching Details. Please Wait.");
            progress.setCancelable(false);
            progress.show();
        }
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

        StringRequest str=new StringRequest(Request.Method.POST, constants.get_details, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                // Display(response);
                if(progress.isShowing()){
                    progress.dismiss();
                }

                try {
                    JSONArray array=new JSONArray(response);
                    JSONObject obj=array.getJSONObject(0);
                    SharedPreferences sharedPreferences = getSharedPreferences(constants.foodsingh,Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name",obj.getString("name"));
                    editor.putString("email",obj.getString("email"));
                    editor.putString("password", obj.getString("password"));
                    editor.apply();
                    //editor..setText(obj.getString("name"));
                    //email.setText(obj.getString("email"));
                    //old_password_check=obj.getString("password");
                    sendRegistrationToServer(constants.token_url);

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
    private Boolean check_if_logged_in() {
        SharedPreferences as=getSharedPreferences(constants.foodsingh,MODE_PRIVATE);
        String pass=as.getString("password","123");
        if(pass.equals("123"))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    private void check_login_details(final String numy, final String passy) {



        StringRequest das=new StringRequest(Request.Method.POST, constants.login_check_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(progress.isShowing()){
                    progress.dismiss();
                }


                try {
                    JSONObject obj=new JSONObject(response);
                    String status=obj.getString("status");
                    if(status.equals("1"))
                    {
                        //       Display("Successfully logged in");
                        SharedPreferences as=getSharedPreferences(constants.foodsingh,MODE_PRIVATE);
                        SharedPreferences.Editor edit=as.edit();
                        edit.putString("password",passy);//100 defined for logged in
                        edit.putString("mobile",numy);
                        edit.apply();
                        getting_setting_details();

                    }
                    else
                    {
                        Display("Wrong credentials ,please try again");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    if(progress.isShowing()){
                        progress.dismiss();
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
                Display("Some error Occured may be due to bad internet connection");
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> maps=new HashMap<>();
                maps.put("number",numy);
                maps.put("password",passy);
                return maps;
            }
        };

        RequestQueue as=Volley.newRequestQueue(this);
        as.add(das);
    }

    private Boolean checking_net_permission() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void Display(String s)
    {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void initialize() {
        mobile=(EditText) findViewById(R.id.mob);
        password=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.btnlogin);
        new_user=(Button)findViewById(R.id.btnToSignUp);
        progress=new ProgressDialog(this);
        forgot_password=(TextView)findViewById(R.id.Forgot_Password);
    }
}
