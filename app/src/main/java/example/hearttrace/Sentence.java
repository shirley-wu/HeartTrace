package example.hearttrace;

import android.provider.ContactsContract;
import android.provider.Telephony;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by huang on 5/10/2018.
 */

@DatabaseTable(tableName = "Sentence")
public class Sentence { // TODO: quite a lot to revise here. by wxq
    public static final String TAG = "Sentence";

    @DatabaseField(generatedId = true, columnName = TAG)
    private int id;

    @DatabaseField(foreign = true, columnName = "sentencebook")
    private Sentencebook Sentencebook;

    @DatabaseField
    String text;

    @DatabaseField(dataType = DataType.DATE_STRING, columnName = "date")
    protected Date date;

    public Sentence(){
    };

    public Sentence(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public Date getDate(){
        return date;
    }

    public void setDate(){
        if(date == null) {
            date = new Date();
        }
    }

    public void setDate(Date date){
        // dangerous!!!!! for test only.
        this.date = date;
        Log.i(TAG, "setDate: dangerous call!");
    }

    public Sentencebook getSentencebook() {
        return Sentencebook;
    }

    public void setSentencebook(Sentencebook Sentencebook) {
        this.Sentencebook = Sentencebook;
    }

    public void insert(DatabaseHelper helper) {
        try {
            Dao<Sentence, Integer> dao = helper.getSentenceDao();
            Log.i("Sentence", "dao = " + dao + " 插入 Sentence " + this);
            int returnValue = dao.create(this);
            Log.i("Sentence", "插入后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void update(DatabaseHelper helper) {
        try {
            Dao<Sentence, Integer> dao = helper.getSentenceDao();
            Log.i("Sentence", "dao = " + dao + " 更新 Sentence " + this);
            int returnValue = dao.update(this);
            Log.i("Sentence", "更新后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void delete(DatabaseHelper helper) {
        try {
            QueryBuilder<Label, Integer> qb = getLabelQueryBuilder(helper);
            List<Label> labelList = qb.query();
            for(final Label label : labelList) {
                deleteLabel(helper, label);
            }
            Log.i(TAG, "delete: 删除标签对应" + labelList);

            Dao<Sentence, Integer> dao = helper.getSentenceDao();
            Log.i("Sentence", "dao = " + dao + " 删除 Sentence " + this);
            int returnValue = dao.delete(this);
            Log.i("Sentence", "删除后返回值：" + returnValue);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void insertLabel(DatabaseHelper helper, Label label) {
        SentenceLabel SentenceLabel = new SentenceLabel();
        SentenceLabel.setSentence(this);
        SentenceLabel.setLabel(label);
        SentenceLabel.insert(helper);
    }

    public void deleteLabel(DatabaseHelper helper, Label label) {
        SentenceLabel SentenceLabel = new SentenceLabel();
        SentenceLabel.setSentence(this);
        SentenceLabel.setLabel(label);
        SentenceLabel.delete(helper);
    }

    public static List<Sentence> getByDate(DatabaseHelper helper, Date date) {
        try {
            Dao<Sentence, Integer> dao = helper.getSentenceDao();
            List<Sentence> SentenceList = dao.queryBuilder().where().eq("date", date).query();
            return SentenceList;
        }
        catch(SQLException e) {
            Log.e(TAG, "getSentenceByDate: cannot query");
            return null;
        }
    }

    public static List<Sentence> getAll(DatabaseHelper helper){
        try {
            Dao<Sentence, Integer> dao = helper.getSentenceDao();
            return dao.queryForAll();
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public List<Label> getAllLabel(DatabaseHelper helper){
        try{
            QueryBuilder<Label, Integer> labelDao = getLabelQueryBuilder(helper);
            return labelDao.query();
        }catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Sentence> getByRestrict(DatabaseHelper helper, String text, Date begin, Date end, List<Label> labelList) throws SQLException {
        QueryBuilder<Sentence, Integer> qb;
        if(labelList != null && labelList.size() > 0){
            qb = getSentenceQueryBuilder(helper, labelList);
        }
        else{
            qb = helper.getSentenceDao().queryBuilder();
        }
        if(text != null){
            buildWhere(qb.where(), text);
        }
        if(begin != null && end != null){
            buildWhere(qb.where(), begin, end);
        }
        return qb.query();
    }

    public static int countByDateLabel (DatabaseHelper helper, Date begin, Date end, List<Label> labelList) {
        try {
            QueryBuilder<Sentence, Integer> queryBuilder = helper.getSentenceDao().queryBuilder();
            queryBuilder.where().between("Date", begin, end);
            return queryBuilder.query().size();
        }
        catch(SQLException e) {
            Log.e(TAG, "countByDateLabel: cannot access database", e);
            return -1;
        }
    }

    private QueryBuilder<Label, Integer> getLabelQueryBuilder(DatabaseHelper helper) {
        // TODO
        return null;
    }

    private static QueryBuilder<Sentence, Integer> getSentenceQueryBuilder(DatabaseHelper helper, List<Label> labelList) {
        // TODO
        return null;
    }

    private static void buildWhere(Where<Sentence, Integer> where, String text) throws SQLException {
        String[] keywordList = text.split(" ");
        where.and();
        for(final String keyword : keywordList) {
            where.or().like("text", keyword);
        }
    }

    private static void buildWhere(Where<Sentence, Integer> where, Date begin, Date end) throws SQLException {
        where.and();
        where.between("date", begin, end);
    }
}
