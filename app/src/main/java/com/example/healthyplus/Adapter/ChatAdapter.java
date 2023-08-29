package com.example.healthyplus.Adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthyplus.Activity.DetailAlarmActivity;

import com.example.healthyplus.Model.Alarm;

import com.example.healthyplus.Model.Chat;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>  {

    private Context context;
    List<Chat> chatList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public ChatAdapter(Context context)
    {
        this.context=context;
    }

    public void setData (List<Chat> list)
    {
        this.chatList=list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_chat_item, parent,false);
        return new ChatAdapter.ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = this.chatList.get(position);
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str1 = dateFormat.format(currentDate).substring(0,10);
        String str2 = chat.getTime().substring(0,10);
        if (str1.equals(str2)) {
            holder.leftTime.setText("Hôm nay" + chat.getTime().substring(10, 16));
            holder.rightTime.setText("Hôm nay" + chat.getTime().substring(10, 16));
        }
        else {
            holder.leftTime.setText(chat.getTime().substring(0,16));
            holder.rightTime.setText(chat.getTime().substring(0,16));
        }

        if(chat.getName().equals("user")) {
            holder.leftChatView.setVisibility(View.GONE);
            holder.rightChatView.setVisibility(View.VISIBLE);
            holder.rightTextView.setText(chat.getContent());
        }
        else if(chat.getName().equals("expert")){
            holder.rightChatView.setVisibility(View.GONE);
            holder.leftChatView.setVisibility(View.VISIBLE);
            holder.leftTextView.setText(chat.getContent());
        }


//        holder.cs.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if(chatList!=null) {
            return chatList.size();
        }
        return 0;
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder{

        LinearLayout leftChatView,rightChatView;
        TextView leftTextView,rightTextView, leftTime, rightTime;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChatView  = itemView.findViewById(R.id.left_chat_view);
            rightChatView = itemView.findViewById(R.id.right_chat_view);
            leftTextView = itemView.findViewById(R.id.left_chat_text_view);
            rightTextView = itemView.findViewById(R.id.right_chat_text_view);
            leftTime = itemView.findViewById(R.id.txv_time_left);
            rightTime = itemView.findViewById(R.id.txv_time_right);
        }
    }
}
