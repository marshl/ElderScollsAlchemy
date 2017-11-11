package com.marshl.elderalchemy;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AlchemyGame implements Parcelable {

    public static final String ALCHEMY_GAME_PARCEL_NAME = "ALCHEMY_GAME";
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public AlchemyGame createFromParcel(Parcel in) {
            return new AlchemyGame(in);
        }

        public AlchemyGame[] newArray(int size) {
            return new AlchemyGame[size];
        }
    };

    private final ArrayList<AlchemyPackage> packages = new ArrayList<>();
    private final Map<String, AlchemyEffect> effectMap = new HashMap<>();
    private final String name;
    private final String prefix;
    private Map<String, List<Ingredient>> effectToIngredientMap = new HashMap<>();
    private List<String> availableEffects = new ArrayList<>();

    private AlchemyGame(Parcel in) {
        super();
        this.name = in.readString();
        this.prefix = in.readString();

        Object[] packageArray = in.readArray(AlchemyPackage.class.getClassLoader());

        for (Object pkg : packageArray) {
            this.packages.add((AlchemyPackage) pkg);
        }

        Object[] effectArray = in.readArray(AlchemyEffect.class.getClassLoader());
        for (Object obj : effectArray) {
            AlchemyEffect effect = (AlchemyEffect) obj;
            this.effectMap.put(effect.getCode(), effect);
        }
    }

    public AlchemyGame(String name, String prefix) {
        this.name = name;
        this.prefix = prefix;
    }

    public void addPackage(AlchemyPackage pkg) {
        this.packages.add(pkg);
    }

    public int getPackageCount() {
        return this.packages.size();
    }

    public AlchemyPackage getPackage(int index) {
        return this.packages.get(index);
    }

    public void addEffect(AlchemyEffect effect) {
        this.effectMap.put(effect.getCode(), effect);
    }

    public int getAvailableEffectCount() {
        return this.availableEffects.size();
    }

    public AlchemyEffect getEffectByIndex(int index) {
        return this.getEffect(this.availableEffects.get(index));
    }

    public AlchemyEffect getEffect(String code) {
        return this.effectMap.get(code);
    }

    public List<Ingredient> getIngredientsForEffect(String effectCode) {
        return this.effectToIngredientMap.get(effectCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.prefix);
        parcel.writeArray(this.packages.toArray());
        parcel.writeArray(this.effectMap.values().toArray());
    }

    public String getGameName() {
        return this.name;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void recalculateIngredientEffects() {
        this.effectToIngredientMap = new HashMap<>();
        this.availableEffects = new ArrayList<>();

        for (AlchemyPackage alchemyPackage : this.packages) {
            for (Ingredient ingredient : alchemyPackage.ingredients) {
                if (!ingredient.isSelected()) {
                    continue;
                }

                for (String effect : ingredient.effectCodes) {
                    if (this.effectToIngredientMap.get(effect) == null) {
                        this.effectToIngredientMap.put(effect, new ArrayList<Ingredient>());
                        this.availableEffects.add(effect);
                    }
                    this.effectToIngredientMap.get(effect).add(ingredient);
                }
            }
        }

        for (int i = 0; i < this.availableEffects.size(); ) {
            String effect = this.availableEffects.get(i);
            if (this.effectToIngredientMap.get(effect).size() < 2) {
                this.effectToIngredientMap.remove(effect);
                this.availableEffects.remove(i);
            } else {
                ++i;
            }
        }
        Collections.sort(this.availableEffects);
    }

    public int getEffectImageResource(String effectCode, Activity context) {
        AlchemyEffect effect = this.effectMap.get(effectCode);
        return context.getResources().getIdentifier(effect.getImage(), "drawable", context.getPackageName().toLowerCase());
    }

    public int getIngredientImageResource(Ingredient ingredient, Activity context) {
        return context.getResources().getIdentifier(ingredient.getImage(), "drawable", context.getPackageName().toLowerCase());
    }

    public void setIngredientSelection(boolean selected) {
        for (AlchemyPackage pack : this.packages) {
            for (Ingredient ingred : pack.ingredients) {
                ingred.setSelected(selected);
            }
        }
    }

    public void loadSelectedIngredients(Set<String> ingredients) {
        for (AlchemyPackage pack : this.packages) {
            for (Ingredient ingredient : pack.ingredients) {
                ingredient.setSelected(ingredients.contains(ingredient.getName()));
            }
        }
    }

    public Set<String> getselectedIngredientSet() {
        Set<String> selectedIngredients = new HashSet<>();
        for (AlchemyPackage pack : this.packages) {
            for (Ingredient ingred : pack.ingredients) {
                if (ingred.isSelected()) {
                    selectedIngredients.add(ingred.getName());
                }
            }
        }

        return selectedIngredients;
    }
}
