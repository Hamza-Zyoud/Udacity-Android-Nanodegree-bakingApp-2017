package com.example.android.bakingapp.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.Model.Recipe;
import com.example.android.bakingapp.R;
import com.squareup.picasso.Picasso;

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
 * Created by Hamza Zyoud on 10/1/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecyclerViewHolder> {


    ArrayList<Recipe> recipes;
    Context mContext;
    boolean flag=true;
    final private ListItemClickListener RecipeOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(Recipe clickedItem);

    }
    public RecipeAdapter(ListItemClickListener listener) {
        RecipeOnClickListener = listener;
    }

    public void setRecipeData(ArrayList<Recipe> recipesIn, Context context) {
        recipes = recipesIn;
        mContext=context;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_cardview_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup,  false);
       RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.recipeText.setText(recipes.get(position).getRecipeName());
        String imageUrl=recipes.get(position).getImage();
        int recipeID=recipes.get(position).getId();
        Log.d("recipeID",recipeID+"");
        int imageID=0;
        if (imageUrl!="") {
            Log.d("inside if","passed");
            Uri builtUri = Uri.parse(imageUrl).buildUpon().build();
            Picasso.with(mContext).load(builtUri).into(holder.recipeImg);
        }
        /*else{
            Log.d("inside else","passed");
            switch(recipeID){
                case 1:
                    Log.d("inside case 1","passed");
                    imageID=R.mipmap.nutella_pie;
                    break;
                case 2:
                    Log.d("inside case 2","passed");
                    imageID=R.mipmap.brownie;
                    break;
                case 3:
                    imageID=R.mipmap.yellow_cake;
                    break;
                case 4:
                    imageID=R.mipmap.cheesecake;
                    break;
                default:
                   break;
            }
            Picasso.with(mContext).load(imageID).into(holder.recipeImg);
        }*/

    }

    @Override
    public int getItemCount() {
        return recipes !=null ? recipes.size():0 ;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView recipeText;
        ImageView recipeImg;


        public RecyclerViewHolder(View itemView) {
            super(itemView);

            recipeText = (TextView) itemView.findViewById(R.id.title);
            recipeImg = (ImageView) itemView.findViewById(R.id.recipeImage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            RecipeOnClickListener.onListItemClick(recipes.get(clickedPosition));
        }

    }
}

