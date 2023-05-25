package com.example.healthyplus.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.healthyplus.Adapter.DishAdapter;
import com.example.healthyplus.Model.Dish;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SuggestionResult extends AppCompatActivity {
    Button btnBack;
    RecyclerView recyclerView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DishAdapter adapter = new DishAdapter(this);
    List<Dish> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_result);

        findView();

        showDishes();

        onClickButton();
    }

    private void showDishes() {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        db.collection("dish").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    ArrayList <String> checkList = getIntent().getStringArrayListExtra("checkList");
                    System.out.println(checkList);
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Dish dish = document.toObject(Dish.class);
                        Set<String> setA = new HashSet<>();
                        for(String recipe: dish.getIngredients()){
                            List<String> ingredients = Arrays.asList(recipe.toLowerCase().split("[(),\\s]+"));
                            setA.addAll(ingredients);
                        }
                        System.out.println(setA);
                        if(setA.containsAll(checkList)) {
                            System.out.println(dish.getName());
                            list.add(dish);
                        }
                    }
                    if(list.isEmpty())
                        Toast.makeText(SuggestionResult.this, "Không tìm thấy món ăn phù hợp", Toast.LENGTH_SHORT).show();
                    adapter.setDishList(list);
                }
                else {
                    Log.e(TAG, "Cannot get dish data", task.getException());
                }
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private void findView() {
        recyclerView = findViewById(R.id.rec);
        btnBack = findViewById(R.id.btnBack);
    }

    private void onClickButton() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}