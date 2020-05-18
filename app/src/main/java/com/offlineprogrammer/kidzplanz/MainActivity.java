package com.offlineprogrammer.kidzplanz;

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
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.offlineprogrammer.kidzplanz.kid.Kid;
import com.offlineprogrammer.kidzplanz.kid.KidAdapter;
import com.offlineprogrammer.kidzplanz.kid.KidGridItemDecoration;
import com.offlineprogrammer.kidzplanz.kid.OnKidListener;
import com.offlineprogrammer.kidzplanz.user.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements OnKidListener {

    private static final String TAG = "MainActivity";
    FirebaseHelper firebaseHelper;
    User m_User;
    private Disposable disposable;
    private RecyclerView recyclerView;
    private KidAdapter mAdapter;
    private ArrayList<Kid> kidzList = new ArrayList<>();
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupProgressBar();

        firebaseHelper = new FirebaseHelper();
        setupRecyclerView();
        getDeviceToken();


    }

    private void configActionButton() {
        FloatingActionButton fab = findViewById(R.id.fab_add_kid);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddKidDialog(MainActivity.this);
            }
        });
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

    private int pickMonster(){
        final TypedArray imgs;
        imgs = getResources().obtainTypedArray(R.array.kidzMonsters);
        final Random rand = new Random();
        final int rndInt = rand.nextInt(imgs.length());
        return imgs.getResourceId(rndInt, 0);
    }

    private void showAddKidDialog(Context c) {

        final AlertDialog builder = new AlertDialog.Builder(c).create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_add_kid, null);
        final TextInputLayout kidNameText = dialogView.findViewById(R.id.kidname_text_input);
        kidNameText.requestFocus();
        Button okBtn= dialogView.findViewById(R.id.kidname_save_button);
        Button cancelBtn = dialogView.findViewById(R.id.kidname_cancel_button);
        okBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String kidName = String.valueOf(kidNameText.getEditText().getText());
                if (!isKidNameValid(kidName)) {
                    kidNameText.setError(getString(R.string.kid_error_name));
                } else {
                    kidNameText.setError(null);
                    Date currentTime = Calendar.getInstance().getTime();
                    int monsterImage = pickMonster();
                    String monsterImageResourceName = getResources().getResourceEntryName(monsterImage);

                    Kid newKid = new Kid(kidName,
                            monsterImageResourceName,
                            currentTime);
                    setupProgressBar();
                    saveKid(newKid);

                  //  mFirebaseAnalytics.logEvent("kid_created", null);
                    builder.dismiss();
                }


            }
        });

        kidNameText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                String kidName = String.valueOf(kidNameText.getEditText().getText());
                if (isKidNameValid(kidName)) {
                    kidNameText.setError(null); //Clear the error
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

    private void saveKid(Kid newKid) {

        firebaseHelper.saveKid(newKid).observeOn(Schedulers.io())
                //.observeOn(Schedulers.m)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Kid>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                        disposable = d;
                    }

                    @Override
                    public void onNext(Kid kid) {
                        Log.d(TAG, "onNext: " + kid.getFirestoreId());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateRecylerView(kid);
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

    private void updateRecylerView(Kid kid) {
        Log.i(TAG, "onClick UserFireStore : " + kid.getUserFirestoreId());
        Log.i(TAG, "onClick KidFireStore : " + kid.getFirestoreId());
        mAdapter.add(kid, 0);
        recyclerView.scrollToPosition(0);
        dismissProgressBar();
    }

    private void updateRecylerView(ArrayList<Kid> kidz) {
        mAdapter.updateData(kidz);
        recyclerView.scrollToPosition(0);
        dismissProgressBar();
    }



    private boolean isKidNameValid(String kidName) {
        return kidName != null && kidName.length() >= 2;
    }

    private void setupRecyclerView() {
        mAdapter = new KidAdapter(kidzList,this);
        recyclerView = findViewById(R.id.kidz_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        int largePadding = getResources().getDimensionPixelSize(R.dimen.kpz_kidz_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.kpz_kidz_grid_spacing_small);
        recyclerView.addItemDecoration(new KidGridItemDecoration(largePadding, smallPadding));

    }

    private void getDeviceToken() {
        firebaseHelper.getDeviceToken().observeOn(Schedulers.io())
                //.observeOn(Schedulers.m)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                        disposable = d;
                    }

                    @Override
                    public void onNext(String deviceToken) {
                        Log.d(TAG, "onNext: " + deviceToken);
                        getUserData(deviceToken);
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

    private void getUserData(String deviceToken) {
        firebaseHelper.getUserData(deviceToken).observeOn(Schedulers.io())
                //.observeOn(Schedulers.m)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                        disposable = d;
                    }

                    @Override
                    public void onNext(User user) {
                        Log.d(TAG, "onNext: " + user.getFirebaseId());
                        m_User = user;
                        if (m_User.getFirebaseId()==null) {
                            saveUser();
                        } else {
                            getKidzData(m_User.getFirebaseId());
                        }

                        configActionButton();


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

    private void getKidzData(String userFireStoreId) {
        firebaseHelper.getKidz(userFireStoreId).observeOn(Schedulers.io())
                //.observeOn(Schedulers.m)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ArrayList<Kid>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                        disposable = d;
                    }

                    @Override
                    public void onNext(ArrayList<Kid> kidz) {
                        Log.d(TAG, "onNext:  " + kidz.size());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateRecylerView(kidz);
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



    private void saveUser() {
        firebaseHelper.saveUser().observeOn(Schedulers.io())
                //.observeOn(Schedulers.m)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                        disposable = d;
                    }

                    @Override
                    public void onNext(User user) {
                        Log.d(TAG, "onNext: " + user.getFirebaseId());
                        m_User = user;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissProgressBar();
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


    @Override
    protected void onDestroy() {
        dismissWithCheck(progressBar);
        super.onDestroy();
        disposable.dispose();
    }

    @Override
    public void onKidClick(int position) {
        kidzList = mAdapter.getAllItems();
        Log.i(TAG, "Clicked " + position);
        Intent intent = new Intent(this, KidActivity.class);
        Log.i(TAG, "onKidClick: " + kidzList.get(position).toString());
        intent.putExtra("selected_kid",kidzList.get(position));
        startActivity(intent);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        recreate();
    }



}
