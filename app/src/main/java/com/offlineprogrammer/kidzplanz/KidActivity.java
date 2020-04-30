package com.offlineprogrammer.kidzplanz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;
import com.offlineprogrammer.kidzplanz.kid.Kid;
import com.offlineprogrammer.kidzplanz.plan.KidPlan;
import com.offlineprogrammer.kidzplanz.plan.OnPlanListener;
import com.offlineprogrammer.kidzplanz.plan.PlanAdapter;
import com.offlineprogrammer.kidzplanz.plan.PlanGridItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.disposables.Disposable;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kid);

        kidImageView = findViewById(R.id.kidMonsterImage);
        kidNameTextView = findViewById(R.id.kidnameTextView);

        if (getIntent().hasExtra("selected_kid")) {
            // setupProgressBar();
            Bundle data = getIntent().getExtras();
            selectedKid = data.getParcelable("selected_kid");
            kidImageView.setImageResource(selectedKid.getMonsterImage());
            kidNameTextView.setText(selectedKid.getKidName());

        }

        configActionButton();
        firebaseHelper = new FirebaseHelper();
        setupRecyclerView();
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
                showAddTaskDialog(KidActivity.this);
            }
        });
    }

    private void showAddTaskDialog(Context c) {
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
                if (!isTaskNameValid(planName)) {
                    planNameText.setError(getString(R.string.kid_error_name));
                } else {
                    planNameText.setError(null);
                    Date currentTime = Calendar.getInstance().getTime();
                    KidPlan newPlan = new KidPlan(planName,
                            "bekind",
                            currentTime);
                    builder.dismiss();
               }
            }
        });
        planNameText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                String kidName = String.valueOf(planNameText.getEditText().getText());
                if (isTaskNameValid(kidName)) {
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

    private boolean isTaskNameValid(String taskName) {
        return true;
    }

    @Override
    public void onPlanClick(int position) {

    }
}
