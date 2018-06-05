package com.example.dell.sync;

import android.util.Log;

import com.example.dell.db.Diary;
import com.example.dell.server.ServerAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by wu-pc on 2018/6/5.
 */

public class SyncServerAccessor {

    final static private String TAG = "SyncServerAccessor";

    public Object getEntries(Class c) {
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

            if(conn.getResponseCode() != 200) return null;

            is = conn.getInputStream(); //获取输入流
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(isr);
            String inputLine  = "";
            while((inputLine = bufferReader.readLine()) != null){
                resultData += inputLine + "\n";
            }
            System.out.println("get方法取回内容："+resultData);
            showRes("get方法取回内容：" + resultData);

            return null;
        } catch(IOException e) {
            Log.e(TAG, "start: ", e);
            return null;
        }
    }

    public List<String> getEntries() {
        Log.d(TAG, "getEntries: " + className);

        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.parse.com/1/classes/tvshows";

        HttpGet httpGet = new HttpGet(url);
        for (Header header : getAppParseComHeaders()) {
            httpGet.addHeader(header);
        }
        httpGet.addHeader("X-Parse-Session-Token", auth); // taken from https://parse.com/questions/how-long-before-the-sessiontoken-expires

        try {
            HttpResponse response = httpClient.execute(httpGet);

            String responseString = EntityUtils.toString(response.getEntity());
            Log.d("udini", "getShows> Response= " + responseString);

            if (response.getStatusLine().getStatusCode() != HttpURLConnection.HTTP_OK) {
                ParseComServer.ParseComError error = new Gson().fromJson(responseString, ParseComServer.ParseComError.class);
                throw new Exception("Error retrieving tv shows ["+error.code+"] - " + error.error);
            }

            TvShows shows = new Gson().fromJson(responseString, TvShows.class);
            return shows.results;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<TvShow>();
    }

    public void putShow(String authtoken, String userId, TvShow showToAdd) throws Exception {

        Log.d("udinic", "putShow ["+showToAdd.name+"]");

        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = "https://api.parse.com/1/classes/tvshows";

        HttpPost httpPost = new HttpPost(url);

        for (Header header : getAppParseComHeaders()) {
            httpPost.addHeader(header);
        }
        httpPost.addHeader("X-Parse-Session-Token", authtoken); // taken from https://parse.com/questions/how-long-before-the-sessiontoken-expires
        httpPost.addHeader("Content-Type", "application/json");

        JSONObject tvShow = new JSONObject();
        tvShow.put("name", showToAdd.name);
        tvShow.put("year", showToAdd.year);

        // Creating ACL JSON object for the current user
        JSONObject acl = new JSONObject();
        JSONObject aclEveryone = new JSONObject();
        JSONObject aclMe = new JSONObject();
        aclMe.put("read", true);
        aclMe.put("write", true);
        acl.put(userId, aclMe);
        acl.put("*", aclEveryone);
        tvShow.put("ACL", acl);

        String request = tvShow.toString();
        Log.d("udinic", "Request = " + request);
        httpPost.setEntity(new StringEntity(request,"UTF-8"));

        try {
            HttpResponse response = httpClient.execute(httpPost);
            String responseString = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() != 201) {
                ParseComServer.ParseComError error = new Gson().fromJson(responseString, ParseComServer.ParseComError.class);
                throw new Exception("Error posting tv shows ["+error.code+"] - " + error.error);
            } else {
//                Log.d("udini", "Response string = " + responseString);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class TvShows implements Serializable {
        List<TvShow> results;
    }
}
