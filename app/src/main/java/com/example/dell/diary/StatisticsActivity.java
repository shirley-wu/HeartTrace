package com.example.dell.diary;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView dateTextView;
    private Button changeData;
    private Button chooseDate;
    private ImageButton help;
    private String end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        dateTextView = (TextView) findViewById(R.id.date_textview);
        changeData = (Button) findViewById(R.id.change_data);
        chooseDate = (Button) findViewById(R.id.choose_date);
        help = (ImageButton) findViewById(R.id.help);

        Calendar now = Calendar.getInstance();
        Calendar WeekAgo = Calendar.getInstance();
        ;
        WeekAgo.set(Calendar.DATE, WeekAgo.get(Calendar.DATE) - 6);
        String init_range = "        您当前选择的日期范围:   " + WeekAgo.get(Calendar.YEAR) + "/" + (WeekAgo.get(Calendar.MONTH) + 1) + "/" + (WeekAgo.get(Calendar.DAY_OF_MONTH)) + " 至 " + now.get(Calendar.YEAR) + "/" + (now.get(Calendar.MONTH) + 1) + "/" + now.get(Calendar.DAY_OF_MONTH);
        end = (now.get(Calendar.MONTH) + 1) + "/" + now.get(Calendar.DAY_OF_MONTH);
        dateTextView.setText(init_range);

        changeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChart();
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                LayoutInflater inflater2 = getLayoutInflater();;
                //得到界面视图
                View currean_View = inflater.inflate(R.layout.activity_statistics, null);
                //得到要弹出的界面视图
                View view = inflater2.inflate(R.layout.help_tips, null);
                WindowManager windowManager = getWindowManager();
                int width = windowManager.getDefaultDisplay().getWidth();
                int heigth = windowManager.getDefaultDisplay().getHeight();
                Log.i("width", width+"");
                Log.i("height", heigth+"");
                PopupWindow popupWindow = new PopupWindow(view,(int)(width*0.8),(int)(heigth*0.5));
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                //显示在屏幕中央
                popupWindow.showAtLocation(currean_View, Gravity.CENTER, 0, 40);
                //popupWindow弹出后屏幕半透明
                BackgroudAlpha((float)0.5);
                //弹出窗口关闭事件
                popupWindow.setOnDismissListener(new popupwindowdismisslistener());

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
        if (dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        String date = "        您当前选择的日期范围:   " + year + "/" + (++monthOfYear) + "/" + dayOfMonth + " 至 " + yearEnd + "/" + (++monthOfYearEnd) + "/" + dayOfMonthEnd;
        end = monthOfYearEnd + "/" + dayOfMonthEnd;
        dateTextView.setText(date);
        showChart();
    }

    private void showChart() {
        LineChart lineChart1 = (LineChart) findViewById(R.id.line_chart1);
        LineChartManager lineChartManager1 = new LineChartManager(lineChart1);

        //设置x轴的数据
        ArrayList<Float> xValues = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            xValues.add((float) i);
        }

        //设置y轴的数据()
        List<Float> yValues = new ArrayList<>();
        for (int j = 1; j <= 7; j++) {
            yValues.add((float) (Math.random() * 8));
        }

        String name = "情绪曲线";
        Legend legend = lineChart1.getLegend();

        int color = Color.parseColor("#3F51B5");
        int color_d = Color.parseColor("#555555");

        //创建多条折线的图表

        lineChartManager1.showLineChart(xValues, yValues, name, color);
        lineChartManager1.setXAxis(7, 1, 7);
        lineChartManager1.setYAxis(10, 0, 5);
        lineChartManager1.setDescription(end, color_d);
        legend.setFormSize(20f);
        legend.setTextSize(12f);

        final MarkerView markerView = new MarkerView(StatisticsActivity.this, R.layout.item_chart);
        lineChart1.setMarker(markerView);
        lineChart1.invalidate();
        final TextView spirit = markerView.findViewById(R.id.spirit_tag);
        //设置数据点点击事件，这里是更新弹窗中的信息
        lineChart1.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                spirit.setText(e.getY() + "");
            }

            @Override
            public void onNothingSelected() {
            }
        });
    }

    //设置屏幕背景透明度
    private void BackgroudAlpha(float alpha) {
        // TODO Auto-generated method stub
        WindowManager.LayoutParams l = this.getWindow().getAttributes();
        l.alpha = alpha;
        getWindow().setAttributes(l);
    }

    //点击其他部分popwindow消失时，屏幕恢复透明度
    class popupwindowdismisslistener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            BackgroudAlpha((float) 1);
        }
    }


}