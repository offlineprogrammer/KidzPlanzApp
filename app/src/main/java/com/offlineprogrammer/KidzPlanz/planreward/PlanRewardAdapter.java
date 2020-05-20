package com.offlineprogrammer.KidzPlanz.planreward;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.offlineprogrammer.KidzPlanz.PlanActivity;
import com.offlineprogrammer.KidzPlanz.R;

import java.util.List;

public class PlanRewardAdapter extends RecyclerView.Adapter<PlanRewardViewHolder> {

    private Context mContext;
    private List<PlanReward> mPlanRewardList;

    public PlanRewardAdapter(Context mContext, List< PlanReward > mPlanRewardList) {
        this.mContext = mContext;
        this.mPlanRewardList = mPlanRewardList;
    }

    @NonNull
    @Override
    public PlanRewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.planreward_itemview, parent, false);
        return new PlanRewardViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final PlanRewardViewHolder holder, int position) {

        holder.mImage.setImageResource( mContext.getResources().getIdentifier(mPlanRewardList.get(position).getPlanRewardImage() , "drawable" ,
                mContext.getPackageName()) );
        holder.mTitle.setText(mPlanRewardList.get(position).getPlanRewardName());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(mContext, PlanActivity.class);
                mIntent.putExtra("Image", mPlanRewardList.get(holder.getAdapterPosition()).getPlanRewardImage());
                mIntent.putExtra("PlanReward", mPlanRewardList.get(holder.getAdapterPosition()).getPlanRewardName());
                ((Activity)mContext).setResult(Activity.RESULT_OK,mIntent);
                ((Activity)mContext).finish();
                mContext.startActivity(mIntent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mPlanRewardList.size();
    }
}