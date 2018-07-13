package com.example.dell.server;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

/**
 * Created by wu-pc on 2018/6/5.
 */

public class ServerAccessor {

    private static final String TAG = "ServerAccessor";

    private static final String SERVER_IP = "http://122.152.195.134";

    static public String getServerIp() {
        return SERVER_IP;
    }

}
