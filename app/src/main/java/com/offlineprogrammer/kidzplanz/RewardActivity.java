package com.offlineprogrammer.kidzplanz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.offlineprogrammer.kidzplanz.planreward.PlanReward;
import com.offlineprogrammer.kidzplanz.planreward.PlanRewardAdapter;
import com.offlineprogrammer.kidzplanz.planreward.PlanRewardGridItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class RewardActivity extends AppCompatActivity {

    private static final String TAG = "RewardActivity";

    RecyclerView mRecyclerView;
    List<PlanReward> mPlanRewardList;
    PlanReward mPlanReward;
    PlanRewardAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);

        mRecyclerView = findViewById(R.id.plan_reward_recyclerview);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(RewardActivity.this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.kpz_plan_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.kpz_kidz_grid_spacing_small);
        mRecyclerView.addItemDecoration(new PlanRewardGridItemDecoration(largePadding, smallPadding));


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                prepareData();
            }
        });



    }

    private void prepareData() {
        mPlanRewardList = new ArrayList<>(7);

        Collections.addAll(mPlanRewardList, new PlanReward("Book",  "book"),
                new PlanReward("Candy",  "candy"),
                new PlanReward("Game",  "game"),
                new PlanReward("Good Job!",  "goodjob"),
                new PlanReward("High Five!",  "highfive"),
                new PlanReward("Hug",  "hug"),
                new PlanReward("Ice Cream",  "icecream"),
                new PlanReward("music",  "Movie"),
                new PlanReward("Music",  "music"),
                new PlanReward("Park",  "park"),
                new PlanReward("Pizza",  "pizza"),
                new PlanReward("Playdate",  "playdate"),
                new PlanReward("Playtime",  "playtime"),
                new PlanReward("Pocket Money",  "pocketmony"),
                new PlanReward("Winner",  "proud"),
                new PlanReward("Resturant",  "resturant"),
                new PlanReward("Snack",  "snack"),
                new PlanReward("Swimming",  "swiming"),
                new PlanReward("Phone/Tablet",  "tablet"),
                new PlanReward("toy",  "toy"));


        Log.i(TAG, "prepareData: Size " + mPlanRewardList.size());







        myAdapter = new PlanRewardAdapter(RewardActivity.this, mPlanRewardList);
        mRecyclerView.setAdapter(myAdapter);
    }
}
