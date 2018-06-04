package com.example.dell.db;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by chen on 2018/5/13.
 */

@DatabaseTable(tableName = "sentencebook")
public class Sentencebook {

    private static final String TAG = "Sentencebook";

    private static final String default_name = "default";

    @DatabaseField(generatedId = true, columnName = TAG)
    private int id;
    @DatabaseField
    private String sentencebookName;

    public Sentencebook(){};
    public Sentencebook(String sentencebookName)
    {
        this.sentencebookName = sentencebookName;
    }

    public String getSentencebookName()
    {
        return sentencebookName;
    }

    public void setSentencebookName(String sentencebookName) {
        this.sentencebookName = sentencebookName;
    }

    public List<Sentence> getAllSubSentence(DatabaseHelper helper) {
        try {
            Dao<Sentence, Integer> dao = helper.getSentenceDao();

            List<Sentence> subSentenceList = dao.queryBuilder().where().eq("Sentencebook", sentencebookName).query();

            return subSentenceList;
        }catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }

    public void deteleSubSentence(DatabaseHelper helper) {
        try {
            Dao<Sentence, Integer> dao = helper.getSentenceDao();
            // dao.deleteBuilder().where().eq("Sentencebook", sentencebookName).delete();
            // TODO: Cannot compile
        }catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't dao database", e);
            throw new RuntimeException(e);
        }
    }
}