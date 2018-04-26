package hu.vadasz.peter.knockmessenger.Activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import hu.vadasz.peter.knockmessenger.Controllers.Validators.InternetConnectionValidator;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Friend;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.User;
import hu.vadasz.peter.knockmessenger.DataPersister.Server.TimeoutHandler;
import hu.vadasz.peter.knockmessenger.R;

/**
 * Splasch screnn activity.
 */

public class SplashActivity extends BaseActivity {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    private static final int SPLASH_SCREEN_DELAY = 3000;

    private TimeoutHandler timeoutHandler;

    private InternetConnectionValidator internetConnectionValidator;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ACTIVITY OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeActivityFullScreen();
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void onStart() {
        super.onStart();
        waitForSplashAnimation(SPLASH_SCREEN_DELAY);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ACTIVITY OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// SPLASH OPERATIONS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method starts the timer for splash screen.
     * @param duration The length of the animation in millisec.
     */

    private void waitForSplashAnimation(int duration) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                clearAndStartActivity(new Intent(SplashActivity.this, MainScreenActivity.class));
            }

        }, duration);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// SPLASH OPERATIONS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

}