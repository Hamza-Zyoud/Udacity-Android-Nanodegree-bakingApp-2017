package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.Adapter.DetailRecipeAdapter;
import com.example.android.bakingapp.Model.Ingredients;
import com.example.android.bakingapp.Model.Recipe;
import com.example.android.bakingapp.Widget.RecieveBakingWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 9/28/2017.
 */

public class DetailActivityFragment extends Fragment {
    static final String SELECTED_RECIPES="Selected_Recipes";
    ArrayList<Recipe> recipe;
    String recipeName;

    public DetailActivityFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView;
        TextView textView;

        recipe=new ArrayList<>();

        if(savedInstanceState != null){
            recipe=savedInstanceState.getParcelableArrayList(SELECTED_RECIPES);
        }else{
            recipe=getArguments().getParcelableArrayList(SELECTED_RECIPES);
        }
        List<Ingredients> ingredients = recipe.get(0).getIngredients();
        recipeName=recipe.get(0).getRecipeName();

        View rootView=inflater.inflate(R.layout.recipe_detail_fragment,container,false);
        textView=(TextView)rootView.findViewById(R.id.Ingredients);
        ArrayList<String> IngredientsForWidgets= new ArrayList<>();

        for(Ingredients Ingredients : ingredients ){
            textView.append(Ingredients.getIngredient()+"\n");
            textView.append(Ingredients.getQuantity()+" ");
            textView.append(Ingredients.getMeasure()+"\n\n");
            IngredientsForWidgets.add(Ingredients.getIngredient()+"\n"+
                    "Quantity: "+Ingredients.getQuantity().toString()+"\n"+
                    "Measure: "+Ingredients.getMeasure()+"\n");
        }
        recyclerView=(RecyclerView)rootView.findViewById(R.id.detail_recyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        DetailRecipeAdapter detailAdapter=new DetailRecipeAdapter((DetailActivity)getActivity());

        recyclerView.setAdapter(detailAdapter);
        detailAdapter.setDetailRecipeData(recipe,recipeName,getContext());

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(recipeName);

        RecieveBakingWidget.startBakingWidget(getContext(),IngredientsForWidgets);
        return rootView;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SELECTED_RECIPES,recipe);
        outState.putString("title",recipeName);
    }
}
