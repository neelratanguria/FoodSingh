package com.foodsingh.mindwires.foodsingh;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tanmay on 17-10-2017.
 */

public class FoodItem implements Parcelable{

    private String id, item, amount, address, date;

    public FoodItem(String id, String item , String amount, String address, String date){
        this.id = id;
        this.item = item;
        this.amount = amount;
        this.address = address;
        this.date = date;
    }

    protected FoodItem(Parcel in) {
        id = in.readString();
        item = in.readString();
        amount = in.readString();
        address = in.readString();
        date = in.readString();
    }

    public static final Creator<FoodItem> CREATOR = new Creator<FoodItem>() {
        @Override
        public FoodItem createFromParcel(Parcel in) {
            return new FoodItem(in);
        }

        @Override
        public FoodItem[] newArray(int size) {
            return new FoodItem[size];
        }
    };

    public String getId(){
        return id;
    }

    public String getItem(){
        return item;
    }

    public String getAmount(){
        return amount;
    }

    public String getAddress(){
        return address;
    }

    public String getDate(){
        return  date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(item);
        parcel.writeString(amount);
        parcel.writeString(address);
        parcel.writeString(date);
    }
}
