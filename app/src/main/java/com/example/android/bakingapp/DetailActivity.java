package com.example.android.bakingapp;

import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.bakingapp.Adapter.DetailRecipeAdapter;
import com.example.android.bakingapp.Model.Recipe;
import com.example.android.bakingapp.Model.Steps;

import java.util.ArrayList;
import java.util.List;
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

public class DetailActivity extends AppCompatActivity implements DetailRecipeAdapter.onListItemClickListener,
        RecipeStepFragment.onListItemClickListener {
    static String SELECTED_RECIPES = "Selected_Recipes";
    static String SELECTED_STEPS = "Selected_Steps";
    static String SELECTED_INDEX = "Selected_Index";
    static String STACK_RECIPE = "STACK_RECIPE";


    private ArrayList<Recipe> recipe;
    String recipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {

            Bundle selectedRecipeBundle = getIntent().getExtras();

            recipe = new ArrayList<>();
            recipe = selectedRecipeBundle.getParcelableArrayList(SELECTED_RECIPES);
            recipeName = recipe.get(0).getRecipeName();

            final DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(selectedRecipeBundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment).addToBackStack(STACK_RECIPE)
                    .commit();
            boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
            if (tabletSize) {
                Log.d("inside tablet","passed");
              final  RecipeStepFragment stepFragment = new RecipeStepFragment();
                stepFragment.setArguments(selectedRecipeBundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_step_container, stepFragment).addToBackStack(STACK_RECIPE)
                        .commit();
            }

        } else {
            recipeName = savedInstanceState.getString("Title");
        }
        getSupportActionBar().setTitle(recipeName);
    }
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack(STACK_RECIPE,0);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onListItemClicked(List<Steps> steps, int stepListIndex, String recipeName) {
       final RecipeStepFragment fragment = new RecipeStepFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        getSupportActionBar().setTitle(recipeName);

        Bundle stepBundle = new Bundle();
        stepBundle.putParcelableArrayList(SELECTED_STEPS, (ArrayList<Steps>) steps);
        stepBundle.putInt(SELECTED_INDEX, stepListIndex);
        stepBundle.putString("title",recipeName);
        fragment.setArguments(stepBundle);

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_step_container, fragment)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment).addToBackStack(STACK_RECIPE)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelableArrayList(SELECTED_RECIPES,recipe);
        outState.putString("title", recipeName);
    }
}
