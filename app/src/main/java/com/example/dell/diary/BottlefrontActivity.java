package com.example.dell.diary;

import android.content.Context;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class BottlefrontActivity extends AppCompatActivity {
    private CoordinatorLayout container;

    public static final String BOTTLE_NAME = "bottle_name";
    public static final String BOTTLE_IMAGE_ID = "bottle_image_id";


    private Note[] notes = {new Note(2018, 2,4,"星期日","我发现了一个秘密"), new Note(2018, 5,15,"星期一","我做了一个梦"),  new Note(2018, 3,20,"星期三","不知如何是好")};
    private List<Note> noteList = new ArrayList<>();

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottletoolbar, menu);
        return true;
    }


    private NoteAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottlefront);
        Intent intent = getIntent();
        String bottleName = intent.getStringExtra(BOTTLE_NAME);
        int bottleImageId  = intent.getIntExtra(BOTTLE_IMAGE_ID, 0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.bottle_front_toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView bottleImageView = (ImageView) findViewById(R.id.bottle_image_view);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(bottleName);
        Glide.with(this).load(bottleImageId).into(bottleImageView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.bottle_add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BottlefrontActivity.this, TicketEditActivity.class);
                startActivity(intent);
            }
        });


        initNotes();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_note_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NoteAdapter(noteList);
        recyclerView.setAdapter(adapter);
    }
    public void initNotes(){
        noteList.clear();
        for( int i = 0; i<2; i++){
            noteList.add(notes[i]);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.delete:
                container=(CoordinatorLayout) findViewById(R.id.bottlefront_coordinatorLayout);
                Snackbar.make(container, "确认删除？",Snackbar.LENGTH_SHORT).setAction("Undo",new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Toast.makeText(BottlefrontActivity.this, "成功拯救一个瓶子",Toast.LENGTH_SHORT).show();
                    }
                }).show();

                break;
            case R.id.settings:
                Toast.makeText(this, "You clicked settings", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

}
