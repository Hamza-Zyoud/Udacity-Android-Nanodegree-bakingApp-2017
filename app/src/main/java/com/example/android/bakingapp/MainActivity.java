package com.example.android.bakingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.core.deps.guava.annotations.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingapp.Adapter.RecipeAdapter;
import com.example.android.bakingapp.Model.Recipe;

import java.util.ArrayList;
/*
 * Copyright 2017, Hamza Zyoud
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Created by Hamza Zyoud  on 9/29/2017.
 * Project "Baking App"- created by Hamza Zyoud as part of the Udacity Android Nanodegree
 */

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener{
    static String ALL_RECIPES="All_Recipes";
    static String SELECTED_RECIPES="Selected_Recipes";
    static String SELECTED_STEPS="Selected_Steps";
    static String SELECTED_INDEX="Selected_Index";
    @Nullable
    private SimpleIdlingResources mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResources();
        }
        return mIdlingResource;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getIdlingResource();
    }

    @Override
    public void onListItemClick(Recipe clickedItem) {
        Bundle selectedRecipeBundle = new Bundle();
        ArrayList<Recipe> selectedRecipe = new ArrayList<>();
        selectedRecipe.add(clickedItem);
        selectedRecipeBundle.putParcelableArrayList(SELECTED_RECIPES,selectedRecipe);

        final Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtras(selectedRecipeBundle);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
