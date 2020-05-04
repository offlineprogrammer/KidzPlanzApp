package com.offlineprogrammer.kidzplanz.planitem;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.offlineprogrammer.kidzplanz.R;

import java.util.ArrayList;

public class PlanItemAdapter extends RecyclerView.Adapter {
    private ArrayList<PlanItem> models = new ArrayList<>();
    private OnPlanItemListener mOnPlanItemListener;
    private static final String TAG = "PlanItemAdapter";

    public PlanItemAdapter(@NonNull final ArrayList<PlanItem> viewModels, OnPlanItemListener onPlanItemListener) {
        this.models.addAll(viewModels);
        this.mOnPlanItemListener =onPlanItemListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new PlanItemViewHolder(view, mOnPlanItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((PlanItemViewHolder) holder).bindData(models.get(position));

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public ArrayList<PlanItem> getAllItems() {
        return models;
    }

    public void updateData(ArrayList<PlanItem> viewModels){
        models.clear();
        models.addAll(viewModels);
        notifyDataSetChanged();

    }

    public void delete(int position) {
        models.remove(position);
        notifyItemRemoved(position);
    }

    public void add(PlanItem item, int position){
        models.add(position, item);
        Log.i(TAG, "add: " + item.toString());
        notifyItemInserted(position);
        //notifyDataSetChanged();
        //notifyItemRangeChanged(0, getItemCount());
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.planitem_itemview;
    }
}

