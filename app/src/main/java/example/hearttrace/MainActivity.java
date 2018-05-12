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
                RuntimeExceptionDao<Diary, Integer> diaryDao = Helper.getRuntimeExceptionDiaryDao();
                Diary diary = new Diary();
                diary.setText(editText.getText().toString());
                diary.setDate();
                diaryDao.create(diary);
                List<Diary> diaryList = diaryDao.queryForAll();
                for(Diary i : diaryList ){
                    Log.i(Diary.TAG, i.getDate()+i.getText());
                }
                Helper.close();
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
