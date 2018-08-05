package com.example.dell.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
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
import com.example.dell.db.User;
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
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.lang.reflect.Method;
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
    
    private static final String ENCODING = "UTF-8";

    private static final String BROADCAST_ACTION = "com.example.dell.diary.SYNC_ACTION";

    private Context mContext;

    private MyAccount myAccount;

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

        Intent intentBegin = new Intent();
        intentBegin.setAction(BROADCAST_ACTION);
        intentBegin.putExtra("message", "同步开始");
        mContext.sendBroadcast(intentBegin);

        DatabaseHelper helper = OpenHelperManager.getHelper(mContext, DatabaseHelper.class);

        myAccount = new MyAccount(mContext);
        if (myAccount == null) {
            Log.e(TAG, "onPerformSync: cannot get my account");

            Intent intent = new Intent();
            intent.setAction(BROADCAST_ACTION);
            intent.putExtra("message", "无账户，同步失败");
            mContext.sendBroadcast(intent);

            return ;
        }

        boolean verified = ServerAuthenticator.veriy(myAccount.getUsername(), myAccount.getToken());
        Log.d(TAG, "onPerformSync: verified = " + verified);
        if (!verified) {
            Bundle bundle = new Bundle();
            boolean status = ServerAuthenticator.signIn(myAccount.getUsername(), myAccount.getPassword(), bundle);
            if (status && bundle.getBoolean("success")) {
                myAccount.setToken(bundle.getString("token"));
                myAccount.saveSetting();
            }
            else {
                myAccount.clearSetting();

                Intent intent = new Intent();
                intent.setAction(BROADCAST_ACTION);
                intent.putExtra("message", "身份认证失败，请重新登录");
                mContext.sendBroadcast(intent);

                return ;
            }
        }

        boolean status1, status2, status3;
        status1 = syncUser(helper);
        Log.d(TAG, "onPerformSync: 同步用户 status = " + status1);
        status2 = syncData(helper);
        Log.d(TAG, "onPerformSync: 同步数据库 status = " + status2);
        status3 = syncPic(helper);
        Log.d(TAG, "onPerformSync: 同步图片 status = " + status3);

        if (status1 && status2 && status3) {
            Intent intent = new Intent();
            intent.setAction(BROADCAST_ACTION);
            intent.putExtra("message", "同步成功");
            mContext.sendBroadcast(intent);
        }
        else {
            Intent intent = new Intent();
            intent.setAction(BROADCAST_ACTION);
            intent.putExtra("message", "同步失败");
            mContext.sendBroadcast(intent);
        }

        Log.d(TAG, "onPerformSync: end");
    }

    public boolean syncData(DatabaseHelper databaseHelper) {
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

            long preAnchor = myAccount.getDataAnchor();
            Log.d(TAG, "syncData: preAnchor = " + preAnchor);

            String content = dataBuilder.toString();
            Log.d(TAG, "syncData: data = " + content);

            ArrayList<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("anchor", "" + preAnchor));
            pairs.add(new BasicNameValuePair("content", content));
            HttpResponse httpResponse = postSyncData(
                    ServerAccessor.getServerIp() + ":8080/HeartTrace_Server_war/Servlet.Sync1", pairs
            );

            int responseCode = httpResponse.getStatusLine().getStatusCode();
            Log.d(TAG, "syncData: response code = " + responseCode);
            HttpEntity entity = httpResponse.getEntity();
            String response = EntityUtils.toString(entity, ENCODING);
            Log.d(TAG, "syncData: response = " + response);

            Header[] headers = httpResponse.getHeaders("anchor");
            if(headers.length != 1) {
                Log.d(TAG, "syncData: headers error");
                return false;
            }

            long afterAnchor = Long.parseLong(headers[0].getValue());
            Log.d(TAG, "syncData: returned anchor = " + afterAnchor);
            myAccount.setDataAnchor(afterAnchor);
            myAccount.saveSetting();

            if (responseCode != 200) return false;

            // 获取json object，进行处理
            JSONObject jsonObject = JSON.parseObject(response);
            for(Class clazz : syncTableList) {
                Log.d(TAG, "syncData: clazz = " + clazz);

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
                            Log.d(TAG, "syncData: 删除 " + o + " 返回值 = " + code);
                        }
                    }
                    catch (Exception e) {
                        Log.e(TAG, "syncData: ", e);
                    }
                }

                // 获取返回的list
                JSONArray jsonArray = jsonObject.getJSONArray(clazz.getSimpleName() + "List");
                Log.d(TAG, "syncData: json array = " + jsonArray.toString());
                list = jsonArray.toJavaList(clazz);
                for (Object o : list) {
                    try {
                        int status = (int) method.invoke(o);
                        if (status == -1) {
                            // 删除
                            int code = dao.delete(o);
                            Log.d(TAG, "syncData: 删除 " + o + " 返回值 = " + code);
                        }
                        if (status != -1) {
                            // 更新
                            Dao.CreateOrUpdateStatus code = dao.createOrUpdate(o);
                            Log.d(TAG, "syncData: 插入或更新 返回值 = 插入" + code.isCreated() + " 更新" + code.isUpdated());
                        }
                    }
                    catch (Exception e) {
                        Log.e(TAG, "syncData: ", e);
                    }
                }

            }

            return true;
        } catch(Exception e) {
            Log.e(TAG, "syncData: ", e);
            return false;
        }
    }

    public boolean syncUser(DatabaseHelper databaseHelper) {
        MyAccount myAccount = new MyAccount(mContext);
        if (myAccount == null) return false;

        try {
            String content = JSON.toJSONString(myAccount.getUser());
            Log.d(TAG, "syncUser: content = " + content);

            ArrayList<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("content", content));
            HttpResponse httpResponse = postSyncData(
                    ServerAccessor.getServerIp() + ":8080/HeartTrace_Server_war/Servlet.SyncUser", pairs
            );;

            int responseCode = httpResponse.getStatusLine().getStatusCode();
            Log.d(TAG, "syncUser: response code = " + responseCode);

            HttpEntity httpEntity = httpResponse.getEntity();
            String response = EntityUtils.toString(httpEntity, ENCODING);
            Log.d(TAG, "syncUser: response = " + response);

            if (responseCode != 200) return false;

            JSONObject jsonObject = JSON.parseObject(response);
            if (jsonObject.isEmpty()) return false;

            myAccount.setUser(JSON.parseObject(response, User.class));
            boolean status = myAccount.saveUser(false);
            Log.d(TAG, "syncUser: save user status = " + status);

            return true;
        }
        catch (Exception e) {
            Log.e(TAG, "syncUser: ", e);
            return false;
        }
    }

    public boolean syncPic(DatabaseHelper databaseHelper) {
        try {
            long preAnchor = myAccount.getPicAnchor();
            Log.d(TAG, "syncPic: preAnchor = " + preAnchor);
            QueryBuilder<Picture, Long> queryBuilder = databaseHelper.getDaoAccess(Picture.class).queryBuilder();
            queryBuilder.where().gt("modified", preAnchor);
            List<Picture> list = queryBuilder.query();

            JSONArray jsonArray = new JSONArray();
            for(Picture picture : list) {
                JSONObject object = new JSONObject();
                object.put("filename", picture.getFileName());
                object.put("content", picture.readBase64(mContext));
                jsonArray.add(object);
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("files", jsonArray);

            String content = jsonObject.toJSONString();
            Log.d(TAG, "syncPic: content = " + content);

            ArrayList<NameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("anchor", "" + preAnchor));
            pairs.add(new BasicNameValuePair("content", content));
            HttpResponse httpResponse = postSyncData(
                    ServerAccessor.getServerIp() + ":8080/HeartTrace_Server_war/Servlet.UploadFile", pairs
            );

            int responseCode = httpResponse.getStatusLine().getStatusCode();
            Log.d(TAG, "syncPic: response code = " + responseCode);

            HttpEntity httpEntity = httpResponse.getEntity();
            String response = EntityUtils.toString(httpEntity, ENCODING);
            Log.d(TAG, "syncPic: response = " + response);

            if (responseCode != 200) return false;

            jsonObject = JSON.parseObject(response);
            jsonArray = jsonObject.getJSONArray("files");
            boolean syncStatus = true;
            for (int i = 0; i < jsonArray.size(); i++) {
                jsonObject = jsonArray.getJSONObject(i);

                String filename = jsonObject.getString("filename");
                Log.d(TAG, "syncPic: filename = " + filename);

                Picture picture = new Picture(filename);
                String base64 = jsonObject.getString("content");
                boolean status = picture.writeBase64(mContext, base64);
                Log.d(TAG, "syncPic: write base64 status = " + status);

                if (status) {
                    picture.setModified(System.currentTimeMillis());
                    status = picture.save(databaseHelper);
                    if (!status) syncStatus = false;
                }
                else syncStatus = false;
            }

            if (syncStatus) {
                myAccount.setPicAnchor(System.currentTimeMillis());
                myAccount.saveSetting();

                return true;
            }
            else return false;
        }
        catch (Exception e) {
            Log.e(TAG, "syncPic: ", e);
            return false;
        }
    }

    public HttpResponse postSyncData(String url, ArrayList<NameValuePair> pairs) {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setSoTimeout(httpParams, 600000);
        HttpClient httpClient = new DefaultHttpClient(httpParams);

        Log.d(TAG, "postSyncData: url " + url);
        HttpPost httpPost = new HttpPost(url);

        if (pairs == null) pairs = new ArrayList<>();

        pairs.add(new BasicNameValuePair("modelnum", Build.MODEL));

        MyAccount myAccount = new MyAccount(mContext);
        pairs.add(new BasicNameValuePair("username", myAccount.getUsername()));
        pairs.add(new BasicNameValuePair("token", myAccount.getToken()));

        try {
            httpPost.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8"));
            httpPost.setHeader(new BasicHeader("Accept", "text/plain; charset=utf-8"));

            HttpEntity requestEntity = new UrlEncodedFormEntity(pairs, ENCODING);
            Log.d(TAG, "postSyncData: request entity = " + EntityUtils.toString(requestEntity, ENCODING));
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