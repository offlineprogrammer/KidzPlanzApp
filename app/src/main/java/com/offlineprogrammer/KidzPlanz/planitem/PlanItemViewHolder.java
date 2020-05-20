package com.offlineprogrammer.KidzPlanz.planitem;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.offlineprogrammer.KidzPlanz.R;

public class PlanItemViewHolder extends RecyclerView.ViewHolder implements SwitchMaterial.OnCheckedChangeListener, ImageButton.OnClickListener {
    private SwitchMaterial planItemSwitch;
    private ImageButton deleteImageButton;
    OnPlanItemListener onPlanItemListener;
    private Context mContext;
    public PlanItemViewHolder(@NonNull View itemView, OnPlanItemListener onPlanItemListener) {
        super(itemView);
        mContext = itemView.getContext();
        planItemSwitch = itemView.findViewById(R.id.planItem_switch);
        deleteImageButton = itemView.findViewById(R.id.planItem_delete_button);
        this.onPlanItemListener = onPlanItemListener;
        planItemSwitch.setOnCheckedChangeListener(this);
        deleteImageButton.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        final AlertDialog builder = new AlertDialog.Builder(mContext).create();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View dialogView = inflater.inflate(R.layout.alert_dialog_delete_plan, null);
        Button okBtn = dialogView.findViewById(R.id.deleteplan_confirm_button);
        Button cancelBtn = dialogView.findViewById(R.id.deleteplan_cancel_button);

        okBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                builder.dismiss();
                onPlanItemListener.deletePlanItem(getAdapterPosition());
                // mFirebaseAnalytics.logEvent("kid_deleted", null);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        builder.setView(dialogView);
        builder.show();


    }
}

