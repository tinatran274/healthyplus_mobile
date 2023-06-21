package com.example.healthyplus.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.MotionEffect;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.healthyplus.Adapter.AlarmAdapter;
import com.example.healthyplus.Adapter.ProductAdapter;
import com.example.healthyplus.Model.Alarm;
import com.example.healthyplus.Model.Ingredient;
import com.example.healthyplus.Model.Product;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlarmActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AlarmAdapter alarmAdapter;
    Button btnBack;
    ImageButton btnAddAlarm;
    List<Alarm> list = new ArrayList<>();

    List<Alarm> activeList =new ArrayList<>();
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    FirebaseFirestore db;
    int today=0;

    private Calendar calendar;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ArrayList<Boolean> day = new ArrayList<Boolean>();
        for(int i=0; i<7;i++){
            day.add(true);
        }
        db = FirebaseFirestore.getInstance();
        recyclerView=findViewById(R.id.rec);
        btnBack = findViewById(R.id.btn_back);
        btnAddAlarm=findViewById(R.id.ibm_add_alarm);

        alarmAdapter=new AlarmAdapter(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        alarmAdapter.setData(list);
        recyclerView.setAdapter(alarmAdapter);

        Log.e(TAG,  " List: => " + list.toString());
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(intent);
                alarmAdapter.notifyDataSetChanged();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        db.collection("alarm")
                .whereEqualTo("userId", currentUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Alarm a = document.toObject(Alarm.class);
                                list.add(a);
                                Log.e(TAG, document.getId() + " => " + document.getData());

                            }
                            alarmAdapter.notifyDataSetChanged();

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        for (int i = 0; i < list.size(); i++) {
                            if(list.get(i).isStatus()==true)
                                activeList.add(list.get(i));
                        }
                        for (int i = 0; i < activeList.size(); i++) {
                            Log.e(TAG, "uong nuoc hom nay " + activeList.get(i).getHour() + ":" + activeList.get(i).getMinute());
                        }

                    }

                });
        //Log.e(TAG,  " List: => " + list.toString());
        calendar=Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        Log.e(MotionEffect.TAG, "Date: "+String.valueOf(dayOfWeek));
        switch (dayOfWeek){
            case 2: case 3: case 4: case 5: case 6: case 7:
                today=dayOfWeek-2;
                break;
            case 1:
                today=6;
        }
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).isStatus()==true)
                activeList.add(list.get(i));
        }
        for (int i = 0;i < activeList.size();i++){
            if(activeList.get(i).getDay().get(today)==true){
                Log.e(TAG, "uong nuoc hom nay "+activeList.get(i).getHour()+":"+activeList.get(i).getMinute());
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(activeList.get(i).getHour()));
                calendar.set(Calendar.MINUTE, Integer.parseInt(activeList.get(i).getMinute()));
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND,0);
                setAlarm();
            }
        }

    }

    private void setAlarm() {
        alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationReceiver.class);

        pendingIntent= PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,pendingIntent);
        Toast.makeText(getApplication(), "Thêm thành công", Toast.LENGTH_SHORT).show();
    }
    private void createNotificationChanel() {

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name ="Nhắc nhỏ uống nước";
            String desc="HealthyPlus nhắc bạn đã đến giờ uống nước rồi !!";
            int importane= NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel=new NotificationChannel("healthyplus", name, importane);
            channel.setDescription(desc);
            channel.enableVibration(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);

        }

    }
}