package com.offlineprogrammer.kidzplanz.planitem;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.offlineprogrammer.kidzplanz.R;

public class PlanItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private SwitchMaterial planItemSwitch;
    OnPlanItemListener onPlanItemListener;
    private Context mContext;
    public PlanItemViewHolder(@NonNull View itemView, OnPlanItemListener onPlanItemListener) {
        super(itemView);
        mContext = itemView.getContext();
        planItemSwitch = itemView.findViewById(R.id.planItem_switch);
        this.onPlanItemListener = onPlanItemListener;
        itemView.setOnClickListener(this);
    }

    public void bindData(final PlanItem viewModel) {
        planItemSwitch.setText(viewModel.getPlanItemName());
        planItemSwitch.setChecked(viewModel.getbCompleted());
    }

    @Override
    public void onClick(View v) {
        onPlanItemListener.onPlanItemClick(getAdapterPosition());

    }
}

