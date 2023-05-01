package com.example.healthyplus.Activity.BasicInfo;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthyplus.R;

public class ExerciseFrequentActivity extends AppCompatActivity {
    Button btnTiepTuc;
    RadioGroup radioGroup;
    RadioButton rbKhong, rbNhe, rbVua, rbNang;
    RadioButton rbChecked;
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

        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedId = radioGroup.getCheckedRadioButtonId();
                rbChecked = (RadioButton)findViewById(selectedId);
                Log.d(TAG, rbChecked.getText().toString());

                Intent intent = new Intent(getApplicationContext(), TargetActivity.class);
                startActivity(intent);
            }
        });
    }
}
