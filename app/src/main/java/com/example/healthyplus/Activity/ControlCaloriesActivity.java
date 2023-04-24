package com.example.healthyplus.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.healthyplus.R;

public class ControlCaloriesActivity extends AppCompatActivity {

    Button addMorning, addNoon, addDinner, addSnack, addExercise, btnBackControlCalories;
    TextView progressCalories, maxCalories, infMorning, infNoon, infDinner, infSnack, infExercise;
    EditText editMorning, editNoon, editDinner, editSnack, editExercise;
    ProgressBar circleBar;
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

        addMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = Integer.parseInt(String.valueOf(progressCalories.getText()));
                int max = Integer.parseInt(String.valueOf(maxCalories.getText()));
                int add = Integer.parseInt(String.valueOf(editMorning.getText()));
                int inf = Integer.parseInt(String.valueOf(infMorning.getText()));
                int circle=((progress+add)*100)/max;
                progressCalories.setText(String.valueOf(progress+add));
                infMorning.setText(String.valueOf(inf+add));
                circleBar.setProgress(circle);
                editMorning.setText("");
                if((progress+add)>max) {
                    int color = getResources().getColor(R.color.red);
                    progressCalories.setTextColor(color);
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
                progressCalories.setText(String.valueOf(progress+add));
                infNoon.setText(String.valueOf(inf+add));
                circleBar.setProgress(circle);
                editNoon.setText("");
                if((progress+add)>max) {
                    int color = getResources().getColor(R.color.red);
                    progressCalories.setTextColor(color);
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
                progressCalories.setText(String.valueOf(progress+add));
                infDinner.setText(String.valueOf(inf+add));
                circleBar.setProgress(circle);
                editDinner.setText("");
                if((progress+add)>max) {
                    int color = getResources().getColor(R.color.red);
                    progressCalories.setTextColor(color);
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
                progressCalories.setText(String.valueOf(progress+add));
                infSnack.setText(String.valueOf(inf+add));
                circleBar.setProgress(circle);
                editSnack.setText("");
                if((progress+add)>max) {
                    int color = getResources().getColor(R.color.red);
                    progressCalories.setTextColor(color);
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
                progressCalories.setText(String.valueOf(progress-sub));
                infExercise.setText(String.valueOf(inf+sub));
                circleBar.setProgress(circle);
                editExercise.setText("");
                if((progress-sub)>max) {
                    int color = getResources().getColor(R.color.red);
                    progressCalories.setTextColor(color);
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