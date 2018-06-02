package com.example.dell.diary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Helen_L on 2018/5/16.
 */

public class NewBottleDialog extends AlertDialog {
    Activity context;

    public EditText bottle_name;
    private Button btn_save;


    public NewBottleDialog(Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.new_bottle);
        this.setTitle("新建一个瓶子");
        bottle_name = (EditText) findViewById(R.id.new_bottle_name);
        btn_save = (Button) findViewById(R.id.btn_save_pop);
        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();
        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(p);
        this.setCancelable(true);
    }

    public Button getBtn_save(){
        return btn_save;
    }
    public EditText getBottleName(){
        return bottle_name;
    }
}