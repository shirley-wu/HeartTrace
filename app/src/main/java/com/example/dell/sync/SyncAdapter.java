package com.example.dell.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.provider.ContactsContract;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.dell.auth.MyAccount;
import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;
import com.example.dell.server.ServerAccessor;
import com.j256.ormlite.cipher.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.lang.ref.Reference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wu-pc on 2018/6/3.
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = "SyncAdapter";

    // Global variables
    // Define a variable to contain a content resolver instance
    private ContentResolver mContentResolver;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "onPerformSync: begin");

        DatabaseHelper helper = OpenHelperManager.getHelper(getContext(), DatabaseHelper.class);
        sync(helper, Diary.class);
        OpenHelperManager.releaseHelper();

        Log.d(TAG, "onPerformSync: end");
    }

    public void syncDiary(DatabaseHelper databaseHelper) {
        try {
            Dao<Diary, Integer> dao = databaseHelper.getDaoAccess(Diary.class);
            QueryBuilder<Diary, Integer> queryBuilder = dao.queryBuilder();
            queryBuilder.where().lt("status", 9);

            List<Diary> list = queryBuilder.query();

            String jo = parseJson(list, Diary.class);
            Log.d(TAG, "parseDiarySync: jo " + jo);
            String response = postSyncData("Diary", jo);
            if (response == null) {
                Log.e(TAG, "DiarySync: error when getting http response");
                return;
            }

        /*list = unparseJson(response, Diary.class);
        for(Diary diary : list) {
            diary.insertOrUpdate(databaseHelper);
        }*/
            int returnVal;
            for (Diary diary : list) {
                if (diary.getStatus() == -1) {
                    returnVal = dao.delete(diary);
                    Log.d(TAG, "syncDiary: 删除返回值 = " + returnVal);
                } else {
                    diary.setStatus(9);
                    returnVal = dao.update(diary);
                    Log.d(TAG, "syncDiary: 更新返回值 = " + returnVal);
                }
            }
        } catch (SQLException e) {
            Log.e(TAG, "syncDiary: ", e);
        }
    }

    public boolean sync(DatabaseHelper databaseHelper, Class c) {
        try {
            Method getAll = c.getDeclaredMethod("getAll", DatabaseHelper.class, boolean.class);
            List list = (List) getAll.invoke(null, databaseHelper, false);

            String jo = parseJson(list, c);
            Log.d(TAG, "parseDiarySync: jo " + jo);

            String response = postSyncData(c.getSimpleName(), jo);
            if (response == null) {
                Log.e(TAG, "sync: syncing " + c.getSimpleName() + ", error when getting http response");
                return false;
            }

            list = unparseJson(response, c);

            Method iou = c.getDeclaredMethod("insertOrUpdate", DatabaseHelper.class);
            for (Object o : list) {
                iou.invoke(o, databaseHelper);
            }

            return true;
        } catch(Exception e) {
            Log.e(TAG, "sync: " + c.getSimpleName() + " ", e);
            return false;
        }
    }

    public String parseJson(List list, Class c) {
        String jo = JSON.toJSONString(list, SerializerFeature.DisableCircularReferenceDetect);
        jo = "{\"" + c.getSimpleName() + "List\":" + jo + "}";
        return jo;
    }

    public List unparseJson(String jsonString, Class c) {
        JSONObject jso = JSON.parseObject(jsonString);
        JSONArray jsarr = jso.getJSONArray(c.getSimpleName() + "List");
        Log.d(TAG, "unparseJson: " + jsarr.toJSONString());
        return jsarr.toJavaList(c);
    }

    public String postSyncData(String table, String sendData) {
        HttpClient httpClient = new DefaultHttpClient();
        // String url = ServerAccessor.getServerIp() + ":8080/HeartTrace_Server_war4/Sync";
        String url = ServerAccessor.getServerIp() + ":8080/HeartTrace_Server_war/Servlet.Sync";
        Log.d(TAG, "postSyncData: url " + url);
        HttpPost httpPost = new HttpPost(url);

        ArrayList<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("table", table));
        pairs.add(new BasicNameValuePair("data", sendData));

        pairs.add(new BasicNameValuePair("modelnum", Build.MODEL));
        pairs.add(new BasicNameValuePair("token", MyAccount.get(getContext()).getToken()));

        try {
            HttpEntity requestEntity = new UrlEncodedFormEntity(pairs);
            httpPost.setEntity(requestEntity);

            try {
                HttpResponse httpResponse = httpClient.execute(httpPost);

                Header[] headers = httpResponse.getAllHeaders();
                for(Header header : headers) {
                    Log.d(TAG, "postSyncData: header " + header.toString());
                }

                int code = httpResponse.getStatusLine().getStatusCode();
                Log.d(TAG, "postSyncData: " + code);
                if (code == 200) {
                    HttpEntity entity = httpResponse.getEntity();
                    String response = EntityUtils.toString(entity, "utf-8");
                    return response.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}