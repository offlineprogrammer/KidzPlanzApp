package com.offlineprogrammer.kidzplanz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.offlineprogrammer.kidzplanz.planitem.PlanItem;

import java.util.Calendar;
import java.util.Date;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PlanActivity extends AppCompatActivity {
    private static final String TAG = "PlanActivity";

    ImageView planImageView;
    TextView planNameTextView;
    KidPlan selectedPlan;
    FirebaseHelper firebaseHelper;
    private Disposable disposable;
    ProgressDialog progressBar;
    Kid selectedKid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        planImageView = findViewById(R.id.planImage);
        planNameTextView = findViewById(R.id.plannameTextView);
        configActionButton();
        firebaseHelper = new FirebaseHelper();


        if (getIntent().getExtras() != null) {
            selectedPlan = getIntent().getExtras().getParcelable("selected_plan");
            selectedKid = getIntent().getExtras().getParcelable("selected_kid");
            planImageView.setImageResource( getApplicationContext().getResources().getIdentifier(selectedPlan.getPlanImageResourceName() , "drawable" ,
                    getApplicationContext().getPackageName()) );
            planNameTextView.setText(selectedPlan.getPlanName());
        }
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
                              //  updateRecylerView(planItem);
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

    private boolean isPlanItemNameValid(String planItemName) {
        return true;
    }
}
