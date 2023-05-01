package com.example.healthyplus.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.healthyplus.Activity.BasicInfo.GenderActivity;
import com.example.healthyplus.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {
    TextView txvDangKy;
    EditText edtName, edtPass;
    Button btnDangNhap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnDangNhap = findViewById(R.id.btnDangNhap);
        edtName = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPassword);
        txvDangKy = findViewById(R.id.txvDangKy);

        // Move to Register Screen
        txvDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(toRegister);
            }
        });

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toBasicInfo = new Intent(getApplicationContext(), GenderActivity.class);
                startActivity(toBasicInfo);
            }
        });


    }


}

