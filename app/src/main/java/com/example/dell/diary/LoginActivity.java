package com.example.dell.diary;

import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.auth.MyAccount;
import com.example.dell.auth.ServerAuthenticator;
import com.j256.ormlite.stmt.query.In;

public class LoginActivity extends AppCompatActivity {

    Button bt_login;
    TextInputLayout username_layout;
    TextInputLayout password_layout;
    EditText username;
    EditText password;
    TextView toRegist;
    Button d_btn;

    MyAccount myAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myAccount = MyAccount.get(this);

        bt_login = (Button) findViewById(R.id.btn_login);
        username_layout = (TextInputLayout) findViewById(R.id.username_wrapper);
        password_layout = (TextInputLayout) findViewById(R.id.password_wrapper);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        toRegist = (TextView)findViewById(R.id.to_regist);
        d_btn = (Button)findViewById(R.id.direct_enter);

        password_layout.setPasswordVisibilityToggleEnabled(true);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = username.getText().toString();
                final String pw = password.getText().toString();
;
                if (validateAccount(name) && validatePassword(pw)) {
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            Bundle bundle = new Bundle();
                            boolean status = ServerAuthenticator.signIn(name, pw, bundle);
                            if(status == false) {
                                // 接口错误或者网络错误
                                Looper.prepare();
                                try {
                                    Toast.makeText(LoginActivity.this, "接口错误或者网络错误", Toast.LENGTH_SHORT).show();
                                }catch (Exception e) {
                                    Log.e("error",e.toString());
                                }
                                Looper.loop();
                            }
                            else {
                                boolean success = bundle.getBoolean("success");
                                String msg = bundle.getString("msg");
                                // success表示操作成功与否；msg表示服务器返回信息
                                if (success) {
                                    myAccount.setName(name);
                                    myAccount.setToken(bundle.getString("token"));
                                    if(myAccount.getNickname() == null){
                                        myAccount.setNickname(name);
                                    }
                                    myAccount.save();

                                    Intent intent = new Intent(LoginActivity.this, DiaryWriteActivity.class);
                                    intent.putExtra("diary_origin", "welcome");
                                    startActivity(intent);
                                    finish();

                                    Looper.prepare();
                                    try {
                                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                    }catch (Exception e) {
                                        Log.e("error",e.toString());
                                    }
                                    Looper.loop();

                                } else {
                                    Looper.prepare();
                                    try {
                                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();

                                    }catch (Exception e) {
                                        Log.e("error",e.toString());
                                    }
                                    Looper.loop();
                                }
                            }
                        }
                    }).start();
                }
            }
        });

        toRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
            }
        });

        d_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAccount.setName("测试用户");
                if(myAccount.getNickname() == null) {
                    myAccount.setNickname("测试用户");
                }
                myAccount.save();

                Intent intent = new Intent(LoginActivity.this, DiaryWriteActivity.class);
                intent.putExtra("diary_origin", "welcome");
                startActivity(intent);
            }
        });

    }

    protected void onRestart(){
        super.onRestart();
        Intent intent = getIntent();
        String origin = intent.getStringExtra("origin");
        if(origin != null){
            username_layout.setErrorEnabled(false);
            password_layout.setErrorEnabled(false);
            username.setText(intent.getStringExtra("name"));
            password.setText(intent.getStringExtra("password"));
            password.requestFocus();
            password.setSelection(password.getText().length());
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private boolean validateAccount(String name) {
        //username_layout.setErrorEnabled(true);
        if (name.isEmpty()) {
            username_layout.setError("用户名不能为空");
            return false;
        } else {
            username_layout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword(String pw) {
        //password_layout.setErrorEnabled(true);
        if (pw.isEmpty()) {
            password_layout.setError("密码不能为空");
            return false;
        }
        else if (pw.length() < 6 || pw.length() > 18) {
            password_layout.setError("密码长度6-18位");
            return false;
        } else {
            password_layout.setErrorEnabled(false);
        }
        return true;
    }
}
