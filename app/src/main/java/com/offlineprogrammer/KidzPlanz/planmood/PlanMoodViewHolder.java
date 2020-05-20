package com.offlineprogrammer.KidzPlanz.planmood;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.offlineprogrammer.KidzPlanz.R;

public class PlanMoodViewHolder extends RecyclerView.ViewHolder {
     ImageView mImage;
     TextView mTitle;
     CardView mCardView;

    PlanMoodViewHolder(View itemView) {
        super(itemView);
        mImage = itemView.findViewById(R.id.planmood_imageView);
        mTitle = itemView.findViewById(R.id.planmood_name_textView);
        mCardView = itemView.findViewById(R.id.planmood_CardView);


    }
}
