package com.example.dell.sync.java;

import android.test.InstrumentationTestCase;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by wu-pc on 2018/7/7.
 */

public class HttpClientTest extends InstrumentationTestCase {

    static private final String TAG = "HttpClientTest";

    @Test
    public void testGet() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://httpbin.org/get");
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = httpResponse.getEntity();
                String response = EntityUtils.toString(entity, "UTF-8");
                Log.d(TAG, "testGet: " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPost() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://httpbin.org/post");

        ArrayList<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("ack", "hello"));
        pairs.add(new BasicNameValuePair("我", "你"));

        try {
            HttpEntity requestEntity = new UrlEncodedFormEntity(pairs, "UTF-8");
            httpPost.setHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8"));
            httpPost.setHeader(new BasicHeader("Accept", "text/plain; charset=UTF-8"));
            httpPost.setEntity(requestEntity);
            Log.d(TAG, "testPost: request entity = " + EntityUtils.toString(requestEntity, "UTF-8"));

            try {
                HttpResponse httpResponse = httpClient.execute(httpPost);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = httpResponse.getEntity();
                    String response = EntityUtils.toString(entity, "UTF-8");
                    Log.d(TAG, "testPost: response entity = " + response);

                    byte[] bytes = response.getBytes();
                    String string = new String(bytes, "UTF-8");
                    Log.d(TAG, "testPost: string = " + string);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
