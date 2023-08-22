package com.example.healthyplus.Adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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
import java.util.List;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_chat_view, parent,false);
        return new ChatAdapter.ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Chat chat = this.chatList.get(position);
        holder.content.setText(chat.getContent());
        holder.time.setText(chat.getTime());



        if(chat.getSender()==0){
            holder.content.setBackgroundResource(R.drawable.border_white_chat);
            holder.content.setTextColor(Color.parseColor("#404040"));

            ConstraintSet constraintSet1 = new ConstraintSet();
            constraintSet1.clone(holder.cs);
            constraintSet1.connect(R.id.txv_time, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);
            constraintSet1.applyTo(holder.cs);

            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(holder.cs);
            constraintSet.connect(R.id.txv_content, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);
            constraintSet.applyTo(holder.cs);
        }

        holder.cs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public int getItemCount() {
        if(chatList!=null) {
            return chatList.size();
        }
        return 0;
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder{

        private ConstraintLayout cs;
        private TextView content, time;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            cs=itemView.findViewById(R.id.cs_layout);
            content=itemView.findViewById(R.id.txv_content);
            time=itemView.findViewById(R.id.txv_time);
        }
    }
}
