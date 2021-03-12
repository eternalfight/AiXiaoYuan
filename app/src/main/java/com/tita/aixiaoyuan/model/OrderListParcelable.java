package com.tita.aixiaoyuan.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderListParcelable implements Parcelable {
   private String objectId;

    protected OrderListParcelable(Parcel in) {
        objectId = in.readString();
    }

    public static final Creator<OrderListParcelable> CREATOR = new Creator<OrderListParcelable>() {
        @Override
        public OrderListParcelable createFromParcel(Parcel in) {
            return new OrderListParcelable(in);
        }

        @Override
        public OrderListParcelable[] newArray(int size) {
            return new OrderListParcelable[size];
        }
    };

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(objectId);
    }
}
