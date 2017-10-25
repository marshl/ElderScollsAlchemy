package com.marshl.alembic;

import android.os.Parcel;
import android.os.Parcelable;

public class AlchemyEffect implements Parcelable {
    public static final String EFFECT_PARCEL_NAME = "ALCHEMY_EFFECT";
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AlchemyEffect createFromParcel(Parcel in) {
            return new AlchemyEffect(in);
        }

        public AlchemyEffect[] newArray(int size) {
            return new AlchemyEffect[size];
        }
    };
    private String code;
    private String image;
    private String name;

    public AlchemyEffect(String code, String name, String image) {
        this.code = code;
        this.name = name;
        this.image = image;
    }

    public AlchemyEffect(Parcel in) {
        super();
        this.code = in.readString();
        this.name = in.readString();
        this.image = in.readString();
    }

    public String getCode() {
        return code;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.code);
        parcel.writeString(this.name);
        parcel.writeString(this.image);
    }

}
