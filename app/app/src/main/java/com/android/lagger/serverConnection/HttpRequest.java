package com.android.lagger.serverConnection;

import com.android.lagger.requestObjects.RequestObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.net.URL;

/**
 * Created by Ewelina Klisowska on 2015-03-19.
 */
public class HttpRequest {

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

        return result;
    }

    public static String DELETE(String url, RequestObject requestObject){
        String result = null;
        java.net.URL urlObject = null;
        try {
            urlObject = new URL(url);
        } catch (MalformedURLException exception) {
            exception.printStackTrace();
        }
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) urlObject.openConnection();

            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestMethod("DELETE");
            //FIXME convert response object to String;
           result = httpURLConnection.getResponseMessage();
        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return result;
    }

    public static String POST(String url, JsonObject jsonObject){
        String result = null;
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            // 3. convert JSONObject to JSON to String
            Gson gson = new GsonHelper().getGson();
            String json = gson.toJson(jsonObject);
//            String json = jsonObject.toString();

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



    //POST with RequestObject in parameter
    public static String POST(String url, RequestObject requestObject){
        String result = null;
        // 1. create HttpClient
        HttpClient httpclient = new DefaultHttpClient();

        // 2. make POST request to the given URL
        HttpPost httpPost = new HttpPost(url);

        // 3. convert JSONObject to JSON to String
        Gson gson = new GsonHelper().getGson();
        String json = gson.toJson(requestObject);

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
            result = getResponseFromHttp(httpResponse);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    private static String getResponseFromHttp(HttpResponse httpResponse){
        String result = null;
        try{
            InputStream inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to jsonObject
            if(inputStream != null) {
                //  result = convertInputStreamToJsonObject(inputStream);
//                String response = convertInputStreamToString(inputStream);
                result = convertInputStreamToString(inputStream);
            }
//             else
//                result = "Did not work!";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

//    private static ResponseObject convertInputStreamToResponseObj(InputStream inputStream) throws IOException {
//        ResponseObject responseObj = null;
//        String jsonToParse = convertInputStreamToString(inputStream);
//
//        Gson gson = new GsonHelper().getGson();
//        responseObj = gson.fromJson(jsonToParse, ResponseObject.class);
//
//        return responseObj;
//    }

}
