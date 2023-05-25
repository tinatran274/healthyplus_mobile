package com.example.healthyplus.Adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthyplus.Activity.IngredientDetailActivity;
import com.example.healthyplus.Model.Ingredient;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    Context context;
    List <Ingredient> IngredientList;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public IngredientAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public IngredientAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_ingredient_view, parent,false);
        return new IngredientAdapter.IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientViewHolder holder, int position) {
        Ingredient ingre = IngredientList.get(position);

        holder.txvName.setText(ingre.getName());
        holder.txvKcal.setText(Double.toString(ingre.getCalo()));
        holder.txvWeight.setText(Double.toString(ingre.getWeight()));

        StorageReference imgRef = storage.getReferenceFromUrl(ingre.getImg());
        try {
            File localFile = File.createTempFile("images", "jpg");
            imgRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    holder.img.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e(TAG, "Error getting ingredient image");
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, IngredientDetailActivity.class);
                intent.putExtra("ingredient", ingre);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(IngredientList != null)
            return IngredientList.size();
        return 0;
    }

    public void setIngreList(List<Ingredient> list) {
        IngredientList = list;
        notifyDataSetChanged();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder{
        private TextView txvName, txvKcal, txvWeight;
        ImageView img;
        CardView cardView;
        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            txvName = itemView.findViewById(R.id.txvName);
            txvKcal = itemView.findViewById(R.id.txvKcal);
            txvWeight = itemView.findViewById(R.id.txvWeight);
            img = itemView.findViewById(R.id.imvIngre);
            cardView = itemView.findViewById(R.id.cvIngredient);
        }
    }
}
