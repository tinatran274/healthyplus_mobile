package com.example.healthyplus.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.healthyplus.Model.Product;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class ControlWaterActivity extends AppCompatActivity {

    Button addWater, btnBackControlWater, btn50, btn100, btn200;
    TextView progressWater, maxWater, infWater;
    EditText editWater;
    ProgressBar circleWaterBar;
    CardView cardView;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_water);

        addWater=findViewById(R.id.btn_add_water);
        progressWater=findViewById(R.id.progress);
        maxWater=findViewById(R.id.max);
        infWater=findViewById(R.id.inf_water);
        editWater=findViewById(R.id.edit_water);
        circleWaterBar=findViewById(R.id.progress_circular_bar);
        btnBackControlWater=findViewById(R.id.btn_back_control_water);
        btn50=findViewById(R.id.btn_50);
        btn100=findViewById(R.id.btn_100);
        btn200=findViewById(R.id.btn_200);
        cardView=findViewById(R.id.cv_notify);

        // Set water and progress to 0 when new day
        prefs = getSharedPreferences("WaterPrefs", MODE_PRIVATE);
        int lastDay = prefs.getInt("lastDay", 0);
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

        if (currentDay != lastDay) {
            prefs.edit().putInt("waterConsumption", 0).apply();
            prefs.edit().putInt("lastDay", currentDay).apply();
            progressWater.setText("0");
            circleWaterBar.setProgressDrawable(getDrawable(R.drawable.circle_red));

        }

        // When open activity, Set water to red if it pass the maximum
        int max = Integer.valueOf(maxWater.getText().toString().trim());
        int sumWater = prefs.getInt("waterConsumption", 0);
        if(sumWater > max){
            int color = getColor(R.color.red);
            progressWater.setTextColor(color);
            circleWaterBar.setProgressDrawable(getDrawable(R.drawable.circle_max));
        }



        // Set water and progress when open the activity
        progressWater.setText(Integer.toString(prefs.getInt("waterConsumption", 0)));
        circleWaterBar.setProgress(prefs.getInt("waterConsumption", 0)*100
                /Integer.valueOf(maxWater.getText().toString().trim()));


        addWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = Integer.parseInt(String.valueOf(progressWater.getText()));
                int max = Integer.parseInt(String.valueOf(maxWater.getText()));
                int add = Integer.parseInt(String.valueOf(editWater.getText()));
                int inf = Integer.parseInt(String.valueOf(infWater.getText()));
                int circle=((progress+add)*100)/max;

                prefs.edit().putInt("waterConsumption", progress + add).apply();
                int sumWater = prefs.getInt("waterConsumption", 0);

                progressWater.setText(String.valueOf(sumWater));
                infWater.setText(String.valueOf(inf+add));
                circleWaterBar.setProgress(circle);
                editWater.setText("");

                if((progress+add)>max) {
                    int color = getColor(R.color.red);
                    progressWater.setTextColor(color);
                    circleWaterBar.setProgressDrawable(getDrawable(R.drawable.circle_max));
                }

            }
        });
        btn50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = Integer.parseInt(String.valueOf(progressWater.getText()));
                int max = Integer.parseInt(String.valueOf(maxWater.getText()));
                int inf = Integer.parseInt(String.valueOf(infWater.getText()));
                int circle=((progress+50)*100)/max;

                prefs.edit().putInt("waterConsumption", progress + 50).apply();
                int sumWater = prefs.getInt("waterConsumption", 0);

                progressWater.setText(String.valueOf(sumWater));
                infWater.setText(String.valueOf(inf+50));
                circleWaterBar.setProgress(circle);
                editWater.setText("");
                if((progress+50)>max) {
                    int color = getColor(R.color.red);
                    progressWater.setTextColor(color);
                    circleWaterBar.setProgressDrawable(getDrawable(R.drawable.circle_max));
                }
            }
        });
        btn100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = Integer.parseInt(String.valueOf(progressWater.getText()));
                int max = Integer.parseInt(String.valueOf(maxWater.getText()));
                int inf = Integer.parseInt(String.valueOf(infWater.getText()));
                int circle=((progress+100)*100)/max;

                prefs.edit().putInt("waterConsumption", progress + 100).apply();
                int sumWater = prefs.getInt("waterConsumption", 0);

                progressWater.setText(String.valueOf(sumWater));
                infWater.setText(String.valueOf(inf+100));
                circleWaterBar.setProgress(circle);
                editWater.setText("");
                if((progress+100)>max) {
                    int color = getColor(R.color.red);
                    progressWater.setTextColor(color);
                    circleWaterBar.setProgressDrawable(getDrawable(R.drawable.circle_max));
                }
            }
        });
        btn200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = Integer.parseInt(String.valueOf(progressWater.getText()));
                int max = Integer.parseInt(String.valueOf(maxWater.getText()));
                int inf = Integer.parseInt(String.valueOf(infWater.getText()));
                int circle=((progress+200)*100)/max;

                prefs.edit().putInt("waterConsumption", progress + 200).apply();
                int sumWater = prefs.getInt("waterConsumption", 0);

                progressWater.setText(String.valueOf(sumWater));
                infWater.setText(String.valueOf(inf+200));
                circleWaterBar.setProgress(circle);
                editWater.setText("");
                if((progress+200)>max) {
                    int color = getColor(R.color.red);
                    progressWater.setTextColor(color);
                    circleWaterBar.setProgressDrawable(getDrawable(R.drawable.circle_max));
                }
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
                startActivity(intent);
            }
        });


        btnBackControlWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}