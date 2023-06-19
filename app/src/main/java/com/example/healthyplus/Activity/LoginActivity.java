package com.example.healthyplus.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.ChecksSdkIntAtLeast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthyplus.Activity.BasicInfo.GenderActivity;
import com.example.healthyplus.Database.PasswordHasher;
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
    TextView txvDangKy, txvQuenMK;
    EditText edtName, edtPass;
    Button btnDangNhap;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnDangNhap = findViewById(R.id.btnDangNhap);
        edtName = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPassword);
        txvDangKy = findViewById(R.id.txvDangKy);
        txvQuenMK = findViewById(R.id.txvQuenMK);


        txvQuenMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater builder = LayoutInflater.from(LoginActivity.this);
                View dialogView = builder.inflate(R.layout.dialog_forgot_password, null);
                AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this).create();

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog);
                dialog.setView(dialogView);


                Button btnGetEmail = dialogView.findViewById(R.id.btnGetResetMail);
                TextView txvBackLogin = dialogView.findViewById(R.id.txvBackLogin);
                TextView txvOK = dialogView.findViewById(R.id.txvOK);
                EditText edtMail = dialogView.findViewById(R.id.edtGetResetMail);

                txvOK.setVisibility(View.GONE);

                txvBackLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                btnGetEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(edtMail.getText().toString().isEmpty()) {
                            edtMail.setError("Vui lòng nhập email");
                            return;
                        }
                        String email = edtMail.getText().toString().trim();
                        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(LoginActivity.this, "Gửi thành công", Toast.LENGTH_SHORT).show();
                                    txvOK.setVisibility(View.VISIBLE);
                                }
                                else {
                                    Log.e(TAG, "Cannot send reset password email");
                                    Toast.makeText(LoginActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                                }
                                    
                            }

                        });
                    }
                });

                dialog.show();
            }
        });

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

                if(TextUtils.isEmpty(name)){
                    edtName.setError("Vui Lòng nhập gmail.");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    edtPass.setError("Vui Lòng nhập mật khẩu.");
                    return;
                }
//                PasswordHasher passwordHasher = new PasswordHasher();
//                pass = passwordHasher.hashPassword(pass);

                auth.signInWithEmailAndPassword(name, pass)
                        .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent toBasicInfo = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(toBasicInfo);
                    } else {
                        Toast.makeText(LoginActivity.this, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


    }


}

