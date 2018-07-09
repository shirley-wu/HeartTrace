package com.example.dell.diary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PersonalInformationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private EditText chooseBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

        chooseBirthday = (EditText) findViewById(R.id.choose_birthday);
        chooseBirthday.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.choose_birthday:
                TimePickerView pvTime = new TimePickerBuilder(PersonalInformationActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        chooseBirthday.setText(FORMATTER.format(date));
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
