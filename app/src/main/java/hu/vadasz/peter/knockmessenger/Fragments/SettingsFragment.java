package hu.vadasz.peter.knockmessenger.Fragments;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import javax.inject.Inject;

import hu.vadasz.peter.knockmessenger.Application.BaseApplication;
import hu.vadasz.peter.knockmessenger.Dialogs.MeasureTimeNumberPickerDialogPreference;
import hu.vadasz.peter.knockmessenger.Dialogs.MicrophoneSensitivityNumberPickerDialogPreference;
import hu.vadasz.peter.knockmessenger.Managers.SharedPreferenceManager;
import hu.vadasz.peter.knockmessenger.R;

/**
 * This class is responsible for the preferences view and actions handlers.
 */

public class SettingsFragment extends PreferenceFragment {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// INNER CLASSES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * PreferenceChange listener for CheckBox-es (boolean values).
     */

    private class CheckBoxPreferenceChangeListener implements Preference.OnPreferenceChangeListener {

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            sharedPreferenceManager.saveBoolean(preference.getKey(), ((Boolean) newValue).booleanValue());
            return true;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// INNER CLASSES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// DEPENDENCIES

    @Inject
    protected SharedPreferenceManager sharedPreferenceManager;

    /// DEPENDENCIES -- END

    private CheckBoxPreference vibrationCheck;
    private CheckBoxPreference soundsCheck;
    private CheckBoxPreference modeCheck;
    private CheckBoxPreference systemNotificationCheck;
    private MeasureTimeNumberPickerDialogPreference measureTimePickerDialogPreference;
    private MicrophoneSensitivityNumberPickerDialogPreference microphoneSensitivityNumberPickerDialogPreference;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FRAGMENT OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main_settings);
        BaseApplication.getInstance().getmMainComponent().inject(this);

        getPreferences();
        setChangeListeners();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FRAGMENT OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method queries the references of the used preferences from the resource files.
     */

    private void getPreferences() {
        vibrationCheck = (CheckBoxPreference) findPreference(SharedPreferenceManager.VIBRATION_PREFERENCE_KEY);
        soundsCheck = (CheckBoxPreference) findPreference(SharedPreferenceManager.SOUNDS_PREFERENCE_KEY);
        modeCheck = (CheckBoxPreference) findPreference(SharedPreferenceManager.MODE_PREFERENCE_KEY);
        systemNotificationCheck = (CheckBoxPreference) findPreference(SharedPreferenceManager.SYSTEM_NOTIFICATION_PREFERENCE_KEY);
        measureTimePickerDialogPreference = (MeasureTimeNumberPickerDialogPreference)
                findPreference(SharedPreferenceManager.SHORT_UNIT_TIME_PREFERENCE_KEY);
        microphoneSensitivityNumberPickerDialogPreference = (MicrophoneSensitivityNumberPickerDialogPreference)
                findPreference(SharedPreferenceManager.MIC_SENSITIVITY_PREFERENCE_KEY);
    }

    /**
     * This method sets the changeListeners of the preferences where needed.
     */

    private void setChangeListeners() {
        vibrationCheck.setOnPreferenceChangeListener(new CheckBoxPreferenceChangeListener());
        soundsCheck.setOnPreferenceChangeListener(new CheckBoxPreferenceChangeListener());
        modeCheck.setOnPreferenceChangeListener(new CheckBoxPreferenceChangeListener());
        systemNotificationCheck.setOnPreferenceChangeListener(new CheckBoxPreferenceChangeListener());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// METHODS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
