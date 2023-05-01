package com.example.healthyplus.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.healthyplus.Model.User;
import com.example.healthyplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    TextView id, name, aim, maxCalories, maxWater, bmi, ttde, age, gender, height, weight, exerciseFrequency;
    Button btnUpdate, btnBackUser;
    FirebaseFirestore db;
    User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        id = findViewById(R.id.txv_id);
        name = findViewById(R.id.txv_name);
        aim = findViewById(R.id.txv_aim);
        maxCalories = findViewById(R.id.txv_max_calories);
        maxWater = findViewById(R.id.txv_max_water);
        bmi = findViewById(R.id.txv_bmi);
        ttde = findViewById(R.id.txv_ttde);
        age = findViewById(R.id.txv_age);
        gender = findViewById(R.id.txv_gender);
        height = findViewById(R.id.txv_height);
        weight = findViewById(R.id.txv_weight);
        exerciseFrequency = findViewById(R.id.txv_exercise);
        btnUpdate = findViewById(R.id.btn_update);
        btnBackUser = findViewById(R.id.btn_back_user);

        setInfoUser();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogUpdate();
            }
        });
        btnBackUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setInfoUser() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("user").document(currentUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot != null){
                                User p = documentSnapshot.toObject(User.class);
                                name.setText(p.getName());
                                id.setText(String.valueOf(p.getId()));
                                age.setText(String.valueOf(p.getAge()));
                                height.setText(String.valueOf(p.getHeight()));
                                weight.setText(String.valueOf(p.getWeight()));
                                bmi.setText(String.valueOf(p.BMICal()));
                                ttde.setText(String.valueOf(p.TTDECal()));
                                maxWater.setText(String.valueOf(Math.round(p.TTDECal() * 1.2)));
                                switch (p.getAim()) {
                                    case 0:
                                        aim.setText("Tăng cân");
                                        maxCalories.setText(String.valueOf(Math.round(p.TTDECal() * 1.1)));
                                        break;
                                    case 1:
                                        aim.setText("Giữ cân");
                                        maxCalories.setText(String.valueOf(Math.round(p.TTDECal() * 1)));
                                        break;
                                    case 2:
                                        aim.setText("Giảm cân");
                                        maxCalories.setText(String.valueOf(Math.round(p.TTDECal() * 0.9)));
                                        break;
                                }
                                switch (p.getGender()) {
                                    case 0:
                                        gender.setText("Nữ");
                                        break;
                                    case 1:
                                        gender.setText("Nam");
                                        break;
                                }
                                switch (p.getExerciseFrequency()) {
                                    case 0:
                                        exerciseFrequency.setText("Không vận động");
                                        break;
                                    case 1:
                                        exerciseFrequency.setText("Vận động nhẹ (1-3 ngày/tuần)");
                                        break;
                                    case 2:
                                        exerciseFrequency.setText("Vận động vừa (4-5 ngày/tuần)");
                                        break;
                                    case 3:
                                        exerciseFrequency.setText("Vận động nặng (6-7 ngày/tuần)");
                                        break;
                                }
                            }
                        }
                    }
                });
    }

    private void showDialogUpdate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_update_info_user, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.show();

        EditText editAge = view.findViewById(R.id.edit_age);
        RadioGroup rdgGender = view.findViewById(R.id.rdg_gender);
        RadioButton rdFemale = view.findViewById(R.id.rd_female);
        RadioButton rdMale = view.findViewById(R.id.rd_male);
        EditText editHeight = view.findViewById(R.id.edit_height);
        EditText editWeight = view.findViewById(R.id.edit_weight);
        RadioGroup rdActivityFrequency = view.findViewById(R.id.rdg_exercise);
        RadioButton rdKhong = view.findViewById(R.id.rd_khong);
        RadioButton rdNhe = view.findViewById(R.id.rd_nhe);
        RadioButton rdVua = view.findViewById(R.id.rd_vua);
        RadioButton rdNang = view.findViewById(R.id.rd_nang);
        Button btnReup = view.findViewById(R.id.btn_reup);


    }

    public void showAimPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.aimm_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        User a;
        switch (item.getItemId()) {
            case R.id.i1:

                return true;
            case R.id.i2:

                return true;
            case R.id.i3:

                return true;
            default:
                return false;
        }
    }


}