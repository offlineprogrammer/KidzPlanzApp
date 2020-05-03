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
import com.google.android.material.textfield.TextInputLayout;
import com.offlineprogrammer.kidzplanz.plan.KidPlan;

import java.util.Calendar;
import java.util.Date;

public class PlanActivity extends AppCompatActivity {
    private static final String TAG = "PlanActivity";

    ImageView planImageView;
    TextView planNameTextView;
    KidPlan selectedPlan;
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        planImageView = findViewById(R.id.planImage);
        planNameTextView = findViewById(R.id.plannameTextView);
        configActionButton();
        firebaseHelper = new FirebaseHelper();

        if (getIntent().hasExtra("selected_plan")) {
            // setupProgressBar();
            Bundle data = getIntent().getExtras();
            if(data != null){
                selectedPlan = data.getParcelable("selected_plan");
                planImageView.setImageResource( getApplicationContext().getResources().getIdentifier(selectedPlan.getPlanImageResourceName() , "drawable" ,
                        getApplicationContext().getPackageName()) );
                planNameTextView.setText(selectedPlan.getPlanName());
            }


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

    private boolean isPlanItemNameValid(String planItemName) {
        return true;
    }
}
