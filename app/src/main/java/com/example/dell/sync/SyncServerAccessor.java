package com.example.dell.sync;

import android.accounts.NetworkErrorException;
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

        HttpURLConnection conn = null;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader bufferedReader = null;

        List list = null;

        try{
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("className", c.getName());

            conn.connect();
            int responseCode = conn.getResponseCode();
            if(responseCode != 200) {
                throw new NetworkErrorException("get response code" + responseCode);
            }

            is = conn.getInputStream();
            isr = new InputStreamReader(is);
            bufferedReader = new BufferedReader(isr);

            String resultData = "";
            String inputLine  = "";
            while((inputLine = bufferedReader.readLine()) != null){
                resultData += inputLine + "\n";
            }

            // TODO: unparse result data into list; but how to?
            // Object o = c.newInstance();

        }
        catch(IOException e) {
            Log.e(TAG, "getEntries: ", e);
        }
        catch (NetworkErrorException e) {
            Log.e(TAG, "getEntries: ", e);
        }
        finally {
            if(bufferedReader != null) {
                try{
                    bufferedReader.close();
                }
                catch (IOException e) {
                    Log.d(TAG, "getEntries: when closing bufferedReader", e);
                }
            }
            if(isr != null) {
                try{
                    isr.close();
                }
                catch (IOException e) {
                    Log.d(TAG, "getEntries: when closing isr", e);
                }
            }
            if(is != null) {
                try{
                    is.close();
                }
                catch (IOException e) {
                    Log.d(TAG, "getEntries: when closing is", e);
                }
            }
            if(conn != null) {
                conn.disconnect();
            }
        }

        return list;
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

        HttpURLConnection conn = null;
        DataOutputStream out = null;
        boolean success = false;

        try{
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("className", c.getName());

            String sendData = "";
            // TODO: parse entries into sendData

            conn.connect();
            out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(sendData);
            out.flush();
            out.close();
            out = null;

            int responseCode = conn.getResponseCode();
            if(responseCode != 200) {
                throw new NetworkErrorException("put response code" + responseCode);
            } else {
                success = true;
            }
        }
        catch(IOException e) {
            Log.e(TAG, "putEntries: ", e);
        }
        catch (NetworkErrorException e) {
            Log.e(TAG, "putEntries: ", e);
        }
        finally {
            if (out != null) {
                try{
                    out.close();
                }
                catch (IOException e) {
                    Log.e(TAG, "putEntries: when closing out", e);
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        return success;
    }
}
