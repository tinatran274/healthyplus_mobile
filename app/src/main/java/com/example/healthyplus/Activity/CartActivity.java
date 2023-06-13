package com.example.healthyplus.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.healthyplus.Adapter.CartAdapter;
import com.example.healthyplus.Model.Product;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button btnBack;
    CartAdapter adapter;
    FirebaseFirestore db;
    FirebaseUser user;
    ArrayList <Product> list;
    ArrayList<String> listKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        findView();
        setView();
        onClickButton();

        readCart(list1 -> {
            System.out.println(listKey);
                readProduct(list2 -> {
                    ArrayList <Product> realList = new ArrayList<>();
                    for(String key: listKey){
                        for(Product product: list){
                            if(product.getId().equals(key))
                            {
                                realList.add(product);
                                break;
                            }
                        }
                    }
                    adapter.setList(realList);
                    recyclerView.setAdapter(adapter);
                });
        });


    }

    private void onClickButton() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void readProduct(FireStoreCallBackProduct callBackProduct){
            db.collection("product")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                        Product p = document.toObject(Product.class);
                                        list.add(p);
                                }
                                callBackProduct.onCallBack(list);
                            }
                        else{
                                Log.e(TAG, "cannot get product in cart");
                            }
                        }
                    });
            db.collection("technology_product")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Product p = document.toObject(Product.class);
                                    list.add(p);
                                }
                                callBackProduct.onCallBack(list);
                            }
                            else{
                                Log.e(TAG, "cannot get product in cart");
                            }
                        }
                    });

    }
    private void readCart(CartActivity.FireStoreCallBackID callBack){
        db.collection("cart").document(user.getUid()).get()
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful()){
                       Map<String, Object> map = new HashMap<>();
                       map = task.getResult().getData();
                       listKey = new ArrayList<>(map.keySet());
                       callBack.onCallBack(listKey);
                   }
                   else{
                       Log.e(TAG, "cannot get cart data");
                   }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }
    private interface FireStoreCallBackID{
        void onCallBack(ArrayList<String> list);
    }
    private interface FireStoreCallBackProduct{
        void onCallBack(ArrayList<Product> list);
    }
    private void setView() {
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        list = new ArrayList<>();
        adapter = new CartAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

    }

    private void findView() {
        recyclerView = findViewById(R.id.rec);
        btnBack = findViewById(R.id.btn_back_cart);
    }
}