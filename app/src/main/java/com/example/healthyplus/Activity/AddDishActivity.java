package com.example.healthyplus.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthyplus.Model.Dish;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AddDishActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser;
    FirebaseStorage storage;

    ImageView imgDish;
    TextView txvName, txvKcal, txvFat, txvCarb, txvProtein, txvIngre, txvRecipe;
    Button btnComplete, btnBack;
    int imageSize = 320;
    Bitmap image;
    private ArrayList<String> recipe;
    private ArrayList<String> ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dish);

        storage = FirebaseStorage.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        imgDish = findViewById(R.id.img_dish);
        txvName = findViewById(R.id.txv_name);
        txvKcal = findViewById(R.id.txvKcal);
        txvCarb = findViewById(R.id.txvCarb);
        txvFat = findViewById(R.id.txvFat);
        txvProtein = findViewById(R.id.txvProtein);
        txvIngre = findViewById(R.id.txv_ingre);
        txvRecipe = findViewById(R.id.txv_recipe);
        btnComplete =findViewById(R.id.btn_complete);
        btnBack = findViewById(R.id.btn_back);

        imgDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent, 1);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int status = checkStatus();
                if (status == 0) {
                    String nameImg = convertToSnakeCase(String.valueOf(txvName.getText())) + ".jpg";
                    StorageReference dishRef = storage.getReference().child("dish").child(nameImg);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();
                    StorageReference folderRef = storage.getReference().child("dish");
                    folderRef.listAll().addOnSuccessListener(listResult -> {
                        boolean fileExists = false;
                        for (StorageReference item : listResult.getItems()) {
                            if (item.getName().equals(nameImg)) {
                                fileExists = true;
                                break;
                            }
                        }
                        if (fileExists) {
                            Toast.makeText(AddDishActivity.this, "Đã tồn tại tên món ăn", Toast.LENGTH_SHORT).show();
                        } else {
                            UploadTask uploadTask = dishRef.putBytes(data);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(AddDishActivity.this, "Lỗi upload hình ảnh!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    String link = "gs://healthyplus-19833.appspot.com/dish/" + nameImg;
                                    upLoadDishData(link);
                                }
                            });
                        }
                    });
                }
                else
                    Toast.makeText(AddDishActivity.this, status + "Bạn chưa điền đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            Uri dat = data.getData();
            image = null;
            try {
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), dat);
                int dimension = Math.min(image.getWidth(), image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);

            } catch (IOException e) {
                e.printStackTrace();
            }
            imgDish.setImageBitmap(image);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void upLoadDishData (String url){

        String tempIngre = String.valueOf(txvIngre.getText());
        String[] linesArrayIngre = tempIngre.split("\n");
        ingredients = new ArrayList<>(Arrays.asList(linesArrayIngre));

        String tempRecipe = String.valueOf(txvRecipe.getText());
        String[] linesArrayRecipe = tempRecipe.split("\n");
        recipe = new ArrayList<>(Arrays.asList(linesArrayRecipe));

        Map<String, Object> dishData = new HashMap<>();
        dishData.put("name", String.valueOf(txvName.getText()));
        dishData.put("creator", currentUser.getUid());
        dishData.put("img", url);
        dishData.put("calo", Integer.parseInt(String.valueOf(txvKcal.getText())));
        dishData.put("carb", Integer.parseInt(String.valueOf(txvCarb.getText())));
        dishData.put("fat", Integer.parseInt(String.valueOf(txvFat.getText())));
        dishData.put("protein", Integer.parseInt(String.valueOf(txvProtein.getText())));
        dishData.put("ingredients", ingredients);
        dishData.put("recipe", recipe);

        db.collection("dish")
                .add(dishData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        dishData.put("id", documentReference.getId());
                        db.collection("dish").document(documentReference.getId()).set(dishData);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    public String convertToSnakeCase(String input) {

        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("[^\\p{ASCII}]", "");
        String cleaned = normalized.replaceAll("[^a-zA-Z0-9]", "_");
        String snakeCase = cleaned.replaceAll("_+", "_").toLowerCase();
        snakeCase = snakeCase.replaceAll("^_|_$", "");

        return snakeCase;
    }

    public int checkStatus() {
        int flag = 0;
        if(image == null){
            flag++;
        }
        if(txvName.getText().toString().trim().isEmpty()){
            flag++;
        }
        if(txvRecipe.getText().toString().trim().isEmpty()){
            flag++;
        }
        if(txvIngre.getText().toString().trim().isEmpty()){
            flag++;
        }
        return flag;
    }

}