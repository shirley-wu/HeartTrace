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
    public List<DiaryCard> diaryCardList = new ArrayList<>();
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
        DiaryCard diaryCard1 = new DiaryCard(2018,5,5,"周六",R.drawable.happy_black,"哈哈哈哈哈哈哈哈哈哈！！！");
        diaryCardList.add(0,diaryCard1);
        DiaryCard diaryCard2 = new DiaryCard(2018,5,6,"周日",R.drawable.normal_black,"　　也许事情总是不一定能如人意的。可是，我总是在想，只要给我一段美好的回忆也就够了。哪怕只有一天，一个晚上，也就应该知足了。\n" +
                "　　很多愿望，我想要的，上苍都给了我，很快或者很慢地，我都一一地接到了。而我对青春的美的渴望，虽然好象一直没有得到，可是走着走着，回过头一看，好象又都已经过去了。有几次，当时并没能马上感觉到，可是，也很有几次，我心里猛然醒悟：原来，这就是青春！");
        diaryCardList.add(0,diaryCard2);
        DiaryCard diaryCard3 = new DiaryCard(2018,5,7,"周一",R.drawable.sad_black,"(ಥ﹏ಥ)");
        diaryCardList.add(0,diaryCard3);
        DiaryCard diaryCard4 = new DiaryCard(2018,5,9,"周三",R.drawable.happy_black,"康鑫好帅！");
        diaryCardList.add(0,diaryCard4);
        DiaryCard diaryCard5 = new DiaryCard(2018,5,10,"周四",R.drawable.sad_black,"学姐好美！");
        diaryCardList.add(0,diaryCard5);
        DiaryCard diaryCard6 = new DiaryCard(2018,5,12,"周六",R.drawable.sad_black,"小哥哥没我可爱hhh");
        diaryCardList.add(0,diaryCard6);
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
