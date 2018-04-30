package hu.vadasz.peter.knockmessenger.Managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import hu.vadasz.peter.knockmessenger.R;

/**
 * This class is responsible for saving and querying the app's preferences.
 */

public class SharedPreferenceManager {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    public static String VIBRATION_PREFERENCE_KEY;
    public static String SOUNDS_PREFERENCE_KEY;
    public static String MODE_PREFERENCE_KEY;
    public static String SHORT_UNIT_TIME_PREFERENCE_KEY;
    public static String MIC_SENSITIVITY_PREFERENCE_KEY;
    public static String SYSTEM_NOTIFICATION_PREFERENCE_KEY;

    public static final boolean DEFAULT_BOOLEAN = true;
    public static final String DEFAULT_STRING = "";
    public static final int DEFAULT_INT = 0;

    public static final boolean MORSE_MODE_ENABLED = true;

    /// CONSTANTS -- END

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public SharedPreferenceManager(Context context) {
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();

        initPreferenceKeys(context);
    }

    /**
     * This methods inits the keys of the preferences from the resource files.
     * @param context the application's context.
     */

    private void initPreferenceKeys(Context context) {
        VIBRATION_PREFERENCE_KEY = context.getString(R.string.pref_key_vibration);
        SOUNDS_PREFERENCE_KEY = context.getString(R.string.pref_key_sounds);
        MODE_PREFERENCE_KEY = context.getString(R.string.pref_key_mode);
        SHORT_UNIT_TIME_PREFERENCE_KEY = context.getString(R.string.pref_shortUnit_time_key);
        MIC_SENSITIVITY_PREFERENCE_KEY = context.getString(R.string.pref_mic_sensitivity);
        SYSTEM_NOTIFICATION_PREFERENCE_KEY = context.getString(R.string.pref_system_notification);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// PREFERENCE HANDLING METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Persists a preference type of integer.
     * @param key the preference's key.
     * @param value the value to save.
     */

    public void saveInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * Persists a preference type of boolean.
     * @param key the preference's key.
     * @param value the value to save
     */
    public void saveBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * Get the value of a persisted (int type) preference if exists, otherwise returns a default value
     * of the returning type.
     * @param key the preference's key.
     * @return
     */

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, DEFAULT_BOOLEAN);
    }

    /**
     * Get the value of a persisted (boolean type) preference if exists, otherwise returns a default value
     * of the returning type.
     * @param key the preference's key.
     * @return
     */

    public int getInt(String key) {
        return sharedPreferences.getInt(key, DEFAULT_INT);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// PREFERENCE HANDLING METHODS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
