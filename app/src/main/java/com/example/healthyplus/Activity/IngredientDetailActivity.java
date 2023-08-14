package com.example.healthyplus.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthyplus.Model.Ingredient;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class IngredientDetailActivity extends AppCompatActivity {

    ImageView img;
    TextView txvWeigt, txvKcal, txvFat, txvCarb, txvProtein;
    EditText edtWeight;
    Button btnBack, btnAdd;
    Spinner spHour;
    String txvHour;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    Long breakfast = Long.valueOf(0), lunch = Long.valueOf(0), dinner = Long.valueOf(0),
            snack = Long.valueOf(0), calories = Long.valueOf(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_detail);

        Ingredient ingredient = (Ingredient) getIntent().getSerializableExtra("ingredient");
        findView();
        edtWeight = findViewById(R.id.edtWeight);
        btnAdd = findViewById(R.id.btn_add);
        spHour = findViewById(R.id.sp_hour);
        setView();
        String hour[]={"Bữa sáng", "Bữa trưa", "Bữa tối", "Ăn vặt"};

        ArrayAdapter adapterHour = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hour);
        adapterHour.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spHour.setAdapter(adapterHour);

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = today.format(formatter);

        DocumentReference userStatRef = db.collection("statistic").document(user.getUid());
        DocumentReference dailyDataRef = userStatRef.collection("dailyData").document(formattedDate);
        dailyDataRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Map<String, Object> data = task.getResult().getData();
                    if (data != null) {
                        if (data.containsKey("breakfast"))
                            breakfast = (Long) data.get("breakfast");
                        if (data.containsKey("lunch"))
                            lunch = (Long) data.get("lunch");
                        if (data.containsKey("dinner"))
                            dinner = (Long) data.get("dinner");
                        if (data.containsKey("snack"))
                            snack = (Long) data.get("snack");
                        if (data.containsKey("calories"))
                            calories = (Long) data.get("calories");
                    }

                }

            }
        });

        spHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                txvHour = hour[i];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newData = (int) (Integer.parseInt(String.valueOf(edtWeight.getText()))*ingredient.getCalo())/100;
                Toast.makeText(IngredientDetailActivity.this, "Bạn đã thêm "+ newData + " kcal vào "+txvHour, Toast.LENGTH_SHORT).show();

                if(txvHour=="Bữa sáng"){
                    breakfast += newData;
                    calories += newData;
                    Map<String, Object> data = new HashMap<>();
                    data.put("breakfast", breakfast);
                    data.put("calories", calories);
                    dailyDataRef.set(data, SetOptions.mergeFields("breakfast", "calories"))
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, e.toString());
                                }
                            });
                }
                else if(txvHour=="Bữa trưa"){
                    lunch += newData;
                    calories += newData;
                    Map<String, Object> data = new HashMap<>();
                    data.put("lunch", lunch);
                    data.put("calories", calories);
                    dailyDataRef.set(data, SetOptions.mergeFields("lunch", "calories"))
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, e.toString());
                                }
                            });
                }
                else if(txvHour=="Bữa tối"){
                    dinner += newData;
                    calories += newData;
                    Map<String, Object> data = new HashMap<>();
                    data.put("dinner", dinner);
                    data.put("calories", calories);
                    dailyDataRef.set(data, SetOptions.mergeFields("dinner", "calories"))
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, e.toString());
                                }
                            });
                }
                else{
                    snack += newData;
                    calories += newData;
                    Map<String, Object> data = new HashMap<>();
                    data.put("snack", snack);
                    data.put("calories", calories);
                    dailyDataRef.set(data, SetOptions.mergeFields("snack", "calories"))
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, e.toString());
                                }
                            });
                }
                Intent intent = new Intent(getApplicationContext(), ControlCaloriesActivity.class);
                startActivity(intent);
                finish();
            }
        });

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