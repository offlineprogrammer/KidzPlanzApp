package com.offlineprogrammer.kidzplanz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.offlineprogrammer.kidzplanz.kid.Kid;
import com.offlineprogrammer.kidzplanz.plan.KidPlan;
import com.offlineprogrammer.kidzplanz.planitem.OnPlanItemListener;
import com.offlineprogrammer.kidzplanz.planitem.PlanItem;
import com.offlineprogrammer.kidzplanz.planitem.PlanItemAdapter;
import com.offlineprogrammer.kidzplanz.planitem.PlanItemGridItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PlanActivity extends AppCompatActivity implements OnPlanItemListener {
    private static final String TAG = "PlanActivity";

    ImageView planImageView;
    ImageView rewardimageView;
    TextView planNameTextView;
    KidPlan selectedPlan;
    FirebaseHelper firebaseHelper;
    private Disposable disposable;
    ProgressDialog progressBar;
    Kid selectedKid;
    private RecyclerView recyclerView;
    private PlanItemAdapter mAdapter;
    private ArrayList<PlanItem> planItemzList = new ArrayList<>();

    CardView planRewardCard;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        planImageView = findViewById(R.id.planImage);
        planNameTextView = findViewById(R.id.plannameTextView);
        planRewardCard = findViewById(R.id.rewardCard);
        rewardimageView = findViewById(R.id.rewardimageView);

        configActionButton();
        firebaseHelper = new FirebaseHelper();
        setupRecyclerView();


        if (getIntent().getExtras() != null) {
            selectedPlan = getIntent().getExtras().getParcelable("selected_plan");
            selectedKid = getIntent().getExtras().getParcelable("selected_kid");
            planImageView.setImageResource( getApplicationContext().getResources().getIdentifier(selectedPlan.getPlanImageResourceName() , "drawable" ,
                    getApplicationContext().getPackageName()) );
            planNameTextView.setText(selectedPlan.getPlanName());
            getPlanItemz();
        }

        planRewardCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(PlanActivity.this, RewardActivity.class);
                startActivityForResult(mIntent, 3);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {


                String rewardImageResourceName =  data.getStringExtra("Image");
                String rewardNAame =  data.getStringExtra("PlanReward");
                rewardimageView.setImageResource( getApplicationContext().getResources().getIdentifier(rewardImageResourceName , "drawable" ,
                        getApplicationContext().getPackageName()) );
                updateRewardImage(rewardImageResourceName);


            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void updateRewardImage(String rewardImageResourceName) {
        selectedPlan.setRewardImageResourceName(rewardImageResourceName);
        Log.i(TAG, "updateRewardImage: " + rewardImageResourceName);
        firebaseHelper.updateRewardImage(selectedPlan, selectedKid)
                .subscribe(() -> {
                    Log.i(TAG, "updateRewardImage: completed");
            // handle completion
                }, throwable -> {
            // handle error
            });
    }

    private void getPlanItemz() {
        firebaseHelper.getPlanItemz(selectedPlan, selectedKid).observeOn(Schedulers.io())
                //.observeOn(Schedulers.m)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ArrayList<PlanItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                        disposable = d;
                    }

                    @Override
                    public void onNext(ArrayList<PlanItem> planItemz) {
                        Log.d(TAG, "onNext:  " + planItemz.size());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateRecylerView(planItemz);
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }

    private void setupRecyclerView() {
        mAdapter = new PlanItemAdapter(planItemzList,this);
        recyclerView = findViewById(R.id.planitems_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        int largePadding = getResources().getDimensionPixelSize(R.dimen.kpz_planItemz_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.kpz_planItemz_grid_spacing_small);
        recyclerView.addItemDecoration(new PlanItemGridItemDecoration(largePadding, smallPadding));

    }

    private void configActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab_add_planitem);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddPlanItemDialog(PlanActivity.this);
            }
        });
    }

    private void showAddPlanItemDialog(Context c) {
        final AlertDialog builder = new AlertDialog.Builder(c).create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_add_planitem, null);
        final TextInputLayout planItemNameText = dialogView.findViewById(R.id.planitem_name_text_input);
        planItemNameText.requestFocus();
        Button okBtn = dialogView.findViewById(R.id.planitem_save_button);
        Button cancelBtn = dialogView.findViewById(R.id.planitem_cancel_button);
        okBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String planItemName = String.valueOf(planItemNameText.getEditText().getText());
                if (!isPlanItemNameValid(planItemName)) {
                    planItemNameText.setError(getString(R.string.kid_error_name));
                } else {
                    planItemNameText.setError(null);
                    Date currentTime = Calendar.getInstance().getTime();
                    PlanItem newItem = new PlanItem(planItemName,currentTime,false);
                   // setupProgressBar();
                    savePlanItem(newItem);
                    builder.dismiss();
                }
            }
        });
        planItemNameText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                String planItemName = String.valueOf(planItemNameText.getEditText().getText());
                if (isPlanItemNameValid(planItemName)) {
                    planItemNameText.setError(null); //Clear the error
                }
                return false;
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        builder.setView(dialogView);
        builder.show();
        builder.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }

    private void savePlanItem(PlanItem newItem) {
        firebaseHelper.savePlanItem(newItem, selectedPlan, selectedKid).observeOn(Schedulers.io())
                //.observeOn(Schedulers.m)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<PlanItem>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                        disposable = d;
                    }

                    @Override
                    public void onNext(PlanItem planItem) {
                        Log.d(TAG, "onNext: " + planItem.getFirestoreId());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateRecylerView(planItem);
                            }
                        });



                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }

    private void updateRecylerView(PlanItem planItem) {
        mAdapter.add(planItem, 0);
        recyclerView.scrollToPosition(0);
       // dismissProgressBar();
    }

    private void updateRecylerView(ArrayList<PlanItem> planItemz) {
        mAdapter.updateData(planItemz);
        recyclerView.scrollToPosition(0);
        //dismissProgressBar();
    }

    private boolean isPlanItemNameValid(String planItemName) {
        return true;
    }



    @Override
    public void onPlanItemCheckedChanged(int position, boolean isChecked) {

        planItemzList = mAdapter.getAllItems();
        PlanItem selectedPlanItem = planItemzList.get(position);
        selectedPlanItem.setbCompleted(isChecked);
        firebaseHelper.updatePlanItem(selectedPlanItem,selectedPlan,selectedKid).observeOn(Schedulers.io())
                //.observeOn(Schedulers.m)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<PlanItem>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                        disposable = d;
                    }

                    @Override
                    public void onNext(PlanItem planItem) {
                        Log.d(TAG, "onNext: " + planItem.getFirestoreId());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });

    }
}
