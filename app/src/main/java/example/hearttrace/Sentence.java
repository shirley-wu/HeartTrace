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
public class Sentence {
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
            List<Label> labelList = getAllLabel(helper);
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
            QueryBuilder<Label, Integer> qb = helper.getLabelDao().queryBuilder();
            buildLabelWhere(qb.where(), helper);
            return qb.query();
        }catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public static List<Sentence> getByRestrict(DatabaseHelper helper, String text, Date begin, Date end, List<Label> labelList) throws SQLException {
        QueryBuilder<Sentence, Integer> qb = helper.getSentenceDao().queryBuilder();
        Boolean started = false;

        if(labelList != null && labelList.size() > 0){
            if(started){
                qb.where().and();
            } else{
                started = true;
            }
            buildWhere(qb.where(), helper, labelList);
        }
        if(text != null){
            if(started){
                qb.where().and();
            } else{
                started = true;
            }
            buildWhere(qb.where(), text);
        }
        if(begin != null && end != null){
            if(started){
                qb.where().and();
            }
            buildWhere(qb.where(), begin, end);
        }
        return qb.query();
    }

    public static int countByDateLabel (DatabaseHelper helper, Date begin, Date end, List<Label> labelList) {
        try {
            QueryBuilder<Sentence, Integer> queryBuilder = helper.getSentenceDao().queryBuilder();
            buildWhere(queryBuilder.where(), begin, end);
            return queryBuilder.query().size();
        }
        catch(SQLException e) {
            Log.e(TAG, "countByDateLabel: cannot access database", e);
            return -1;
        }
    }

    private void buildLabelWhere(Where<Label, Integer> where, DatabaseHelper helper) {
        try{
            QueryBuilder<SentenceLabel, Integer> sentenceLabelBuilder = helper.getSentenceLabelDao().queryBuilder();
            sentenceLabelBuilder.selectColumns(SentenceLabel.LABEL_TAG);
            sentenceLabelBuilder.where().eq(SentenceLabel.SENTENCE_TAG, this);

            where.in(Label.TAG, sentenceLabelBuilder);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    private static void buildWhere(Where<Sentence, Integer> where, DatabaseHelper helper, List<Label> labelList) {
        try{
            QueryBuilder<SentenceLabel, Integer> sentenceLabelBuilder = helper.getSentenceLabelDao().queryBuilder();
            sentenceLabelBuilder.selectColumns(SentenceLabel.SENTENCE_TAG);

            Where<SentenceLabel, Integer> SentenceLabelWhere = sentenceLabelBuilder.where();
            for(int i = 0; i < labelList.size(); i++) {
                if(i != 0){
                    SentenceLabelWhere.and();
                }
                sentenceLabelBuilder.where().eq(SentenceLabel.LABEL_TAG, labelList.get(i)).and();
            }

            where.in(Sentence.TAG, sentenceLabelBuilder);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    private static void buildWhere(Where<Sentence, Integer> where, String text) throws SQLException {
        String[] keywordList = text.split(" ");
        for(int i = 0; i < keywordList.length; i++) {
            if(i != 0) {
                where.or();
            }
            where.like("text", "%" + keywordList[i] + "%");
        }
    }

    private static void buildWhere(Where<Sentence, Integer> where, Date begin, Date end) throws SQLException {
        where.between("date", begin, end);
    }
}
