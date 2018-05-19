package com.example.dell.diary;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    public List<DiaryCard> diaryCardList = new ArrayList<>();
    private DiaryCardAdapter adapter;
    int flag = 0;
    private FloatingActionButton add;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView)findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu_white);
        }
        navView.setCheckedItem(R.id.nav_user);
        //navView.setItemIconTintList(null);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        FloatingActionButton test = (FloatingActionButton) findViewById(R.id.test);
        test.setVisibility(View.INVISIBLE);
        final FloatingActionButton addDiary = (FloatingActionButton) findViewById(R.id.add_diary);
        final FloatingActionButton addBottle = (FloatingActionButton) findViewById(R.id.add_bottle);
        addDiary.setVisibility(View.INVISIBLE);
        addBottle.setVisibility(View.INVISIBLE);
        add = (FloatingActionButton) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag == 0) {
                    addDiary.setVisibility(View.VISIBLE);
                    addBottle.setVisibility(View.VISIBLE);
                    flag = 1;
                }
                else{
                    addDiary.setVisibility(View.INVISIBLE);
                    addBottle.setVisibility(View.INVISIBLE);
                    flag = 0;
                }
            }
        });
        addDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "写新日记", Toast.LENGTH_SHORT).show();
                diaryCardList.add(0,new DiaryCard(2018,5,14,"周一",R.drawable.happy_black,"一篇新日记"));
                adapter.notifyItemInserted(0);
                recyclerView.scrollToPosition(0);
            }
        });
        addBottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "写新瓶子", Toast.LENGTH_SHORT).show();
            }
        });
        Button enterBottle = (Button)findViewById(R.id.enter_bottle);
        enterBottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BottlesActivity.class);
                startActivity(intent);
            }
        });
        initDiaryCard();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DiaryCardAdapter(diaryCardList);
        recyclerView.setAdapter(adapter);
    }

    protected void onResume(){
        super.onResume();
        final FloatingActionButton addDiary = (FloatingActionButton) findViewById(R.id.add_diary);
        final FloatingActionButton addBottle = (FloatingActionButton) findViewById(R.id.add_bottle);
        addDiary.setVisibility(View.INVISIBLE);
        addBottle.setVisibility(View.INVISIBLE);
        flag = 0;
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
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.calendar:
                Toast.makeText(this, "打开日历", Toast.LENGTH_SHORT).show();
                break;
            case R.id.search:
                //Toast.makeText(this, "搜索你的日记", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.add:
                Toast.makeText(this, "写新的日记", Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_setting:
                Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }
}
