package hackathon.greenhouse.currentdata;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import gauge.GaugeView;
import hackathon.greenhouse.R;
import weatherdata.CurrentDataValues;
import weatherdata.POJO.CurrentSensorStatus;

public class CurrentData extends Activity {
    private GaugeView mGaugeView1;
    private GaugeView mGaugeView2;
    private GaugeView mGaugeView3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_data);
        mGaugeView1 = (GaugeView) findViewById(R.id.gauge_view1);
        mGaugeView2 = (GaugeView) findViewById(R.id.gauge_view2);
        mGaugeView3 = (GaugeView) findViewById(R.id.gauge_view3);
        mGaugeView1.setTargetValue(550);
        mGaugeView2.setTargetValue(25);
        mGaugeView3.setTargetValue(50);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.current_data, menu);
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

    private class AsyncGetCurrentData extends AsyncTask<String, Void, CurrentSensorStatus>
    {

        @Override
        protected CurrentSensorStatus doInBackground(String... strings) {
            return CurrentDataValues.GetValues();
        }
        protected void onPostExecute(final CurrentSensorStatus result) {

            runOnUiThread(new Runnable() {
                public void run() {
                    UpdateGuages(result);
                }
            });
        }
    }

    private void UpdateGuages(CurrentSensorStatus result) {
        double d = result.getCo2Value().getValue();
        float f =(float) d;
        mGaugeView1.setTargetValue(f);

        d = result.getTemperatureValue().getValue();
        f = (float) d;
        mGaugeView2.setTargetValue(f);

        d = result.getHumidityValue().getValue();
        f = (float)d;
        mGaugeView3.setTargetValue(f);
    }
}
