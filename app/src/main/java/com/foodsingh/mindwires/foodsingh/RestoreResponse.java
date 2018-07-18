package com.foodsingh.mindwires.foodsingh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tanmay on 01-11-2017.
 */

public class RestoreResponse {

    public static void getResponse(Context c) {
        SharedPreferences sharedPreferences = c.getSharedPreferences(constants.foodsingh, Context.MODE_PRIVATE);
        final String response = sharedPreferences.getString(constants.mainresponsesharedpreferences, "");

        if (!response.equals("")) {

            try {
                JSONObject mainObject = new JSONObject(response);
                localdatabase.about_text =mainObject.getString("about_text");
                localdatabase.cartCoupon = mainObject.getString("cart_ad");
                localdatabase.about_img = mainObject.getString("about_image");
                localdatabase.metaData = new MetaData(mainObject.getString("stax_amount"),
                        mainObject.getString("latest_version_new"),
                        mainObject.getString("service"),
                        mainObject.getString("min_order"),mainObject.getString("msg_api"),
                        mainObject.getInt("amount_for_free_delivery")
                );
                localdatabase.aboutText = mainObject.getString("about_text");
                localdatabase.aboutImage = mainObject.getString("about_image");
                localdatabase.delivery = mainObject.getString("location");
                localdatabase.drinks = mainObject.getString("drinks");
                localdatabase.share_text =mainObject.getString("share_text");
                localdatabase.share_url = mainObject.getString("share_url");
                localdatabase.deliveryCharge = Integer.parseInt(mainObject.getString("delivery_charge"));
                localdatabase.stax_amount = Integer.parseInt(mainObject.getString("stax_amount"));
                localdatabase.kitchenClosedText = mainObject.getString("kitchen_closed_text");
                localdatabase.location_id = mainObject.getString("location_id");
                Log.i("mainresponse","location_id:"+localdatabase.location_id);

                JSONArray Categories = mainObject.getJSONArray("categories");
                for (int i = 0; i < Categories.length(); i++) {
                    String name, image, cuisine, time, combo;
                    String default_no;
                    Boolean defau_bool;
                    List<MenuItems> menuItemsList = new ArrayList<>();
                    JSONObject tempObject = Categories.getJSONObject(i);
                    name = tempObject.getString("name");
                    image = tempObject.getString("image");
                    cuisine = tempObject.getString("cuisine");
                    time = tempObject.getString("time");
                    combo = tempObject.getString("combo");
                    JSONArray miniMenu = tempObject.getJSONArray("menu");
                    default_no=tempObject.getString("default");
                    if(default_no.equals("1"))
                    {
                        defau_bool=true;
                    }
                    else
                    {
                        defau_bool=false;
                    }

                    for (int j = 0; j < miniMenu.length(); j++) {
                        String id, name_, category, price, image_, status, detail, r_price;
                        JSONObject miniTempObject = miniMenu.getJSONObject(j);
                        id = miniTempObject.getString("id");
                        name_ = miniTempObject.getString("name");
                        category = miniTempObject.getString("category");
                        price = miniTempObject.getString("price");
                        image_ = miniTempObject.getString("image");
                        status = miniTempObject.getString("status");
                        detail = miniTempObject.getString("detail");
                        r_price = miniTempObject.getString("r_price");
                        // detail = "";
                        MenuItems menuItems = new MenuItems(id, name_, category, price, image_, status, detail, r_price,localdatabase.location_id);
                        menuItemsList.add(menuItems);
                        String available = miniTempObject.getString("status");

                        if (available.equals("NA")) {
                            UnavailableItems ii = new UnavailableItems(name_, Integer.parseInt(price));
                            localdatabase.unavailableItemsList.add(ii);
                        }
                    }
                    MasterMenuItems menuItemsObject = new MasterMenuItems(name, image, cuisine, combo, menuItemsList, time, tempObject.getString("drinks"), tempObject.getString("detail"),defau_bool);
                    Log.i("checking details", tempObject.getString("drinks"));
                    localdatabase.masterList.add(menuItemsObject);
                }
                JSONArray BannerImages = mainObject.getJSONArray("home_images");

                for (int i = 0; i < BannerImages.length(); i++) {
                    localdatabase.BannerUrls.add(BannerImages.getJSONObject(i).getString("url"));
                }

                JSONArray getAdImages = mainObject.getJSONArray("ad_bars");

                for (int i = 0; i < getAdImages.length(); i++) {
                    JSONObject tempJson = getAdImages.getJSONObject(i);
                    CouponClass temp = new CouponClass(tempJson.getString("id"), tempJson.getString("coupon"), tempJson.getString("image"));
                    localdatabase.couponClassList.add(temp);
                }

                JSONArray superCategories = mainObject.getJSONArray("super_categories");

                for (int i = 0; i < superCategories.length(); i++) {
                    SuperCategories su = new SuperCategories(superCategories.getJSONObject(i).getString("id"),
                            superCategories.getJSONObject(i).getString("name"));
                    localdatabase.superCategoriesList.add(su);
                    Log.i("super_categories", localdatabase.superCategoriesList.get(i).getName());
                }

              Location l = new Location("provider");

                double latitude = sharedPreferences.getFloat("latitude",0);
                double longitude = sharedPreferences.getFloat("longitude",0);
                l.setLatitude(latitude);
                l.setLongitude(longitude);

                localdatabase.deliveryLocation = l;

                String temp = sharedPreferences.getString(constants.cartrestore,"");
                if(temp!=""){
                    Gson gson = new Gson();
                    Carerestore care = gson.fromJson(temp,Carerestore.class);
                    localdatabase.cartList = care.getCart();
                    localdatabase.sidesList = (ArrayList<MenuItems>) care.getSides();
                }


            } catch (Exception e) {
                e.printStackTrace();

                Log.i("mainresponse", "3" + e.toString());
                // finish();
            } finally {

            }

        }

    }

}
