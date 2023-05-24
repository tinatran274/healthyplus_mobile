package com.example.healthyplus.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.healthyplus.Model.Dish;
import com.example.healthyplus.R;

public class DishDetailActivity extends AppCompatActivity {
    Dish dish;
    TextView txvKCal, txvCarb, txvProtein, txvFat, txvIngre, txvRecipe;
    ImageView img;
    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);

        findView();
        setView();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setView() {
        txvKCal.setText(Double.toString(dish.getCalo()));
        txvCarb.setText(Double.toString(dish.getCarb()));
        txvFat.setText(Double.toString(dish.getFat()));
        txvProtein.setText(Double.toString(dish.getProtein()));

        String ingredients = "", recipe = "";

        for(String i: dish.getIngredients()) ingredients += "\n- " + i + "\n";
        for(String i: dish.getRecipe()) recipe += "\n" + i + "\n";

        txvIngre.setText(ingredients);
        txvRecipe.setText(recipe);
    }

    private void findView() {
        dish = (Dish) getIntent().getSerializableExtra("dish");
        txvKCal = findViewById(R.id.txvKcal);
        txvCarb = findViewById(R.id.txvCarb);
        txvProtein = findViewById(R.id.txvProtein);
        txvFat = findViewById(R.id.txvFat);
        txvIngre = findViewById(R.id.txvNguyenLieu);
        txvRecipe = findViewById(R.id.txvHuongDan);
        img = findViewById(R.id.imvDish);
        btnBack = findViewById(R.id.btn_back_dish_detail);
    }
}