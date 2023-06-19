package com.example.healthyplus.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthyplus.Model.Product;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DetailProductActivity extends AppCompatActivity {

    TextView name, cost, supplier, btnAdd, btnPay;
    ImageView img;
    Button buttonBack;
    FirebaseStorage storage;
    FirebaseFirestore db;
    FirebaseUser user;
    HashMap<Product, Long> choseProduct = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        Bundle bundle = getIntent().getExtras();
        if (bundle==null)
            return;
        Product product= (Product) bundle.get("object_product");

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        name=findViewById(R.id.tv_name);
        cost=findViewById(R.id.tv_cost);
        supplier=findViewById(R.id.tv_supplier_name);
        img=findViewById(R.id.iv_img);
        buttonBack = findViewById(R.id.btn_back);
        btnAdd=findViewById(R.id.btn_add_cart);
        btnPay=findViewById(R.id.btn_pay);

        choseProduct.put(product, 1L);
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
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                // Lấy số lượng hiện tại của sản phẩm trong giỏ hàng và tăng lên 1
                DocumentReference userCartRef = db.collection("cart").document(user.getUid());
                String productID = product.getId(); // ID của sản phẩm cần tăng số lượng

                userCartRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Document của người dùng đã tồn tại
                            Map<String, Object> cartData = document.getData();
                            if (cartData != null && cartData.containsKey(productID)) {
                                // Sản phẩm đã tồn tại trong giỏ hàng, tăng số lượng lên 1
                                int currentQuantity = ((Long) cartData.get(productID)).intValue();
                                int newQuantity = currentQuantity + 1;

                                userCartRef.update(productID, newQuantity)
                                        .addOnSuccessListener(aVoid -> Toast.makeText(DetailProductActivity.this, "+1 "+product.getName(), Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> System.err.println("Lỗi khi tăng số lượng: " + e));
                            } else {
                                // Sản phẩm chưa tồn tại trong giỏ hàng, đặt số lượng là 1
                                userCartRef.update(productID, 1)
                                        .addOnSuccessListener(aVoid -> Toast.makeText(DetailProductActivity.this, product.getName()+" đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show())
                                        .addOnFailureListener(e -> System.err.println("Lỗi khi thêm sản phẩm: " + e));
                            }
                        }
                        else{
                            // Document của người dùng chưa tồn tại
                            userCartRef.set(Collections.singletonMap(productID, 1))
                                    .addOnSuccessListener(aVoid -> System.out.println("Sản phẩm đã được thêm vào giỏ hàng"))
                                    .addOnFailureListener(e -> System.err.println("Lỗi khi thêm sản phẩm: " + e));
                        }

                    } else {
                        System.err.println("Lỗi khi lấy dữ liệu: " + task.getException());
                    }
                });
            }
        });
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, String.valueOf(choseProduct.size()));
                if(choseProduct.size()>0){
                    Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                    intent.putExtra("hashMapData", choseProduct);
                    startActivity(intent);
                }
            }
        });
    }
}