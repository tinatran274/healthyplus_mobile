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

import com.example.healthyplus.Model.User;
import com.example.healthyplus.R;

public class GenderActivity extends AppCompatActivity {
    CardView cardMale, cardFemale;
    Button btnTiepTuc;
    User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_selection);

        user = new User();
        user = (User) getIntent().getSerializableExtra("user");
        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        cardFemale = findViewById(R.id.cardFemale);
        cardMale = findViewById(R.id.cardMale);


        // selecting male
        cardMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardMale.setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_view_border));
                cardFemale.setForeground(null);
                user.setGender(1);
            }
        });

        // selecting female
        cardFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardFemale.setForeground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.card_view_border));
                cardMale.setForeground(null);
                user.setGender(0);
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
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            }
        });
    }
}
