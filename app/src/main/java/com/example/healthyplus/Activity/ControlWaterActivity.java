package com.example.healthyplus.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.healthyplus.Model.Product;
import com.example.healthyplus.Model.User;
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
import com.orbitalsonic.waterwave.WaterWaveView;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ControlWaterActivity extends AppCompatActivity {

    Button addWater, btnBackControlWater, btn50, btn100, btn200, btn_minus50;
    TextView progressWater, maxWater, infWater, txvDay;
    EditText editWater;
    WaterWaveView waterWaveView;
    CardView cardView;
    SharedPreferences prefs;
    ImageView nextDay, previousDay, imvChart;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    User u;
    int maxWaterPerDate;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_water);

        findView();

        getUser();

        calendarView();

        imvChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WaterChartActivity.class);
                intent.putExtra("user", u);
                startActivity(intent);
            }
        });

        addWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = Integer.parseInt(String.valueOf(progressWater.getText()));
                int max = Integer.parseInt(String.valueOf(maxWater.getText()));
                int add = Integer.parseInt(String.valueOf(editWater.getText()));
                int inf = Integer.parseInt(String.valueOf(infWater.getText()));
                int circle=((progress+add)*100)/max;

                progressWater.setText(String.valueOf(progress + add));
                infWater.setText(String.valueOf(inf+add));
                waterWaveView.setProgress(circle);
                editWater.setText("");

                if((progress+add)>max) {
                    int color = getColor(R.color.red);
                    progressWater.setTextColor(color);
                }
                setWater(progress + add);
            }
        });

        btn_minus50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = Integer.parseInt(String.valueOf(progressWater.getText()));
                int max = Integer.parseInt(String.valueOf(maxWater.getText()));
                int inf = Integer.parseInt(String.valueOf(infWater.getText()));
                int circle=((progress-100)*100)/max;

                int sumWater = progress - 100;

                if(sumWater >= 0) {
                    progressWater.setText(String.valueOf(sumWater));
                    infWater.setText(String.valueOf(inf-100));
                    waterWaveView.setProgress(circle);
                    editWater.setText("");
                    if((progress-100) < max) {
                        int color = getColor(R.color.blue_water);
                        progressWater.setTextColor(color);
                    }
                    setWater(sumWater);
                }
            }
        });
        btn50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = Integer.parseInt(String.valueOf(progressWater.getText()));
                int max = Integer.parseInt(String.valueOf(maxWater.getText()));
                int inf = Integer.parseInt(String.valueOf(infWater.getText()));
                int circle=Math.min(((progress+50)*100)/max, 100);


                int sumWater = progress + 50;

                progressWater.setText(String.valueOf(sumWater));
                infWater.setText(String.valueOf(inf+50));
                waterWaveView.setProgress(circle);
                editWater.setText("");
                if((progress+50)>max) {
                    int color = getColor(R.color.red);
                    progressWater.setTextColor(color);
                }
                setWater(sumWater);
            }
        });
        btn100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = Integer.parseInt(String.valueOf(progressWater.getText()));
                int max = Integer.parseInt(String.valueOf(maxWater.getText()));
                int inf = Integer.parseInt(String.valueOf(infWater.getText()));
                int circle = Math.min(((progress+200)*100)/max, 100);


                int sumWater = progress + 200;

                progressWater.setText(String.valueOf(sumWater));
                infWater.setText(String.valueOf(inf+200));
                System.out.println(circle);
                waterWaveView.setProgress(circle);
                editWater.setText("");
                if((progress+200)>max) {
                    int color = getColor(R.color.red);
                    progressWater.setTextColor(color);
                }
                setWater(sumWater);
            }
        });
        btn200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int progress = Integer.parseInt(String.valueOf(progressWater.getText()));
                int max = Integer.parseInt(String.valueOf(maxWater.getText()));
                int inf = Integer.parseInt(String.valueOf(infWater.getText()));
                int circle=Math.min(((progress+500)*100)/max, 100);

                int sumWater = progress + 500;
                progressWater.setText(String.valueOf(sumWater));
                infWater.setText(String.valueOf(inf+500));
                waterWaveView.setProgress(circle);
                editWater.setText("");
                if((progress+500)>max) {
                    int color = getColor(R.color.red);
                    progressWater.setTextColor(color);
                }
                setWater(sumWater);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void calendarView() {
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

    private void openCalendarDialog(View view) {
        // Create a calendar instance with the current date
        Calendar calendar = Calendar.getInstance();

        // Get the current year, month, and day from the calendar
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and set the initial date to the current date
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                ControlWaterActivity.this,
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

    private void getDataAtDay(String formattedDate) {
        DocumentReference userStatRef = db.collection("statistic").document(user.getUid());
        DocumentReference dailyDataRef = userStatRef.collection("dailyData").document(formattedDate);

        dailyDataRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Map <String, Object> data = task.getResult().getData();
                    if(data != null){
                        if(data.containsKey("water")){
                            long l = (Long) data.get("water");
                            int x = (int) l * 100;
                            infWater.setText(Long.toString(l));
                            progressWater.setText(Long.toString(l));
                            maxWater.setText(Long.toString(maxWaterPerDate));
                            if(l > maxWaterPerDate){
                                progressWater.setTextColor(getColor(R.color.red));
                            }
                            else progressWater.setTextColor(getColor(R.color.blue_water));

                            if(maxWaterPerDate > 0){

                                waterWaveView.setProgress(x/maxWaterPerDate);
                            }
                            else{
                                waterWaveView.setProgress(x/9999);
                                Log.e(TAG, "not yet get the max calories from firebase");
                            }

                        }
                        else {
                            maxWater.setText(Long.toString(maxWaterPerDate));
                            waterWaveView.setProgress(0);
                            progressWater.setTextColor(getColor(R.color.blue_water));
                            progressWater.setText("0");
                            infWater.setText("0");
                        }

                    }
                    else {
                        maxWater.setText(Long.toString(maxWaterPerDate));
                        waterWaveView.setProgress(0);
                        progressWater.setTextColor(getColor(R.color.blue_water));
                        progressWater.setText("0");
                        infWater.setText("0");
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

    private void setWater(int water){
        DocumentReference userStatRef = db.collection("statistic").document(user.getUid());
        DocumentReference dailyDataRef = userStatRef.collection("dailyData").document(txvDay.getText().toString());
        Map<String, Object> data = new HashMap<>();
        data.put("water", water);
        dailyDataRef.set(data, SetOptions.mergeFields("water"))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }

    private void getUser() {
        db.collection("user").document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            u = task.getResult().toObject(User.class);
                            maxWaterPerDate = u.WaterCal();
                            maxWater.setText(String.valueOf(maxWaterPerDate));
                            LocalDate today  = LocalDate.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                            String formattedDate = today.format(formatter);
                            getDataAtDay(formattedDate);
                        }
                    }
                });
    }

    private void findView() {
        addWater=findViewById(R.id.btn_add_water);
        progressWater=findViewById(R.id.progress);
        maxWater=findViewById(R.id.max);
        infWater=findViewById(R.id.inf_water);
        editWater=findViewById(R.id.edit_water);
        waterWaveView = findViewById(R.id.waterWaveView);
        btnBackControlWater=findViewById(R.id.btn_back_control_water);
        btn50=findViewById(R.id.btn_50);
        btn100=findViewById(R.id.btn_100);
        btn200=findViewById(R.id.btn_200);
        cardView=findViewById(R.id.cv_notify);
        imvChart = findViewById(R.id.img_cart);
        txvDay = findViewById(R.id.txvDay);
        nextDay = findViewById(R.id.imvNextDay);
        previousDay = findViewById(R.id.imvPreviousDay);
        btn_minus50 = findViewById(R.id.btn_minus50);

        u = new User();
        maxWaterPerDate = 9999;
    }
}