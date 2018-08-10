package com.example.android.newsapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference value1 = findPreference(getString(R.string.settings_order_key));
            Preference value2 = findPreference(getString(R.string.settings_question_key));
            Preference value3 = findPreference(getString(R.string.settings_section_key));
            bindPreferenceSummaryToValue(value1);
            bindPreferenceSummaryToValue(value2);
            bindPreferenceSummaryToValue(value3);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            String stringValue = o.toString();
            if(preference instanceof ListPreference)
            {
                ListPreference list = (ListPreference) preference;
                int i = list.findIndexOfValue(stringValue);
                if(i >= 0)
                {
                    CharSequence[] labels = list.getEntries();
                    preference.setSummary(labels[i]);
                }
            } else
            {
                preference.setSummary(stringValue);
            }
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }
    }
}