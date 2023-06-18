package com.example.healthyplus.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthyplus.Model.Product;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class DetailProductActivity extends AppCompatActivity {

    TextView name, cost, supplier;
    ImageView img;
    Button buttonBack;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        Bundle bundle = getIntent().getExtras();
        if (bundle==null)
            return;
        Product product= (Product) bundle.get("object_product");

        name=findViewById(R.id.tv_name);
        cost=findViewById(R.id.tv_cost);
        supplier=findViewById(R.id.tv_supplier_name);
        img=findViewById(R.id.iv_img);
        buttonBack = findViewById(R.id.btn_back);

        name.setText(product.getName());
        String numString = String.valueOf(product.getCost());
        String str = "";
        for (int i = 0; i < numString.length() ; i++){
            if((numString.length() - i - 1) % 3 == 0 && i < numString.length()-1){
                str += Character.toString(numString.charAt(i)) + ".";
            }else{
                str += Character.toString(numString.charAt(i));
            }
        }
        cost.setText(str);
        supplier.setText(product.getSupplierName());

        storage = FirebaseStorage.getInstance();
        StorageReference imgRef = storage.getReferenceFromUrl(product.getImg());
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
                    Toast.makeText(DetailProductActivity.this, "Loi", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(getApplicationContext(), ProductActivity.class);
//                startActivity(intent);
                finish();
            }
        });
    }
}