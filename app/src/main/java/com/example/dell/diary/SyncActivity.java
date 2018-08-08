package com.example.dell.diary;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.PeriodicSync;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class SyncActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{
    private TextView spinnertext;
    private Spinner spinner;
    private ArrayAdapter spinnerAdapter;
    private static final String ACCOUNT_TYPE = "dell.example.com";
    private static final String AUTHORITY = "com.example.dell.sync.stub.provider";
    private String name = "stub";
    private SwitchCompat syncEnableswitch;
    private SwitchCompat syncAutoSwitch;
    private TextView syncableT;
    private TextView syncAutoT;
    private TextView syncOneT;
    private int  syncable ;
    private List<PeriodicSync> list;
    private Account mAccount;
    private LinearLayout autoLinear;
    private long period;
    private AccountManager accountManager;
    private View line1;
    private View line2;
    private View line3;
    private View line4;
    private LinearLayout syncLinear1;
    private LinearLayout syncLinear2;
    private LinearLayout syncLinear3;
    private LinearLayout syncLinear4;
    private LinearLayout syncLinear5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        ToolBarInit();
        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);
        line3 = findViewById(R.id.line3);
        line4 = findViewById(R.id.line4);
        syncLinear1 = findViewById(R.id.sync_linear1);
        syncLinear2 = findViewById(R.id.sync_linear2);
        syncLinear3 = findViewById(R.id.sync_linear3);
        syncLinear4 = findViewById(R.id.sync_linear4);
        syncLinear5 = findViewById(R.id.sync_linear5);
        syncableT = findViewById(R.id.syncableText);
        syncEnableswitch = findViewById(R.id.syncable_switch);
        syncEnableswitch.setOnCheckedChangeListener(this);
        syncAutoT = findViewById(R.id.auto_syncText);
        syncOneT = findViewById(R.id.one_syncText);
        syncAutoSwitch = findViewById(R.id.syncAuto_switch);
        syncAutoSwitch.setOnCheckedChangeListener(this);
        spinner = findViewById(R.id.sync_period_spinner);
        spinnertext = findViewById(R.id.autoSetting_syncText);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.sync_period, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setVisibility(View.VISIBLE);
        mAccount = new Account(name,ACCOUNT_TYPE);
        accountManager = (AccountManager)SyncActivity.this.getSystemService(ACCOUNT_SERVICE);


        class SpinnerXMLSelectedListener implements AdapterView.OnItemSelectedListener {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {
                spinnertext.setText((String)spinnerAdapter.getItem(arg2));
                getSyncStatus();
                if(list.size()>0){
                    Log.i("在列表选择时自动同步？？？", "onCheckedChanged:是 ");
                }
                else{
                    Log.i("在列表选择时自动同步？？？", "onCheckedChanged:否 ");
                }
                if(syncable>0){
                    Log.i("在列表中允许同步？", "onItemSelected:允许 ");
                }
                else{
                    Log.i("在列表中允许同步？", "onItemSelected: 不允许同步");
                }

                if(syncable>0) {
                    if(arg2 == 0){
                        // Toast.makeText(SyncActivity.this, "请选择", Toast.LENGTH_SHORT).show();
                        ContentResolver.removePeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY);
                    }
                    else if (arg2 == 1) {
                        Toast.makeText(SyncActivity.this, "每半小时", Toast.LENGTH_SHORT).show();
                        ContentResolver.removePeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY);
                        ContentResolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, 1800);
                    }
                    else if (arg2 == 2) {
                        Toast.makeText(SyncActivity.this, "每一小时", Toast.LENGTH_SHORT).show();
                        ContentResolver.removePeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY);
                        ContentResolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, 3600);
                    }
                    else if (arg2 == 3) {
                        Toast.makeText(SyncActivity.this, "每两小时", Toast.LENGTH_SHORT).show();
                        ContentResolver.removePeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY);
                        ContentResolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, 7200);
                    }
                    else if (arg2 == 4) {
                        Toast.makeText(SyncActivity.this, "每天", Toast.LENGTH_SHORT).show();
                        ContentResolver.removePeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY);
                        ContentResolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, 86400);
                    }
                    else if (arg2 == 5) {
                        Toast.makeText(SyncActivity.this, "每周", Toast.LENGTH_SHORT).show();
                        ContentResolver.removePeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY);
                        ContentResolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, 604800);
                    }
                    getSyncStatus();
                    if(list.size()>0){
                        Log.i("列表结束后自动同步？？？", "onCheckedChanged:是 ");
                    }
                    else{
                        Log.i("列表结束后自动同步？？？", "onCheckedChanged:否 ");
                    }
                }



            }


            public void onNothingSelected(AdapterView<?> arg0) {
            }
        }
        spinner.setOnItemSelectedListener(new SpinnerXMLSelectedListener());

        if(accountManager.addAccountExplicitly(mAccount, null,null)){
            Log.i("SyncAvtivity", "onCreate:account Create OK ");
        }
        else{
            Log.i("SyncAvtivity", "onCreate:cannot create ");
        }
        getSyncStatus();
       if(list.size()>0){
            Log.i("创建时自动同步？？？", "onCheckedChanged:是 ");
        }
        else{
            Log.i("创建时自动同步？？？", "onCheckedChanged:否 ");
        }


        if(syncable > 0){
            syncEnableswitch.setChecked(true);
            syncableT.setText("允许同步");
            syncLinear2.setVisibility(View.VISIBLE);
            if(list.size() > 0) {
                syncAutoSwitch.setChecked(true);
                syncAutoT.setText("自动同步开");
                syncLinear5.setVisibility(View.VISIBLE);
                line4.setVisibility(View.VISIBLE);
                period = list.get(0).period ;
               /* String string = spinnertext.getText().toString();*/
               Log.i("实验咯", "onCheckedChanged: "+period);
                if (period == 1800) {
                   /* Toast.makeText(SyncActivity.this, "每半小时", Toast.LENGTH_SHORT).show();
                    ContentResolver.removePeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY);
                    ContentResolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, 1800);*/
                    spinnertext.setText("每半小时");
                    spinner.setSelection(1);
                }
                else if (period == 3600) {
                  /*  Toast.makeText(SyncActivity.this, "每一小时", Toast.LENGTH_SHORT).show();
                    ContentResolver.removePeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY);
                    ContentResolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, 3600);*/
                    spinnertext.setText("每一小时");
                    spinner.setSelection(2);
                }
                else if (period == 7200) {
                  /*  Toast.makeText(SyncActivity.this, "每两小时", Toast.LENGTH_SHORT).show();
                    ContentResolver.removePeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY);
                    ContentResolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, 7200);*/
                    spinnertext.setText("每两小时");
                    spinner.setSelection(3);
                }
                else if (period == 86400) {
                  /*  Toast.makeText(SyncActivity.this, "每天", Toast.LENGTH_SHORT).show();
                    ContentResolver.removePeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY);
                    ContentResolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, 86400);*/
                    spinnertext.setText("每天");
                    spinner.setSelection(4);
                }
                else if (period == 86400 * 7) {
                   /* Toast.makeText(SyncActivity.this, "每周", Toast.LENGTH_SHORT).show();
                    ContentResolver.removePeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY);
                    ContentResolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, 604800);*/
                    spinnertext.setText("每周");
                    spinner.setSelection(5);
                }
                getSyncStatus();
            }
            else{
                syncAutoSwitch.setChecked(false);
                syncAutoT.setText("自动同步关");
                syncLinear5.setVisibility(View.INVISIBLE);
                line4.setVisibility(View.INVISIBLE);
            }
        }
        else{
            syncEnableswitch.setChecked(false);
            syncableT.setText("禁止同步");
            syncLinear2.setVisibility(View.INVISIBLE);
        }
        syncLinear3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
                bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                ContentResolver.requestSync(mAccount, AUTHORITY,bundle);
            }
        });



    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.i("onCheckedChanged", "onCheckedChanged: 已进入");
        if(list.size()>0){
            Log.i("自动同步？？？", "onCheckedChanged:是 ");
        }
        else{
            Log.i("自动同步？？？", "onCheckedChanged:否 ");
        }
        switch (buttonView.getId()){
            case R.id.syncable_switch:
                if(isChecked){
                    syncableT.setText("允许同步");
//                    autoLinear.setVisibility(AutoCard.VISIBLE);
//                    oneSyncCard.setVisibility(oneSyncCard.VISIBLE);
                    syncLinear2.setVisibility(View.VISIBLE);
                    ContentResolver.setIsSyncable(mAccount,AUTHORITY, 1);
                   if(list.size()>0){
                        syncAutoSwitch.setChecked(true);
                        syncAutoT.setText("自动同步");
                       //autoSettingCard.setVisibility(autoSettingCard.VISIBLE);
                       syncLinear5.setVisibility(View.VISIBLE);
                       line4.setVisibility(View.VISIBLE);
                    }
                    else{
                        syncAutoSwitch.setChecked(false);
                        syncAutoT.setText("自动同步");
                       //autoSettingCard.setVisibility(autoSettingCard.INVISIBLE);
                       syncLinear5.setVisibility(View.INVISIBLE);
                       line4.setVisibility(View.INVISIBLE);
                    }

                }
                else {
                    syncableT.setText("禁止同步");
                    syncLinear2.setVisibility(View.INVISIBLE);
//                    autoLinear.setVisibility(AutoCard.INVISIBLE);
//                    oneSyncCard.setVisibility(oneSyncCard.INVISIBLE);
                    ContentResolver.setIsSyncable(mAccount,AUTHORITY,0);

                }
                getSyncStatus();
                if(list.size()>0){
                    Log.i("自动同步？？？", "onCheckedChanged:是 ");
                }
                else{
                    Log.i("自动同步？？？", "onCheckedChanged:否 ");
                }
                break;
            case R.id.syncAuto_switch:
                if(isChecked){
                    syncAutoT.setText("自动同步");
                    //autoSettingCard.setVisibility(autoSettingCard.VISIBLE);
                    syncLinear5.setVisibility(View.VISIBLE);
                    line4.setVisibility(View.VISIBLE);
                    String string = spinnertext.getText().toString();
                    Log.i("实验咯", "onCheckedChanged: "+string);
                    if(string.equals("请选择")){
                        // Toast.makeText(SyncActivity.this, "请选择", Toast.LENGTH_SHORT).show();
                        ContentResolver.removePeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY);
                    }
                    else if (string.equals("每半小时")) {
                        Toast.makeText(SyncActivity.this, "每半小时", Toast.LENGTH_SHORT).show();
                        ContentResolver.removePeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY);
                        ContentResolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, 1800);
                    }
                    else if (string.equals("每一小时")) {
                        Toast.makeText(SyncActivity.this, "每一小时", Toast.LENGTH_SHORT).show();
                        ContentResolver.removePeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY);
                        ContentResolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, 3600);
                    }
                    else if (string.equals("每两小时")) {
                        Toast.makeText(SyncActivity.this, "每两小时", Toast.LENGTH_SHORT).show();
                        ContentResolver.removePeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY);
                        ContentResolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, 7200);
                    }
                    else if (string.equals("每天")) {
                        Toast.makeText(SyncActivity.this, "每天", Toast.LENGTH_SHORT).show();
                        ContentResolver.removePeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY);
                        ContentResolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, 86400);
                    }
                    else if (string.equals("每周")) {
                        Toast.makeText(SyncActivity.this, "每周", Toast.LENGTH_SHORT).show();
                        ContentResolver.removePeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY);
                        ContentResolver.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, 604800);
                    }
                    getSyncStatus();


                }
                else{
                    syncAutoT.setText("自动同步");
                    syncLinear5.setVisibility(View.INVISIBLE);
                    line4.setVisibility(View.INVISIBLE);
                    //autoSettingCard.setVisibility(autoSettingCard.INVISIBLE);
                    ContentResolver.removePeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY);

                }
                getSyncStatus();
                if(list.size()>0){
                    Log.i("自动同步？？？", "onCheckedChanged:是 ");
                }
                else{
                    Log.i("自动同步？？？", "onCheckedChanged:否 ");
                }
            default:
                break;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.synctoolbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //mDrawLayout.openDrawer(GravityCompat.START);
                finish();
                break;
        }
        return true;
    }

    public void getSyncStatus(){
        syncable = ContentResolver.getIsSyncable(mAccount,AUTHORITY);
        list = ContentResolver.getPeriodicSyncs(mAccount,AUTHORITY);
    }

    public void  ToolBarInit(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.sync_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){

            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.menu_white);
        }
        actionBar.setTitle("同步设置");
    }

    public void viewInit(){
        syncableT = findViewById(R.id.syncableText);
        syncEnableswitch = findViewById(R.id.syncable_switch);
        syncEnableswitch.setOnCheckedChangeListener(this);
        syncAutoT = findViewById(R.id.auto_syncText);
        syncOneT = findViewById(R.id.one_syncText);

        syncAutoSwitch = findViewById(R.id.syncAuto_switch);
        syncAutoSwitch.setOnCheckedChangeListener(this);

        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);
        line3 = findViewById(R.id.line3);
        line4 = findViewById(R.id.line4);
        syncLinear1 = findViewById(R.id.sync_linear1);
        syncLinear2 = findViewById(R.id.sync_linear2);
        syncLinear3 = findViewById(R.id.sync_linear3);
        syncLinear4 = findViewById(R.id.sync_linear4);
        syncLinear5 = findViewById(R.id.sync_linear5);

        spinner = findViewById(R.id.sync_period_spinner);
        spinnertext = findViewById(R.id.autoSetting_syncText);

    }

}
