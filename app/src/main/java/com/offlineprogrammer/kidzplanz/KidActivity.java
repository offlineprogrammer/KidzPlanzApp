package com.offlineprogrammer.kidzplanz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.offlineprogrammer.kidzplanz.kid.Kid;

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

    }
}
