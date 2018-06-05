package com.example.dell.sync;

import android.util.Log;

import com.example.dell.db.Diary;
import com.example.dell.db.Sentence;
import com.example.dell.server.ServerAccessor;

import java.io.BufferedReader;
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
            conn.setRequestProperty("className", c.getName());

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
            Log.e(TAG, "start: ", e);
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
            conn.setRequestProperty("className", c.getName());

            // TODO: parse entries

            // Post请求的url，与get不同的是不需要带参数
            URL postUrl = new URL("http://www.xxxxxxx.com");
            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
            // 设置是否向connection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true
            connection.setDoOutput(true);
            // Read from the connection. Default is true.
            connection.setDoInput(true);
            // 默认是 GET方式
            connection.setRequestMethod("POST");
            // Post 请求不能使用缓存
            connection.setUseCaches(false);
            //设置本次连接是否自动重定向
            connection.setInstanceFollowRedirects(true);
            // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
            // 意思是正文是urlencoded编码过的form参数
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
            // 要注意的是connection.getOutputStream会隐含的进行connect。
            connection.connect();
            DataOutputStream out = new DataOutputStream(connection
                    .getOutputStream());
            // 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致
            String content = "字段名=" + URLEncoder.encode("字符串值", "编码");
            // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
            out.writeBytes(content);
            //流用完记得关
            out.flush();
            out.close();
            //获取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null){
                System.out.println(line);
            }
            reader.close();

            int responseCode = conn.getResponseCode();
            if(responseCode != 200) {
                Log.d(TAG, "getEntries: response code" + responseCode);
                return false;
            }

            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(isr);

            String resultData = "";
            String inputLine  = "";
            while((inputLine = bufferReader.readLine()) != null){
                resultData += inputLine + "\n";
            }

            return new ArrayList();
        } catch(IOException e) {
            Log.e(TAG, "start: ", e);
            return null;
        }
    }
}
