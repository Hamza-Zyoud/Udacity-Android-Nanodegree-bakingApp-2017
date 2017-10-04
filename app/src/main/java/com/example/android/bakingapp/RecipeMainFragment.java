package com.example.android.bakingapp;

import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.Adapter.RecipeAdapter;
import com.example.android.bakingapp.Model.Recipe;
import com.example.android.bakingapp.Retrofit.RecipeRetrofit;
import com.example.android.bakingapp.Retrofit.RetrofitBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

public class RecipeMainFragment extends Fragment {
    static final String ALL_RECIPES="All_Recipes";
    RecipeAdapter recipesAdapter;
    public RecipeMainFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView;

        View rootView = inflater.inflate(R.layout.recipe_main_fragment, container, false);

        recyclerView=(RecyclerView)  rootView.findViewById(R.id.recipe_recyclerView);
        recipesAdapter=new RecipeAdapter((MainActivity)getActivity());
        recyclerView.setAdapter(recipesAdapter);



        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),4);
            recyclerView.setLayoutManager(gridLayoutManager);
        }
        else {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
        }

        RecipeRetrofit Recipes = RetrofitBuilder.Retrieve();
        final Call<ArrayList<Recipe>> recipe = Recipes.getRecipe();

        final SimpleIdlingResources idlingResource = (SimpleIdlingResources)((MainActivity)getActivity()).getIdlingResource();

        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }


        recipe.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                Integer statusCode = response.code();
                Log.v("status code: ", statusCode.toString());
                ArrayList<Recipe> recipes = response.body();
                Log.d("response:",recipes.get(0).getImage());

                Bundle recipesBundle = new Bundle();
                recipesBundle.putParcelableArrayList(ALL_RECIPES, recipes);

                recipesAdapter.setRecipeData(recipes,getContext());
                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.v("http fail: ", t.getMessage());

            }
        });

        return rootView;
    }

}
