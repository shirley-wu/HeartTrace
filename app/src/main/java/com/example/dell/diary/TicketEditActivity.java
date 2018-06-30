package com.example.dell.diary;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Sentence;
import com.example.dell.db.Sentencebook;
import com.example.dell.diary.R;

public class TicketEditActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String BOTTLE_NAME = "bottle_name";
    public static final String SENTENCE_THIS = "sentence_this";
    public static final String NOTE_EDITABLE = "note_editable";
    public static final String NOTE_NEW = "true";
    private static final String TAG = "TicketEditActivity";


    private FloatingActionButton ticket_confirm;
    private FloatingActionButton ticket_edit;
    private EditText editText ;
    private String note_editable = "true";
    private Sentence sentence;
    private String note_new;


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_edit_toolbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //mDrawLayout.openDrawer(GravityCompat.START);
                finish();
                break;
            case R.id.note_delete:
                Log.d(TAG, "onOptionsItemSelected: click");
                AlertDialog.Builder dialog = new AlertDialog.Builder(TicketEditActivity.this);
                dialog.setTitle("提示");
                dialog.setMessage("你确定要删除你的纸条吗？");
                dialog.setCancelable(true);
                dialog.setPositiveButton("确认",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        Log.d(TAG, "onOptionsItemSelected: build");
                        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
                        Log.d(TAG, "onOptionsItemSelected: build1");
                        Log.d(TAG, "onOptionsItemSelected: build2");
                        sentence.delete(helper);
                        finish();
                    }
                });
                dialog.setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });
                dialog.show();

                break;
            case R.id.note_label:
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: correct");
        setContentView(R.layout.activity_ticket_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.note_edit_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){

            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.menu_white);
        }
        actionBar.setTitle("Little Note");

        Intent intent = getIntent();
        note_editable = intent.getStringExtra(NOTE_EDITABLE);
        sentence = (Sentence) intent.getSerializableExtra(SENTENCE_THIS);
        note_new = intent.getStringExtra(NOTE_NEW);
        ticket_confirm = (FloatingActionButton)findViewById(R.id.ticket_confirm);
        editText = findViewById(R.id.ticket_text);
        ticket_edit = (FloatingActionButton) findViewById(R.id.ticket_edit);
        ticket_confirm.setOnClickListener(this);
        ticket_edit.setOnClickListener(this);
        Init();
    }

    void Init(){
        if(note_editable.equals("false") ){
            ticket_confirm.setVisibility(View.INVISIBLE);
            editText.setEnabled(false);
            editText.setText(sentence.getText());

        }
        else if (note_editable.equals("true")){
            ticket_edit.setVisibility(View.INVISIBLE);
            editText.setEnabled(true);
            editText.setText(sentence.getText());
        }
    }



    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.ticket_confirm:
                ticket_edit.setVisibility(View.VISIBLE);
                ticket_confirm.setVisibility(View.INVISIBLE);
                editText.setEnabled(false);
                //添加新的纸条
                if(note_new.equals("true")) {
                    String sentenceContent = editText.getText().toString();
                    DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
                    sentence.setText(sentenceContent);
                    sentence.setDate();
                    sentence.insert(helper);
                    Log.d(TAG, "onClick: 保存新添加的纸条");
                }
                //修改原有纸条
                if(note_new.equals("false")){
                    DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
                    String sentenceContent = editText.getText().toString();
                    sentence.setText(sentenceContent);
                    sentence.update(helper);
                }
                break;
            case R.id.ticket_edit:
                ticket_edit.setVisibility(View.INVISIBLE);
                ticket_confirm.setVisibility(View.VISIBLE);
                editText.setEnabled(true);
                break;
        }
    }
}
