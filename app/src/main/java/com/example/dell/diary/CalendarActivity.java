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

import com.bigkoo.pickerview.TimePickerView;


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
    public List<Diary> diaryCardList = new ArrayList<>();
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

        calendar_picture.setOnClickListener(this);
        selectDate.setOnClickListener(this);

        initdata();
        initDiaryCard();

        initRecyclerView();

/*      recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int mScrollThreshold;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isSignificantDelta = Math.abs(dy) > mScrollThreshold;
                if (isSignificantDelta) {
                    if (dy > 0) {
                        onScrollUp();
                    } else {
                        onScrollDown();
                    }
                }
            }
            public void setScrollThreshold(int scrollThreshold) {
                mScrollThreshold = scrollThreshold;
            }
        });*/

    }

    private void initdata()
    {
        calendarView.setTopbarVisible(false);
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        calendarView.state().edit().setFirstDayOfWeek(Calendar.MONDAY).commit();
        calendarView.setSelectedDate(new Date());
        calendarView.setSelectionColor(getResources().getColor(R.color.colorBase));
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                CalendarDay selected_date = calendarView.getSelectedDate();
                toolbarTitle.setText(FORMATTER.format(selected_date.getDate()));
                Toast.makeText(CalendarActivity.this, FORMATTER.format(selected_date.getDate()), Toast.LENGTH_SHORT).show();
            }
        });
        toolbarTitle.setText(FORMATTER.format(new Date()));


    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new com.example.dell.diary.DiaryCardAdapter(diaryCardList);
        recyclerView.setAdapter(adapter);
    }

    private void initDiaryCard(){

        diaryCardList.clear();
        Diary diary1 = new Diary("我是一篇日记。");
        diary1.setDate(new Date(2018,6,2));
        diaryCardList.add(0,diary1);
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.calendar_picture:
                calendarView.setSelectedDate(new Date());
                calendarView.setCurrentDate(new Date());
                toolbarTitle.setText(FORMATTER.format(new Date()));
                break;
            case R.id.selectDate:
                Toast.makeText(CalendarActivity.this, "succeed", Toast.LENGTH_SHORT).show();
                TimePickerView.Builder pvTime = new TimePickerView.Builder(CalendarActivity.this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        calendarView.setSelectedDate(date);
                        calendarView.setCurrentDate(date);
                        toolbarTitle.setText(FORMATTER.format(date));
                    }
                });
                pvTime.setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                        .isCyclic(true)//是否循环滚动
                        .setLineSpacingMultiplier((float) 2.5)
                        .setLabel("", "", "", "", "", "")//默认设置为年月日时分秒
                        .build()
                        .show();


                break;
        }
    }

    private void initLayoutManager() {
        if (layoutManager == null) {
            RecyclerView.LayoutManager layout = recyclerView.getLayoutManager();
            if (layout != null && layout instanceof LinearLayoutManager) {
                layoutManager = (LinearLayoutManager) layout;
            }
        }
    }

    public boolean isGetTop() {
        initLayoutManager();
        if (layoutManager != null) {
            if (layoutManager.getItemCount() == 0) {
                return true;
            } else if (layoutManager.findFirstVisibleItemPosition() == 0 && recyclerView.getChildAt(0).getTop() >= recyclerView.getPaddingTop()) {
                return true;
            }
        }
        return false;
    }

    public boolean isGetBottom() {
        initLayoutManager();
        if (layoutManager != null) {
            int count = layoutManager.getItemCount();
            if (count == 0) {
                return true;
            } else if (layoutManager.findLastCompletelyVisibleItemPosition() == count - 1) {
                return true;
            }
        }
        return false;
    }

    private void onScrollDown() {     //下滑时要执行的代码
        //Toast.makeText(CalendarActivity.this, "down", Toast.LENGTH_SHORT).show();
        if(calendar_mode==false && isGetTop()==true) {
            calendarView.state().edit().setCalendarDisplayMode(CalendarMode.MONTHS).commit();
            calendar_mode=true;
        }
    }

    private void onScrollUp() {       //上滑时要执行的代码
        //Toast.makeText(CalendarActivity.this, "up", Toast.LENGTH_SHORT).show();
        if(calendar_mode==true) {
            calendarView.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
            calendar_mode=false;
        }

    }

}
