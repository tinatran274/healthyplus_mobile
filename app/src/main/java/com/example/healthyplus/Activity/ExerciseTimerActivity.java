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
import android.widget.Toast;

import com.example.healthyplus.Adapter.ExerciseAdapter;
import com.example.healthyplus.Model.Dish;
import com.example.healthyplus.Model.Exercise;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ExerciseTimerActivity extends AppCompatActivity {
    Button btnBackToHome;
    RecyclerView recyclerView;
    ExerciseAdapter adapter;
    ArrayList<Exercise> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_timer);

        findView();

        readData(list ->{
            adapter.setList(list);
            recyclerView.setAdapter(adapter);
        });



        btnBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void readData(FireStoreCallBack callBack){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("exercise").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Exercise exercise = document.toObject(Exercise.class);
                                list.add(exercise);
                                //System.out.println(exercise.getCaloriesPerHour() + exercise.getName() + exercise.getImg());
                            }
                            callBack.onCallBack(list);
                        }
                        else {
                            Log.e(TAG, "Cannot get exercise data", task.getException());
                        }
                    }
                });
    }
    private interface FireStoreCallBack{
        void onCallBack(ArrayList<Exercise> list);
    }

    private void findView() {
        btnBackToHome = findViewById(R.id.btnBackVanDong);
        recyclerView = findViewById(R.id.rcExercise);
        adapter = new ExerciseAdapter(this);

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
    }
}