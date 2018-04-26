package hu.vadasz.peter.knockmessenger.Tools;

import android.content.Context;
import android.os.Vibrator;

/**
 * This class uses the device's vibrator if there is.
 */

public class VibratorEngine {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    public static final int SHORT_VIBRATION_TIME = 250;
    public static final int LONG_VIBRATION_TIME = 500;

    /// CONSTANTS -- END

    private Vibrator vibrator;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public VibratorEngine(Context context) {

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The vibrator of the device can be switched on for a certain time by this method.
     * @param vibrationLength the length of the vibration in millis.
     */

    public void vibrate(int vibrationLength) {
        if (vibrator != null) {
            vibrator.vibrate(vibrationLength);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// METHODS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
