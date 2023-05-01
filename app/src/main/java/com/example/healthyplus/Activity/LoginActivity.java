package com.example.healthyplus.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.example.healthyplus.Activity.BasicInfo.GenderActivity;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
                String name = edtName.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                db.collection("user")
//                        .whereEqualTo("name", name)
//                        .get()
//                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                        Log.d(TAG, document.getId() + " => " + document.getData().get("name"));
//                                    }
//                                } else {
//                                    Log.d(TAG, "Error getting documents: ", task.getException());
//                                }
//                            }
//                        });

                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signInWithEmailAndPassword(name, pass)
                        .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent toBasicInfo = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(toBasicInfo);
                    } else {
                        Toast.makeText(LoginActivity.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


    }


}

