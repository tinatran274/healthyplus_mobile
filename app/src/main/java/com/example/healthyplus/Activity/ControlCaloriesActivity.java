package com.example.healthyplus.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.healthyplus.Model.User;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ControlCaloriesActivity extends AppCompatActivity {

    Button addMorning, addNoon, addDinner, addSnack, addExercise, btnBackControlCalories;
    TextView progressCalories, infMorning, infNoon, infDinner, infSnack, infExercise,
            txvDay, txvMax;
    ImageView nextDay, previousDay, imvChart;
    EditText editMorning, editNoon, editDinner, editSnack, editExercise;
    ProgressBar circleBar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    User u;
    int maxCalories;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_calories);

        findView();

        getUserMaxCalories();

        // get user calories when start the app
        LocalDate today  = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = today.format(formatter);
        getDataAtDay(formattedDate);

        // Calendar
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());

        txvDay.setText(currentDate);
        txvDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCalendarDialog(view);
            }
        });

        nextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToNextDay();
            }
        });

        previousDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToPreviousDay();
            }
        });

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

        imvChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CaloriesChart.class);
                intent.putExtra("user", u);
                startActivity(intent);
            }
        });
    }

    private void getUserMaxCalories() {
        db.collection("user").document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            u = task.getResult().toObject(User.class);
                            maxCalories = u.TTDECal();
                            txvMax.setText(String.valueOf(maxCalories));
                        }
                    }
                });
    }

    private void switchToNextDay() {
        // Get the current date from the TextView
        String currentDate = txvDay.getText().toString();

        try {
            // Parse the current date to a Date object
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date date = dateFormat.parse(currentDate);

            // Create a calendar instance and set it to the current date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            // Add one day from the calendar
            calendar.add(Calendar.DAY_OF_MONTH, 1);

            // Format the new date
            String previousDate = dateFormat.format(calendar.getTime());

            // Set the new date in the TextView
            txvDay.setText(previousDate);

            getDataAtDay(previousDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void switchToPreviousDay() {
        // Get the current date from the TextView
        String currentDate = txvDay.getText().toString();

        try {
            // Parse the current date to a Date object
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date date = dateFormat.parse(currentDate);

            // Create a calendar instance and set it to the current date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            // Subtract one day from the calendar
            calendar.add(Calendar.DAY_OF_MONTH, -1);

            // Format the new date
            String previousDate = dateFormat.format(calendar.getTime());

            // Set the new date in the TextView
            txvDay.setText(previousDate);
            getDataAtDay(previousDate);
        } catch (ParseException e) {
            Log.e(TAG,e.toString());
        }
    }

    private void openCalendarDialog(View view) {
        // Create a calendar instance with the current date
        Calendar calendar = Calendar.getInstance();

        // Get the current year, month, and day from the calendar
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and set the initial date to the current date
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                ControlCaloriesActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Format the selected date
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        calendar.set(year, month, dayOfMonth);
                        String selectedDate = dateFormat.format(calendar.getTime());

                        // Set the selected date in the TextView
                        txvDay.setText(selectedDate);
                        getDataAtDay(selectedDate);
                    }
                },
                year,
                month,
                day);
        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void onClickAddExercise() {
        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = Integer.parseInt(String.valueOf(progressCalories.getText()));
                int sub = Integer.parseInt(String.valueOf(editExercise.getText()));
                int inf = Integer.parseInt(String.valueOf(infExercise.getText()));
                int circle=((progress-sub)*100)/maxCalories;

                progressCalories.setText(String.valueOf(progress - sub));
                infExercise.setText(String.valueOf(inf+sub));
                circleBar.setProgress(circle);
                editExercise.setText("");
                if(progress > maxCalories && (progress-sub)<maxCalories) {
                    int color = getColor(R.color.green_main);
                    progressCalories.setTextColor(color);
                    circleBar.setProgressDrawable(getDrawable(R.drawable.circle));
                }

                setExercise(inf + sub, progress - sub);
            }
        });
    }

    private void onClickAddSnack() {
        addSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = Integer.parseInt(String.valueOf(progressCalories.getText()));
                int add = Integer.parseInt(String.valueOf(editSnack.getText()));
                int inf = Integer.parseInt(String.valueOf(infSnack.getText()));
                int circle=((progress+add)*100)/maxCalories;

                progressCalories.setText(String.valueOf(progress + add));
                infSnack.setText(String.valueOf(inf+add));
                circleBar.setProgress(circle);
                editSnack.setText("");
                if((progress+add)>maxCalories) {
                    int color = getResources().getColor(R.color.red);
                    progressCalories.setTextColor(color);
                    circleBar.setProgressDrawable(getDrawable(R.drawable.circle_max));
                }
                setSnack(inf + add, progress + add);
            }
        });
    }

    private void onClickAddDinner() {
        addDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = Integer.parseInt(String.valueOf(progressCalories.getText()));
                int add = Integer.parseInt(String.valueOf(editDinner.getText()));
                int inf = Integer.parseInt(String.valueOf(infDinner.getText()));
                int circle=((progress+add)*100)/maxCalories;

                progressCalories.setText(String.valueOf(progress + add));
                infDinner.setText(String.valueOf(inf+add));
                circleBar.setProgress(circle);
                editDinner.setText("");
                if((progress+add)>maxCalories) {
                    int color = getResources().getColor(R.color.red);
                    progressCalories.setTextColor(color);
                    circleBar.setProgressDrawable(getDrawable(R.drawable.circle_max));
                }
                setDinner(inf + add, add + progress);
            }
        });
    }

    private void onClickAddLunch() {
        addNoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = Integer.parseInt(String.valueOf(progressCalories.getText()));
                int add = Integer.parseInt(String.valueOf(editNoon.getText()));
                int inf = Integer.parseInt(String.valueOf(infNoon.getText()));
                int circle=((progress+add)*100)/maxCalories;

                progressCalories.setText(String.valueOf(progress + add));
                infNoon.setText(String.valueOf(inf+add));
                circleBar.setProgress(circle);
                editNoon.setText("");
                if((progress+add)>maxCalories) {
                    int color = getResources().getColor(R.color.red);
                    progressCalories.setTextColor(color);
                    circleBar.setProgressDrawable(getDrawable(R.drawable.circle_max));
                }
                setLunch(inf + add,progress + add);
            }
        });
    }

    private void onClickAddMorning() {
        addMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = Integer.parseInt(String.valueOf(progressCalories.getText()));
                int add = Integer.parseInt(String.valueOf(editMorning.getText()));
                int inf = Integer.parseInt(String.valueOf(infMorning.getText()));
                int circle=((progress+add)*100)/maxCalories;

                progressCalories.setText(String.valueOf(progress + add));
                infMorning.setText(String.valueOf(inf+add ));
                circleBar.setProgress(circle);
                editMorning.setText("");
                if((progress+add)>maxCalories) {
                    int color = getResources().getColor(R.color.red);
                    progressCalories.setTextColor(color);
                    circleBar.setProgressDrawable(getDrawable(R.drawable.circle_max));
                }

                setMorning(inf + add, progress + add);
            }
        });
    }

    private void setMorning(int calo, int sum) {
        DocumentReference userStatRef = db.collection("statistic").document(user.getUid());
        DocumentReference dailyDataRef = userStatRef.collection("dailyData").document(txvDay.getText().toString());
        Map<String, Object> data = new HashMap<>();
        data.put("breakfast", calo);
        data.put("calories", sum);
        dailyDataRef.set(data, SetOptions.mergeFields("breakfast", "calories"))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });

    }
    private void setLunch(int calo,  int sum) {
        DocumentReference userStatRef = db.collection("statistic").document(user.getUid());
        DocumentReference dailyDataRef = userStatRef.collection("dailyData").document(txvDay.getText().toString());
        Map<String, Object> data = new HashMap<>();
        data.put("lunch", calo);
        data.put("calories", sum);
        dailyDataRef.set(data, SetOptions.mergeFields("lunch", "calories"))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }
    private void setDinner(int calo, int sum) {
        DocumentReference userStatRef = db.collection("statistic").document(user.getUid());
        DocumentReference dailyDataRef = userStatRef.collection("dailyData").document(txvDay.getText().toString());
        Map<String, Object> data = new HashMap<>();
        data.put("dinner", calo);
        data.put("calories", sum);
        dailyDataRef.set(data, SetOptions.mergeFields("dinner", "calories"))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }
    private void setSnack(int calo,  int sum) {
        DocumentReference userStatRef = db.collection("statistic").document(user.getUid());
        DocumentReference dailyDataRef = userStatRef.collection("dailyData").document(txvDay.getText().toString());
        Map<String, Object> data = new HashMap<>();
        data.put("snack", calo);
        data.put("calories", sum);
        dailyDataRef.set(data, SetOptions.mergeFields("snack", "calories"))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }
    private void setExercise(int calo,  int sum) {
        DocumentReference userStatRef = db.collection("statistic").document(user.getUid());
        DocumentReference dailyDataRef = userStatRef.collection("dailyData").document(txvDay.getText().toString());
        Map<String, Object> data = new HashMap<>();
        data.put("exercise", calo);
        data.put("calories", sum);
        dailyDataRef.set(data, SetOptions.mergeFields("exercise", "calories"))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
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
        txvDay = findViewById(R.id.txvDay);
        nextDay = findViewById(R.id.imvNextDay);
        previousDay = findViewById(R.id.imvPreviousDay);
        txvMax = findViewById(R.id.max);
        imvChart = findViewById(R.id.img_cart);

        u = new User();
        maxCalories = 9999;
    }


    private void getDataAtDay(String formattedDate) {

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
                        if(data.containsKey("breakfast")) infMorning.setText(Long.toString((Long) data.get("breakfast")));
                        else infMorning.setText("0");
                        if(data.containsKey("lunch")) infNoon.setText(Long.toString((Long) data.get("lunch")));
                        else infNoon.setText("0");
                        if(data.containsKey("dinner")) infDinner.setText(Long.toString((Long) data.get("dinner")));
                        else infDinner.setText("0");
                        if(data.containsKey("exercise")) infExercise.setText(Long.toString((Long) data.get("exercise")));
                        else infExercise.setText("0");
                        if(data.containsKey("snack")) infSnack.setText(Long.toString((Long) data.get("snack")));
                        else infSnack.setText("0");
                        if(data.containsKey("calories"))
                        {
                            progressCalories.setText(Long.toString((Long) data.get("calories")));
                            long l = (Long) data.get("calories");
                            int x = (int) l * 100;

                            if(l > maxCalories){
                                int color = getColor(R.color.red);
                                progressCalories.setTextColor(color);
                                circleBar.setProgressDrawable(getDrawable(R.drawable.circle_max));
                            }
                            else {
                                int color = getColor(R.color.green_main);
                                progressCalories.setTextColor(color);
                                circleBar.setProgressDrawable(getDrawable(R.drawable.circle));
                            }
                                circleBar.setProgress( x / maxCalories);
                                String s = maxCalories + "";
                                Log.d(TAG, "success get the max calories from firebase " + s);

                        }
                        else {
                            int color = getColor(R.color.green_main);
                            progressCalories.setTextColor(color);
                            circleBar.setProgressDrawable(getDrawable(R.drawable.circle));
                            progressCalories.setText("0");
                            circleBar.setProgress(0);
                        }

                    }
                    else{
                        int color = getColor(R.color.green_main);
                        progressCalories.setTextColor(color);
                        circleBar.setProgressDrawable(getDrawable(R.drawable.circle));

                        infMorning.setText("0");
                        infNoon.setText("0");
                        infDinner.setText("0");
                        infExercise.setText("0");
                        infSnack.setText("0");
                        progressCalories.setText("0");
                        circleBar.setProgress(0);

                    }
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }


}