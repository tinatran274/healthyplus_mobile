package com.example.healthyplus.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WaterChartActivity extends AppCompatActivity {

    LineChart lineChart;
    Button btnBack;
    FirebaseFirestore db;
    DocumentReference userStatRef;
    CollectionReference collectionRef;
    List <String> date_label = new ArrayList<>();
    List <Long> water = new ArrayList<>();
    User u;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_chart);

        findView();

        backWater();

        getDataStringList(list -> {
            populateChart();
        });
    }

    private void populateChart() {

        // Get the user's TDEE (replace with your actual TDEE calculation)
        int waterCal = u.WaterCal();


        List<Entry> dateEntries = new ArrayList<>();
        // set date line chart data
        for (int i = 0; i < water.size(); i++) {
            dateEntries.add(new Entry(i, water.get(i)));
        }

        LineDataSet dateData = new LineDataSet(dateEntries, "Water");
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

        LineDataSet lineDataSet = new LineDataSet(lineEntry, "Water intake");
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
        db = FirebaseFirestore.getInstance();
        userStatRef = db.collection("statistic").document(u.getId());
        collectionRef = userStatRef.collection("dailyData");

        lineChart = findViewById(R.id.lineChart);
        btnBack = findViewById(R.id.btnBackChart);
    }
}