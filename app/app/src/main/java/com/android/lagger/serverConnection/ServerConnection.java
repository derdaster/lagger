package com.android.lagger.serverConnection;

import com.android.lagger.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Ewelina Klisowska on 2015-03-19.
 */
public class ServerConnection {

    public static final String LOGIN_URL = "http://abecadlo.zapto.org:9999/LaggerService.svc/user/login";

    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make GET request to the given URL
            HttpGet httpGet = new HttpGet(url);

            // 3. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpGet);

            // 4. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 5. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Exception ex = e;
        }

        // 11. return result
        return result;
    }

    public static String POST(String url, JsonObject jsonObject){
        String result = null;
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            // 3. convert JSONObject to JSON to String
            String json = jsonObject.toString();
            // 3. build gson object
//            Gson g = new Gson();
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("Login", "john");
//            jsonObject.addProperty("Password", "4321");

//             4. convert JSONObject to JSON to String
//            json = g.toJson(jsonObject).toString();
        try {
            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);
            result = getResponseFormHttp(httpResponse);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 11. return result
        return result;
    }

    private static String getResponseFormHttp(HttpResponse httpResponse){
        String result = null;
        try{
            InputStream inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to jsonObject
            if(inputStream != null) {
              //  result = convertInputStreamToJsonObject(inputStream);
                result = convertInputStreamToString(inputStream);
            }
//             else
//                result = "Did not work!";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static JsonObject convertInputStreamToJsonObject(InputStream inputStream) throws IOException {
        JsonObject jsonObject = null;
        String jsonToParse = inputStream.toString();
        jsonObject = new JsonParser().parse(jsonToParse).getAsJsonObject();

        return jsonObject;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

}