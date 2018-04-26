package hu.vadasz.peter.knockmessenger.Activities;

import android.content.pm.PackageManager;
import android.os.Bundle;

import javax.inject.Inject;

import hu.vadasz.peter.knockdetector.Interfaces.DetectionVisualizer;
import hu.vadasz.peter.knockmessenger.Application.BaseApplication;
import hu.vadasz.peter.knockmessenger.PermissionHandler.PermissionHandler;
import hu.vadasz.peter.knockmessenger.Tools.InternetConnectionChecker;
import hu.vadasz.peter.morsecodedecoder.Decoder.MorseCodeDecoder;

/**
 * Classes implementing this abstract class can receive detected signals from KnockDecoder module.
 * This class is responsible for permission requests result.
 */

public abstract class BaseActivityWithKnockDecoder extends BaseActivity implements MorseCodeDecoder.DecodedSignalReceiver, DetectionVisualizer {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// DEPENDENCIES

    @Inject
    protected InternetConnectionChecker internetConnectionChecker;

    /// DEPENDENCIES -- END

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ACTIVITY OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BaseApplication.getInstance().getmMainComponent().inject(this);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ACTIVITY OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
