package com.example.dell.diary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;
import com.example.dell.db.DiaryLabel;
import com.example.dell.db.Label;
import com.example.dell.db.Sentence;
import com.example.dell.db.Sentencebook;

import java.util.ArrayList;
import java.util.List;

public class BottlefrontActivity extends AppCompatActivity {
    private CoordinatorLayout container;
    private static String TAG = "123";
    public static final String BOTTLE_NAME = "bottle_name";
    public static final String BOTTLE_IMAGE_ID = "bottle_image_id";

    private DatabaseHelper databaseHelper = null;
    private List<Sentence> sentenceList = new ArrayList<>();
    private NoteAdapter adapter;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottletoolbar, menu);
        return true;
    }

   @Override
   protected  void onResume(){
        super.onResume();
       DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
       CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
       String sentencebookname = collapsingToolbar.getTitle().toString();
       Sentencebook sentencebook = Sentencebook.getByName(helper, sentencebookname);
       sentenceList = sentencebook.getAllSubSentence(helper);
       adapter.update(sentenceList);
   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode){
            case 1:

        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottlefront);
        Intent intent = getIntent();
        final String sentencebookName = intent.getStringExtra(BOTTLE_NAME);
       // int bottleImageId  = intent.getIntExtra(BOTTLE_IMAGE_ID, 0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.bottle_front_toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView bottleImageView = (ImageView) findViewById(R.id.bottle_image_view);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(sentencebookName);
      //  Glide.with(this).load(bottleImageId).into(bottleImageView);
       //悬浮按钮添加纸条
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.note_add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
                CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
                String sentencebookname = collapsingToolbar.getTitle().toString();
                Sentencebook sentencebook = Sentencebook.getByName(helper,sentencebookname);
                Sentence sentence = new Sentence();
                sentence.setSentencebook(sentencebook);
                Intent intent = new Intent(BottlefrontActivity.this, TicketEditActivity.class);
                intent.putExtra(TicketEditActivity.NOTE_EDITABLE, "true");
                intent.putExtra(TicketEditActivity.NOTE_NEW, "true");
                intent.putExtra(TicketEditActivity.SENTENCE_THIS, sentence);
                startActivity(intent);

                /*Sentence sentence = new Sentence();
                sentence.setText("123");
                sentence.setDate();*/
               /* Log.d(TAG, sentencebookname);*/
               /* Sentencebook sentencebook = Sentencebook.getByName(helper, sentencebookname);
                if(sentencebook == null){
                    Log.d(TAG, "onClick: no sentencebook");
                }
                *//*sentence.setSentencebook(sentencebook);
                sentence.insert(helper);*//*
                sentenceList = sentencebook.getAllSubSentence(helper);
                 Log.d(TAG, "onClick: correct");
                int a =0;
                a = sentenceList.size();
                if(a == 0) Log.d(TAG, "onClick: 没有");
                adapter.update(sentenceList);*/
            }
        });

         //根据sentencebookName 获取sentencebook对象
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        Sentencebook sentencebook = Sentencebook.getByName(helper, sentencebookName);
        initSententceList(sentencebook);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_note_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NoteAdapter(sentenceList, sentencebookName);
        recyclerView.setAdapter(adapter);

    }

    //初始化sentenceList
    public void initSententceList(Sentencebook sentencebook ){
        sentenceList.clear();
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        sentenceList= sentencebook.getAllSubSentence(helper);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.rename:
                final EditText renameBottleName = new EditText(BottlefrontActivity.this);
                renameBottleName.setId(R.id.edit_In_BottleName);
                renameBottleName.setHint("请输入瓶子的名字");
                renameBottleName.setFocusable(true);
                AlertDialog.Builder builder = new AlertDialog.Builder(BottlefrontActivity.this);
                builder.setTitle("修改瓶子的名字##")
                        .setView(renameBottleName)                //设置一个控件（其他控件同理）
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String rename = renameBottleName.getText().toString();
                        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
                        String oldname = collapsingToolbar.getTitle().toString();
                        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
                        Sentencebook sententcebook = Sentencebook.getByName(helper, oldname);
                        sententcebook.setSentencebookName(rename);
                        sententcebook.update(helper);
                        collapsingToolbar.setTitle(rename);
                        /* adapter.addSentencebook(sentencebookList.size(),sentencebook);*/
                        //adapter.addSentencebook(sentencebook);
                    }
                });
                builder.show();

                break;
            case R.id.settings:
                Toast.makeText(this, "You clicked settings", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

}
