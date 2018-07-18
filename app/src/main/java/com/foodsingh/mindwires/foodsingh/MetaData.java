package com.foodsingh.mindwires.foodsingh;

/**
 * Created by Tanmay on 14-10-2017.
 */

public class MetaData {

    private String stax_amount;
    private String Latest_version;
    private String service;
    private String Min_Order;
    private String msg_api;
    private int amount_for_free_delivery;

    public MetaData(String stax_amount,
                    String Latest_Version,
                    String service,
                    String Min_Order,
                    String msg_api,
                    int amount_for_free_delivery){

        this.stax_amount = stax_amount;
        this.amount_for_free_delivery = amount_for_free_delivery;
        this.Latest_version = Latest_Version;
        this.service = service;
        this.Min_Order = Min_Order;
        this.msg_api = msg_api;

    }

    public  String getstax_amount() {return stax_amount; }

    public int getamount_for_free_delivery () {return amount_for_free_delivery; }

    public String getLatest_version(){ return Latest_version; }

    public String getservice(){
        return service;
    }

    public String getMin_Order(){
        return Min_Order;
    }

    public String getMsg_api(){
        return msg_api;
    }

    public void setService(String s){
        service = s;
    }
}

