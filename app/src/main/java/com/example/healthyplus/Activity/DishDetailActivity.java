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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthyplus.Model.Dish;
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

public class DishDetailActivity extends AppCompatActivity {
    Dish dish;
    TextView txvKCal, txvCarb, txvProtein, txvFat, txvIngre, txvRecipe, txvCreator;
    ImageView img;
    FirebaseStorage storage;
    TextView txvBicycle, txvJump, txvRun, txvWalk, txvBurnKcal;
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
        setContentView(R.layout.activity_dish_detail);

//        dish = (Dish) getIntent().getSerializableExtra("dish");
        findView();
        btnAdd = findViewById(R.id.btn_add);
        spHour = findViewById(R.id.sp_hour);
        txvCreator = findViewById(R.id.txv_creator);
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
                    if(data != null) {
                        if (data.containsKey("bresakfast"))
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

        setView();
        buttonOnClik();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newData = (int) dish.getCalo();
                Toast.makeText(DishDetailActivity.this, "Bạn đã thêm "+ newData + " kcal vào "+txvHour, Toast.LENGTH_SHORT).show();

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
        btnBack.setText(dish.getName());
        txvCreator.setText(dish.getCreator());

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


        showTimeToBurn();
    }

    private void showTimeToBurn() {
        int calo = (int) dish.getCalo();

        txvBurnKcal.setText(String.format("%d", calo));

        int bicycle = calo*60/500;
        txvBicycle.setText(String.format("%d", bicycle));

        int walk = calo*60/300;
        txvWalk.setText(String.format("%d", walk));

        int jump = calo*60/800;
        txvJump.setText(String.format("%d", jump));

        int run = calo*60/450;
        txvRun.setText(String.format("%d", run));
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

        txvBicycle = findViewById(R.id.txvBicycle);
        txvJump = findViewById(R.id.txvJumpRope);
        txvRun = findViewById(R.id.txvRun);
        txvWalk  = findViewById(R.id.txvWalk);
        txvBurnKcal = findViewById(R.id.txvBurnKcal);
    }
}