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
        String resultData = getResponseData(ServerAccessor.SERVER_IP + "/sync", c);

        if(resultData == null) {
            // error
            return null;
        }

        List list = new ArrayList();

        // TODO: unparse

        return list;
    }

    public String getResponseData(String urlString, Class c) {
        // return the data of sync get response only when response code == 200;
        // null representing error, "" representing no data
        URL url;
        try{
            url = new URL(urlString);
        }
        catch (MalformedURLException e) {
            Log.e(TAG, "start: server ip is wrong");
            return null;
        }

        HttpURLConnection conn = null;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader bufferedReader = null;

        String resultData = null;

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

            resultData = "";
            String inputLine  = "";
            while((inputLine = bufferedReader.readLine()) != null){
                resultData += inputLine + "\n";
            }
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

        return resultData;
    }

    public boolean putEntries(List entries, Class c) {
        String sendData = "";

        // TODO: parse

        Integer responseCode = putResponseCode(ServerAccessor.SERVER_IP + "/sync", sendData, c);
        return (responseCode != null && responseCode == 200);
    }

    public Integer putResponseCode(String urlString, String sendData, Class c) {
        // return the response code of put request
        // null representing error
        URL url;
        try{
            url = new URL(urlString);
        }
        catch (MalformedURLException e) {
            Log.e(TAG, "start: server ip is wrong");
            return null;
        }

        HttpURLConnection conn = null;
        DataOutputStream out = null;

        Integer responseCode = null;

        try{
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("className", c.getName());

            conn.connect();
            out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(sendData);
            out.flush();
            out.close();
            out = null;

            responseCode = conn.getResponseCode();
        }
        catch(IOException e) {
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

        return responseCode;
    }
}
