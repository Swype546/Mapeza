package be.ecam.mapeza.mapeza;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * Created by 12415 on 07-03-17.
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        // Add preferences, defined in the XML file
        ///in res->xml->preferences
        addPreferencesFromResource(R.xml.preferences);
    }
}