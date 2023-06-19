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

import com.example.healthyplus.Adapter.ProductAdapter;
import com.example.healthyplus.Model.Product;
import com.example.healthyplus.Model.User;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private Button btnBackProduct;
    SearchView svProduct;
    User u;
    List<Product> filterList = new ArrayList<>();
    List<Product> list = new ArrayList<>();
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        btnBackProduct=findViewById(R.id.btn_back_product);
        recyclerView=findViewById(R.id.rec);
        svProduct = findViewById(R.id.svProduct);

        productAdapter=new ProductAdapter(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        productAdapter.setData(list);
        recyclerView.setAdapter(productAdapter);

        db = FirebaseFirestore.getInstance();
        db.collection("product").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product p = document.toObject(Product.class);
                                list.add(p);
                                productAdapter.notifyDataSetChanged();
                                Log.e(TAG, document.getId() + " => " + document.getData() + p.getImg());
                            }
                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        btnBackProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });


        svProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return false;
            }
        });
    }

    private void filterList(String s) {
        filterList.clear();
        for(Product product: list){
            if(product.getName().toLowerCase().contains(s))
                filterList.add(product);
        }
        productAdapter.setData(filterList);
    }

    private List<Product> getListProduct()
    {
        List<Product> list = new ArrayList<>();

        return list;
    }
}