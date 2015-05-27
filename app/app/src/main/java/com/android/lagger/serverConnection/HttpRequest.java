package com.android.lagger.serverConnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.android.lagger.R;
import com.android.lagger.requestObjects.RequestObject;
import com.android.lagger.responseObjects.ResponseObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
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
public class HttpRequest {

    private Context context;

    public HttpRequest(Context context) {
        this.context = context;
    }

    public static String GET(String url) {
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
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Exception ex = e;
        }

        return result;
    }

    public static String POST(String url, JsonObject jsonObject) {
        String result = null;
        // 1. create HttpClient
        HttpClient httpclient = new DefaultHttpClient();

        // 2. make POST request to the given URL
        HttpPost httpPost = new HttpPost(url);

        // 3. convert JSONObject to JSON to String
        Gson gson = new GsonHelper().getGson();
        String json = gson.toJson(jsonObject);
        //json = json.replaceAll("\\\\/", "\\/");
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
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static String getResponseFormHttp(HttpResponse httpResponse) {
        String result = null;
        try {
            InputStream inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to jsonObject
            if (inputStream != null) {
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
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    private boolean isInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()) {
            return true;
        } else {
            return false;
        }
    }

    //POST with RequestObject in parameter
    public ResponseObject POST(String url, RequestObject requestObject) {
        ResponseObject response = null;
        if (isInternetConnection(context)) {
            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            // 3. convert JSONObject to JSON to String
            Gson gson = new GsonHelper().getGson();
            String json = gson.toJson(requestObject);

            try {
                // 4. set json to StringEntity
                StringEntity se = new StringEntity(json);

                // 5. set httpPost Entity
                httpPost.setEntity(se);

                // 6. Set some headers to inform server about the type of the content
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

                // 7. Execute POST request to the given URL
                response = executeRequestAndGetResponse(httpclient, httpPost);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 8. no Internet connection
            String result = context.getResources().getString(R.string.no_connection);
            response = new ResponseObject(result, true);

        }
        return response;
    }

    private ResponseObject executeRequestAndGetResponse(final HttpClient httpClient, final HttpPost httpPost) {
        ResponseObject response = null;
        String result = "";

        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpPost);

            final StatusLine statusLine = httpResponse.getStatusLine();
            Integer statusCode = statusLine.getStatusCode();
            String statusMessage = statusLine.getReasonPhrase();

            if (statusCode == 200) {
                result = getResponseFromHttp(httpResponse);
                response = new ResponseObject(result, false);
                Log.d("HttpRequest.POST()", statusCode + ": " + statusLine.getStatusCode());
            } else {
                result = getErrorMessage(statusCode);
                response = new ResponseObject(result, true);
                Log.e("HttpRequest.POST()", statusCode + ": " + statusLine.getStatusCode() + " reason: " + statusMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private String getErrorMessage(final Integer statusCode) {
        String errorMessage = context.getResources().getString(R.string.error_occurred) + statusCode;

        switch (statusCode) {
            case 400:
                errorMessage = context.getResources().getString(R.string.server_code_message_400);
                break;
            case 404:
                errorMessage = context.getResources().getString(R.string.server_code_message_404);
                break;
            case 503:
                errorMessage = context.getResources().getString(R.string.server_code_message_503);
                break;
        }
        return errorMessage;
    }

    private static String getResponseFromHttp(HttpResponse httpResponse) {
        String result = null;
        try {
            InputStream inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to jsonObject
            if (inputStream != null) {
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

}
