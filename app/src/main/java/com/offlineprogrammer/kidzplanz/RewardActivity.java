package com.offlineprogrammer.kidzplanz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.offlineprogrammer.kidzplanz.planreward.PlanReward;
import com.offlineprogrammer.kidzplanz.planreward.PlanRewardAdapter;
import com.offlineprogrammer.kidzplanz.planreward.PlanRewardGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class RewardActivity extends AppCompatActivity {

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

        mPlanRewardList = new ArrayList<>();

        mPlanReward = new PlanReward("Book",  "book");
        mPlanRewardList.add(mPlanReward);

        mPlanReward = new PlanReward("Candy",  "candy");
        mPlanRewardList.add(mPlanReward);

        mPlanReward = new PlanReward("Game",  "game");
        mPlanRewardList.add(mPlanReward);

       /*   mPlanReward = new PlanReward("Good Job!",  "goodjob");
        mPlanRewardList.add(mPlanReward);

        mPlanReward = new PlanReward("High Five!",  "highfive");
        mPlanRewardList.add(mPlanReward);


        mPlanReward = new PlanReward("Hug",  "hug");
        mPlanRewardList.add(mPlanReward);

        mPlanReward = new PlanReward("Ice Cream",  "icecream");
        mPlanRewardList.add(mPlanReward);

        mPlanReward = new PlanReward("music",  "Movie");
        mPlanRewardList.add(mPlanReward);

        mPlanReward = new PlanReward("Music",  "music");
        mPlanRewardList.add(mPlanReward);

        mPlanReward = new PlanReward("Park",  "park");
        mPlanRewardList.add(mPlanReward);


        mPlanReward = new PlanReward("Pizza",  "pizza");
        mPlanRewardList.add(mPlanReward);

        mPlanReward = new PlanReward("Playdate",  "playdate");
        mPlanRewardList.add(mPlanReward);

        mPlanReward = new PlanReward("Playtime",  "playtime");
        mPlanRewardList.add(mPlanReward);

        mPlanReward = new PlanReward("Pocket Money",  "pocketmony");
        mPlanRewardList.add(mPlanReward);

        mPlanReward = new PlanReward("Winner",  "proud");
        mPlanRewardList.add(mPlanReward);


        mPlanReward = new PlanReward("Resturant",  "resturant");
        mPlanRewardList.add(mPlanReward);

        mPlanReward = new PlanReward("Snack",  "snack");
        mPlanRewardList.add(mPlanReward);

        mPlanReward = new PlanReward("Swimming",  "swiming");
        mPlanRewardList.add(mPlanReward);

        mPlanReward = new PlanReward("Phone/Tablet",  "tablet");
        mPlanRewardList.add(mPlanReward);

        mPlanReward = new PlanReward("toy",  "toy");
        mPlanRewardList.add(mPlanReward);*/



        myAdapter = new PlanRewardAdapter(RewardActivity.this, mPlanRewardList);
        mRecyclerView.setAdapter(myAdapter);
    }
}
