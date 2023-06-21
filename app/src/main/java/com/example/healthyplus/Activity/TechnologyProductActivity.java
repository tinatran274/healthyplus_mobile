package com.example.healthyplus.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.SearchEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;

import com.example.healthyplus.Adapter.ProductAdapter;
import com.example.healthyplus.Model.Product;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TechnologyProductActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private Button btnTechnologyProduct;
    SearchView svTechProduct;
    List<Product> list = new ArrayList<>();
    List<Product> filterList = new ArrayList<>();
    FirebaseFirestore db;
    ImageView imgCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technology_product);

        db = FirebaseFirestore.getInstance();

        recyclerView=findViewById(R.id.rec);
        btnTechnologyProduct=findViewById(R.id.btn_back_technology_product);
        svTechProduct = findViewById(R.id.svTechPro);
        productAdapter=new ProductAdapter(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        productAdapter.setData(list);
        recyclerView.setAdapter(productAdapter);

        imgCart = findViewById(R.id.img_cart);
        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CartActivity.class));
            }
        });
        db = FirebaseFirestore.getInstance();
        db.collection("technology_product").get()
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
        btnTechnologyProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        svTechProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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