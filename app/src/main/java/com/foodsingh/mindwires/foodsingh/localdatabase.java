package com.foodsingh.mindwires.foodsingh;

import android.location.Location;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class localdatabase {

    public static boolean loaded = false;

    public static String drinks = "false";

    public static List<SuperCategories> superCategoriesList = new ArrayList<>();

    public static String share_text, share_url;

    public static String lane;

    public static int kitchen  = 0;

    public static TextView notifmount;

    public static String delivery = "";

    public static int cache = 0;

    public static String about_text, about_img;

    public static List<CouponClass> couponClassList = new ArrayList<>();

    public static String cartCoupon = "false";

    final static String file_path = "/fav.tom";

    public static Location deliveryLocation = null;

    public static int notifications = 0;

    public static List<MasterMenuItems> masterList = new ArrayList<>();

    public static List<String> BannerUrls = new ArrayList<>();

    public static MetaData metaData;

    public static List<MenuItems> cartList = new ArrayList<>();

    public static String city = null;

    public static ArrayList<MenuItems> sidesList=new ArrayList<>();

    public static int deliveryCharge = 10;

    public static int stax_amount = 0;

    public static  int discount = 10;

    public static List<UnavailableItems> unavailableItemsList = new ArrayList<>();

    public static String aboutText;

    public static String aboutImage;

    public static int amount = 0;

    public static String kitchenClosedText = "";

    public static String lane2 = "";

    public static String sublocality = "";

    public static String location_id = "";

    public static String shared_location_key="location";

}
