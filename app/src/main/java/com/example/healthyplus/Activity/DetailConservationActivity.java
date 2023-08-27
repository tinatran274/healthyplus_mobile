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
import android.widget.TextView;

import com.example.healthyplus.Adapter.AlarmAdapter;
import com.example.healthyplus.Adapter.ChatAdapter;
import com.example.healthyplus.Model.Alarm;
import com.example.healthyplus.Model.Chat;
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

    TextView txvQ0, txvQ1, txvQ2;

    List<Chat> list = new ArrayList<>();
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
        txvQ0 = findViewById(R.id.txv_q0);
        txvQ1 = findViewById(R.id.txv_q1);
//        txvQ2 = findViewById(R.id.txv_q2);

        chatAdapter = new ChatAdapter(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this, 1);
        rec.setLayoutManager(gridLayoutManager);
        chatAdapter.setData(list);
        rec.setAdapter(chatAdapter);

        btnBack.setText(conservation.getEid());
        Map<String, Object> chatData = conservation.getChatData();
        for (String key : chatData.keySet()) {
            String value = String.valueOf(chatData.get(key));

            db.collection("message").document(value)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Map<String, Object> data = new HashMap<>();
                                    data = document.getData();
                                    Chat c = new Chat(String.valueOf(data.get("content")), key,
                                            Integer.parseInt(String.valueOf((Long)data.get("sender"))),
                                            String.valueOf(data.get("name")));
                                    list.add(c);
                                    sortByTime(list);
                                    chatAdapter.notifyDataSetChanged();
                                    Log.d(TAG, list.toString());
                                    gridLayoutManager.scrollToPosition(chatAdapter.getItemCount() - 1);

                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txvQ0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chat c = new Chat("Xin chào chuyên gia", getCurrentDateTime(), 0, "user");
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
                gridLayoutManager.scrollToPosition(chatAdapter.getItemCount() - 1);
            }
        });

        txvQ1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.collection("expert").document(conservation.getEid())
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    Expert e = document.toObject(Expert.class);

                                    Chat c = new Chat(e.toString(), getCurrentDateTime(), 1, "expert");
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
                                    gridLayoutManager.scrollToPosition(chatAdapter.getItemCount() - 1);

                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
            }
        });

        imbSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Chat c = new Chat(String.valueOf(edtMessage.getText()), getCurrentDateTime(), 0, "user");
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
                gridLayoutManager.scrollToPosition(chatAdapter.getItemCount() - 1);
            }
        });

    }

    private String getCurrentDateTime() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTimeString = dateFormat.format(currentDate);
        return currentDateTimeString;
    }
    public void sortByTime(List<Chat> chats) {
        Collections.sort(chats, new Comparator<Chat>() {
            @Override
            public int compare(Chat chat1, Chat chat2) {
                return chat1.getTime().compareTo(chat2.getTime());
            }
        });
    }

}