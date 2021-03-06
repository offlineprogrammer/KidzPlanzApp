package com.offlineprogrammer.KidzPlanz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.offlineprogrammer.KidzPlanz.kid.Kid;
import com.offlineprogrammer.KidzPlanz.plan.KidPlan;
import com.offlineprogrammer.KidzPlanz.plan.OnPlanListener;
import com.offlineprogrammer.KidzPlanz.plan.PlanAdapter;
import com.offlineprogrammer.KidzPlanz.plan.PlanGridItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class KidActivity extends AppCompatActivity implements OnPlanListener {

    private static final String TAG = "KidActivity";
    ImageView kidImageView;
    TextView kidNameTextView;
    Kid selectedKid;
    FirebaseHelper firebaseHelper;
    private Disposable disposable;
    private RecyclerView recyclerView;
    private PlanAdapter mAdapter;
    private ArrayList<KidPlan> planzList = new ArrayList<>();
    ProgressDialog progressBar;
    ImageButton deleteImageButton;
    private com.google.android.gms.ads.AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kid);

        kidImageView = findViewById(R.id.kidMonsterImage);
        kidNameTextView = findViewById(R.id.kidnameTextView);
        deleteImageButton = findViewById(R.id.delete_button);
        configActionButton();
        firebaseHelper = new FirebaseHelper(getApplicationContext());
        setupRecyclerView();

        deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteKidDialog(KidActivity.this);
            }
        });

        if (getIntent().hasExtra("selected_kid")) {
            // setupProgressBar();
            Bundle data = getIntent().getExtras();
            selectedKid = data.getParcelable("selected_kid");
            kidImageView.setImageResource( getApplicationContext().getResources().getIdentifier(selectedKid.getMonsterImageResourceName() , "drawable" ,
                    getApplicationContext().getPackageName()) );
            kidNameTextView.setText(selectedKid.getKidName());
            getKidPlanz();

        }

        setupAds();


    }

    private void setupAds() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        adView = findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void showDeleteKidDialog(Context c) {


        final AlertDialog builder = new AlertDialog.Builder(c).create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_delete_kid, null);
        Button okBtn = dialogView.findViewById(R.id.deletekid_confirm_button);
        Button cancelBtn = dialogView.findViewById(R.id.deletekid_cancel_button);

        okBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                builder.dismiss();
                deleteKid();
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

    private void deleteKid() {
        firebaseHelper.deleteKid(selectedKid)
                .subscribe(() -> {
                    Log.i(TAG, "updateRewardImage: completed");
                    firebaseHelper.logEvent("kid_deleted");
                    finish();
                }, throwable -> {
                    // handle error
                });
    }

    private void getKidPlanz() {
        firebaseHelper.getKidPlanz(selectedKid).observeOn(Schedulers.io())
                //.observeOn(Schedulers.m)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ArrayList<KidPlan>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                        disposable = d;
                    }

                    @Override
                    public void onNext(ArrayList<KidPlan> kidPlanz) {
                        Log.d(TAG, "onNext:  " + kidPlanz.size());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateRecylerView(kidPlanz);
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
        mAdapter = new PlanAdapter(planzList,this);
        recyclerView = findViewById(R.id.planz_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        int largePadding = getResources().getDimensionPixelSize(R.dimen.kpz_kidz_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.kpz_kidz_grid_spacing_small);
        recyclerView.addItemDecoration(new PlanGridItemDecoration(largePadding, smallPadding));

    }

    private void configActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab_add_plan);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddPlanDialog(KidActivity.this);
            }
        });
    }

    private void showAddPlanDialog(Context c) {
        final AlertDialog builder = new AlertDialog.Builder(c).create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_add_plan, null);
        final TextInputLayout planNameText = dialogView.findViewById(R.id.plan_name_text_input);
        planNameText.requestFocus();
        Button okBtn = dialogView.findViewById(R.id.plan_save_button);
        Button cancelBtn = dialogView.findViewById(R.id.plan_cancel_button);
        okBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String planName = String.valueOf(planNameText.getEditText().getText());
                if (!isPlanNameValid(planName)) {
                    planNameText.setError(getString(R.string.kid_error_name));
                } else {
                    planNameText.setError(null);
                    Date currentTime = Calendar.getInstance().getTime();
                    KidPlan newPlan = new KidPlan(planName,
                            "neutral",
                            currentTime, "proud");
                    setupProgressBar();
                    saveKidPlan(newPlan);
                    builder.dismiss();
               }
            }
        });
        planNameText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                String kidName = String.valueOf(planNameText.getEditText().getText());
                if (isPlanNameValid(kidName)) {
                    planNameText.setError(null); //Clear the error
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

    private void saveKidPlan(KidPlan newPlan) {
        firebaseHelper.saveKidPlan(newPlan, selectedKid).observeOn(Schedulers.io())
                //.observeOn(Schedulers.m)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<KidPlan>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                        disposable = d;
                    }

                    @Override
                    public void onNext(KidPlan kidPlan) {
                        Log.d(TAG, "onNext: " + kidPlan.getFirestoreId());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                firebaseHelper.logEvent("plan_created");
                                updateRecylerView(kidPlan);
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

    private void updateRecylerView(KidPlan kidPlan) {
        Log.i(TAG, "onClick UserFireStore : " + kidPlan.getKidFirestoreId());
        Log.i(TAG, "onClick KidFireStore : " + kidPlan.getFirestoreId());
        mAdapter.add(kidPlan, 0);
        recyclerView.scrollToPosition(0);
        dismissProgressBar();
    }

    private void updateRecylerView(ArrayList<KidPlan> kidPlanz) {
        mAdapter.updateData(kidPlanz);
        recyclerView.scrollToPosition(0);
        dismissProgressBar();
    }

    private boolean isPlanNameValid(String planName) {

        return planName != null && planName.length() >= 2;
    }

    @Override
    public void onPlanClick(int position) {

        planzList = mAdapter.getAllItems();
        Intent intent = new Intent(this, PlanActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("selected_kid",  selectedKid);
        bundle.putParcelable("selected_plan",planzList.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void setupProgressBar() {
        dismissProgressBar();
        progressBar = new ProgressDialog(this);
        progressBar.setMessage("Loading data ...");
        progressBar.show();
    }

    private void dismissProgressBar() {
        dismissWithCheck(progressBar);
    }

    public void dismissWithCheck(ProgressDialog dialog) {
        if (dialog != null) {
            if (dialog.isShowing()) {

                //get the Context object that was used to great the dialog
                Context context = ((ContextWrapper) dialog.getContext()).getBaseContext();

                // if the Context used here was an activity AND it hasn't been finished or destroyed
                // then dismiss it
                if (context instanceof Activity) {

                    // Api >=17
                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                        dismissWithTryCatch(dialog);
                    }
                } else
                    // if the Context used wasn't an Activity, then dismiss it too
                    dismissWithTryCatch(dialog);
            }
            dialog = null;
        }
    }

    public void dismissWithTryCatch(ProgressDialog dialog) {
        try {
            dialog.dismiss();
        } catch (final IllegalArgumentException e) {
            // Do nothing.
        } catch (final Exception e) {
            // Do nothing.
        } finally {
            dialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        dismissWithCheck(progressBar);
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        recreate();
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    /** Called before the activity is destroyed */


}
