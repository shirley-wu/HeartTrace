package com.example.dell.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
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

    private static final String PREFERENCE_NAME = "SYNC ANCHOR";

    private Context mContext;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
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
        mContext = context;
    }

    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "onPerformSync: begin");

        DatabaseHelper helper = OpenHelperManager.getHelper(mContext, DatabaseHelper.class);
        sync(helper, Diary.class);
        OpenHelperManager.releaseHelper();

        Log.d(TAG, "onPerformSync: end");
    }

    public boolean sync(DatabaseHelper databaseHelper, Class c) {
        try {
            Class[] tableList = databaseHelper.getTableList();

            StringBuilder dataBuilder = new StringBuilder("{");

            for(Class clazz : tableList) {
                QueryBuilder queryBuilder = databaseHelper.getDaoAccess(clazz).queryBuilder();
                queryBuilder.where().lt("status", 9);
                List list = queryBuilder.query();

                dataBuilder.append(
                        c.getSimpleName() + "List\":" + JSON.toJSONString(list, SerializerFeature.DisableCircularReferenceDetect)
                );
            }

            String data = dataBuilder.toString();
            Log.d(TAG, "sync: data = " + data);
            String response = postSyncData(data);

            return true;
        } catch(Exception e) {
            Log.e(TAG, "sync: " + c.getSimpleName() + " ", e);
            return false;
        }
    }

    /*public List unparseJson(String jsonString, Class c) {
        JSONObject jso = JSON.parseObject(jsonString);
        JSONArray jsarr = jso.getJSONArray(c.getSimpleName() + "List");
        Log.d(TAG, "unparseJson: " + jsarr.toJSONString());
        return jsarr.toJavaList(c);
    }*/

    public String postSyncData(String sendData) {
        HttpClient httpClient = new DefaultHttpClient();
        // String url = ServerAccessor.getServerIp() + ":8080/HeartTrace_Server_war4/Sync";
        String url = ServerAccessor.getServerIp() + ":8080/HeartTrace_Server_war/Servlet.Sync";
        Log.d(TAG, "postSyncData: url " + url);
        HttpPost httpPost = new HttpPost(url);

        ArrayList<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("modelnum", Build.MODEL));
        pairs.add(new BasicNameValuePair("token", MyAccount.get(mContext).getToken()));
        pairs.add(new BasicNameValuePair("data", sendData));

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        Long anchor = sharedPreferences.getLong("anchor", -1);
        pairs.add(new BasicNameValuePair("anchor", anchor.toString()));

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