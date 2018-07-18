package com.foodsingh.mindwires.foodsingh;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tanmay on 01-11-2017.
 */

public class localdatabasesaver implements Parcelable{
    private static localdatabase local;

    public localdatabasesaver(localdatabase local){
        this.local = local;
    }

    protected localdatabasesaver(Parcel in) {
    }

    public static final Creator<localdatabasesaver> CREATOR = new Creator<localdatabasesaver>() {
        @Override
        public localdatabasesaver createFromParcel(Parcel in) {
            return new localdatabasesaver(in);
        }

        @Override
        public localdatabasesaver[] newArray(int size) {
            return new localdatabasesaver[size];
        }
    };

    public localdatabase getLocal(){
        return local;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
