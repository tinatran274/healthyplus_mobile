package com.example.healthyplus.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.healthyplus.Adapter.DishAdapter;
import com.example.healthyplus.Model.Dish;
import com.example.healthyplus.Model.Product;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DishActivity extends AppCompatActivity {
    Button btnYeuThich, btnBack;
    RecyclerView rec;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);

        btnBack = findViewById(R.id.btn_back_dish);
        btnYeuThich = findViewById(R.id.btnYeuThich);
        rec = findViewById(R.id.rec);


        DishAdapter adapter = new DishAdapter(this);
        rec.setLayoutManager(new GridLayoutManager(this, 2));
        List<Dish> list = new ArrayList<>();
        db.collection("dish").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Dish dish = document.toObject(Dish.class);
                        list.add(dish);
                    }
                        adapter.setDishList(list);
                        adapter.notifyDataSetChanged();
                }
                else {
                    Log.e(TAG, "Canont get data", task.getException());
                }
            }
        });

        rec.setAdapter(adapter);

        btnYeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LikedDishActivity.class);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}