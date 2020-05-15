package com.offlineprogrammer.kidzplanz.planmood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.offlineprogrammer.kidzplanz.PlanActivity;
import com.offlineprogrammer.kidzplanz.R;
import com.offlineprogrammer.kidzplanz.planreward.PlanRewardViewHolder;

import java.util.List;

public class PlanMoodAdapter extends RecyclerView.Adapter<PlanMoodViewHolder> {

    private Context mContext;
    private List<PlanMood> mPlanMoodList;

    public PlanMoodAdapter(Context mContext, List< PlanMood > mPlanMoodList) {
        this.mContext = mContext;
        this.mPlanMoodList = mPlanMoodList;
    }

    @NonNull
    @Override
    public PlanMoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.planmood_itemview, parent, false);
        return new PlanMoodViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PlanMoodViewHolder holder, int position) {

        holder.mImage.setImageResource( mContext.getResources().getIdentifier(mPlanMoodList.get(position).getPlanMoodImage() , "drawable" ,
                mContext.getPackageName()) );
        holder.mTitle.setText(mPlanMoodList.get(position).getPlanMoodName());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(mContext, PlanActivity.class);
                mIntent.putExtra("Image", mPlanMoodList.get(holder.getAdapterPosition()).getPlanMoodImage());
                mIntent.putExtra("PlanMood", mPlanMoodList.get(holder.getAdapterPosition()).getPlanMoodName());
                ((Activity)mContext).setResult(Activity.RESULT_OK,mIntent);
                ((Activity)mContext).finish();
                mContext.startActivity(mIntent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mPlanMoodList.size();
    }
}