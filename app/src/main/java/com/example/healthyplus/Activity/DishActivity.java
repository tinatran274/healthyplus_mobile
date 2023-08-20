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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DishActivity extends AppCompatActivity {
    Button btnYeuThich, btnBack;
    RecyclerView rec;
    DishAdapter adapter = new DishAdapter(this);
    SearchView svDish;
    List<Dish> list = new ArrayList<>();
    List<Dish> filterList = new ArrayList<>();

    FirebaseFirestore db;
    FirebaseUser user;
    Long isPre;
    ImageButton ibmAddDish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        findView();

        rec.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        adapter.setDishList(list);
        rec.setAdapter(adapter);

//        readData(list -> {
//            rec.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
//            adapter.setDishList(list);
//            rec.setAdapter(adapter);
//        });

        db.collection("user").document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> data = new HashMap<>();
                                data = document.getData();
                                isPre = (Long) data.get("isPremium");
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
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

        onClickButton();
    }

//    private void readData(FireStoreCallBack callBack){
//        db.collection("dish").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Dish dish = document.toObject(Dish.class);
//                        list.add(dish);
//                    }
//                callBack.onCallBack(list);
//                }
//                else {
//                    Log.e(TAG, "Cannot get dish data", task.getException());
//                }
//            }
//        });
//    }
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

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        db.collection("dish").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Dish dish = document.toObject(Dish.class);
                        list.add(dish);
                    }
//                    callBack.onCallBack(list);
                    adapter.notifyDataSetChanged();
                }
                else {
                    Log.e(TAG, "Cannot get dish data", task.getException());
                }
            }
        });
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
                if (isPre == 1){
                    Intent intent=new Intent(getApplicationContext(), AddDishActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent=new Intent(getApplicationContext(), UnlockPremiumActivity.class);
                    startActivity(intent);
                }
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