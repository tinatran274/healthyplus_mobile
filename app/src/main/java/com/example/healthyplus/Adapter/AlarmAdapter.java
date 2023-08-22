package com.example.healthyplus.Adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthyplus.Activity.DetailAlarmActivity;

import com.example.healthyplus.Model.Alarm;

import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.Serializable;
import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>  {

    public interface OnDatasetChangeListener {
        void onDatasetChanged();
    }
    private Context context;
    List<Alarm> alarmList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public AlarmAdapter(Context context)
    {
        this.context=context;
    }
    private OnDatasetChangeListener datasetChangeListener;
    public void setOnDatasetChangeListener(OnDatasetChangeListener listener) {
        this.datasetChangeListener = listener;
    }
    public void setData (List<Alarm> list)
    {
        this.alarmList=list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_alarm_view, parent,false);
        return new AlarmAdapter.AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        Alarm alarm = this.alarmList.get(position);
        holder.hour.setText(alarm.getHour());
        holder.minute.setText(alarm.getMinute());
        holder.switchStatus.setChecked(alarm.isStatus());
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
        holder.day.setText(days);

        holder.switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                db.collection("alarm").document(alarm.getId())
                        .update("status", b)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error updating document", e);
                            }
                        });
            }
        });
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailAlarmActivity.class);
                Bundle bundle =new Bundle();
                bundle.putSerializable("object_alarm", (Serializable) alarm);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(alarmList!=null) {
            return alarmList.size();
        }
        return 0;
    }

    public class AlarmViewHolder extends RecyclerView.ViewHolder{

        private CardView cv;
        private TextView hour, minute, day;
        private Switch switchStatus;
        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            cv=itemView.findViewById(R.id.cv_layout);
            hour=itemView.findViewById(R.id.txv_hour);
            minute=itemView.findViewById(R.id.txv_minute);
            day=itemView.findViewById(R.id.txv_day);
            switchStatus=itemView.findViewById(R.id.sw_status);
        }
    }
}
