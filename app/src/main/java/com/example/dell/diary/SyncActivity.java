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
    private boolean syncAuto = true;
    private List<PeriodicSync> list;
    private Account mAccount;
    private CardView AutoCard;
    private CardView oneSyncCard;
    private CardView autoSettingCard;
    private LinearLayout autoLinear;
    private long period;
    private AccountManager accountManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        ToolBarInit();

        mAccount = new Account(name,ACCOUNT_TYPE);
        accountManager = (AccountManager)SyncActivity.this.getSystemService(ACCOUNT_SERVICE);
        if(accountManager.addAccountExplicitly(mAccount, null,null)){
            Log.i("SyncAvtivity", "onCreate:account Create OK ");
        }
        else{
            Log.i("SyncAvtivity", "onCreate:cannot create ");
        }

        getSyncStatus();
        viewInit();

        if(syncable > 0){
            syncEnableswitch.setChecked(true);
            syncableT.setText("允许同步");
            autoLinear.setVisibility(autoLinear.VISIBLE);
            oneSyncCard.setVisibility(oneSyncCard.VISIBLE);
            if(syncAuto) {
                syncAutoSwitch.setChecked(true);
                syncAutoT.setText("自动同步开");
                autoSettingCard.setVisibility(autoSettingCard.VISIBLE);
            }
            else{
                syncAutoSwitch.setChecked(false);
                syncAutoT.setText("自动同步关");
                autoSettingCard.setVisibility(autoSettingCard.INVISIBLE);
            }
        }
        else{
            syncEnableswitch.setChecked(false);
            syncableT.setText("禁止同步");
            autoLinear.setVisibility(autoLinear.INVISIBLE);
            oneSyncCard.setVisibility(oneSyncCard.INVISIBLE);
            autoSettingCard.setVisibility(autoSettingCard.INVISIBLE);
        }
        oneSyncCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
                bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                ContentResolver.requestSync(mAccount, AUTHORITY,bundle);
            }
        });


        class SpinnerXMLSelectedListener implements AdapterView.OnItemSelectedListener {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {
                spinnertext.setText("选择："+spinnerAdapter.getItem(arg2));
                getSyncStatus();
                if(syncable>0 && syncAuto) {
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
                }



            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        }
        spinner = findViewById(R.id.sync_period_spinner);
        spinnertext = findViewById(R.id.autoSetting_syncText);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.sync_period, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
        spinner.setVisibility(View.VISIBLE);


    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.syncable_switch:
                if(isChecked){
                    syncableT.setText("允许同步");
                    autoLinear.setVisibility(AutoCard.VISIBLE);
                    oneSyncCard.setVisibility(oneSyncCard.VISIBLE);
                    ContentResolver.setIsSyncable(mAccount,AUTHORITY, 1);
                    if(syncAuto) {
                        syncAutoSwitch.setChecked(true);
                        syncAutoT.setText("自动同步开");
                        syncAutoSettingOn();
                    }
                    else{
                        syncAutoSwitch.setChecked(false);
                        syncAutoT.setText("自动同步关");
                        syncAutoSettingOff();
                    }
                }
                else {
                    syncableT.setText("禁止同步");
                    autoLinear.setVisibility(AutoCard.INVISIBLE);
                    oneSyncCard.setVisibility(oneSyncCard.INVISIBLE);
                    ContentResolver.setIsSyncable(mAccount,AUTHORITY,0);
                    syncAutoSettingOff();
                }
                getSyncStatus();
                if(syncAuto){
                    Log.i("自动同步", "onCheckedChanged: 1");
                }
                else{
                    Log.i("自动同步", "onCheckedChanged: 0");
                }
                break;
            case R.id.syncAuto_switch:
                if(isChecked){
                    syncAutoT.setText("自动同步开");
                    syncAutoSettingOn();
                }
                else{
                    syncAutoT.setText("自动同步关");
                    syncAutoSettingOff();
                }
                getSyncStatus();
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
    public void syncAutoSettingOn(){
        autoSettingCard.setVisibility(autoSettingCard.VISIBLE);
        ContentResolver.setSyncAutomatically(mAccount,AUTHORITY, true);

    }
    public void syncAutoSettingOff(){
        autoSettingCard.setVisibility(autoSettingCard.INVISIBLE);
        ContentResolver.setSyncAutomatically(mAccount,AUTHORITY, false);
    }
    public void getSyncStatus(){
        syncable = ContentResolver.getIsSyncable(mAccount,AUTHORITY);
        syncAuto = ContentResolver.getSyncAutomatically(mAccount,AUTHORITY);
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
        AutoCard = findViewById(R.id.AutoCard);
        oneSyncCard = findViewById(R.id.OneSyncCard);
        autoLinear = findViewById(R.id.autoLinear);
        autoSettingCard = findViewById( R.id.AutoSettingCard);
    }

}
