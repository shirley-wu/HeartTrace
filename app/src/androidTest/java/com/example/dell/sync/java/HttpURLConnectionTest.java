package com.example.dell.sync.java;

import android.test.InstrumentationTestCase;
import android.util.Log;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wu-pc on 2018/6/6.
 */

public class HttpURLConnectionTest extends InstrumentationTestCase {

    private final static String TAG = "NetworkCommonTest";

    private HttpURLConnection conn = null;
    private InputStream is = null;
    private InputStreamReader isr = null;
    private BufferedReader bufferedReader = null;

    @After
    public void tearDown() {
        Log.d(TAG, "tearDown: after getting baidu");
        if(conn != null) {
            conn.disconnect();
        }
        if(is != null) {
            try{
                is.close();
            }
            catch (IOException e) {
                Log.e(TAG, "tearDown: closing is, ", e);
            }
        }
        if(isr != null) {
            try{
                isr.close();
            }
            catch (IOException e) {
                Log.e(TAG, "tearDown: closing isr, ", e);
            }
        }
        if(bufferedReader != null) {
            try{
                bufferedReader.close();
            }
            catch (IOException e) {
                Log.e(TAG, "tearDown: closing buffered reader", e);
            }
        }
    }

    @Test
    public void testGetBaidu() {
        try {
            URL url = new URL("http://www.baidu.com");
            // Always using baidu as a test. fuckkkkkkkkkkk

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            conn.connect();
            int responseCode = conn.getResponseCode();
            assertEquals(200, responseCode);

            is = conn.getInputStream();
            isr = new InputStreamReader(is);
            bufferedReader = new BufferedReader(isr);

            String inputLine = "";
            Log.d(TAG, "testGetBaidu: start reading content");
            while ((inputLine = bufferedReader.readLine()) != null) {
                Log.d(TAG, "testGetBaidu: " + inputLine);
            }
            Log.d(TAG, "testGetBaidu: reading content finished");
        }
        catch (IOException e) {

        }
    }

}
