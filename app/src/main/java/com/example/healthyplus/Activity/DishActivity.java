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
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

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
    DishAdapter adapter = new DishAdapter(this);
    SearchView svDish;
    List<Dish> list = new ArrayList<>();
    List<Dish> filterList = new ArrayList<>();

    ImageButton ibmAddDish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);

        findView();

        onClickButton();

        readData(list -> {
            rec.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
            adapter.setDishList(list);
            rec.setAdapter(adapter);
        });

        // Tim kiem mon an
        svDish.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

    private void readData(FireStoreCallBack callBack){
        db.collection("dish").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Dish dish = document.toObject(Dish.class);
                        list.add(dish);
                    }
                callBack.onCallBack(list);
                }
                else {
                    Log.e(TAG, "Cannot get dish data", task.getException());
                }
            }
        });
    }
    private interface FireStoreCallBack{
        void onCallBack(List<Dish> list);
    }
    private void filterList(String s) {
        filterList.clear();
        for(Dish dish: list){
            if(dish.getName().toLowerCase().contains(s))
                filterList.add(dish);
        }
        adapter.setDishList(filterList);
    }

    private void showDishes() {

    }

    private void onClickButton() {
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
                finish();
            }
        });

        ibmAddDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddDishActivity.class);
                startActivity(intent);
            }
        });
    }

    private void findView() {
        btnBack = findViewById(R.id.btn_back_dish);
        btnYeuThich = findViewById(R.id.btnYeuThich);
        rec = findViewById(R.id.rec);
        svDish = findViewById(R.id.svDish);
        ibmAddDish = findViewById(R.id.ibm_add_dish);
    }
}