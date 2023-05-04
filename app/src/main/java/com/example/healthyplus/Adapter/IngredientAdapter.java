package com.example.healthyplus.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthyplus.Model.Ingredient;
import com.example.healthyplus.R;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    Context context;
    List <Ingredient> IngredientList;
    @NonNull
    @Override
    public IngredientAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_ingredient_view, parent,false);
        return new IngredientAdapter.IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if(IngredientList != null)
            return IngredientList.size();
        return 0;
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder{
        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
