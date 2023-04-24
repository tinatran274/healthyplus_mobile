package com.example.healthyplus.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.healthyplus.Adapter.ProductAdapter;
import com.example.healthyplus.Model.Product;
import com.example.healthyplus.R;

import java.util.ArrayList;
import java.util.List;

public class TechnologyProductActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private Button btnTechnologyProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technology_product);

        recyclerView=findViewById(R.id.rec);
        btnTechnologyProduct=findViewById(R.id.btn_back_technology_product);
        productAdapter=new ProductAdapter(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        productAdapter.setData(getListProduct());
        recyclerView.setAdapter(productAdapter);
        btnTechnologyProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private List<Product> getListProduct()
    {
        List<Product> list = new ArrayList<>();

        return list;
    }

}