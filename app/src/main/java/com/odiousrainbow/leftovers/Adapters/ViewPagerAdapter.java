package com.odiousrainbow.leftovers.Adapters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.odiousrainbow.leftovers.DataModel.Recipe;
import com.odiousrainbow.leftovers.DishDetailsFragments.FragmentHow;
import com.odiousrainbow.leftovers.DishDetailsFragments.FragmentIngredient;
import com.odiousrainbow.leftovers.DishDetailsFragments.FragmentNutrition;

import java.io.Serializable;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private static final String INGRE = "NGUYÊN LIỆU";
    private static final String HOW = "CÁCH NẤU";
    private static final String DD = "DINH DƯỠNG";
    private Recipe currentRecipe;

    public ViewPagerAdapter(FragmentManager fm, Recipe recipe) {
        super(fm);
        this.currentRecipe = recipe;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                Bundle b = new Bundle();
                b.putSerializable("recipes",(Serializable) currentRecipe.getIngredients());
                FragmentIngredient fragmentIngredient = FragmentIngredient.getInstance();
                fragmentIngredient.setArguments(b);

                return fragmentIngredient;
            }

            case 1: {
                Bundle b = new Bundle();
                b.putString("instr",currentRecipe.getInstruction());
                FragmentHow fragmentHow = FragmentHow.getInstance();
                fragmentHow.setArguments(b);

                return fragmentHow;
            }

            case 2: {
                return FragmentNutrition.getInstance();
            }

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: {
                return INGRE;
            }
            case 1: {
                return HOW;
            }
            case 2: {
                return DD;
            }
        }
        return super.getPageTitle(position);
    }
}
