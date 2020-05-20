package com.offlineprogrammer.KidzPlanz.plan;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.offlineprogrammer.KidzPlanz.R;

public class PlanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView planNameTextView;
    private ImageView planImageView;
    private Context mContext;
    OnPlanListener onPlanListener;
    public PlanViewHolder(@NonNull View itemView, OnPlanListener onPlanListener) {
        super(itemView);
        mContext = itemView.getContext();
        planNameTextView = itemView.findViewById(R.id.plan_name_textView);
        planImageView = itemView.findViewById(R.id.plan_imageView);
        this.onPlanListener = onPlanListener;
        itemView.setOnClickListener(this);
    }

    public void bindData(final KidPlan viewModel) {
        planNameTextView.setText(viewModel.getPlanName());
        planImageView.setImageResource( mContext.getResources().getIdentifier(viewModel.getPlanImageResourceName() , "drawable" ,
                mContext.getPackageName()) );
    }

    @Override
    public void onClick(View v) {
        onPlanListener.onPlanClick(getAdapterPosition());

    }
}


