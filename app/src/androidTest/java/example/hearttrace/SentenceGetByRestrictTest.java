package example.hearttrace;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import junit.framework.TestResult;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by huang on 5/17/2018.
 */

public class SentenceGetByRestrictTest extends InstrumentationTestCase {

    private static final String TAG = "SentenceGetByRestrictTest";

    private DatabaseHelper databaseHelper;
    private Dao<Sentence, Integer> dao;

    private List<Label> labels = new ArrayList();
    private List<Sentence> sentenceShirly = new ArrayList();
    private List<Sentence> sentenceLisa = new ArrayList();
    private List<Sentence> sentenceMmp = new ArrayList();

    @Before
    public void setUp() throws SQLException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        databaseHelper = OpenHelperManager.getHelper(appContext, DatabaseHelper.class);
        dao = databaseHelper.getSentenceDao();

        labels.add(new Label("label1sadfvabe"));
        labels.add(new Label("janvp"));
        labels.add(new Label("fancevbotvptjvo"));

        for(final Label label : labels) {
            label.insert(databaseHelper);
        }

        String text;

        text = "hello, this is Shirley";
        for(int i = 1; i <= 30; i++) {
            Sentence sentence = new Sentence(text);
            sentence.setDate(new Date(2016, 1, i));
            sentence.insert(databaseHelper);
            sentence.insertLabel(databaseHelper, labels.get(0));
            sentenceShirly.add(sentence);
        }

        text = "hello, this is Lisa";
        for(int i = 1; i <= 29; i++) {
            Sentence sentence = new Sentence(text);
            sentence.setDate(new Date(2016, 1, i));
            sentence.insert(databaseHelper);
            sentenceLisa.add(sentence);
            sentence.insertLabel(databaseHelper, labels.get(1));
        }

        text = "emmmmmmmmmmm";
        for(int i = 1; i <= 28; i++) {
            Sentence sentence = new Sentence(text);
            sentence.setDate(new Date(2015, 1, i));
            sentence.insert(databaseHelper);
            sentenceLisa.add(sentence);
            sentence.insertLabel(databaseHelper, labels.get(0));
            sentence.insertLabel(databaseHelper, labels.get(1));
        }
    }

    @After
    public void tearDown() {
        Log.d(TAG, "tearDown");
        for(final Label label : labels) {
            label.delete(databaseHelper);
        }
        for(final Sentence sentence : sentenceShirly) {
            sentence.delete(databaseHelper);
        }
        for(final Sentence sentence : sentenceLisa) {
            sentence.delete(databaseHelper);
        }
        for(final Sentence sentence : sentenceMmp) {
            sentence.delete(databaseHelper);
        }
        OpenHelperManager.releaseHelper();
    }

    // @Test
    public void testWhereSingleText() throws SQLException {
        QueryBuilder<Sentence, Integer> qb = databaseHelper.getSentenceDao().queryBuilder();
        Where<Sentence, Integer> where = qb.where();
        where.like("text", "%hi%");
        where.or(1);
        Log.d(TAG, "instance initializer: " + qb.prepareStatementString());
    }

    // @Test
    public void testWhereMultipleText() throws SQLException {
        QueryBuilder<Sentence, Integer> qb = databaseHelper.getSentenceDao().queryBuilder();
        Where<Sentence, Integer> where = qb.where();
        where.like("text", "%hi%");
        where.like("text", "%hello%");
        where.or(2);
        Log.d(TAG, "instance initializer: " + qb.prepareStatementString());
    }

    @Test
    public void testBuildWhereSingleText() throws SQLException {
        QueryBuilder<Sentence, Integer> qb = databaseHelper.getSentenceDao().queryBuilder();
        Sentence.buildWhere(qb.where(), "hellohi");
        Log.d(getClass().getName(), "testBuild: " + qb.prepareStatementString());
    }

    @Test
    public void testBuildWhereMultilpleText() throws SQLException {
        QueryBuilder<Sentence, Integer> qb = databaseHelper.getSentenceDao().queryBuilder();
        Sentence.buildWhere(qb.where(), "hello hi lueluelue blablabla");
        Log.d(getClass().getName(), "testBuild: " + qb.prepareStatementString());
    }

    @Test
    public void testBuildWhereDate() throws SQLException {
        QueryBuilder<Sentence, Integer> qb = databaseHelper.getSentenceDao().queryBuilder();
        Sentence.buildWhere(qb.where(), new Date(1998, 2, 19), new Date(2000, 2, 4));
        Log.d(getClass().getName(), "testBuild: " + qb.prepareStatementString());
    }

    @Test
    public void testBuildWhereDateAndSingleText() throws SQLException {
        QueryBuilder<Sentence, Integer> qb = databaseHelper.getSentenceDao().queryBuilder();
        Where<Sentence, Integer> w = qb.where();
        Sentence.buildWhere(w, "hi");
        Sentence.buildWhere(w, new Date(1998, 2, 19), new Date(2000, 2, 4));
        w.and(2);
        Log.d(getClass().getName(), "testBuild: " + qb.prepareStatementString());
    }

    @Test
    public void testBuildWhereDateAndMultipleText() throws SQLException {
        QueryBuilder<Sentence, Integer> qb = databaseHelper.getSentenceDao().queryBuilder();
        Where<Sentence, Integer> w = qb.where();
        Sentence.buildWhere(w, "hi hello");
        Sentence.buildWhere(w, new Date(1998, 2, 19), new Date(2000, 2, 4));
        w.and(2);
        Log.d(getClass().getName(), "testBuild: " + qb.prepareStatementString());
    }

    @Test
    public void testBuildQuerySingleLabel() throws SQLException {
        QueryBuilder<Sentence, Integer> qb = databaseHelper.getSentenceDao().queryBuilder();
        List<Label> labelList = new ArrayList();
        labelList.add(labels.get(0));
        Sentence.buildQuery(qb, databaseHelper, labelList);
        Log.d(getClass().getName(), "testBuild: " + qb.prepareStatementString());
    }

    @Test
    public void testBuildQueryMultipleLabel() throws SQLException {
        QueryBuilder<Sentence, Integer> qb = databaseHelper.getSentenceDao().queryBuilder();
        Sentence.buildQuery(qb, databaseHelper, labels);
        Log.d(getClass().getName(), "testBuild: " + qb.prepareStatementString());
    }

    @Test
    public void testGetByRestrictText() throws SQLException {
        List<Sentence> list;

        list = Sentence.getByRestrict(databaseHelper, "Shirley", null, null, null);
        assertEquals(30, list.size());

        list = Sentence.getByRestrict(databaseHelper, "Lisa", null, null, null);
        assertEquals(29, list.size());

        list = Sentence.getByRestrict(databaseHelper, "hello Lisa", null, null, null);
        assertEquals(30 + 29, list.size());
    }

    @Test
    public void testGetByRestrictDate() throws SQLException {
        List<Sentence> list;
        list = Sentence.getByRestrict(databaseHelper, null, new Date(2016, 1, 5), new Date(2016, 1, 23), null);
        assertEquals(2 * (23 - 5 + 1), list.size());
    }

    @Test
    public void testGetByRestrictLabel() throws SQLException {
        List<Label> labelList;
        List<Sentence> list;

        labelList = new ArrayList();
        labelList.add(labels.get(0));
        list = Sentence.getByRestrict(databaseHelper, null, null, null, labelList);
        assertEquals(30 + 28, list.size());

        labelList = new ArrayList();
        labelList.add(labels.get(1));
        list = Sentence.getByRestrict(databaseHelper, null, null, null, labelList);
        assertEquals(29 + 28, list.size());

        labelList = new ArrayList();
        labelList.add(labels.get(1));
        labelList.add(labels.get(0));
        list = Sentence.getByRestrict(databaseHelper, null, null, null, labelList);
        assertEquals(28, list.size());
    }

    @Test
    public void testGetByRestrictAll() throws SQLException {
        List<Label> labelList = new ArrayList();
        List<Sentence> list;

        labelList.add(labels.get(1));
        list = Sentence.getByRestrict(databaseHelper, "this",
                new Date(2016, 1, 8),
                new Date(2016, 1, 27),
                labelList);
        assertEquals(27 - 8 + 1, list.size());
    }
}
