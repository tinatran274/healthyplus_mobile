package com.example.healthyplus.Adapter;

import static android.content.ContentValues.TAG;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthyplus.Activity.DetailConservationActivity;
import com.example.healthyplus.Model.Conservation;
import com.example.healthyplus.Model.Dish;
import com.example.healthyplus.Model.Expert;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpertAdapter extends RecyclerView.Adapter<ExpertAdapter.ExpertViewHolder>{
    private Context context;
    List<Expert> expertList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    public ExpertAdapter(Context context)
    {
        this.context=context;
    }

    public void setData (List<Expert> list)
    {
        this.expertList=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExpertAdapter.ExpertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_expert_view, parent,false);
        return new ExpertAdapter.ExpertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpertAdapter.ExpertViewHolder holder, int position) {
        Expert expert = this.expertList.get(position);
        holder.name.setText(expert.getName());
        holder.age.setText(String.valueOf(expert.getAge()));

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, DetailExpertActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("object_expert", (Serializable) expert);
//                intent.putExtras(bundle);
//                context.startActivity(intent);

                Map<String, Object> conservation = new HashMap<>();
                conservation.put("id", currentUser.getUid()+expert.getId());
                conservation.put("uid", currentUser.getUid());
                conservation.put("eid", expert.getId());
                Map<String, Object> chatData = new HashMap<>();
                Conservation addData = new Conservation(currentUser.getUid()+expert.getId(),
                        currentUser.getUid(), expert.getId(), chatData);

                db.collection("conservation").document(currentUser.getUid()+expert.getId())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Conservation c = document.toObject(Conservation.class);
                                Intent intent = new Intent(context, DetailConservationActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("object_conservation", (Serializable) c);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
//                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                db.collection("conservation").document(currentUser.getUid()+expert.getId())
                                        .set(addData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                                Intent intent = new Intent(context, DetailConservationActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("object_conservation", (Serializable) addData);
                                                intent.putExtras(bundle);
                                                context.startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error writing document", e);
                                            }
                                        });
//                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

//                List<Map<String, Object>> mapList = new ArrayList<>();
//                Map<String, Object> map1 = new HashMap<>();
//                map1.put("2023-08-21 10:30:00", "456");
//                mapList.add(map1);
//
//                Map<String, Object> map2 = new HashMap<>();
//                map2.put("2023-08-21 23:15:22", "789");
//                mapList.add(map2);
//
//                Map<String, Object> map3 = new HashMap<>();
//                map3.put("2023-08-20 01:15:22", "123");
//                mapList.add(map3);
//
//                Log.e(TAG, mapList.toString());
//                sortMapListByTimestamp(mapList);
//                Log.e(TAG, mapList.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        if(expertList!=null) {
            return expertList.size();
        }
        return 0;
    }
    public class ExpertViewHolder extends RecyclerView.ViewHolder{

        private CardView cv;
        private TextView name, age;
        public ExpertViewHolder(@NonNull View itemView) {
            super(itemView);
            cv=itemView.findViewById(R.id.cv_layout);
            name=itemView.findViewById(R.id.txv_name);
            age=itemView.findViewById(R.id.txv_age);
        }
    }

    private String getCurrentDateTime() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTimeString = dateFormat.format(currentDate);
        return currentDateTimeString;
    }

    private void sortMapListByTimestamp (List<Map<String, Object>> mapList) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Collections.sort(mapList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> map1, Map<String, Object> map2) {
                String timestamp1 = map1.keySet().iterator().next();
                String timestamp2 = map2.keySet().iterator().next();

                try {
                    Date date1 = dateFormat.parse(timestamp1);
                    Date date2 = dateFormat.parse(timestamp2);
                    return date1.compareTo(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0; // Handle the error case
                }
            }
        });

        // Now the mapList is sorted by timestamps
    }


}
