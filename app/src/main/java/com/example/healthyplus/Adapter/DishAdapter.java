package com.example.healthyplus.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthyplus.Activity.DishDetailActivity;
import com.example.healthyplus.Model.Dish;
import com.example.healthyplus.Model.Product;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder>{


    private Context context;
    List<Dish> DishList;

    public DishAdapter(Context context) {
        this.context = context;
    }

    public void setDishList(List<Dish> dishList) {
        DishList = dishList;
    }

    @NonNull
    @Override
    public DishAdapter.DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_dish_view, parent,false);
        return new DishAdapter.DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishAdapter.DishViewHolder holder, int position) {
        Dish dish = this.DishList.get(position);

        holder.name.setText(dish.getName());
        holder.kcal.setText(Double.toString(dish.getCalo()));
        holder.protein.setText(Double.toString(dish.getProtein()));
        holder.fat.setText(Double.toString(dish.getFat()));
        holder.carbs.setText(Double.toString(dish.getCarb()));

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDishDetail(dish);
            }
        });

    }

    private void showDishDetail(Dish dish) {
        Intent intent = new Intent(context, DishDetailActivity.class);
        intent.putExtra("dish", dish);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if(DishList != null)
            return DishList.size();
        return 0;
    }

    public class DishViewHolder extends RecyclerView.ViewHolder{
        private CardView cv;
        private ImageView img;
        private TextView name, kcal, carbs, protein, fat;
        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.img);
            name=itemView.findViewById(R.id.txvName);
            kcal=itemView.findViewById(R.id.txvKcal);
            carbs=itemView.findViewById(R.id.txvCarbs);
            protein=itemView.findViewById(R.id.txvProtein);
            fat=itemView.findViewById(R.id.txvFat);
            cv=itemView.findViewById(R.id.cv);
        }
    }


}
