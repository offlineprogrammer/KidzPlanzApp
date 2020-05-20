package com.offlineprogrammer.KidzPlanz.planreward;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.offlineprogrammer.KidzPlanz.R;

public class PlanRewardViewHolder extends RecyclerView.ViewHolder {
     ImageView mImage;
     TextView mTitle;
     CardView mCardView;

    PlanRewardViewHolder(View itemView) {
        super(itemView);
        mImage = itemView.findViewById(R.id.planreward_imageView);
        mTitle = itemView.findViewById(R.id.planreward_name_textView);
        mCardView = itemView.findViewById(R.id.planreward_CardView);


    }
}
