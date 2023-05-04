package com.example.healthyplus.Activity.BasicInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.healthyplus.Activity.HomeActivity;
import com.example.healthyplus.DAO.UserDAO;
import com.example.healthyplus.Model.User;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class StatisticActivity extends AppCompatActivity {
    TextView txv1, txv2, txv3, txv4, txv5;

    Button btnBatDau;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        
        txv4 = findViewById(R.id.txv_bmi);
        txv5 = findViewById(R.id.txv_ttde);
        btnBatDau = findViewById(R.id.btnBatDau);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("user").document(currentUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                                if(documentSnapshot != null){
                                    User userBase = documentSnapshot.toObject(User.class);
                                    txv1.setText(userBase.getId() + "");
                                    txv2.setText(userBase.getAge() + "");
                                    txv3.setText(userBase.getName());
                                    txv4.setText(String.valueOf(userBase.BMICal()));
                                    txv5.setText(String.valueOf(userBase.TTDECal()));
                                }
                        }
                    }
                });

        btnBatDau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}