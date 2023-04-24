package com.example.healthyplus.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.healthyplus.Adapter.ProductAdapter;
import com.example.healthyplus.Model.Product;
import com.example.healthyplus.R;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private CardView userCardView, controlCaloriesCardView, productCardView, technologyCardView, controlWaterCardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView=findViewById(R.id.rec);
        userCardView=findViewById(R.id.cv_user);
        controlCaloriesCardView=findViewById(R.id.cv_control_calories);
        productCardView=findViewById(R.id.cv_product);
        technologyCardView=findViewById(R.id.cv_technology_product);
        controlWaterCardView=findViewById(R.id.cv_control_water);

        productAdapter=new ProductAdapter(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        productAdapter.setData(getListProduct());
        recyclerView.setAdapter(productAdapter);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        userCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intent);
                finish();
            }
        });
        controlCaloriesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ControlCaloriesActivity.class);
                startActivity(intent);
                finish();
            }
        });
        productCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                startActivity(intent);
                finish();
            }
        });
        technologyCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TechnologyProductActivity.class);
                startActivity(intent);
                finish();
            }
        });
        controlWaterCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), ControlWaterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private List<Product> getListProduct()
    {
        List<Product> list = new ArrayList<>();
        /*list.add(new Product(1, "Viên uống dầu cá Omega3", 570000, "gs://healthyplus-612a6.appspot.com/food_products/sp_01.jpg", "Healthy Care", 1));
        list.add(new Product(2, "Bánh biscostti vị matcha", 50000, "gs://healthyplus-612a6.appspot.com/food_products/sp_02.jpg", "Rainbow Healthy", 1));
        list.add(new Product(3, "Hạt chia hữu cơ chuẩn Organic", 70000, "gs://healthyplus-612a6.appspot.com/food_products/sp_03.jpg", "Rainbow Healthy", 1));*/
        return list;
    }

}