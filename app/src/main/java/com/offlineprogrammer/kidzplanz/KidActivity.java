package com.offlineprogrammer.kidzplanz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class KidActivity extends AppCompatActivity {

    private static final String TAG = "KidActivity";
    ImageView kidImageView;
    TextView kidNameTextView;
    Kid selectedKid;


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
}
