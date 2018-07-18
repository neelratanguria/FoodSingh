package com.foodsingh.mindwires.foodsingh;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

public class set_password extends AppCompatActivity {
    Button confirm;
    String mob;

    EditText enter_pass,reenter_pass;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_set_password);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialize();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a=enter_pass.getText().toString();
                String b=reenter_pass.getText().toString();

                if((a.equals(b))&(a.length()>=4))
                {
                    if(checking_net_permission())
                    {
                        update_password(mob,a);
                        progress.setMessage("Reaching Server");
                        progress.show();
                    }
                    else
                    {
                        Display("No internet connection,kindly try after sometime");
                    }
                }
                else
                {

                    Display("Both the passwords should match and the length should be atleast 4");
                }
            }
        });

    }

    private void update_password(final String mob, final String a) {

        StringRequest str=new StringRequest(Request.Method.POST, constants.update_password, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(progress.isShowing())
                {
                    progress.dismiss();
                }
            //    Display(response);
                try {
                    JSONObject as=new JSONObject(response);
                    String a=as.getString("result");
                    String b=as.getString("message");
                    if(a.equalsIgnoreCase("true")&&b.equalsIgnoreCase("success"))
                    {
                        Toast.makeText(set_password.this, "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),login_page.class));
                        finish();
                    }
                    else
                    {
                        Toast.makeText(set_password.this, "Password Not updated, please try again", Toast.LENGTH_SHORT).show();
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
                Display("There is some error, may be due to server fault or bad internet connection");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> maps=new HashMap<>();
                maps.put("mobile",mob);
                maps.put("password",a);
                return maps;

            }
        };
        RequestQueue r= Volley.newRequestQueue(this);
        r.add(str);
    }

    private void Display(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void initialize() {

        enter_pass=(EditText)findViewById(R.id.enter_pass);
        reenter_pass=(EditText)findViewById(R.id.reenter_pass);
        confirm=(Button) findViewById(R.id.enter);
        Bundle g=getIntent().getExtras();
        mob=g.getString("mob");
        progress=new ProgressDialog(this);
        progress.setCancelable(false);

        Typeface t = Typeface.createFromAsset(getAssets(), "fonts/android.ttf");
        enter_pass.setTypeface(t);
        reenter_pass.setTypeface(t);

        TextView txtSetPass = (TextView) findViewById(R.id.txt_set_pass);
        Typeface freescpt = Typeface.createFromAsset(getAssets(), "fonts/FREESCPT.TTF");
        txtSetPass.setTypeface(freescpt);

        Typeface  tf = Typeface.createFromAsset(getAssets(), "fonts/OratorStd.otf");
        confirm.setTypeface(tf);



    }


    private Boolean checking_net_permission() {
        final ConnectivityManager connectivityManager = ((ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
