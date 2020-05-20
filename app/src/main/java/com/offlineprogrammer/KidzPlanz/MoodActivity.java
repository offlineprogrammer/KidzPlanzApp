package com.offlineprogrammer.KidzPlanz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.offlineprogrammer.KidzPlanz.planmood.PlanMood;
import com.offlineprogrammer.KidzPlanz.planmood.PlanMoodAdapter;
import com.offlineprogrammer.KidzPlanz.planmood.PlanMoodGridItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoodActivity extends AppCompatActivity {
    private static final String TAG = "MoodActivity";

    RecyclerView mRecyclerView;
    List<PlanMood> mPlanMoodList;
    PlanMood mPlanMood;
    PlanMoodAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);

        mRecyclerView = findViewById(R.id.plan_mood_recyclerview);
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(MoodActivity.this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.kpz_plan_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.kpz_kidz_grid_spacing_small);
        mRecyclerView.addItemDecoration(new PlanMoodGridItemDecoration(largePadding, smallPadding));

        prepareData();
    }

    private void prepareData() {
        mPlanMoodList = new ArrayList<>(6);

        Collections.addAll(mPlanMoodList,
                new PlanMood("Happy",  "happy"),
                new PlanMood("Angry",  "angry"),
                new PlanMood("Silly",  "crazy"),
                new PlanMood("Crying",  "crying"),
                new PlanMood("Ok",  "neutral"),
                new PlanMood("Sad",  "sad"));


        Log.i(TAG, "prepareData: Size " + mPlanMoodList.size());







        myAdapter = new PlanMoodAdapter(MoodActivity.this, mPlanMoodList);
        mRecyclerView.setAdapter(myAdapter);
    }
}
