package com.kippinretail.Modal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sandeep.singh on 7/27/2016.
 */
public class ModelForLoyaltyAndPunchCard implements Parcelable {
    private String LoyalityCardName;
    private String barCode;
    protected ModelForLoyaltyAndPunchCard(Parcel in) {
    }

    public static final Creator<ModelForLoyaltyAndPunchCard> CREATOR = new Creator<ModelForLoyaltyAndPunchCard>() {
        @Override
        public ModelForLoyaltyAndPunchCard createFromParcel(Parcel in) {
            return new ModelForLoyaltyAndPunchCard(in);
        }

        @Override
        public ModelForLoyaltyAndPunchCard[] newArray(int size) {
            return new ModelForLoyaltyAndPunchCard[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
