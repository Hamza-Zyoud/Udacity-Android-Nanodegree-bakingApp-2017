package com.example.android.bakingapp.Widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;

import java.util.List;

import static com.example.android.bakingapp.Widget.BakingAppWidget.ingredientsList;

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
 * Created by Hamza Zyoud  on 10/01/2017.
 * Project "Baking App"- created by Hamza Zyoud as part of the Udacity Android Nanodegree
 */
public class GridWidgetService extends RemoteViewsService {
    List<String> remoteViewsIngredients;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        Context mContext = null;

        public GridRemoteViewsFactory(Context applicationContext, Intent intent) {
            mContext = applicationContext;

        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            remoteViewsIngredients = ingredientsList;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return remoteViewsIngredients.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_grid_view_item);
            views.setTextViewText(R.id.widget_grid_view_item, remoteViewsIngredients.get(i));
            Intent fillInIntent = new Intent();
            views.setOnClickFillInIntent(R.id.widget_grid_view_item, fillInIntent);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}