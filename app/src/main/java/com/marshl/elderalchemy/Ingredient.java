package com.marshl.elderalchemy;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Ingredient implements Parcelable {
    public static final String INGREDIENT_PARCEL_NAME = "INGREDIENT";
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
    private final String name;
    private final String image;
    public List<String> effectCodes = new ArrayList<>();
    private boolean selected = false;

    public Ingredient(String name, String imageName, List<String> effectCodes) {
        this.name = name;
        this.effectCodes = effectCodes;
        this.image = imageName;
    }

    public Ingredient(Parcel in) {
        super();
        this.name = in.readString();
        this.image = in.readString();
        this.selected = in.readInt() == 1;

        String[] tempEffects = new String[in.readInt()];
        in.readStringArray(tempEffects);
        this.effectCodes.addAll(Arrays.asList(tempEffects));
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.image);
        parcel.writeInt(this.selected ? 1 : 0);
        parcel.writeInt(this.effectCodes.size());
        parcel.writeStringArray(this.effectCodes.toArray(new String[0]));
    }


}
