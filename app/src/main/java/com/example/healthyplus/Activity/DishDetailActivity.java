package com.example.healthyplus.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthyplus.Model.Dish;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class DishDetailActivity extends AppCompatActivity {
    Dish dish;
    TextView txvKCal, txvCarb, txvProtein, txvFat, txvIngre, txvRecipe;
    ImageView img;
    Button btnBack;
    FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);

        findView();
        setView();
        buttonOnClik();


    }

    private void buttonOnClik() {
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

        storage = FirebaseStorage.getInstance();
        StorageReference imgRef = storage.getReferenceFromUrl(dish.getImg());
        try {
            File localFile = File.createTempFile("images", "jpg");
            imgRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    img.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(getApplicationContext(), "Loi", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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