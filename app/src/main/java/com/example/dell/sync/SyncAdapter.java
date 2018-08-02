package com.example.dell.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.content.SyncStats;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.example.dell.auth.MyAccount;
import com.example.dell.auth.ServerAuthenticator;
import com.example.dell.db.DatabaseHelper;
import com.example.dell.db.Diary;
import com.example.dell.db.DiaryLabel;
import com.example.dell.db.Diarybook;
import com.example.dell.db.Label;
import com.example.dell.db.Picture;
import com.example.dell.db.Sentence;
import com.example.dell.db.SentenceLabel;
import com.example.dell.db.Sentencebook;
import com.example.dell.diary.DiaryWriteActivity;
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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by wu-pc on 2018/6/3.
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = "SyncAdapter";

    private static final String PREFERENCE_NAME = "SYNC ANCHOR";

    private Context mContext;

    private Long preAnchor = null;

    private Long afterAnchor = null;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContext = context;
        Log.d(TAG, "SyncAdapter: package name = " + context.getApplicationContext().getPackageName());
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

        MyAccount myAccount = MyAccount.get(mContext);
        if (myAccount == null) {
            Log.e(TAG, "onPerformSync: cannot get my account");
            return ;
        }

        boolean verified = ServerAuthenticator.veriy(myAccount.getUsername(), myAccount.getToken());
        Log.d(TAG, "onPerformSync: verified = " + verified);
        if (!verified) {
            Bundle bundle = new Bundle();
            boolean status = ServerAuthenticator.signIn(myAccount.getUsername(), myAccount.getPassword(), bundle);
            if (status && bundle.getBoolean("success")) {
                myAccount.setToken(bundle.getString("token"));
                myAccount.save();
            }
            else {
                myAccount.setPassword(null);
                myAccount.setToken(null);
                myAccount.save();
                // TODO: return ;
            }
        }

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        preAnchor = sharedPreferences.getLong("anchor", -1);
        Log.d(TAG, "postSyncData: preAnchor = " + preAnchor);

        boolean status1, status2, status3;
        status1 = sync(helper);
        Log.d(TAG, "onPerformSync: 同步数据库 status = " + status1);
        status2 = syncPic(helper);
        Log.d(TAG, "onPerformSync: 同步图片 status = " + status2);
        status3 = syncUser(helper);
        Log.d(TAG, "onPerformSync: 同步用户 status = " + status3);

        if (status1 && status2 && status3 && afterAnchor != null) {
            Log.d(TAG, "onPerformSync: afterAnchor = " + afterAnchor);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("anchor", afterAnchor);
            editor.commit();
        }

        OpenHelperManager.releaseHelper();

        Log.d(TAG, "onPerformSync: end");
    }

    public boolean sync(DatabaseHelper databaseHelper) {
        final Class[] syncTableList = {
                Diary.class,
                Diarybook.class,
                DiaryLabel.class,
                Label.class,
                Sentence.class,
                Sentencebook.class,
                SentenceLabel.class
        };

        try {
            Map<Class, List> classListMap = new Hashtable<>();

            StringBuilder dataBuilder = new StringBuilder("{");

            for(int i=0; ; ) {
                Class clazz = syncTableList[i];
                QueryBuilder queryBuilder = databaseHelper.getDaoAccess(clazz).queryBuilder();
                queryBuilder.where().lt("status", 9);
                List list = queryBuilder.query();

                classListMap.put(clazz, list);

                dataBuilder.append(
                        "\"" + clazz.getSimpleName() + "List\":" + JSON.toJSONString(list, SerializerFeature.DisableCircularReferenceDetect)
                );
                i++;
                if (i < syncTableList.length) dataBuilder.append(",");
                else break;
            }

            dataBuilder.append("}");

            String data = dataBuilder.toString();
            Log.d(TAG, "sync: data = " + data);

            HttpResponse httpResponse = postSyncData(data);

            int responseCode = httpResponse.getStatusLine().getStatusCode();
            Log.d(TAG, "sync: response code = " + responseCode);
            HttpEntity entity = httpResponse.getEntity();
            String response = EntityUtils.toString(entity, "utf-8");
            Log.d(TAG, "sync: response = " + response);

            Header[] headers = httpResponse.getHeaders("anchor");
            if(headers.length != 1) {
                Log.d(TAG, "sync: headers error");
                return false;
            }

            afterAnchor = Long.parseLong(headers[0].getValue());
            Log.d(TAG, "sync: returned anchor = " + afterAnchor);

            if (responseCode != 200) return false;

            // 获取json object，进行处理
            JSONObject jsonObject = JSON.parseObject(response);
            for(Class clazz : syncTableList) {
                Log.d(TAG, "sync: clazz = " + clazz);

                // 获得需要的Dao
                Dao dao = databaseHelper.getDaoAccess(clazz);

                // 通过反射，获取getStatus方法
                Method method = clazz.getDeclaredMethod("getStatus");

                // 删除list中需要删除的部分
                List list = classListMap.get(clazz);
                for (Object o : list) {
                    try {
                        int status = (int) method.invoke(o);
                        if (status == -1) {
                            int code = dao.delete(o);
                            Log.d(TAG, "sync: 删除 " + o + " 返回值 = " + code);
                        }
                    }
                    catch (Exception e) {
                        Log.e(TAG, "sync: ", e);
                    }
                }

                // 获取返回的list
                JSONArray jsonArray = jsonObject.getJSONArray(clazz.getSimpleName() + "List");
                Log.d(TAG, "sync: json array = " + jsonArray.toString());
                list = jsonArray.toJavaList(clazz);
                for (Object o : list) {
                    try {
                        int status = (int) method.invoke(o);
                        if (status == -1) {
                            // 删除
                            int code = dao.delete(o);
                            Log.d(TAG, "sync: 删除 " + o + " 返回值 = " + code);
                        }
                        if (status != -1) {
                            // 更新
                            Dao.CreateOrUpdateStatus code = dao.createOrUpdate(o);
                            Log.d(TAG, "sync: 插入或更新 返回值 = 插入" + code.isCreated() + " 更新" + code.isUpdated());
                        }
                    }
                    catch (Exception e) {
                        Log.e(TAG, "sync: ", e);
                    }
                }

            }

            return true;
        } catch(Exception e) {
            Log.e(TAG, "sync: ", e);
            return false;
        }
    }

    public boolean syncUser(DatabaseHelper databaseHelper) {
        MyAccount myAccount = MyAccount.get(mContext);
        if (myAccount == null) return false;
        // TODO: if (myAccount.getModified() < preAnchor) return true;
        try {
            HttpClient httpClient = new DefaultHttpClient();

            String url = ServerAccessor.getServerIp() + ":8080/HeartTrace_Server_war/Servlet.SyncUser";
            Log.d(TAG, "syncUser: url " + url);
            HttpPost httpPost = new HttpPost(url);

            ArrayList<NameValuePair> pairs = new ArrayList<>();

            pairs.add(new BasicNameValuePair("modelnum", Build.MODEL));
            pairs.add(new BasicNameValuePair("username", myAccount.getUsername()));
            pairs.add(new BasicNameValuePair("token", myAccount.getToken()));

            SimplePropertyPreFilter filter = new SimplePropertyPreFilter(MyAccount.class,
                    "username", "password", "modified",
                    "nickname", "gender", "birthday", "email",
                    "school", "signature", "headimage"
            );
            String content = JSON.toJSONString(myAccount, filter);
            Log.d(TAG, "syncUser: content = " + content);
            pairs.add(new BasicNameValuePair("content", content));

            HttpEntity requestEntity = new UrlEncodedFormEntity(pairs);
            Log.d(TAG, "postSyncData: request entity = " + EntityUtils.toString(requestEntity, "utf-8"));
            httpPost.setEntity(requestEntity);

            // HttpResponse httpResponse = httpClient.execute(httpPost);

            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean syncPic(DatabaseHelper databaseHelper) {
        try {
            QueryBuilder<Picture, Long> queryBuilder = databaseHelper.getDaoAccess(Picture.class).queryBuilder();
            queryBuilder.where().gt("modified", preAnchor);
            List<Picture> list = queryBuilder.query();

            for(Picture picture : list) {
                String path = DiaryWriteActivity.SD_PATH + "image_" + picture.getId() + ".jpg";
                // TODO: ???
            }

            return true;
        }
        catch (Exception e) {
            Log.e(TAG, "syncPic: ", e);
            return false;
        }
    }

    public HttpResponse postSyncData(String sendData) {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setSoTimeout(httpParams, 60000);
        HttpClient httpClient = new DefaultHttpClient(httpParams);

        String url = ServerAccessor.getServerIp() + ":8080/HeartTrace_Server_war/Servlet.Sync1";
        Log.d(TAG, "postSyncData: url " + url);
        HttpPost httpPost = new HttpPost(url);

        ArrayList<NameValuePair> pairs = new ArrayList<>();

        pairs.add(new BasicNameValuePair("modelnum", Build.MODEL));

        MyAccount myAccount = MyAccount.get(mContext);
        pairs.add(new BasicNameValuePair("username", myAccount.getUsername()));
        pairs.add(new BasicNameValuePair("token", myAccount.getToken()));
        pairs.add(new BasicNameValuePair("content", sendData));
        pairs.add(new BasicNameValuePair("anchor", preAnchor.toString()));

        try {
            HttpEntity requestEntity = new UrlEncodedFormEntity(pairs);
            Log.d(TAG, "postSyncData: request entity = " + EntityUtils.toString(requestEntity, "utf-8"));
            httpPost.setEntity(requestEntity);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            return httpResponse;
        }
        catch (Exception e) {
            Log.e(TAG, "postSyncData: ", e);
        }

        return null;
    }

}