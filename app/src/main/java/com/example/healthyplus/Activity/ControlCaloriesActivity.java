package com.example.healthyplus.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ControlCaloriesActivity extends AppCompatActivity {

    Button addMorning, addNoon, addDinner, addSnack, addExercise, btnBackControlCalories;
    TextView progressCalories, maxCalories, infMorning, infNoon, infDinner, infSnack, infExercise;
    EditText editMorning, editNoon, editDinner, editSnack, editExercise;
    ProgressBar circleBar;
    SharedPreferences prefs;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_calories);

        findView();

        // get user calories when start the app
        getDataWhenStart();


        // Set water and progress to 0 when new day
        prefs = getSharedPreferences("CaloriesPrefs", MODE_PRIVATE);
        int lastDay = prefs.getInt("lastDay", 0);
        int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

        // when new day
        if (currentDay != lastDay) {

            //----------- push the calories of the previous date to firebase--------------------------------------------------------

            // TODO push calories everytime user change the calories, not at the end of the day
            LocalDate yesterdayDate  = LocalDate.now().minusDays(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String formattedDate = yesterdayDate.format(formatter);

            DocumentReference userStatRef = db.collection("statistic").document(user.getUid());
            DocumentReference dailyDataRef = userStatRef.collection("dailyData").document(formattedDate);

            Map<String, Object> data = new HashMap<>();
            data.put("calories", Integer.valueOf(progressCalories.getText().toString()));

            dailyDataRef.set(data)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    });


            // set calo to 0 when new day
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


        onClickAddMorning();
        onClickAddLunch();
        onClickAddDinner();
        onClickAddSnack();
        onClickAddExercise();

        btnBackControlCalories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void onClickAddExercise() {
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
    }

    private void onClickAddSnack() {
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
    }

    private void onClickAddDinner() {
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
    }

    private void onClickAddLunch() {
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
    }

    private void onClickAddMorning() {
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
    }

    private void findView() {
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
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getDataWhenStart() {

        LocalDate today  = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = today.format(formatter);

        DocumentReference userStatRef = db.collection("statistic").document(user.getUid());
        DocumentReference dailyDataRef = userStatRef.collection("dailyData").document(formattedDate);
        dailyDataRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Map <String, Object> data = task.getResult().getData();
                    System.out.println(formattedDate);
                    System.out.println(data);
                    if(data != null) {
                        infMorning.setText(Long.toString((Long) data.get("breakfast")));
                        infNoon.setText(Long.toString((Long) data.get("lunch")));
                        infDinner.setText(Long.toString((Long) data.get("dinner")));
                        infExercise.setText(Long.toString((Long) data.get("exercise")));
                        infSnack.setText(Long.toString((Long) data.get("snack")));
                        progressCalories.setText(Long.toString((Long) data.get("calories")));
                    }
                }
            }
        });
    }


}