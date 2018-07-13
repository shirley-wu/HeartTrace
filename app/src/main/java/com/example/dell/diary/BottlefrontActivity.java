package com.example.dell.diary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import android.widget.Button;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class BottlefrontActivity extends AppCompatActivity {
    private CoordinatorLayout container;
    private static String TAG = "123";
    public static final String BOTTLE_NAME = "bottle_name";
    public static final String BOTTLE_IMAGE_ID = "bottle_image_id";

    private Sentencebook sentencebook_this;
    private String read_me_text = null;
    private DatabaseHelper databaseHelper = null;
    private List<Sentence> sentenceList = new ArrayList<>();
    private NoteAdapter adapter;
    private NavigationView navView;
    private DrawerLayout mDrawerLayout;



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
                if(resultCode == 1){
                    int return_position = data.getIntExtra("result",0);
                    if(return_position < sentenceList.size() ) {
                        int new_positon = return_position + 1;
                        Sentence sentence = sentenceList.get(new_positon);
                        Intent intent = new Intent(BottlefrontActivity.this, TicketEditActivity.class);
                        intent.putExtra(TicketEditActivity.POSITION, new_positon);
                        intent.putExtra(TicketEditActivity.SENTENCE_THIS, sentence);
                        intent.putExtra(TicketEditActivity.NOTE_EDITABLE, "false");
                        intent.putExtra(TicketEditActivity.NOTE_NEW, "false");
                        intent.putExtra(TicketEditActivity.TYPE, "ordinary");
                        startActivityForResult(intent, 1);
                    }
                }
                else if(resultCode == -1){
                    int return_positon1 = data.getIntExtra("result",0);
                    if(return_positon1>=0){
                        int new_positon1 = return_positon1 - 1;
                        Sentence sentence = sentenceList.get(new_positon1);
                        Intent intent = new Intent(BottlefrontActivity.this, TicketEditActivity.class);
                        intent.putExtra(TicketEditActivity.POSITION, new_positon1);
                        intent.putExtra(TicketEditActivity.SENTENCE_THIS, sentence);
                        intent.putExtra(TicketEditActivity.NOTE_EDITABLE, "false");
                        intent.putExtra(TicketEditActivity.NOTE_NEW, "false");
                        intent.putExtra(TicketEditActivity.TYPE, "ordinary");
                        startActivityForResult(intent, 1);
                    }
                }
                else if(resultCode == 2){
                    int return_position2 = data.getIntExtra("result",0);
                    Sentence sentence = sentenceList.get(return_position2);
                    DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
                    sentence.delete(helper);
                    sentenceList.remove(return_position2);
                    adapter.update(sentenceList);
                }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottlefront);
        Intent intent = getIntent();
        final String sentencebookName = intent.getStringExtra(BOTTLE_NAME);
        Toolbar toolbar = (Toolbar) findViewById(R.id.bottle_front_toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView bottleImageView = (ImageView) findViewById(R.id.bottle_image_view);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu_white);
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
                sentenceList = sentencebook.getAllSubSentence(helper);
                int position = sentenceList.size();
                Intent intent = new Intent(BottlefrontActivity.this, TicketEditActivity.class);
                intent.putExtra(TicketEditActivity.POSITION, position);
                intent.putExtra(TicketEditActivity.NOTE_EDITABLE, "true");
                intent.putExtra(TicketEditActivity.NOTE_NEW, "true");
                intent.putExtra(TicketEditActivity.SENTENCE_THIS, sentence);
                intent.putExtra(TicketEditActivity.TYPE, "ordinary");
                startActivityForResult(intent,1);
            }
        });




        //根据sentencebookName 获取sentencebook对象
        databaseHelper = new DatabaseHelper(getApplicationContext());
        sentencebook_this = Sentencebook.getByName(databaseHelper, sentencebookName);
        initSententceList(sentencebook_this);



        Button edit_read_me = findViewById(R.id.bottle_readme_botton);
        edit_read_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText inputBottleDescribe = new EditText(BottlefrontActivity.this);
                inputBottleDescribe.setHint("请输入瓶子的描述");
                inputBottleDescribe.setFocusable(true);
                AlertDialog.Builder builder = new AlertDialog.Builder(BottlefrontActivity.this);
                builder.setTitle("read me ##")
                        .setView(inputBottleDescribe)                //设置一个控件（其他控件同理）
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        read_me_text = inputBottleDescribe.getText().toString();
                        TextView read_me = findViewById(R.id.bottle_readme_text);
                        read_me.setText(read_me_text);
                        sentencebook_this.setDescription(read_me_text);
                        sentencebook_this.update(databaseHelper);
                    }
                });
                builder.show();

            }
        });
        TextView read_me = findViewById(R.id.bottle_readme_text);
        read_me_text = sentencebook_this.getDescription();
        read_me.setText(read_me_text);



        mDrawerLayout = findViewById(R.id.note_drawer_layout);
        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.favorite:
                        Intent intent0 = new Intent(BottlefrontActivity.this, CollectActivity.class);
                        startActivity(intent0);
                        break;
                    case R.id.statistics:
                        Intent intent = new Intent(BottlefrontActivity.this,StatisticsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.calendar_search:
                        Intent intent2 = new Intent(BottlefrontActivity.this,CalendarActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.time_line:
                        Intent intent3 = new Intent(BottlefrontActivity.this,TimeLineActivity.class);
                        startActivity(intent3);
                        break;
                }
                return true;
            }
        });



        RecyclerView recyclerView = findViewById(R.id.recycler_note_view);
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


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottletoolbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
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
    public void  update(){

    }
}
