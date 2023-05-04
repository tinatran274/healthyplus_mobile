package com.example.healthyplus.Activity.BasicInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthyplus.Model.User;
import com.example.healthyplus.R;

public class ExerciseFrequentActivity extends AppCompatActivity {
    Button btnTiepTuc;
    RadioGroup radioGroup;
    RadioButton rbKhong, rbNhe, rbVua, rbNang;
    View rbChecked;
    User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_frequent);

        rbKhong = findViewById(R.id.rbKhong);
        rbVua = findViewById(R.id.rbVua);
        rbNhe = findViewById(R.id.rbNhe);
        rbNang = findViewById(R.id.rbNang);
        radioGroup = findViewById(R.id.radioGroup);
        btnTiepTuc = findViewById(R.id.btnTiepTuc);
        user = new User();

        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user = (User) getIntent().getSerializableExtra("user");

                int selectedId = radioGroup.getCheckedRadioButtonId();
                rbChecked = (View)findViewById(selectedId);

                int position = radioGroup.indexOfChild(rbChecked);

                user.setExerciseFrequency(position);

                Intent intent = new Intent(getApplicationContext(), TargetActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }
}
