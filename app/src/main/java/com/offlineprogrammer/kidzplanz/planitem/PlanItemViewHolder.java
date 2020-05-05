package com.offlineprogrammer.kidzplanz.planitem;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.offlineprogrammer.kidzplanz.R;

public class PlanItemViewHolder extends RecyclerView.ViewHolder implements SwitchMaterial.OnCheckedChangeListener {
    private SwitchMaterial planItemSwitch;
    OnPlanItemListener onPlanItemListener;
    private Context mContext;
    public PlanItemViewHolder(@NonNull View itemView, OnPlanItemListener onPlanItemListener) {
        super(itemView);
        mContext = itemView.getContext();
        planItemSwitch = itemView.findViewById(R.id.planItem_switch);
        this.onPlanItemListener = onPlanItemListener;
        planItemSwitch.setOnCheckedChangeListener(this);
    }

    public void bindData(final PlanItem viewModel) {
        planItemSwitch.setText(viewModel.getPlanItemName());
        planItemSwitch.setChecked(viewModel.getbCompleted());
        if (viewModel.getbCompleted()) {
            planItemSwitch.setPaintFlags(planItemSwitch.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            planItemSwitch.setPaintFlags(planItemSwitch.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }

    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.isPressed()) {
            onPlanItemListener.onPlanItemCheckedChanged(getAdapterPosition(), isChecked);
            if (isChecked) {
                planItemSwitch.setPaintFlags(planItemSwitch.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                planItemSwitch.setPaintFlags(planItemSwitch.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            }
        }
    }
}

