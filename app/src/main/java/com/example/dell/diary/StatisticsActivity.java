package com.example.dell.diary;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView dateTextView;
    private Button changeData;
    private Button chooseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        dateTextView = (TextView) findViewById(R.id.date_textview);
        changeData = (Button) findViewById(R.id.change_data);
        chooseDate = (Button) findViewById(R.id.choose_date);

        changeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChart();
            }
        });

        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();

                DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                        StatisticsActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setAccentColor(Color.parseColor("#3F51B5"));
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        showChart();

    }

    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth,int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        String date = "You picked the following date: From- "+dayOfMonth+"/"+(++monthOfYear)+"/"+year+" To "+dayOfMonthEnd+"/"+(++monthOfYearEnd)+"/"+yearEnd;
        dateTextView.setText(date);
    }

    private void showChart()
    {
        LineChart lineChart1 = (LineChart) findViewById(R.id.line_chart1);
        LineChartManager lineChartManager1 = new LineChartManager(lineChart1);

        //设置x轴的数据
        ArrayList<Float> xValues = new ArrayList<>();
        for (int i = 0; i <= 7; i++) {
            xValues.add((float) i);
        }

        //设置y轴的数据()
        List<Float> yValues = new ArrayList<>();
        for (int j = 0; j <= 7; j++) {
            yValues.add((float) (Math.random() * 8));
        }

        String name = "情绪曲线";

        int color = Color.parseColor("#3F51B5");

        //创建多条折线的图表

        lineChartManager1.showLineChart(xValues, yValues, name, color);
        lineChartManager1.setDescription("");
        lineChartManager1.setYAxis(10, 0, 5);
    }
}
