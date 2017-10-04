package com.example.android.bakingapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.Model.Recipe;
import com.example.android.bakingapp.Model.Steps;
import com.example.android.bakingapp.R;

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
 * Created by Hamza Zyoud on 10/1/2017.
 */

public class DetailRecipeAdapter extends RecyclerView.Adapter<DetailRecipeAdapter.RecyclerViewHolder> {

    private onListItemClickListener clickListener;
    private List<Steps> stepsList;
    private String recipeName;

    public interface onListItemClickListener {
        void onListItemClicked(List<Steps> steps, int stepListIndex, String recipeName);
    }

    public DetailRecipeAdapter(onListItemClickListener listener) {
        clickListener = listener;
    }

    public void setDetailRecipeData(List<Recipe> recipes, String recipeName, Context context) {
        stepsList = recipes.get(0).getSteps();
        this.recipeName = recipeName;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_detail_cardview_item, viewGroup, false);

        RecyclerViewHolder viewHolder = new RecyclerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.stepsRecyclerView.setText(stepsList.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView stepsRecyclerView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            stepsRecyclerView = (TextView) itemView.findViewById(R.id.shortDescription);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            clickListener.onListItemClicked(stepsList, clickedPosition, recipeName);
        }

    }
}

