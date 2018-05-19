package example.hearttrace;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;

/**
 * Created by chen on 5/19/2018.
 */

@DatabaseTable(tableName = "SearchHistory")
public class SearchHistory {
    public static final String TAG = "SearchHistory";

    @DatabaseField(generatedId = true, columnName = TAG)
    private int id;

    @DatabaseField(unique = true)
    String entry;

    public SearchHistory() {
    };

    public SearchHistory(String entry) {
        this.entry = entry;
    }

    public String getEntry() {
        return this.entry;
    }

    public void deleteEntry(DatabaseHelper helper) {
        try {
            Dao<SearchHistory, Integer> dao = helper.getSearchHistoryDao();

            dao.delete(this);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static void deleteAllEntry(DatabaseHelper helper) {
        try {
            Dao<SearchHistory, Integer> dao = helper.getSearchHistoryDao();

            dao.deleteBuilder().delete();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }
}