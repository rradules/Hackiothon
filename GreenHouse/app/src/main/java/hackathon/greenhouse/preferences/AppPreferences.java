package hackathon.greenhouse.preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import hackathon.greenhouse.R;

public class AppPreferences extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}