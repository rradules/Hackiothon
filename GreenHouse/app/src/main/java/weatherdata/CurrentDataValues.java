package weatherdata;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import weatherdata.POJO.CurrentSensorStatus;
import weatherdata.POJO.SensorResponse;

/**
 * Created by christophe on 01.07.14.
 */
public class CurrentDataValues {
    private final static String authUrl = "http://www.nassist-test.com/api/auth";
    private final static String co2Sensor = "http://www.nassist-test.com/api/sensors/4Noks_77000000-0000-0000-0000-000000000077_ZED_THL_16_Humidity_8/values?format=json&pagesize=1&page=1";
    private final static String humiditySensor = "http://www.nassist-test.com/api/sensors/4Noks_77000000-0000-0000-0000-000000000077_ZED_THL_16_Humidity_8/values?format=json&pagesize=1&page=1";
    private final static String temperatureSensor = "http://www.nassist-test.com/api/sensors/4Noks_77000000-0000-0000-0000-000000000077_ZED_THL_16_Temperature_6/values?format=json&pagesize=1&page=1";

    public static CurrentSensorStatus GetValues() {
        CurrentSensorStatus data = new CurrentSensorStatus();
        try {
            /////////////////
            /// Cookies /////
            /////////////////
            CookieStore cookieStore = new BasicCookieStore();
            HttpContext localContext = new BasicHttpContext();
            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

            /////////////////////
            /// Authentication //
            /////////////////////
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost authPost = new HttpPost(authUrl);
            authPost.setHeader("Content-type", "application/json");

            JSONObject creds = new JSONObject();
            creds.put("UserName", "vubgreenhouse");
            creds.put("password", "vubgreenhouse");
            String authString = creds.toString();
            StringEntity se = new StringEntity(authString);

            authPost.setEntity(se);
            HttpResponse response = httpclient.execute(authPost, localContext);

            HttpEntity entity = response.getEntity();
            if (entity != null) {

                // A Simple JSON Response Read
                InputStream instream = entity.getContent();
                String result = convertStreamToString(instream);
                // now you have the string representation of the HTML request
                instream.close();
            }
            /////////////////////////
            /// Carbon Dioxide Sens//
            /////////////////////////
            // Make the request for the data.
            HttpGet req = new HttpGet(co2Sensor);
            response = httpclient.execute(req, localContext);
            entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                String result = convertStreamToString(instream);
                instream.close();
                Gson gson = new Gson();
                SensorResponse resp = gson.fromJson(result, SensorResponse.class);
                data.setCo2Value(resp.getValues().get(1));
            }
            /////////////////////////
            /// Temperature Sensor //
            /////////////////////////
            // Make the request for the data.
            req = new HttpGet(temperatureSensor);
            response = httpclient.execute(req, localContext);
            entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                String result = convertStreamToString(instream);
                instream.close();
                Gson gson = new Gson();
                SensorResponse resp = gson.fromJson(result, SensorResponse.class);
                data.setTemperatureValue(resp.getValues().get(1));
            }
            /////////////////////////
            /// Humidity Sensor /////
            /////////////////////////
            // Make the request for the data.
            req = new HttpGet(humiditySensor);
            response = httpclient.execute(req, localContext);
            entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                String result = convertStreamToString(instream);
                instream.close();
                Gson gson = new Gson();
                SensorResponse resp = gson.fromJson(result, SensorResponse.class);
                data.setHumidityValue(resp.getValues().get(1));
            }
            return data;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String convertStreamToString(InputStream is) {
    /*
     * To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String.
     */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
