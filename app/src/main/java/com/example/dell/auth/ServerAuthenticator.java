package com.example.dell.auth;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.dell.server.ServerAccessor;

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
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wu-pc on 2018/7/12.
 */

public class ServerAuthenticator {

    private static final String TAG = "ServerAuthenticator";

    static public boolean signIn(String username, String password, Bundle bundle) {
        HttpClient httpClient = new DefaultHttpClient();
        String url = ServerAccessor.getServerIp() + ":8080/HeartTrace_Server_war/Servlet.Loginup";
        Log.d(TAG, "signIn: url " + url);
        HttpPost httpPost = new HttpPost(url);

        ArrayList<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("username", username));
        pairs.add(new BasicNameValuePair("password", password));
        pairs.add(new BasicNameValuePair("modelnum", Build.MODEL));

        try {
            HttpEntity requestEntity = new UrlEncodedFormEntity(pairs);
            httpPost.setEntity(requestEntity);

            try {
                HttpResponse httpResponse = httpClient.execute(httpPost);

                Header[] headers = httpResponse.getAllHeaders();
                for(Header header : headers) {
                    Log.d(TAG, "signIn: header " + header.toString());
                }

                HttpEntity entity = httpResponse.getEntity();
                String response = EntityUtils.toString(entity, "utf-8");
                Log.d(TAG, "signIn: http response " + response);

                int code = httpResponse.getStatusLine().getStatusCode();
                Log.d(TAG, "signIn: " + code);
                if (code == 200) {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.optBoolean("success");
                    String msg = jsonObject.optString("msg");
                    String token = jsonObject.optString("token");
                    bundle.putString("msg", msg);
                    bundle.putBoolean("success", success);
                    bundle.putString("token", token);

                    return true;
                }
                else {
                    return false;
                }
            } catch (Exception e) {
                Log.e(TAG, "signIn: ", e);;
            }
        } catch (Exception e) {
            Log.e(TAG, "signIn: ", e);
        }

        return false;
    }

    static public boolean signUp(String username, String password, Bundle bundle) {
        HttpClient httpClient = new DefaultHttpClient();
        String url = ServerAccessor.getServerIp() + ":8080/HeartTrace_Server_war/Servlet.Signin";
        Log.d(TAG, "signIn: url " + url);
        HttpPost httpPost = new HttpPost(url);

        ArrayList<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("username", username));
        pairs.add(new BasicNameValuePair("password", password));
        pairs.add(new BasicNameValuePair("modelnum", Build.MODEL));

        try {
            HttpEntity requestEntity = new UrlEncodedFormEntity(pairs);
            httpPost.setEntity(requestEntity);

            try {
                HttpResponse httpResponse = httpClient.execute(httpPost);

                Header[] headers = httpResponse.getAllHeaders();
                for(Header header : headers) {
                    Log.d(TAG, "signUp: header " + header.toString());
                }

                HttpEntity entity = httpResponse.getEntity();
                String response = EntityUtils.toString(entity, "utf-8");
                Log.d(TAG, "signUp: http response " + response);

                int code = httpResponse.getStatusLine().getStatusCode();
                Log.d(TAG, "signUp: " + code);
                if (code == 200) {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.optBoolean("success");
                    String msg = jsonObject.optString("msg");
                    bundle.putString("msg", msg);
                    bundle.putBoolean("success", success);

                    return true;
                }
                else {
                    return false;
                }
            } catch (Exception e) {
                Log.e(TAG, "signUp: ", e);
            }
        } catch (Exception e) {
            Log.e(TAG, "signUp: ", e);
        }

        return false;
    }

    static public boolean veriy(String username, String token) {
        HttpClient httpClient = new DefaultHttpClient();
        String url = ServerAccessor.getServerIp() + ":8080/HeartTrace_Server_war/Servlet.Verify";
        Log.d(TAG, "verify: url " + url);
        HttpPost httpPost = new HttpPost(url);

        ArrayList<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("username", username));
        pairs.add(new BasicNameValuePair("token", token));

        try {
            HttpEntity requestEntity = new UrlEncodedFormEntity(pairs);
            httpPost.setEntity(requestEntity);

            try {
                HttpResponse httpResponse = httpClient.execute(httpPost);

                Header[] headers = httpResponse.getAllHeaders();
                for(Header header : headers) {
                    Log.d(TAG, "verify: header " + header.toString());
                }

                int code = httpResponse.getStatusLine().getStatusCode();
                Log.d(TAG, "verify: " + code);
                if (code == 200) {
                    HttpEntity entity = httpResponse.getEntity();
                    String response = EntityUtils.toString(entity, "utf-8");
                    Log.d(TAG, "verify: http response " + response);

                    return true;
                }
                else {
                    return false;
                }
            } catch (Exception e) {
                Log.e(TAG, "veriy: ", e);
            }
        } catch (Exception e) {
            Log.e(TAG, "veriy: ", e);
        }

        return false;
    }

}
