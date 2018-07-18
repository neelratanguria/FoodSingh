package com.foodsingh.mindwires.foodsingh;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tanmay on 14-10-2017.
 */

public class MenuItems implements Parcelable{




    private String Id, Name, Category, Price, Image, Status, Detail,R_price,location_id;

    private int quantity = 0;

    MenuItems(String Id, String Name, String Category, String Price, String Image, String Status, String Detail, String R_price, String location_id){
        this.Id = Id;
        this.Name = Name;
        this.Category = Category;
        this.Price = Price;
        this.Image = Image;
        this.Status = Status;
        this.Detail = Detail;
        this.R_price = R_price;
        this.location_id = location_id;

    }

    protected MenuItems(Parcel in) {
        Id = in.readString();
        Name = in.readString();
        Category = in.readString();
        Price = in.readString();
        Image = in.readString();
        quantity = in.readInt();
        Detail = in.readString();
        Status = in.readString();
        R_price = in.readString();
        location_id = in.readString();
    }

    public String getR_price() {
        return R_price;
    }

    public String getDetail() {
        return Detail;
    }

    public static final Creator<MenuItems> CREATOR = new Creator<MenuItems>() {
        @Override
        public MenuItems createFromParcel(Parcel in) {
            return new MenuItems(in);
        }

        @Override
        public MenuItems[] newArray(int size) {
            return new MenuItems[size];
        }
    };

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getId(){
        return Id;
    }

    public String getName(){
        return Name;
    }


    public String getCategory(){
        return Category;
    }

    public String getPrice(){
        return Price;
    }

    public String getImage(){

        return Image;
    }

    public String getStatus() {
        return Status;
    }



    public void setStatus(String status) {
        Status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Id);
        parcel.writeString(Name);
        parcel.writeString(Category);
        parcel.writeString(Price);
        parcel.writeString(Image);
        parcel.writeInt(quantity);
        parcel.writeString(Detail);
        parcel.writeString(Status);
        parcel.writeString(R_price);
        parcel.writeString(location_id);
    }

    public String getLocation_id(){
        return location_id;
    }
}
