package com.example.healthyplus.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.healthyplus.Adapter.AlarmAdapter;
import com.example.healthyplus.Adapter.ProductAdapter;
import com.example.healthyplus.Model.Alarm;
import com.example.healthyplus.Model.Product;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AlarmActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AlarmAdapter alarmAdapter;
    Button btnBack;
    ImageButton btnAddAlarm;
    List<Alarm> list = new ArrayList<>();
    FirebaseFirestore db;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ArrayList<Boolean> day = new ArrayList<Boolean>();
        for(int i=0; i<7;i++){
            day.add(true);
        }
        db = FirebaseFirestore.getInstance();
        recyclerView=findViewById(R.id.rec);
        btnBack = findViewById(R.id.btn_back);
        btnAddAlarm=findViewById(R.id.ibm_add_alarm);

        alarmAdapter=new AlarmAdapter(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        alarmAdapter.setData(list);
        recyclerView.setAdapter(alarmAdapter);


        db.collection("alarm")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Alarm a = document.toObject(Alarm.class);
                                list.add(a);
                                alarmAdapter.notifyDataSetChanged();
                                Log.e(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(intent);
            }
        });


    }
}