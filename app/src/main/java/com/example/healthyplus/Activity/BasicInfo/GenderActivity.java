package com.example.healthyplus.Activity.BasicInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.example.healthyplus.R;

public class GenderActivity extends AppCompatActivity {
    CardView cardMale, cardFemale;
    Button btnTiepTuc;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_selection);

        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        cardFemale = findViewById(R.id.cardFemale);
        cardMale = findViewById(R.id.cardMale);


        // selecting gender
        cardMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardMale.setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_view_border));
                cardFemale.setForeground(null);
            }
        });

        // selecting gender
        cardFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardFemale.setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_view_border));
                cardMale.setForeground(null);
            }
        });

        // Move to name, height, weight info activity
        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                if(cardFemale.getForeground() == null && cardMale.getForeground() == null)
                    Toast.makeText(GenderActivity.this, "Vui lòng lựa chọn", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(getApplicationContext(), AgeHeightWeightActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
