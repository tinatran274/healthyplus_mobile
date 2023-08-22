package com.example.healthyplus.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.healthyplus.Adapter.AlarmAdapter;
import com.example.healthyplus.Adapter.ExpertAdapter;
import com.example.healthyplus.Model.Alarm;
import com.example.healthyplus.Model.Expert;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ExpertActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExpertAdapter expertAdapter;
    Button btnBack;
    List<Expert> list = new ArrayList<>();
    FirebaseFirestore db;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        recyclerView=findViewById(R.id.rec);
        btnBack = findViewById(R.id.btn_back);

        expertAdapter=new ExpertAdapter(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        expertAdapter.setData(list);
        recyclerView.setAdapter(expertAdapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        db.collection("expert")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Expert e = document.toObject(Expert.class);
                                list.add(e);
                            }
                            expertAdapter.notifyDataSetChanged();

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}