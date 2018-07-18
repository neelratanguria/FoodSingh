package com.foodsingh.mindwires.foodsingh;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;


public class Support extends AppCompatActivity {

    private WebView mWebView;
    private Random rand = new Random();
    private int value = rand.nextInt(10000);
    public static TextView cartitemcount1,notifamounts;
    BroadcastReceiver broadcastReceiver;
    View actionView;
    ProgressDialog progress;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        mWebView = (WebView) findViewById(R.id.support_web);
        back=(ImageView)findViewById(R.id.back);
        progress=new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setCancelable(false);

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new myWebClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String request) {
               if(request.startsWith("http:")||request.startsWith("https:")){
                   return false;
               }else{
                   Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(request));
                   startActivity(i);
                   return true;

               }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(progress.isShowing())
                {
                    progress.dismiss();
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progress.show();
            }
        });


     //   mWebView.loadUrl("axuip.foodsingh.com/login.php");

       mWebView.loadUrl("http://www.foodsingh.com/app_files/user/support/");
        SetupBroadcastReceiver();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a=new Intent(getApplicationContext(),menu.class);
                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(a);
            }
        });
    }

    private void SetupBroadcastReceiver() {

        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(constants.broaadcastReceiverMenu);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
              //  notifamounts = (TextView)actionView.findViewById(R.id.notification_badge);
                if(intent.getAction().equals(constants.broaadcastReceiverMenu)){


                    notifamounts.setVisibility(View.VISIBLE);
                    notifamounts.setText(localdatabase.notifications+"");


                    Log.i("broadcastreceiver1", localdatabase.notifications+"");
                }else if(intent.getAction().equals(constants.menu2BroadcastReceiver)){
                    notifamounts.setVisibility(View.INVISIBLE);
                }

            }


        };

        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(constants.menu2BroadcastReceiver);

        registerReceiver(broadcastReceiver,intentFilter);
        registerReceiver(broadcastReceiver,intentFilter2);
    }



    @Override

    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack())
        {
            mWebView.goBack();
        }
        else
        {
            super.onBackPressed();

        }
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
                startActivity(new Intent(Support.this, NotificationActivity.class));
            }
        });

        notifamounts = (TextView)actionView.findViewById(R.id.notification_badge);
        if(localdatabase.notifications==0){
            notifamounts.setVisibility(View.INVISIBLE);
        }else {
            notifamounts.setVisibility(View.VISIBLE);
            notifamounts.setText(localdatabase.notifications+"");
        }

        return true;
    }

    public class myWebClient extends WebViewClient
    {
        boolean cond = false;


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            SharedPreferences sharedPreferences = getSharedPreferences(constants.foodsingh, Context.MODE_PRIVATE);
            String nameBuffer = sharedPreferences.getString("name","_");
            String nameFinal = null;
            if(!nameBuffer.equals("_")){
               nameFinal = nameBuffer;
            }


            if(url.startsWith("mailto:")){

                String email = url.substring(7);

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.setType("vnd.android.cursor.item/email");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
                if(!nameFinal.equals(null)) {
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Customer Support ID:" + value + " " + nameFinal);
                }
                else {
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Customer Support ID:" + value + " ");
                }
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(emailIntent, "Send mail using..."));

                mWebView.loadUrl("http://foodsingh.com/support");
                cond = true;

                return;
            }

            if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(intent);
                mWebView.loadUrl("http://foodsingh.com/support");
                cond = true;
                return;
            }

            if(cond==false) {
                Snackbar snackbar = Snackbar.make(view, "Loading Please Wait", Snackbar.LENGTH_LONG);
                snackbar.show();
            }



            super.onPageStarted(view, url, favicon);
        }
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            if (cond==false) {
                Snackbar snackbarError = Snackbar.make(view, "Error Connecting Server", Snackbar.LENGTH_LONG);
                snackbarError.show();
            }
            super.onReceivedError(view, request, error);
        }
    }
}


