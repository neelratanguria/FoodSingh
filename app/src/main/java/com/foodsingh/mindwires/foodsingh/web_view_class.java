package com.foodsingh.mindwires.foodsingh;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by PRANSHOO VERMA on 21/10/2017.
 */

public class web_view_class extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        if(url.startsWith("https:")||url.startsWith("http:")) {
            view.loadUrl(url);
            return true;
        }else {
            return true;
        }
    }
}
