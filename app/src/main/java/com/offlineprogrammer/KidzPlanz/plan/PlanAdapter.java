package com.offlineprogrammer.KidzPlanz.plan;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.offlineprogrammer.KidzPlanz.R;

import java.util.ArrayList;

public class PlanAdapter extends RecyclerView.Adapter {
    private ArrayList<KidPlan> models = new ArrayList<>();
    private OnPlanListener mOnPlanListener;
    private static final String TAG = "KidAdapter";

    public PlanAdapter(@NonNull final ArrayList<KidPlan> viewModels, OnPlanListener onPlanListener) {
        this.models.addAll(viewModels);
        this.mOnPlanListener=onPlanListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new PlanViewHolder(view, mOnPlanListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((PlanViewHolder) holder).bindData(models.get(position));

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public ArrayList<KidPlan> getAllItems() {
        return models;
    }

    public void updateData(ArrayList<KidPlan> viewModels){
        models.clear();
        models.addAll(viewModels);
        notifyDataSetChanged();

    }

    public void delete(int position) {
        models.remove(position);
        notifyItemRemoved(position);
    }

    public void add(KidPlan item, int position){
        models.add(position, item);
        Log.i(TAG, "add: " + item.toString());
        notifyItemInserted(position);
        //notifyDataSetChanged();
        //notifyItemRangeChanged(0, getItemCount());
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.plan_itemview;
    }
}