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

import com.example.healthyplus.Model.Ingredient;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class IngredientDetailActivity extends AppCompatActivity {

    ImageView img;
    TextView txvWeigt, txvKcal, txvFat, txvCarb, txvProtein;
    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_detail);

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
        Ingredient ingredient = (Ingredient) getIntent().getSerializableExtra("ingredient");
        btnBack.setText(ingredient.getName());
        txvWeigt.setText(Double.toString(ingredient.getWeight()));
        txvProtein.setText(Double.toString(ingredient.getProtein()));
        txvCarb.setText(Double.toString(ingredient.getCarb()));
        txvKcal.setText(Double.toString(ingredient.getCalo()));
        txvFat.setText(Double.toString(ingredient.getFat()));


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imgRef = storage.getReferenceFromUrl(ingredient.getImg());
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
        txvWeigt = findViewById(R.id.txvWeight);
        txvKcal = findViewById(R.id.txvKcal);
        txvFat = findViewById(R.id.txvFat);
        txvCarb = findViewById(R.id.txvCarb);
        txvProtein = findViewById(R.id.txvProtein);
        btnBack = findViewById(R.id.btn_back_ingredient_detail);
        img = findViewById(R.id.imgIngre);
    }
}