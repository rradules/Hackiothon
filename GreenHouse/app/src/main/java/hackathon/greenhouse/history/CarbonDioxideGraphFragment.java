package hackathon.greenhouse.history;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import hackathon.greenhouse.R;
import weatherdata.POJO.SensorResponse;
import weatherdata.POJO.SensorStatus;


public class CarbonDioxideGraphFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";

    private FrameLayout frameLayout;

    public CarbonDioxideGraphFragment() {
    }
    public void onCreate(Bundle savedInstance)
    {
        setRetainInstance(true);
        super.onCreate(savedInstance);
        frameLayout = (FrameLayout) getActivity().findViewById(R.id.co2graph);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true); // So we don't reload on orientation
        View rootView = inflater.inflate(R.layout.fragment_co2graph, container, false);
        AsyncGetTemperature toExec = new AsyncGetTemperature();
        toExec.execute("");
        return rootView;
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

    private class AsyncGetTemperature extends AsyncTask<String, Void, SensorResponse> {
        private DateFormat requestDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        private String authUrl = "http://www.nassist-test.com/api/auth";
        private String username = "vubgreenhouse";
        private String pass = username;
        private String jsonSensorData = "http://www.nassist-test.com/api/sensors/4Noks_77000000-0000-0000-0000-000000000077_ZED_CO2_24_CO2_6/values?format=json&";


        @Override
        protected SensorResponse doInBackground(String... params) {
            for (String param : params) {
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

                    /////////////////////////
                    /// Temperature Sensor //
                    /////////////////////////
                    HttpResponse response = httpclient.execute(authPost, localContext);

                    HttpEntity entity = response.getEntity();
                    if (entity != null) {

                        // A Simple JSON Response Read
                        InputStream instream = entity.getContent();
                        String result = convertStreamToString(instream);
                        // now you have the string representation of the HTML request
                        instream.close();
                    }
                    // Construct the URL to go exactly 48 hours back.
                    Date now = new Date();
                    Date dateBefore = new Date(now.getTime() - 2 * 24 * 3600 * 1000); //Subtract 2 days
                    String begindate = requestDateFormat.format(dateBefore);
                    String endDate = requestDateFormat.format(now);

                    // append "fromDate=2014-06-01T00:00:00&toDate=2014-06-12T23:59:59"
                    HttpGet req = new HttpGet(jsonSensorData + "fromDate=" + begindate + "&toDate=" + endDate);
                    System.out.println(jsonSensorData + "fromDate=" + begindate + "&toDate=" + endDate);
                    response = httpclient.execute(req, localContext);
                    entity = response.getEntity();
                    if (entity != null) {
                        InputStream instream = entity.getContent();
                        String result = convertStreamToString(instream);
                        // now you have the string representation of the HTML request
                        instream.close();
                        Gson gson = new Gson();
                        // Convert to JSON
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
            }
            return null;
        }

        protected void onPostExecute(final SensorResponse result) {

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    SetTemperatureChart(result);
                }
            });
        }

        /**
         * Create the graph for temperature sensor
         */
        private void SetTemperatureChart(SensorResponse data) {

            List<SensorStatus> unsorted = data.getValues();

            Collections.sort(unsorted, new Comparator<SensorStatus>() {
                public int compare(SensorStatus b1, SensorStatus b2) {
                    if (b1.getDateObject().before(b2.getDateObject()))
                        return -1;
                    if (b1.getDateObject().after(b2.getDateObject()))
                        return +1;
                    return 0;
                }
            });
            SensorStatus[] sorted = new SensorStatus[unsorted.size()];

            for(int i = 0; i < unsorted.size(); i++)
                sorted[i] = unsorted.get(i);

            GraphView.GraphViewData[] dataset = new GraphView.GraphViewData[sorted.length];

            for (int i = 0; i < data.getValues().size(); i++) {
                SensorStatus val = sorted[i];
                long milisecs = val.getDateObject().getTime();
                dataset[i] = new GraphView.GraphViewData(milisecs, val.getValue());
            }

            GraphViewSeries exampleSeries = new GraphViewSeries("", new GraphViewSeries.GraphViewSeriesStyle(getResources().getColor(R.color.vubgreen), 3), dataset);

            GraphView graphView = new LineGraphView(getActivity(), "");
            graphView.getGraphViewStyle().setNumVerticalLabels(10);
            final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d HH:mm");
            graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
                @Override
                public String formatLabel(double value, boolean isValueX) {
                    if (isValueX)
                        return dateFormat.format(new Date((long) value));
                    else
                        return String.valueOf(((int) (value * 10)) / 10.0);
                }
            });


            graphView.addSeries(exampleSeries); // data
            FrameLayout layout = (FrameLayout) getActivity().findViewById(R.id.co2graph);
            layout.addView(graphView);
            TextView dummyTextView = (TextView) getActivity().findViewById(R.id.section_label3);
            dummyTextView.setText("");
        }


    }
}