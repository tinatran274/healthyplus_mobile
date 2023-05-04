package com.example.healthyplus.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.healthyplus.R;

import java.util.Calendar;

public class ControlCaloriesActivity extends AppCompatActivity {

    Button addMorning, addNoon, addDinner, addSnack, addExercise, btnBackControlCalories;
    TextView progressCalories, maxCalories, infMorning, infNoon, infDinner, infSnack, infExercise;
    EditText editMorning, editNoon, editDinner, editSnack, editExercise;
    ProgressBar circleBar;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_calories);
        addMorning=findViewById(R.id.btn_add_morning);
        addNoon=findViewById(R.id.btn_add_noon);
        addDinner=findViewById(R.id.btn_add_dinner);
        addSnack=findViewById(R.id.btn_add_snack);
        addExercise=findViewById(R.id.btn_add_exercise);
        progressCalories=findViewById(R.id.progress);
        maxCalories=findViewById(R.id.max);
        editMorning=findViewById(R.id.edit_morning);
        editNoon=findViewById(R.id.edit_noon);
        editDinner=findViewById(R.id.edit_dinner);
        editSnack=findViewById(R.id.edit_snack);
        editExercise=findViewById(R.id.edit_exercise);
        infMorning=findViewById(R.id.inf_morning);
        infNoon=findViewById(R.id.inf_noon);
        infDinner=findViewById(R.id.inf_dinner);
        infSnack=findViewById(R.id.inf_snack);
        infExercise=findViewById(R.id.inf_exercise);
        circleBar=findViewById(R.id.progress_circular_bar);
        btnBackControlCalories=findViewById(R.id.btn_back_control_calories);

        // Set water and progress to 0 when new day
        prefs = getSharedPreferences("CaloriesPrefs", MODE_PRIVATE);
        int lastDay = prefs.getInt("lastDay", 0);
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

        if (currentDay != lastDay) {
            prefs.edit().putInt("caloriesConsumption", 0).apply();
            prefs.edit().putInt("lastDay", currentDay).apply();
            progressCalories.setText("0");
            circleBar.setProgressDrawable(getDrawable(R.drawable.circle_red));
        }

        // When open activity, Set water to red if it pass the maximum
        int max = Integer.valueOf(maxCalories.getText().toString().trim());
        int sumCalories = prefs.getInt("caloriesConsumption", 0);
        if(sumCalories > max){
            int color = getColor(R.color.red);
            progressCalories.setTextColor(color);
            circleBar.setProgressDrawable(getDrawable(R.drawable.circle_max));
        }

        // Set water and progress when open the activity
        progressCalories.setText(Integer.toString(prefs.getInt("caloriesConsumption", 0)));
        circleBar.setProgress(prefs.getInt("caloriesConsumption", 0)*100
                /Integer.valueOf(maxCalories.getText().toString().trim()));


        addMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = Integer.parseInt(String.valueOf(progressCalories.getText()));
                int max = Integer.parseInt(String.valueOf(maxCalories.getText()));
                int add = Integer.parseInt(String.valueOf(editMorning.getText()));
                int inf = Integer.parseInt(String.valueOf(infMorning.getText()));
                int circle=((progress+add)*100)/max;

                prefs.edit().putInt("caloriesConsumption", progress + add).apply();
                int sumCalories = prefs.getInt("caloriesConsumption", 0);

                progressCalories.setText(String.valueOf(sumCalories));
                infMorning.setText(String.valueOf(inf+add ));
                circleBar.setProgress(circle);
                editMorning.setText("");
                if((progress+add)>max) {
                    int color = getResources().getColor(R.color.red);
                    progressCalories.setTextColor(color);
                    circleBar.setProgressDrawable(getDrawable(R.drawable.circle_max));
                }
            }
        });
        addNoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = Integer.parseInt(String.valueOf(progressCalories.getText()));
                int max = Integer.parseInt(String.valueOf(maxCalories.getText()));
                int add = Integer.parseInt(String.valueOf(editNoon.getText()));
                int inf = Integer.parseInt(String.valueOf(infNoon.getText()));
                int circle=((progress+add)*100)/max;
                prefs.edit().putInt("caloriesConsumption", progress + add).apply();
                int sumCalories = prefs.getInt("caloriesConsumption", 0);

                progressCalories.setText(String.valueOf(sumCalories));
                infNoon.setText(String.valueOf(inf+add));
                circleBar.setProgress(circle);
                editNoon.setText("");
                if((progress+add)>max) {
                    int color = getResources().getColor(R.color.red);
                    progressCalories.setTextColor(color);
                    circleBar.setProgressDrawable(getDrawable(R.drawable.circle_max));
                }
            }
        });
        addDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = Integer.parseInt(String.valueOf(progressCalories.getText()));
                int max = Integer.parseInt(String.valueOf(maxCalories.getText()));
                int add = Integer.parseInt(String.valueOf(editDinner.getText()));
                int inf = Integer.parseInt(String.valueOf(infDinner.getText()));
                int circle=((progress+add)*100)/max;

                prefs.edit().putInt("caloriesConsumption", progress + add).apply();
                int sumCalories = prefs.getInt("caloriesConsumption", 0);

                progressCalories.setText(String.valueOf(sumCalories));
                infDinner.setText(String.valueOf(inf+add));
                circleBar.setProgress(circle);
                editDinner.setText("");
                if((progress+add)>max) {
                    int color = getResources().getColor(R.color.red);
                    progressCalories.setTextColor(color);
                    circleBar.setProgressDrawable(getDrawable(R.drawable.circle_max));
                }
            }
        });
        addSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = Integer.parseInt(String.valueOf(progressCalories.getText()));
                int max = Integer.parseInt(String.valueOf(maxCalories.getText()));
                int add = Integer.parseInt(String.valueOf(editSnack.getText()));
                int inf = Integer.parseInt(String.valueOf(infSnack.getText()));
                int circle=((progress+add)*100)/max;

                prefs.edit().putInt("caloriesConsumption", progress + add).apply();
                int sumCalories = prefs.getInt("caloriesConsumption", 0);

                progressCalories.setText(String.valueOf(sumCalories));
                infSnack.setText(String.valueOf(inf+add));
                circleBar.setProgress(circle);
                editSnack.setText("");
                if((progress+add)>max) {
                    int color = getResources().getColor(R.color.red);
                    progressCalories.setTextColor(color);
                    circleBar.setProgressDrawable(getDrawable(R.drawable.circle_max));
                }
            }
        });
        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = Integer.parseInt(String.valueOf(progressCalories.getText()));
                int max = Integer.parseInt(String.valueOf(maxCalories.getText()));
                int sub = Integer.parseInt(String.valueOf(editExercise.getText()));
                int inf = Integer.parseInt(String.valueOf(infExercise.getText()));
                int circle=((progress-sub)*100)/max;

                prefs.edit().putInt("caloriesConsumption", progress - sub).apply();
                int sumCalories = prefs.getInt("caloriesConsumption", 0);

                progressCalories.setText(String.valueOf(sumCalories));
                infExercise.setText(String.valueOf(inf+sub));
                circleBar.setProgress(circle);
                editExercise.setText("");
                if((progress-sub)>max) {
                    int color = getResources().getColor(R.color.red);
                    progressCalories.setTextColor(color);
                    circleBar.setProgressDrawable(getDrawable(R.drawable.circle_max));
                }
            }
        });
        btnBackControlCalories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}