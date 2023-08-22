package com.example.healthyplus.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.healthyplus.Adapter.AlarmAdapter;
import com.example.healthyplus.Adapter.ChatAdapter;
import com.example.healthyplus.Model.Alarm;
import com.example.healthyplus.Model.Chat;
import com.example.healthyplus.Model.Conservation;
import com.example.healthyplus.Model.Dish;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailConservationActivity extends AppCompatActivity {

    Button btnBack;
    EditText edtMessage;
    ImageButton imbSend;
    RecyclerView rec;

    List<Chat> list = new ArrayList<>();

    List<Map<String, Object>> sortedList = new ArrayList<>();
    ChatAdapter chatAdapter;
    FirebaseFirestore db;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_conservation);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        Bundle bundle = getIntent().getExtras();
        if (bundle==null)
            return;
        Conservation conservation = (Conservation) bundle.get("object_conservation");

        btnBack = findViewById(R.id.btn_back);
        rec = findViewById(R.id.rec);
        edtMessage = findViewById(R.id.edt_message);
        imbSend = findViewById(R.id.imb_send);
        chatAdapter = new ChatAdapter(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this, 1);
        rec.setLayoutManager(gridLayoutManager);
        chatAdapter.setData(list);
        rec.setAdapter(chatAdapter);

        btnBack.setText(conservation.getEid());
        Map<String, Object> chatData = conservation.getChatData();
        for (String key : chatData.keySet()) {
            String value = String.valueOf(chatData.get(key));
            Map<String, Object> map = new HashMap<>();
            map.put(key, value);
            sortedList.add(map);
        }
        sortMapListByTimestamp(sortedList);
        Collections.reverse(sortedList);

        for (Map<String, Object> map : sortedList) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Log.e(TAG, entry.getKey()+ " "+String.valueOf(entry.getValue()));
                db.collection("message").document(String.valueOf(entry.getValue()))
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> data = new HashMap<>();
                                data = document.getData();
                                Chat c = new Chat(String.valueOf(data.get("content")), entry.getKey(), Integer.parseInt(String.valueOf((Long)data.get("sender"))));
                                list.add(c);
                                chatAdapter.notifyDataSetChanged();
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

            }
        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        imbSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chat c = new Chat(String.valueOf(edtMessage.getText()), getCurrentDateTime(), 0);
//                Map<String, Object> data = new HashMap<>();
//                data.put("content", String.valueOf(edtMessage.getText()));
//                data.put("sender", 0); //1:usersend 0:expertsend

                db.collection("message")
                        .add(c)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                conservation.addChatData(getCurrentDateTime(), documentReference.getId());
                                db.collection("conservation").document(conservation.getId()).set(conservation);
                                edtMessage.setText("");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
                list.add(c);
                chatAdapter.notifyDataSetChanged();
            }
        });

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