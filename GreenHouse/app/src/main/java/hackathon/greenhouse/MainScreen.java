package hackathon.greenhouse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import hackathon.greenhouse.currentdata.CurrentData;
import hackathon.greenhouse.history.SlidingGraphs;
import hackathon.greenhouse.predictions.Predictions;
import hackathon.greenhouse.preferences.AppPreferences;


public class MainScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
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

    public void showCharts(View view) {
        Intent intent = new Intent(this, SlidingGraphs.class);
        startActivity(intent);
    }

    public void ShowCurrentStatus(View view) {
        Intent intent = new Intent(this, CurrentData.class);
        startActivity(intent);
    }

    public void showSettings(MenuItem item) {
        Intent intent = new Intent(this, AppPreferences.class);
        startActivity(intent);
    }

    public void showPredictions(View view) {
        Intent intent = new Intent(this, Predictions.class);
        startActivity(intent);
    }
}
