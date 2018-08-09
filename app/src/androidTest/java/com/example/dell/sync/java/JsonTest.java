package com.example.dell.sync.java;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;
import com.example.dell.db.Diarybook;
import com.example.dell.db.Sentence;
import com.example.dell.db.Sentencebook;
import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wu-pc on 2018/7/7.
 */

public class JsonTest extends InstrumentationTestCase {

    final static private String TAG = "JsonTest";

    private Diarybook diarybook = new Diarybook();

    private Diary d1 = new Diary();

    private Diary d2 = new Diary("2: hi");

    @Before
    public void setUp() throws SQLException {
        diarybook.setId(4399);

        d1.setText("1: This is Jodie");
        d1.setHtmlText("<h>1:</h> <p>This is <b>Jodie</b></p>");
        d1.setDate();
        d1.setIslike(true);
        d1.setLetterSpacing(1.5F);
        d1.setLineSpacingMultiplier(15);
        d1.setLineSpacingExtra(20);
        d1.setTextSize(0.2F);
        d1.setTextAlignment(12);

        d1.setDiarybook(diarybook);

        d2.setDate();

        d2.setDiarybook(diarybook);
    }

    @Test
    public void testObjectIntoJson() {
        String jo;
        jo = JSON.toJSONString(d1);
        Log.d(TAG, "testObjectIntoJson: d1 -> " + jo);
        jo = JSON.toJSONString(d2);
        Log.d(TAG, "testObjectIntoJson: d2 -> " + jo);
    }

    @Test
    public void testArrayIntoJson() {
        List<Diary> list = new ArrayList<>();
        list.add(d1);
        list.add(d2);
        String jo;
        jo = JSON.toJSONString(list, SerializerFeature.DisableCircularReferenceDetect);
        Log.d(TAG, "testArrayIntoJson: " + jo);
    }

    @Test
    public void testJsonIntoObject() {
        String jo = "{\"date\":1531215766554," +
                "\"diarybook\":{\"id\":4399}," +
                "\"htmlText\":\"<h>1:</h> <p>This is <b>Jodie</b></p>\",\"id\":16," +
                "\"letterSpacing\":1.5," +
                "\"like\":true," +
                "\"lineSpacingExtra\":20," +
                "\"lineSpacingMultiplier\":15," +
                "\"text\":\"1: This is Jodie\"," +
                "\"textAlignment\":12," +
                "\"textSize\":0.2}";
        Diary diary;
        // diary = JSON.parseObject(jo, new TypeReference<Diary>(){});
        diary = JSON.parseObject(jo, Diary.class);
        assertEquals(1531215766554L, diary.getDate().getTime());
        assertEquals(4399, diary.getDiarybook().getId());
        assertEquals(16, diary.getId());
        assertEquals("1: This is Jodie", diary.getText());
        assertEquals("<h>1:</h> <p>This is <b>Jodie</b></p>", diary.getHtmlText());
        assertEquals(1.5F, diary.getLetterSpacing());
        assertEquals(20, diary.getLineSpacingExtra());
    }

    @Test
    public void testJsonIntoArray() {
        String jo = "[" +
                "{\"date\":1531216236466,\"diarybook\":{\"id\":4399}," +
                "\"htmlText\":\"<h>1:</h> <p>This is <b>Jodie</b></p>\",\"id\":0,\"letterSpacing\":1.5,\"like\":true," +
                "\"lineSpacingExtra\":20,\"lineSpacingMultiplier\":15,\"text\":\"1: This is Jodie\",\"textAlignment\":12,\"textSize\":0.2}," +
                "{\"date\":1531216236466,\"diarybook\":{\"id\":4399}," +
                "\"id\":0,\"letterSpacing\":0.2,\"like\":false,\"lineSpacingExtra\":1,\"lineSpacingMultiplier\":0," +
                "\"text\":\"2: hi\",\"textAlignment\":0,\"textSize\":20.0}]";
        List<Diary> list = JSON.parseArray(jo, Diary.class);
        assertEquals(2, list.size());
    }

    @Test
    public void testSentencebook() {
        String jo = "{\"id\":1027194900021477376,\"modified\":1533737323180,\"sentencebookName\":\"嘤\",\"status\":0}";
        Sentencebook sentencebook = JSON.parseObject(jo, Sentencebook.class);
        Log.d(TAG, "testSentencebook: id = " + sentencebook.getId());
    }

}
