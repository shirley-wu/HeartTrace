package com.example.dell.diary;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.dell.diary.R.styleable.RecyclerView;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener{

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private View view;
    boolean month_mode=true;

    private MaterialCalendarView calendarView;
    private ImageView calendar_picture;
    private RecyclerView recyclerView;
    public List<Diary> diaryList = new ArrayList<>();
    private com.example.dell.diary.DiaryCardAdapter adapter;
    private TextView toolbarTitle;
    private ImageButton selectDate;
    private boolean calendar_mode;
    private LinearLayoutManager layoutManager;
    private int dayOfWeek;
    private int dayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendar_mode=true;
        calendar_picture = (ImageView) findViewById(R.id.calendar_picture);
        calendarView = (MaterialCalendarView) findViewById(R.id.calendar);
        toolbarTitle = (TextView) findViewById(R.id.toolbarTitle);
        selectDate = (ImageButton) findViewById(R.id.selectDate);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        calendar_picture.setOnClickListener(this);
        selectDate.setOnClickListener(this);

        initdata();
        initDiaryList();
        initRecyclerView();

    }

    private void initdata()
    {
        calendarView.setTopbarVisible(false);
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        calendarView.state().edit().setFirstDayOfWeek(Calendar.SUNDAY).commit();
        calendarView.setSelectedDate(new Date());
        calendarView.setSelectionColor(getResources().getColor(R.color.colorBase));
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                CalendarDay selected_date = calendarView.getSelectedDate();

                diaryList.clear();
                DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
                diaryList = Diary.getByDate(helper,selected_date.getYear(),selected_date.getMonth() + 1,selected_date.getDay(), false);
                if(diaryList == null){
                    diaryList = new ArrayList<>();
                }

                adapter = new com.example.dell.diary.DiaryCardAdapter(diaryList);
                recyclerView.setAdapter(adapter);

                toolbarTitle.setText(FORMATTER.format(selected_date.getDate()));
                //Toast.makeText(CalendarActivity.this, FORMATTER.format(selected_date.getDate()), Toast.LENGTH_SHORT).show();
            }
        });
        toolbarTitle.setText(FORMATTER.format(new Date()));
    }

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new com.example.dell.diary.DiaryCardAdapter(diaryList);
        recyclerView.setAdapter(adapter);
    }

    private void initDiaryList(){
        diaryList.clear();
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Log.i("date",year + " "+month + " "+ day);
        diaryList = Diary.getByDate(helper,year,month,day, false);
        //diaryList = Diary.getAll(helper,false);//getbydate
        if(diaryList == null){
            diaryList = new ArrayList<>();
        }
    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.calendar_picture:
                calendarView.setSelectedDate(new Date());
                calendarView.setCurrentDate(new Date());
                toolbarTitle.setText(FORMATTER.format(new Date()));

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH)+1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                diaryList.clear();
                DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
                diaryList = Diary.getByDate(helper,year,month,day, false);
                if(diaryList == null){
                    diaryList = new ArrayList<>();
                }

                adapter = new com.example.dell.diary.DiaryCardAdapter(diaryList);
                recyclerView.setAdapter(adapter);
                break;
            case R.id.selectDate:
                TimePickerView pvTime = new TimePickerBuilder(CalendarActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        calendarView.setSelectedDate(date);
                        calendarView.setCurrentDate(date);
                        toolbarTitle.setText(FORMATTER.format(date));
                    }
                })
                        .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                        .isCyclic(true)//是否循环滚动
                        .setLineSpacingMultiplier((float)2.5)
                        .setLabel("","","","","","")//默认设置为年月日时分秒
                        .build();
                pvTime.show();
                break;
        }
    }
}