package com.example.weatherapp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class weatherObject  implements Parcelable {
    // Properties of the Album class
    private String tempMin;
    private String tempMax;
    private String tempVal;
    private String feelsLike;

    public weatherObject(String tempMin, String tempMax, String tempVal, String feelsLike)
    {
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.tempVal = tempVal;
        this.feelsLike = feelsLike;

    }

    public String tempMin() {
        return tempMin;
    }

    public String tempMax() {
        return tempMax();
    }

    public String tempVal() {
        return tempVal;
    }


    public String feelsLike() {
        return feelsLike;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {


    }
}
