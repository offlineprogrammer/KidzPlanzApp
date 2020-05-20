package com.offlineprogrammer.KidzPlanz.planitem;

public interface OnPlanItemListener {
    void onPlanItemCheckedChanged(int position, boolean isChecked);
    void deletePlanItem(int position);
}
