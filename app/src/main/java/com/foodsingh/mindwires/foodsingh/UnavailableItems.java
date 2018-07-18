package com.foodsingh.mindwires.foodsingh;

/**
 * Created by Tanmay on 18-10-2017.
 */

public class UnavailableItems {

    private String  name;
    private int price;

    public UnavailableItems(String name,int price){

        this.name = name;
        this.price=price;
    }


    public String getName(){
        return name;
    }

    public int getPrice(){
        return price;
    }
}
