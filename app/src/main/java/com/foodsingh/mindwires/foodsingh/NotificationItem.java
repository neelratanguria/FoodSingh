package com.foodsingh.mindwires.foodsingh;

/**
 * Created by Tanmay on 20-10-2017.
 */

public class NotificationItem {

    private String body,title, img, url, activity, notificationType, time, coupon;
    private boolean read = false;

    public NotificationItem(String body, String title, String img, String url, String activity, String notificationType,String time, boolean read,String coupon){
        this.read = read;
        this.body = body;
        this.title = title;
        this.time = time;
        this.img = img;
        this.url = url;
        this.activity = activity;
        this.notificationType = notificationType;
        this.coupon = coupon;

    }

    public String getBody(){
        return body;
    }

    public String getTitle(){
        return title;
    }

    public String getImg(){
        return img;
    }

    public String getActivity(){
        return activity;
    }

    public boolean getRead(){
        return read;
    }

    public String getUrl(){
        return url;
    }

    public String getNotificationType(){
        return notificationType;
    }

    public String getTime(){
        return time;
    }

    public void setRead(boolean v){
        read = v;
    }

    public String getCoupon(){
        return coupon;
    }

}
