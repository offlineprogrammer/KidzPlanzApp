package com.offlineprogrammer.kidzplanz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.offlineprogrammer.kidzplanz.plan.KidPlan;

public class PlanActivity extends AppCompatActivity {
    private static final String TAG = "PlanActivity";

    ImageView planImageView;
    TextView planNameTextView;
    KidPlan selectedPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        planImageView = findViewById(R.id.planImage);
        planNameTextView = findViewById(R.id.plannameTextView);

        if (getIntent().hasExtra("selected_plan")) {
            // setupProgressBar();
            Bundle data = getIntent().getExtras();
            selectedPlan = data.getParcelable("selected_plan");
            planImageView.setImageResource( getApplicationContext().getResources().getIdentifier(selectedPlan.getPlanImageResourceName() , "drawable" ,
                    getApplicationContext().getPackageName()) );
            planNameTextView.setText(selectedPlan.getPlanName());

        }
    }
}
