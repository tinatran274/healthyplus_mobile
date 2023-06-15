package com.example.healthyplus.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthyplus.Model.Alarm;
import com.example.healthyplus.Model.Product;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class DetailAlarmActivity extends AppCompatActivity {

    private Spinner spHour, spMinute;
    Button btnSet, btnBack, btnDelete;
    TextView txvHour, txvMinute, txvDay;
    ConstraintLayout constraintLayout;

    FirebaseFirestore db;
    int countDoc=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_alarm);

        Bundle bundle = getIntent().getExtras();
        if (bundle==null)
            return;
        Alarm alarm= (Alarm) bundle.get("object_alarm");

        db = FirebaseFirestore.getInstance();
        String hour[]={"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};
        String minute[]={"00","10","20","30","40","50","60"};
        ArrayList<Boolean> day = new ArrayList<Boolean>();
        for(int i=0; i<7;i++){
            day.add(alarm.getDay().get(i));
        }
        btnSet=findViewById(R.id.btn_set);
        btnBack=findViewById(R.id.btn_back);
        btnDelete=findViewById(R.id.btn_delete);
        txvHour=findViewById(R.id.txv_hour);
        txvMinute=findViewById(R.id.txv_minute);
        txvDay=findViewById(R.id.txv_day);
        spHour=findViewById(R.id.sp_hour);
        spMinute=findViewById(R.id.sp_minute);
        constraintLayout=findViewById(R.id.layout_repeat);

        txvHour.setText(alarm.getHour());
        txvMinute.setText(alarm.getMinute());
        String days="";
        if(alarm.getDay().get(0)==true){
            days=days+"T2 ";
        }
        if(alarm.getDay().get(1)==true){
            days=days+"T3 ";
        }
        if(alarm.getDay().get(2)==true){
            days=days+"T4 ";
        }
        if(alarm.getDay().get(3)==true){
            days=days+"T5 ";
        }
        if(alarm.getDay().get(4)==true){
            days=days+"T6 ";
        }
        if(alarm.getDay().get(5)==true){
            days=days+"T7 ";
        }
        if(alarm.getDay().get(6)==true){
            days=days+"CN ";
        }
        txvDay.setText(days);

        ArrayAdapter adapterHour = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hour);
        adapterHour.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spHour.setAdapter(adapterHour);

        ArrayAdapter adapterMinute = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, minute);
        adapterMinute.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spMinute.setAdapter(adapterMinute);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        for(int h=0;h<24;h++){
            if((h+1)==Integer.valueOf(alarm.getHour())){
                spHour.setSelection(h);
                break;
            }
        }
        for(int m=0;m<7;m++){
            if((m*10)==Integer.valueOf(alarm.getMinute())){
                spMinute.setSelection(m);
                break;
            }
        }
        spHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplication(), "Bạn đã chọn:"+hour[i], Toast.LENGTH_LONG).show();
                txvHour.setText(hour[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                txvHour.setText(alarm.getHour());
            }
        });
        spMinute.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplication(), "Bạn đã chọn :"+minute[i], Toast.LENGTH_LONG).show();
                txvMinute.setText(minute[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                txvMinute.setText(alarm.getMinute());
            }
        });

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailAlarmActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View viewDay = inflater.inflate(R.layout.dialog_choose_day, null);
                builder.setView(viewDay);
                Dialog dialog = builder.create();
                dialog.show();

                CheckBox cbMon= viewDay.findViewById(R.id.cb_mon);
                CheckBox cbTue= viewDay.findViewById(R.id.cb_tue);
                CheckBox cbWed= viewDay.findViewById(R.id.cb_wed);
                CheckBox cbThu= viewDay.findViewById(R.id.cb_thu);
                CheckBox cbFri= viewDay.findViewById(R.id.cb_fri);
                CheckBox cbSat= viewDay.findViewById(R.id.cb_sat);
                CheckBox cbSun= viewDay.findViewById(R.id.cb_sun);

                Button btnCancel = viewDay.findViewById(R.id.btn_cancel);
                Button btnConfirm = viewDay.findViewById(R.id.btn_confirm);

                cbMon.setChecked(day.get(0));
                cbTue.setChecked(day.get(1));
                cbWed.setChecked(day.get(2));
                cbThu.setChecked(day.get(3));
                cbFri.setChecked(day.get(4));
                cbSat.setChecked(day.get(5));
                cbSun.setChecked(day.get(6));

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        day.set(0, cbMon.isChecked());
                        day.set(1, cbTue.isChecked());
                        day.set(2, cbWed.isChecked());
                        day.set(3, cbThu.isChecked());
                        day.set(4, cbFri.isChecked());
                        day.set(5, cbSat.isChecked());
                        day.set(6, cbSun.isChecked());

                        String dayChange="";
                        if(day.get(0)==true){
                            dayChange=dayChange+"T2 ";
                        }
                        if(day.get(1)==true){
                            dayChange=dayChange+"T3 ";
                        }
                        if(day.get(2)==true){
                            dayChange=dayChange+"T4 ";
                        }
                        if(day.get(3)==true){
                            dayChange=dayChange+"T5 ";
                        }
                        if(day.get(4)==true){
                            dayChange=dayChange+"T6 ";
                        }
                        if(day.get(5)==true){
                            dayChange=dayChange+"T7 ";
                        }
                        if(day.get(6)==true){
                            dayChange=dayChange+"CN ";
                        }
                        txvDay.setText(dayChange);
                        dialog.dismiss();
                    }
                });
            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alarm alarmChange = new Alarm((String)alarm.getId(), (String)alarm.getUserId(), (String) txvHour.getText(), (String) txvMinute.getText(),
                        true, day);
                db.collection("alarm").document(alarm.getId()).set(alarmChange);
                finish();

            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("alarm").document(alarm.getId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });
                finish();

            }
        });
    }
}