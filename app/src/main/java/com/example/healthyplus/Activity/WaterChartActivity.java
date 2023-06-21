package com.example.healthyplus.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.healthyplus.Model.User;
import com.example.healthyplus.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WaterChartActivity extends AppCompatActivity {
    CardView cv1Calo, cv3Calo;
    LinearLayout cv2Calo;
    LineChart lineChart;
    Button btnBack;
    FirebaseFirestore db;
    DocumentReference userStatRef;
    CollectionReference collectionRef;
    List <String> date_label = new ArrayList<>();
    List <Long> water = new ArrayList<>();
    User u;
    int waterCal;
    int animationDuration = 1000;
    TextView txvTotalDate, txvCurrentStreak, txvLongestStreak
            , txvBigWater, txvSmallWater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_chart);

        findView();

        backWater();

        getDataStringList(list -> {
            populateChart();
            setAnimation();
            showLongestStreak();
            showTotalDate_CurrentStreak();
            showTotal();
        });
    }

    private void setAnimation() {


        ObjectAnimator animator1 = ObjectAnimator.ofFloat(cv1Calo, "translationX", -cv1Calo.getWidth(), 0);
        animator1.setDuration(animationDuration);
        animator1.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(cv2Calo, "translationX", -cv2Calo.getWidth(), 0);
        animator2.setDuration(animationDuration);
        animator2.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator animator3 = ObjectAnimator.ofFloat(cv3Calo, "translationX", -cv3Calo.getWidth(), 0);
        animator3.setDuration(animationDuration);
        animator3.setInterpolator(new AccelerateDecelerateInterpolator());

        animator1.start();

        new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> {
            animator2.start();
        }, 200);

        new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> {
            animator3.start();
        }, 400);

    }

    private void showTotal() {
        int cntBig = 0, cntSmall = 0;
        for(long i: water){
            if(i > waterCal - 500 && i < waterCal + 500)
                cntSmall ++;
            else
                cntBig ++;
        }
        txvBigWater.setText(Long.toString(cntBig));
        txvSmallWater.setText(Long.toString(cntSmall));
    }

    private void showTotalDate_CurrentStreak() {
        int totalDate = date_label.size();
        txvTotalDate.setText(Integer.toString(totalDate));

        int currentStreak = getCurrentStreak();
        txvCurrentStreak.setText(Integer.toString(currentStreak));
    }

    private int getCurrentStreak() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        Collections.sort(date_label, (date1, date2) -> {
            LocalDate localDate1 = LocalDate.parse(date1, formatter);
            LocalDate localDate2 = LocalDate.parse(date2, formatter);
            return localDate1.compareTo(localDate2);
        });

        LocalDate previousDate = LocalDate.now();
        String preDate = formatter.format(previousDate);
        int index = -1;
        if(date_label.contains(preDate)) index = date_label.indexOf(preDate);

        int length = 0;

        if(index == -1){
            return 0;
        }

        for (int i = index - 1; i >= 0; i--) {
            LocalDate entryDate = LocalDate.parse(date_label.get(i), formatter);
            if(previousDate.minusDays(1).equals(entryDate)){
                length++;
                previousDate = entryDate;
            }
        }

        return length + 1;
    }

    private void showLongestStreak() {
        List<Date> dates = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        // Convert strings to date objects
        for (String i : date_label) {
            try {
                Date dateObject = dateFormat.parse(i);
                dates.add(dateObject);
            } catch (ParseException e) {
                Log.e(TAG, e.toString());
                return;
            }
        }

        dates = findLongestStreak(dates);

        txvLongestStreak.setText(Integer.toString(dates.size()));

    }

    private List<Date> findLongestStreak(List<Date> dates) {
        int maxLength = 0;
        int startIndex = 0;
        int endIndex = 0;
        int currentStartIndex = 0;
        int currentLength = 1;

        for (int i = 1; i < dates.size(); i++) {
            long diff = dates.get(i).getTime() - dates.get(i - 1).getTime();
            if (diff == 86400000L) { // Difference of one day (24 * 60 * 60 * 1000 ms)
                currentLength++;
            } else {
                if (currentLength > maxLength) {
                    maxLength = currentLength;
                    startIndex = currentStartIndex;
                    endIndex = i - 1;
                }
                currentStartIndex = i;
                currentLength = 1;
            }
        }

        // Check if the last sequence is the longest
        if (currentLength > maxLength) {
            maxLength = currentLength;
            startIndex = currentStartIndex;
            endIndex = dates.size() - 1;
        }

        // Extract the longest sequence
        List<Date> longestSequence = new ArrayList<>();
        for (int i = startIndex; i <= endIndex; i++) {
            longestSequence.add(dates.get(i));
        }

        return longestSequence;
    }

    private void populateChart() {

        // Get the user's TDEE (replace with your actual TDEE calculation)



        List<Entry> dateEntries = new ArrayList<>();
        // set date line chart data
        for (int i = 0; i < water.size(); i++) {
            dateEntries.add(new Entry(i, water.get(i)));
        }

        LineDataSet dateData = new LineDataSet(dateEntries, "Thực tế");
        dateData.setAxisDependency(YAxis.AxisDependency.LEFT);
        dateData.setColor(ContextCompat.getColor(this, R.color.blue_water));
        dateData.setLineWidth(2f);
        dateData.setCircleColor(ContextCompat.getColor(this, R.color.blue_water));
        dateData.setCircleRadius(4f);
        dateData.setDrawCircleHole(false);

        // set tdee line chart data
        List <Entry> lineEntry =  new ArrayList<>();
        for(int i = 0; i < date_label.size(); i ++){
            lineEntry.add(new Entry(i, waterCal));
        }

        LineDataSet lineDataSet = new LineDataSet(lineEntry, "Cần thiết");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setColor(ContextCompat.getColor(this, R.color.orange));
        lineDataSet.setLineWidth(2f);
        lineDataSet.setCircleColor(ContextCompat.getColor(this, R.color.orange));
        lineDataSet.setCircleRadius(4f);
        lineDataSet.setDrawCircleHole(false);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(lineDataSet);
        dataSets.add(dateData);

        LineData lineData = new LineData(dataSets);

        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        List <String> labels = date_label.stream().map(date -> date.substring(0, 5)).collect(Collectors.toList());
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMaximum(waterCal*2f);


        lineChart.setDrawGridBackground(false);
        lineChart.setDrawBorders(false);
        lineChart.getLegend().setEnabled(true);
        lineChart.getAxisRight().setEnabled(false);
        Description lineDesc = lineChart.getDescription();
        lineDesc.setEnabled(false);
        lineChart.invalidate();

    }

    private void getDataStringList(WaterChartActivity.chartDataCallBack callBack) {
        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc: task.getResult().getDocuments()){
                        Map<String, Object> data = doc.getData();
                        if(data != null){
                            if(data.containsKey("water"))
                            {
                                date_label.add(doc.getId());
                                water.add((long) data.get("water"));
                                System.out.println(doc.getId());
                            }

                        }
                    }
                    callBack.onCallBack(date_label);
                }
            }
        });
    }

    private interface chartDataCallBack{
        void onCallBack(List<String> list);
    }

    private void backWater() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void findView() {

        u = (User) getIntent().getSerializableExtra("user");
        waterCal = u.WaterCal();
        db = FirebaseFirestore.getInstance();
        userStatRef = db.collection("statistic").document(u.getId());
        collectionRef = userStatRef.collection("dailyData");

        lineChart = findViewById(R.id.lineChart);
        btnBack = findViewById(R.id.btnBackChart);
        txvCurrentStreak = findViewById(R.id.txvCurrentStreak);
        txvTotalDate = findViewById(R.id.txvTotalDate);
        txvLongestStreak = findViewById(R.id.txvStreak);

        txvBigWater = findViewById(R.id.txvBigTdee);
        txvSmallWater = findViewById(R.id.txvSmallTdee);

        cv1Calo = findViewById(R.id.cv1Calo);
        cv2Calo = findViewById(R.id.cv2Calo);
        cv3Calo = findViewById(R.id.cv3Calo);

        cv1Calo.setTranslationX(-10000);
        cv2Calo.setTranslationX(-10000);
        cv3Calo.setTranslationX(-10000);
    }
}