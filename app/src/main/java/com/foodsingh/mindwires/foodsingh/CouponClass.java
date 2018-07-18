package com.foodsingh.mindwires.foodsingh;

/**
 * Created by Tanmay on 23-10-2017.
 */

public class CouponClass {
    private String id, couponCode, url;

    public CouponClass(String id, String couponCode, String url){

        this.id = id;
        this.couponCode = couponCode;

        this.url = url;

    }

    public String getId(){
        return id;
    }

    public String getCouponCode(){
        return couponCode;
    }

    public String getUrl(){
        return url;
    }

}
