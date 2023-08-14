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
import android.widget.TextView;

import com.example.healthyplus.Adapter.DishAdapter;
import com.example.healthyplus.Adapter.LikedDishAdapter;
import com.example.healthyplus.Model.Dish;
import com.example.healthyplus.Model.User;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SuggestMenuActivity extends AppCompatActivity {

    Button btnBack;
    TextView txvAim, txvBe, txvTDEE, txvTotal;
    RecyclerView rec;
    DishAdapter adapter = new DishAdapter(this);
    List<Dish> recommendMenu = new ArrayList<>();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Dish> favList = new ArrayList<>();
    List<Dish> allList = new ArrayList<>();
    List<String> favID = new ArrayList<>();
    User p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_menu);

        txvAim = findViewById(R.id.txv_aim);
        txvBe = findViewById(R.id.txv_be);
        txvTDEE = findViewById(R.id.txv_tdee);
        txvTotal = findViewById(R.id.txv_total);
        btnBack = findViewById(R.id.btn_back_dish);
        rec = findViewById(R.id.rec);
        rec.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        showFavDish();




        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Log.e(TAG, favList.toString());
    }

    private void showFavDish() {
        db.collection("favorite").document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            Map<String, Object> data = doc.getData();
                            if(data != null && data.containsKey("listID")) {
                                favID = (List<String>) data.get("listID");
                                getFavoriteDish(favID);
                            }
                        }
                    }
                });
    }
    private void getFavoriteDish(List<String> favID){

        db.collection("dish").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Dish dish = document.toObject(Dish.class);
                        if(favID.contains(dish.getId())) favList.add(dish); // if the dish in favorite then display it
                        allList.add(dish);
                    }
                    getUserInfo(favList, allList);
                }
                else {
                    Log.e(TAG, "Cannot get data", task.getException());
                }
            }
        });
    }
    private void getUserInfo(List<Dish> favoLi, List<Dish> allLi){

        db.collection("user").document(user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot != null){
                                p = documentSnapshot.toObject(User.class);
                                menu(favoLi, allLi, p);
                            }
                        }
                    }
                });
    }

    private void menu (List<Dish> favoList, List<Dish> allList, User u){

        List<Dish> increaseWeightMain = new ArrayList<>();
        List<Dish> decreaseWeightMain = new ArrayList<>();
        int totalKcal = 0;
        int tdee = u.TTDECal();
        txvTDEE.setText(String.valueOf(tdee));
        int mainKcal = (tdee*25)/100;

        for (Dish item : favoList) {
            if (item.getCalo()>mainKcal)
                increaseWeightMain.add(item);
            else decreaseWeightMain.add(item);
        }
        for (Dish item : allList) {
            if (item.getCalo()>mainKcal)
                increaseWeightMain.add(item);
            else decreaseWeightMain.add(item);
        }
        switch (p.getAim()) {
            case 2:
                txvAim.setText("Tăng cân");
                txvBe.setText("nhiều hơn");
                for (int i = 0; i < 3; i++) {
                    Random random = new Random();
                    int randomIndex = random.nextInt(increaseWeightMain.size()); // Generates a random index within the list size
                    Dish randomItem = increaseWeightMain.get(randomIndex); // Get the random element from the list
                    recommendMenu.add(randomItem);
                    totalKcal +=randomItem.getCalo();
                    boolean removed = increaseWeightMain.remove(randomItem); // Removes the object from the list

                }
                break;
            case 1:
                txvAim.setText("Giữ cân");
                txvBe.setText("khoảng");
                for (int i = 0; i < 3; i++) {
                    Random random = new Random();
                    int randomIndex = random.nextInt(decreaseWeightMain.size()); // Generates a random index within the list size
                    Dish randomItem = decreaseWeightMain.get(randomIndex); // Get the random element from the list
                    recommendMenu.add(randomItem);
                    totalKcal +=randomItem.getCalo();
                    boolean removed = decreaseWeightMain.remove(randomItem); // Removes the object from the list
                }
                break;
            case 0:
                txvAim.setText("Giảm cân");
                txvBe.setText("ít hơn");
                for (int i = 0; i < 3; i++) {
                    Random random = new Random();
                    int randomIndex = random.nextInt(decreaseWeightMain.size()); // Generates a random index within the list size
                    Dish randomItem = decreaseWeightMain.get(randomIndex); // Get the random element from the list
                    recommendMenu.add(randomItem);
                    totalKcal +=randomItem.getCalo();
                    boolean removed = decreaseWeightMain.remove(randomItem); // Removes the object from the list
                }
                break;
        }

        adapter.setDishList(recommendMenu);
        rec.setAdapter(adapter);
        txvTotal.setText(String.valueOf(totalKcal));


    }
}