package com.example.android.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.Adapter.DetailRecipeAdapter;
import com.example.android.bakingapp.Model.Recipe;
import com.example.android.bakingapp.Model.Steps;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.google.android.exoplayer2.C;

import org.w3c.dom.Text;

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
public class RecipeStepFragment extends Fragment {
    static String ALL_RECIPES="All_Recipes";
    static String SELECTED_RECIPES="Selected_Recipes";
    static String SELECTED_STEPS="Selected_Steps";
    static String SELECTED_INDEX="Selected_Index";

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private DefaultTrackSelector trackSelector;
    private BandwidthMeter bandwidthMeter;
    private Handler mainHandler;
    private TextView stepText;
    private ImageView thumbnailImage;
    private Button nextBtn;
    private Button prevBtn;
    private ArrayList<Steps> steps=new ArrayList<>();
    private int selectedIndex;
    private ArrayList<Recipe> recipe;
    private DetailRecipeAdapter.onListItemClickListener clickListener;
    String recipeName;
    private boolean shouldAutoPlay;
    private int resumeWindow;
    private long resumePosition;
    private static final String SELECTED_POSITION  = "selectedPosition";
    private Long position = C.TIME_UNSET;

    public RecipeStepFragment(){}

    public interface onListItemClickListener {
        void onListItemClicked(List<Steps> steps, int stepListIndex, String recipeName);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        clickListener=(DetailActivity)getActivity();
        recipe=new ArrayList<>();
        mainHandler=new Handler();
        if(savedInstanceState != null){
            steps=savedInstanceState.getParcelableArrayList(SELECTED_STEPS);
            selectedIndex=savedInstanceState.getInt(SELECTED_INDEX);
            position = savedInstanceState.getLong(SELECTED_POSITION, C.TIME_UNSET);
            recipeName = savedInstanceState.getString("Title");

        }else{
            steps=getArguments().getParcelableArrayList(SELECTED_STEPS);
            if(steps != null){
                selectedIndex=getArguments().getInt(SELECTED_INDEX);
                recipeName = getArguments().getString("title");
            }else{
                recipe=getArguments().getParcelableArrayList(SELECTED_RECIPES);
                steps=(ArrayList<Steps>)recipe.get(0).getSteps();
                selectedIndex=0;
            }
        }
        View rootView=inflater.inflate(R.layout.recipe_step_fragment,container,false);
        stepText=(TextView)rootView.findViewById(R.id.stepInstruction);
        stepText.setText(steps.get(selectedIndex).getDescription());

        mPlayerView=(SimpleExoPlayerView)rootView.findViewById(R.id.playerView);

        String videoURL=steps.get(selectedIndex).getVideoURL();
        if(!videoURL.isEmpty()){
            initializePlayer(Uri.parse(steps.get(selectedIndex).getVideoURL()));
            boolean tabletSize=getActivity().getResources().getBoolean(R.bool.isTablet);
            if (tabletSize) {
                Log.d("inside recipe step","passed");
                getActivity().findViewById(R.id.fragment_step_container).setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
                mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_WIDTH);
            }else {
               // stepText.setVisibility(View.GONE);
            }
        }else{
            mExoPlayer=null;
            mPlayerView.setForeground(ContextCompat.getDrawable(getContext(),R.drawable.exoplayer_background));
            mPlayerView.setLayoutParams(new LinearLayout.LayoutParams(500, 500));

        }
        String imageURL=steps.get(selectedIndex).getThumbnailURL();
        if(imageURL!=""){
            Uri imgUri = Uri.parse(imageURL).buildUpon().build();
            thumbnailImage=(ImageView)rootView.findViewById(R.id.thumbnailImage);
            Picasso.with(getContext()).load(imgUri).into(thumbnailImage);
        }else{
            thumbnailImage.setVisibility(View.GONE);
        }
        nextBtn=(Button)rootView.findViewById(R.id.NextStep);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lastIndex=steps.size()-1;
                if(steps.get(selectedIndex).getStepID() < steps.get(lastIndex).getStepID()){
                    if(mExoPlayer != null){
                        mExoPlayer.stop();
                    }
                    Log.d("recipe name",recipeName);
                    clickListener.onListItemClicked(steps,steps.get(selectedIndex).getStepID()+1,recipeName);
                }else{
                    Toast.makeText(getContext(),"You are in the last step",Toast.LENGTH_SHORT).show();
                }
            }
        });
        prevBtn=(Button)rootView.findViewById(R.id.PreviousStep);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(steps.get(selectedIndex).getStepID() > 0){
                    if(mExoPlayer != null){
                        mExoPlayer.stop();
                    }
                    clickListener.onListItemClicked(steps,steps.get(selectedIndex).getStepID()-1,recipeName);
                }else{
                    Toast.makeText(getContext(),"You are in the first step",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            String userAgent = Util.getUserAgent(getContext(), "Baking App");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            if(position != C.TIME_UNSET){
                mExoPlayer.seekTo(position);
            }

            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }
    private void releasePlayer() {
        mExoPlayer.stop();
        updateResumePosition();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mExoPlayer != null) {
            position = mExoPlayer.getCurrentPosition();
            mExoPlayer.stop();
            releasePlayer();
            mExoPlayer=null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mExoPlayer != null)
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mExoPlayer != null)
        releasePlayer();
    }
    private void updateResumePosition() {
        resumeWindow = mExoPlayer.getCurrentWindowIndex();
        resumePosition = Math.max(0, mExoPlayer.getCurrentPosition());
    }


    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(SELECTED_STEPS,steps);
        currentState.putInt(SELECTED_INDEX,selectedIndex);
        currentState.putLong(SELECTED_POSITION, position);
        currentState.putString("title",recipeName);
    }
}
