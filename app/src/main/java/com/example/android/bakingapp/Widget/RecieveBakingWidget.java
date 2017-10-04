package com.example.android.bakingapp.Widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.widget.ArrayAdapter;

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
 * Created by Hamza Zyoud  on 10/01/2017.
 * Project "Baking App"- created by Hamza Zyoud as part of the Udacity Android Nanodegree
 */
public class RecieveBakingWidget extends IntentService {

    public static String ACTIVITY_INGREDIENTS_LIST="ACTIVITY_INGREDIENTS_LIST";

    public RecieveBakingWidget(){
        super("RecieveBakingWidget");
    }

    public static void startBakingWidget(Context context, ArrayList<String> ActivityIngredients) {
        Intent intent = new Intent(context, RecieveBakingWidget.class);
        intent.putExtra(ACTIVITY_INGREDIENTS_LIST,ActivityIngredients);
        context.startService(intent);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent != null){
            ArrayList<String> ActivityIngredients=intent.getExtras().getStringArrayList(ACTIVITY_INGREDIENTS_LIST);
           handleAction(ActivityIngredients);
        }
    }

    private void handleAction(ArrayList<String> list){
        Intent UpdateIntent = new Intent("android.appwidget.action.APPWIDGET_UPDATE2");
        UpdateIntent.setAction("android.appwidget.action.APPWIDGET_UPDATE2");
        UpdateIntent.putExtra(ACTIVITY_INGREDIENTS_LIST,list);
        sendBroadcast(UpdateIntent);
    }
}
