package com.foodsingh.mindwires.foodsingh;

import java.util.ArrayList;
import java.util.List;



public class MasterMenuItems {

    private String name, image, cuisine, time , combo,drinks, detail;

    private boolean default_no;

    private List<MenuItems> menuList = new ArrayList<>();

    MasterMenuItems(String name, String image, String cuisine, String Combo, List<MenuItems> menuList,String time, String drinks, String detail,boolean defau){
        this.name = name;
        this.image = image;
        this.cuisine = cuisine;
        this.combo = Combo;
        this.menuList = menuList;
        this.time = time;
        this.drinks = drinks;
        this.detail = detail;
        this.default_no=defau;
    }

    public String getName(){
        return name;
    }

    public String getImage(){
        return image;
    }

    public boolean getDefault()
    {
        return default_no;
    }

    public String getDetail() {
        return detail;
    }

    public String getCuisine(){
        return cuisine;
    }

    public String getTime(){
        return time;
    }

    public String getCombo(){
        return combo;
    }

    public List<MenuItems> getMenuList(){
        return menuList;
    }

    public String getDrinks(){
        return drinks;
    }

}
