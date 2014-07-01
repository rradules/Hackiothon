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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import weatherdata.POJO.SensorResponse;

/**
 * Created by christophe on 30.06.14.
 */
public class CarbonDioxideSensor {
    private final static String authUrl = "http://www.nassist-test.com/api/auth";
    private final static DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private final static String rangeValuesUrl = "http://www.nassist-test.com/api/sensors/4Noks_77000000-0000-0000-0000-000000000077_ZED_CO2_24_CO2_6/values?";

    public static SensorResponse GetValues()
    {
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

            // Construct the URL to go exactly 48 hours back.
            Date now = new Date();
            Date dateBefore = new Date(now.getTime() - 2 * 24 * 3600 * 1000); //Subtract 2 days
            String begindate = requestDateFormat.format(dateBefore);
            String endDate = requestDateFormat.format(now);

            // Make the request for the data.
            HttpGet req = new HttpGet(rangeValuesUrl + "fromDate=" + begindate + "&toDate=" + endDate);
            response = httpclient.execute(req, localContext);
            entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                String result = convertStreamToString(instream);
                instream.close();
                Gson gson = new Gson();
                SensorResponse resp = gson.fromJson(result, SensorResponse.class);
                return resp;
            }
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
