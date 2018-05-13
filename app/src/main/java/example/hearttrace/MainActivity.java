package example.hearttrace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.sql.SQLException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private DatabaseHelper databaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = (EditText) findViewById(R.id.edit_text);


        Button button = (Button) findViewById(R.id.button_submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper Helper = new DatabaseHelper(getApplicationContext());
                Diary diary = new Diary();
                diary.setText(editText.getText().toString());
                diary.setDate();
                Helper.insertDiary(diary);
                Label label = new Label("happy");
                Label label1 = new Label("sad");
                Label label2 = new Label("normal");
                Log.i(Diary.TAG, "this is the hot point");
                Helper.insertLabel(new Label("happy"));
                Helper.insertDiaryLabel(new DiaryLabel(diary, label1));
                Helper.insertDiaryLabel(new DiaryLabel(diary, label1));
                try {
                    List<Label> labelNewDiary = Helper.lookupLabelForDiary(diary);
                    List<Diary> diaryHappy = Helper.lookupDiaryForLabel(label1);
                    List<Label> labelList = Helper.getAllLabel();
                    List<Diary> diaryList = Helper.getAllDiary();
                    for(Diary i :diaryHappy){
                        Log.i(Diary.TAG, i.getDate()+i.getText());
                    }
                    Log.i(Diary.TAG, "=====================================");
                    for(Label i : labelNewDiary){
                        Log.i(Label.TAG, i.getLabelname());
                    }
                    Log.i(Diary.TAG, "=====================================");
                    for(Diary i : diaryList ){
                        Log.i(Diary.TAG, i.getDate()+i.getText());
                    }
                    Log.i(Diary.TAG, "=====================================");
                    for(Label i : labelList){
                        Log.i(Label.TAG, i.getLabelname());
                    }
                    Log.i(Label.TAG, "=====================================");
                    Helper.close();
                } catch (SQLException e) {
                    Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
                    throw new RuntimeException(e);
                }
            }
        });
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
            databaseHelper = OpenHelperManager.getHelper(MainActivity.this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

}
