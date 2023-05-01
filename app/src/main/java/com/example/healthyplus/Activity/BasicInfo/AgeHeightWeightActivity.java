package com.example.healthyplus.Activity.BasicInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthyplus.R;

public class AgeHeightWeightActivity extends AppCompatActivity {
    Button btnTiepTuc;
    EditText edtAge, edtHeight, edtWeight;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ageheightweight);

        edtWeight = findViewById(R.id.edtWeight);
        edtHeight = findViewById(R.id.edtHeight);
        edtAge = findViewById(R.id.edtAge);
        btnTiepTuc = findViewById(R.id.btnTiepTuc);


        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Check if the information is filled
                if(edtAge.getText().toString() == "") {
                    Toast.makeText(AgeHeightWeightActivity.this, "Vui lòng thêm tuổi của bạn", Toast.LENGTH_SHORT).show();
                }
                else if (edtHeight.getText().toString() == "") {
                    Toast.makeText(AgeHeightWeightActivity.this, "Vui lòng thêm chiều cao của bạn", Toast.LENGTH_SHORT).show();
                } 
                else if (edtWeight.getText().toString() == "") {
                    Toast.makeText(AgeHeightWeightActivity.this, "Vui lòng thêm cân nặng của bạn", Toast.LENGTH_SHORT).show();
                }
                // Move to next activity
                else {
                    Intent intent = new Intent(getApplicationContext(), AgeHeightWeightActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
