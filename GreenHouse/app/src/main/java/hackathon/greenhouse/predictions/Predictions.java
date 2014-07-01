package hackathon.greenhouse.predictions;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import hackathon.greenhouse.R;
import hackathon.greenhouse.predictions.POJO.PredictionResult;

public class Predictions extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predictions);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        new AsyncGetPredictionData().execute(sharedPrefs);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.predictions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void showPredictions(View view) {
        Intent intent = new Intent(this, Predictions.class);
        startActivity(intent);
    }

    /**********************************************************************************************/
    /**********************************************************************************************/
    /**
     * ******************************************************************************************
     */
    private class AsyncGetPredictionData extends AsyncTask<SharedPreferences, Void, PredictionResult> {
        @Override
        protected PredictionResult doInBackground(SharedPreferences... prefs) {
            for (SharedPreferences pref : prefs)
                return GetPredictions(pref);
            return null;
        }

        protected void onPostExecute(final PredictionResult result) {

            runOnUiThread(new Runnable() {
                public void run() {
                    GenerateGraphs(result);
                }
            });
        }

        private PredictionResult GetPredictions(SharedPreferences pref) {
            PredictionResult x = null;
            HttpPost httppost = new HttpPost("http://192.168.1.108:8080/OptimizeGreenhouseWS/GreenhouseOptWS?wsdl");
            StringEntity se;
            try {
                String opt_temp = pref.getString("opt_temp", "35");
                String opt_bright = pref.getString("opt_bright", "600");
                String t_weight = pref.getString("t_weight", "0.5");
                double tw = Double.parseDouble(t_weight);
                double tb = 1 - tw;
                String budget = pref.getString("budget", "750");
                String max_heat = pref.getString("max_heat", "4");
                String max_light = pref.getString("max_light", "700");

                String SOAPRequestXML =
                        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:gre=\"http://greenhouseoptws/\">\n" +
                                "   <soapenv:Header/>\n" +
                                "   <soapenv:Body>\n" +
                                "      <gre:getCostEfficiency>\n" +
                                "         <!--Optional:-->\n" +
                                "         <json>{\"opt_temp\":\"" + opt_temp + "\",\"opt_bright\":\"" + opt_bright + "\",\"wT\":\"" + t_weight + "\",\"wB\":\"" + tb + "\",\"budget\":\"" + budget + "\",\"max_temp_heat\":\"" + max_heat+ "\",\"max_bright_light\":\"" + opt_temp + "\"}</json>\n" +
                                "      </gre:getCostEfficiency>\n" +
                                "   </soapenv:Body>\n" +
                                "</soapenv:Envelope>";
                Log.d("request is ", SOAPRequestXML);
                se = new StringEntity(SOAPRequestXML, HTTP.UTF_8);


                se.setContentType("text/xml");
                httppost.setHeader("Accept-Charset", "utf-8");
                httppost.setHeader("Accept", "text/xml");
                httppost.setEntity(se);

                HttpClient httpclient = new DefaultHttpClient();
                BasicHttpResponse httpResponse =
                        (BasicHttpResponse) httpclient.execute(httppost);
                HttpEntity resEntity = httpResponse.getEntity();
                String result = EntityUtils.toString(resEntity);

                String msg = result;
                if (msg.indexOf("&amp;") != -1) {
                    msg = msg.replaceAll("&amp;", "&");
                }
                if (msg.indexOf("&quot;") != -1) {
                    msg = msg.replaceAll("&quot;", "\"");
                }
                result = msg;
                int lio = result.lastIndexOf("<return>");
                int l = result.length();
                String trimmed = result.substring(lio + "<return>".length(), l);

                lio = trimmed.lastIndexOf("</return>");
                trimmed = trimmed.substring(0, lio);
                msg = trimmed;
                if (msg.indexOf("&amp;") != -1) {
                    msg = msg.replaceAll("&amp;", "&");
                }
                if (msg.indexOf("&quot;") != -1) {
                    msg = msg.replaceAll("&quot;", "\"");
                }
                trimmed = msg;
                Gson gson = new Gson();
                x = gson.fromJson(trimmed, PredictionResult.class);
                return x;
            } catch (Exception e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
            }
            return x;
        }
    }

    private void GenerateGraphs(PredictionResult result) {
        List<Double> xvals = result.getT();
        List<Double> yvals = result.getTemp_cost();

        GraphView.GraphViewData[] dataset = new GraphView.GraphViewData[xvals.size()];

        for (int i = 0; i < xvals.size(); i++)
            dataset[i] = new GraphView.GraphViewData(xvals.get(i), yvals.get(i));

        GraphViewSeries exampleSeries = new GraphViewSeries("Predicted kW/h (5 days)", new GraphViewSeries.GraphViewSeriesStyle(getResources().getColor(R.color.vubgreen), 3), dataset);

        GraphView graphView = new LineGraphView(Predictions.this, "Predicted kW/h (5 days)");
        //graphView.getGraphViewStyle().setNumVerticalLabels(10);
        //final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d HH:mm");
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d HH:mm");
        graphView.setCustomLabelFormatter(new CustomLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX)
                {
                    long epoch = System.currentTimeMillis();
                    long minutes = (long) (value * 60);
                    long seconds = minutes * 60;
                    long miliseconds = seconds * 1000;
                    long totalTime = epoch + miliseconds;
                    Date actualTime = new Date(totalTime);
                    return dateFormat.format(actualTime);
                }

                else
                    return String.valueOf(((int)(value * 10)) / 10.0);
            }
        });


        graphView.addSeries(exampleSeries); // data
        FrameLayout layout = (FrameLayout) Predictions.this.findViewById(R.id.predictionsGraph);
        layout.addView(graphView);

        TextView tv = (TextView) Predictions.this.findViewById(R.id.textbox);
        tv.setText("");
    }
}
