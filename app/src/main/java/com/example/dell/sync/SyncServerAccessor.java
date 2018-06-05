package com.example.dell.sync;

import android.util.Log;

import com.example.dell.db.Diary;
import com.example.dell.db.Sentence;
import com.example.dell.server.ServerAccessor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wu-pc on 2018/6/5.
 */

public class SyncServerAccessor {

    final static private String TAG = "SyncServerAccessor";

    public List getEntries(Class c) {
        URL url;
        try{
            url = new URL(ServerAccessor.SERVER_IP + "/sync");
        }
        catch (MalformedURLException e) {
            Log.e(TAG, "start: server ip is wrong");
            return null;
        }

        try{
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("className", c.getName());

            conn.connect();
            int responseCode = conn.getResponseCode();
            if(responseCode != 200) {
                Log.d(TAG, "getEntries: response code" + responseCode);
                return null;
            }

            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(isr);

            String resultData = "";
            String inputLine  = "";
            while((inputLine = bufferReader.readLine()) != null){
                resultData += inputLine + "\n";
            }

            List list = new ArrayList();

            // TODO: unparse result data; but how to?
            // Object o = c.newInstance();

            return list;
        } catch(IOException e) {
            Log.e(TAG, "error: ", e);
            return null;
        }
    }

    public boolean putEntries(List entries, Class c) {
        URL url;
        try{
            url = new URL(ServerAccessor.SERVER_IP + "/sync");
        }
        catch (MalformedURLException e) {
            Log.e(TAG, "start: server ip is wrong");
            return false;
        }

        try{
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("className", c.getName());

            String sendData = "";
            // TODO: parse entries

            conn.connect();
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(sendData);
            out.flush();
            out.close();

            int responseCode = conn.getResponseCode();
            if(responseCode != 200) {
                Log.d(TAG, "putEntries: response code" + responseCode);
                return false;
            }

            return true;
        } catch(IOException e) {
            Log.e(TAG, "error: ", e);
            return false;
        }
    }
}
