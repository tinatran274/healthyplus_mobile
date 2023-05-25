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

import com.example.healthyplus.Adapter.SuggestionAdapter;
import com.example.healthyplus.Model.Ingredient;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SuggestionActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SuggestionAdapter adapter = new SuggestionAdapter(this);
    Button btnSearch;
    List<Ingredient> list = new ArrayList<>();
    List<Ingredient> filterList = new ArrayList<>();
    ArrayList<String> checkList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    SearchView svIngre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        findView();
        showIngre();
        btnSearch();

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
        if(filterList.isEmpty())
            Toast.makeText(this, "Không tìm thấy nguyên liệu phù hợp", Toast.LENGTH_SHORT).show();
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
                    adapter.setIngreList(list);
                }
                else {
                    Log.e(TAG, "Cannot get ingredient data", task.getException());
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void btnSearch() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkList = new ArrayList<>(adapter.getCheckList());
                Intent intent = new Intent(getApplicationContext(), SuggestionResult.class);
                intent.putStringArrayListExtra("checkList", checkList);
                startActivity(intent);
            }
        });
    }

    private void findView() {
        recyclerView = findViewById(R.id.rec);
        btnSearch = findViewById(R.id.btnSearch);
        svIngre = findViewById(R.id.svIngre);
    }
}