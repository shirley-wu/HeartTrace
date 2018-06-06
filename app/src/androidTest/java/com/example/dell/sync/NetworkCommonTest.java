package com.example.dell.sync;

import android.test.InstrumentationTestCase;
import android.util.Log;

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

public class NetworkCommonTest extends InstrumentationTestCase {

    private final static String TAG = "NetworkCommonTest";

    private InputStream is = null;

    @Test
    public void testGet() throws MalformedURLException, ProtocolException, IOException {
        URL url = new URL("www.baidu.com");
        // Always using baidu as a test. fuckkkkkkkkkkk

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        conn.connect();
        int responseCode = conn.getResponseCode();
        assertEquals(200, responseCode);

        is = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader bufferReader = new BufferedReader(isr);

        String inputLine  = "";
        while((inputLine = bufferReader.readLine()) != null){
            Log.d(TAG, "testGet: " + inputLine);
        }

        bufferReader.close();
        isr.close();
        is.close();

        conn.disconnect();
    }

}
