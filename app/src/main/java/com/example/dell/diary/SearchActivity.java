package com.example.dell.diary;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Date;

public class SearchActivity extends AppCompatActivity {

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mYear2;
    private int mMonth2;
    private int mDay2;
    private Date startDate;
    private Date endDate;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    TextView dateDisplayStart;
    TextView dateDisplayEnd;
    DatePickerDialog datePickerDialog1;
    DatePickerDialog datePickerDialog2;
    final int DATE_DIALOG = 1;
    final int DATE_DIALOG_END = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle("");

        final CheckedTextView checkTag = (CheckedTextView) findViewById(R.id.check_tag);
        final CheckedTextView checkTime = (CheckedTextView) findViewById(R.id.check_time);
        final LinearLayout timeSelect = (LinearLayout) findViewById(R.id.time_select);
        timeSelect.setVisibility(View.INVISIBLE);
        checkTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                CheckedTextView checkedTextView = (CheckedTextView) v;
                checkedTextView.toggle();
                if (checkTag.isChecked() == true) {

                }
            }
        });
        checkTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                CheckedTextView checkedTextView = (CheckedTextView) v;
                checkedTextView.toggle();
                if (checkTime.isChecked() == true) {
                    timeSelect.setVisibility(View.VISIBLE);
                } else {
                    timeSelect.setVisibility(View.INVISIBLE);
                }
            }
        });
        Date d = new Date();
        mYear2 = mYear = d.getYear();
        mMonth2 = mMonth = d.getMonth();
        mDay2 = mDay = d.getDate();
        dateDisplayStart = (TextView)findViewById(R.id.date_display);
        dateDisplayEnd = (TextView)findViewById(R.id.date_display2);
        datePickerDialog1 = new DatePickerDialog(this, dateListener, mYear+1900, mMonth, mDay);
        datePickerDialog2 = new DatePickerDialog(this, dateListener2, mYear2+1900, mMonth2, mDay2);

        dateDisplayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG);
            }
        });

        dateDisplayEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_END);
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        final SearchView searchView = (SearchView)findViewById(R.id.search_view);
        //设置我们的SearchView
        searchView.setIconifiedByDefault(false);//设置展开后图标的样式,这里只有两种,一种图标在搜索框外,一种在搜索框内
        searchView.onActionViewExpanded();// 写上此句后searchView初始是可以点击输入的状态，如果不写，那么就需要点击下放大镜，才能出现输入框,也就是设置为ToolBar的ActionView，默认展开
        searchView.setIconified(false);//输入框内icon不显示
        searchView.requestFocus();//输入焦点
        //searchView.setSubmitButtonEnabled(true);//添加提交按钮，监听在OnQueryTextListener的onQueryTextSubmit响应
        //searchView.setFocusable(true);//将控件设置成可获取焦点状态,默认是无法获取焦点的,只有设置成true,才能获取控件的点击事件
        //searchView.requestFocusFromTouch();//模拟焦点点击事件

        //searchView.setFocusable(false);//禁止弹出输入法，在某些情况下有需要
        //searchView.clearFocus();//禁止弹出输入法，在某些情况下有需要
        EditText seachTextView = (EditText) searchView.findViewById(R.id.search_src_text);
        seachTextView.setTextColor(getResources().getColor(R.color.black));
        seachTextView.setHintTextColor(getResources().getColor(R.color.lightgray));
        seachTextView.setTextSize(17);
//        int imgId = searchView.getContext().getResources().getIdentifier("android:id/search_mag_icon",null,null);
//获取ImageView
        ImageView searchButton = (ImageView)searchView.findViewById(R.id.search_mag_icon);
//设置图片
        searchButton.setImageResource(R.drawable.search_gray);
        ImageView closeButton = (ImageView)searchView.findViewById(R.id.search_close_btn);
//设置图片
        closeButton.setImageResource(R.drawable.close_gray);

////不使用默认
//        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(SearchActivity.this, "begin search", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putExtra("search_text",query);
                if(checkTime.isChecked() == true){
                    startDate = new Date(mYear,mMonth,mDay);
                    endDate = new Date(mYear2,mMonth2,mDay2);
                    intent.putExtra("start_date",startDate);
                    intent.putExtra("end_date",endDate);
                }
                else{
//                    startDate = new Date(1900,1,1);
//                    endDate = new Date(1900,1,1);
//                    intent.putExtra("start_date",startDate);
//                    intent.putExtra("end_date",endDate);
                }
                startActivity(intent);
                return true;
            }

            public boolean onQueryTextChange(String newText) {
                if (newText != null && newText.length() > 0) {
                    //currentSearchTip = newText;
                    //showSearchTip(newText);
                    //Toast.makeText(SearchActivity.this, newText, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                //datePickerDialog1 = new DatePickerDialog(this, dateListener, mYear, mMonth, mDay);
                return datePickerDialog1;
            case DATE_DIALOG_END:
                //datePickerDialog2 = new DatePickerDialog(this, dateListener2, mYear2, mMonth2, mDay2);
                return datePickerDialog2;
        }
        return null;
    }

    /**
     * 设置日期
     */
    public void display() {
        dateDisplayStart.setText("起始时间："+(mYear+1900)+"年"+(mMonth+1)+"月"+mDay+"日");
    }
    public void display2() {
        dateDisplayEnd.setText("结束时间："+ (mYear2+1900)+"年"+ (mMonth2+1)+"月"+mDay2+"日");
    }

    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year-1900;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            datePickerDialog2.getDatePicker().setMinDate(new Date(mYear,mMonth,mDay).getTime());
            display();
        }
    };

    private DatePickerDialog.OnDateSetListener dateListener2 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear2 = year-1900;
            mMonth2 = monthOfYear;
            mDay2 = dayOfMonth;
            datePickerDialog1.getDatePicker().setMaxDate(new Date(mYear2,mMonth2,mDay2).getTime());
            display2();
        }
    };

//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.search_menu, menu);
//        return true;
//    }
//
////    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                return true;
//            default:
//        }
//        return true;
//    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Search Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
