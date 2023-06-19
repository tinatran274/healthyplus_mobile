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
import android.widget.SearchView;
import android.widget.Toast;

import com.example.healthyplus.Adapter.IngredientAdapter;
import com.example.healthyplus.Model.Ingredient;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class IngredientActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    IngredientAdapter adapter = new IngredientAdapter(this);
    List<Ingredient> list = new ArrayList<>();
    List<Ingredient> filterList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btnBack;
    SearchView svIngre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_igredient);

        findView();
        showIngre();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        });

        svIngre.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
            }
        });
    }

    private void filterList(String s) {
        filterList.clear();
        for(Ingredient ingredient: list){
            if(ingredient.getName().toLowerCase().contains(s))
                filterList.add(ingredient);
        }
        adapter.setIngreList(filterList);
    }

    private void showIngre() {

        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        db.collection("ingredient").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Ingredient ingredient = document.toObject(Ingredient.class);
                        list.add(ingredient);
                    }
                    System.out.println(list);
                    adapter.setIngreList(list);
                }
                else {
                    Log.e(TAG, "Cannot get ingredient data", task.getException());
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void findView() {
        recyclerView = findViewById(R.id.rec);
        btnBack = findViewById(R.id.btn_back_ingredient);
        svIngre = findViewById(R.id.svIngre);
    }
}