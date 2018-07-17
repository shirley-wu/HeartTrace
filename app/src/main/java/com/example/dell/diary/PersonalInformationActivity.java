package com.example.dell.diary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.dell.auth.MyAccount;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalInformationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private EditText chooseBirthday;
    private CircleImageView personalImage;
    private EditText setName;
    private EditText chooseSex;
    private EditText setEmail;
    private EditText setSchool;
    private EditText setSignature;
    private Button personalConfirm;
    private MyAccount myAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        myAccount = MyAccount.get(getApplicationContext());

        setView();
        setListener();
        init();

    }

    private void setView(){
        chooseBirthday = (EditText) findViewById(R.id.choose_birthday);
        personalImage = (CircleImageView) findViewById(R.id.personal_image);
        setName = (EditText) findViewById(R.id.set_name);
        chooseSex = (EditText) findViewById(R.id.choose_sex);
        setEmail = (EditText) findViewById(R.id.set_email);
        setSchool = (EditText) findViewById(R.id.set_school);
        setSignature = (EditText) findViewById(R.id.set_signature);
        personalConfirm = (Button) findViewById(R.id.personal_confirm);
    }

    private void setListener(){
        chooseBirthday.setOnClickListener(this);
        personalImage.setOnClickListener(this);
        setName.setOnClickListener(this);
        chooseSex.setOnClickListener(this);
        setEmail.setOnClickListener(this);
        setSchool.setOnClickListener(this);
        setSignature.setOnClickListener(this);
        personalConfirm.setOnClickListener(this);
    }

    private void init(){
        setName.setCursorVisible(false);
        chooseSex.setCursorVisible(false);
        setEmail.setCursorVisible(false);
        setSchool.setCursorVisible(false);
        setSignature.setCursorVisible(false);

        if(myAccount.getNickname() != null) setName.setText(myAccount.getNickname());
        if(myAccount.getGender() != null) chooseSex.setText(myAccount.getGender());
        if(myAccount.getBirthday() != null) chooseBirthday.setText(myAccount.getBirthday());
        if(myAccount.getEmail() != null) setEmail.setText(myAccount.getEmail());
        if(myAccount.getSchool() != null) setSchool.setText(myAccount.getSchool());
        if(myAccount.getSignature() != null) setSignature.setText(myAccount.getSignature());

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
            case R.id.set_name:
                setName.setCursorVisible(true);
                break;
            case R.id.choose_sex:
                chooseSex.setCursorVisible(true);
                break;
            case R.id.set_email:
                setEmail.setCursorVisible(true);
                break;
            case R.id.set_school:
                setSchool.setCursorVisible(true);
                break;
            case R.id.set_signature:
                setSignature.setCursorVisible(true);
                break;
            case R.id.personal_image: //打开相册选择图片

                break;
            case R.id.personal_confirm:
                myAccount.setNickname(setName.getText().toString());
                myAccount.setGender(chooseSex.getText().toString());
                myAccount.setBirthday(chooseBirthday.getText().toString());
                myAccount.setEmail(setEmail.getText().toString());
                myAccount.setSchool(setSchool.getText().toString());
                myAccount.setSignature(setSignature.getText().toString());
                myAccount.save();
                Intent intent = new Intent(PersonalInformationActivity.this, DiaryWriteActivity.class);
                startActivity(intent);
                break;
        }
    }

}


