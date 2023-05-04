package com.example.healthyplus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthyplus.Model.Dish;
import com.example.healthyplus.Model.Product;
import com.example.healthyplus.R;

import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder>{


    private Context context;
    List<Dish> DishList;

    @NonNull
    @Override
    public DishAdapter.DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_dish_view, parent,false);
        return new DishAdapter.DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishAdapter.DishViewHolder holder, int position) {

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
