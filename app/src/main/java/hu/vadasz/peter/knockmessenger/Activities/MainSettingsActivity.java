package hu.vadasz.peter.knockmessenger.Activities;

import android.os.Bundle;

import hu.vadasz.peter.knockmessenger.Fragments.SettingsFragment;
import hu.vadasz.peter.knockmessenger.R;

/**
 * This Activity is a wrapper for the setting's fragment.
 */

public class MainSettingsActivity extends BaseActivity {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ACTIVITY OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_settings);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ACTIVITY OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

}
