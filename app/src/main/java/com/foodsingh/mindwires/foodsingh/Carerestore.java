package com.foodsingh.mindwires.foodsingh;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanmay on 02-11-2017.
 */

public class Carerestore {

    List<MenuItems> cart = new ArrayList<>();
    List<MenuItems> sides = new ArrayList<>();

    public Carerestore(List<MenuItems> cart, List<MenuItems> sides){
        this.cart = cart;
        this.sides = sides;
    }

    public List<MenuItems> getCart(){
        return cart;
    }

    public List<MenuItems> getSides(){
        return sides;
    }
}
