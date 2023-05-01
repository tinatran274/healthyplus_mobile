package com.example.healthyplus.Activity.BasicInfo;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.healthyplus.R;

public class GenderActivity extends AppCompatActivity {
    CardView cardMale, cardFemale;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_selection);

        cardFemale = findViewById(R.id.cardFemale);
        cardMale = findViewById(R.id.cardMale);

        cardMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardMale.setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_view_border));
                cardFemale.setForeground(null);
            }
        });

        cardFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardFemale.setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_view_border));
                cardMale.setForeground(null);
            }
        });
    }
}
