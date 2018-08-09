package com.example.dell.diary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diarybook;
import com.example.dell.db.Label;
import com.example.dell.db.Sentencebook;
import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;

import java.util.ArrayList;
import java.util.List;

public class BottlesActivity extends AppCompatActivity {
    private DrawerLayout mDrawLayout;
    private CoordinatorLayout container;
    private DatabaseHelper databaseHelper = null;
    private  List<Sentencebook> sentencebookList =  new ArrayList<>();
    private String TAG = "233";
    private BottleAdapter adapter;
    private final int ICON_LIST_DIALOG = 1;
    private String label_name = "travel";
    private int[] imgIds = {R.drawable.travel,
            R.drawable.study, R.drawable.work};

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottlestoolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //mDrawLayout.openDrawer(GravityCompat.START);
                finish();
                break;
            case R.id.delete:
                container=(CoordinatorLayout) findViewById(R.id.bottles_coordinatorLayout);
                Snackbar.make(container, "确认删除？",Snackbar.LENGTH_SHORT).setAction("Undo",new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Toast.makeText(BottlesActivity.this, "成功拯救一个瓶子",Toast.LENGTH_SHORT).show();
                    }
                }).show();
                break;
//            case R.id.settings:
//                Toast.makeText(this, "You clicked settings", Toast.LENGTH_SHORT).show();
//                break;
            default:
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottles);
        Toolbar toolbar = (Toolbar) findViewById(R.id.bottles_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){

            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.menu_white);
        }
        actionBar.setTitle("Bottles");

        //初始化日记本
        initSententcebookList();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_bottle_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BottleAdapter(sentencebookList);
        recyclerView.setAdapter(adapter);



        FloatingActionButton fab =  findViewById(R.id.add_bottle_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText inputBottleName = new EditText(BottlesActivity.this);
                inputBottleName.setId(R.id.edit_In_BottleName);
                inputBottleName.setHint("请输入瓶子的名字");
                inputBottleName.setFocusable(true);

                AlertDialog.Builder builder = new AlertDialog.Builder(BottlesActivity.this);
                builder.setTitle("新建一个瓶子")
                        .setView(inputBottleName)                //设置一个控件（其他控件同理）
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String inputName = inputBottleName.getText().toString();
                                Sentencebook sentencebook = new Sentencebook(inputName);

                                DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
                                sentencebook.insert(helper);
                                sentencebookList = Sentencebook.getAll(helper,false);
                                adapter.update(sentencebookList);

                                /* adapter.addSentencebook(sentencebookList.size(),sentencebook);*/
                                //adapter.addSentencebook(sentencebook);
                            }
                        });
                builder.show();
            }
        });




    }


    public void initSententcebookList() {
        sentencebookList.clear();
        DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
        sentencebookList = Sentencebook.getAll(helper,false);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(databaseHelper != null){
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    private DatabaseHelper getDataBaseHelper(){
        if(databaseHelper == null){
            databaseHelper = OpenHelperManager.getHelper(BottlesActivity.this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch(id) {
            case ICON_LIST_DIALOG:
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setIcon(R.drawable.mood);
                builder.setTitle("贴个标签？");
                BaseAdapter adapter = new BottlesActivity.ListItemAdapter();
                DialogInterface.OnClickListener listener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                if(which == 0) label_name = "travel";
                                else if(which == 1) label_name = "study";
                                else if(which == 2) label_name = "work";
                                else label_name = "travel";

                                DatabaseHelper helper = new DatabaseHelper(getApplicationContext());

                                Label label = Label.getByName(helper,label_name);
                                if(label == null) {
                                    label = new Label(label_name);
                                    label.insert(helper);
                                }
                                //sentencebookList.insertLabel(helper, label);
                                //getLabelsOfDiary(diary,helper);

                            }
                        };
                builder.setAdapter(adapter, listener);
                dialog = builder.create();
                break;
        }
        return dialog;
    }

    class ListItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return imgIds.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position,
                            View contentView, ViewGroup parent) {
            TextView textView =
                    new TextView(BottlesActivity.this);
            //获得array.xml中的数组资源getStringArray返回的是一个String数组
            String text = getResources().getStringArray(R.array.mood)[position];
            textView.setText(text);
            //设置字体大小
            textView.setTextSize(20);
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                    WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(layoutParams);
            //设置水平方向上居中
            textView.setGravity(android.view.Gravity.CENTER_VERTICAL);
            textView.setMinHeight(65);
            //设置文字颜色
            textView.setTextColor(Color.BLACK);
            //设置图标在文字的左边
            textView.setCompoundDrawablesWithIntrinsicBounds(imgIds[position], 0, 0, 0);
            //设置textView的左上右下的padding大小
            textView.setPadding(30, 10, 30, 10);
            //设置文字和图标之间的padding大小
            textView.setCompoundDrawablePadding(25);
            return textView;
        }
    }
}


