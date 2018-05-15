package hu.vadasz.peter.knockmessenger.Application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.facebook.stetho.Stetho;

import hu.vadasz.peter.knockmessenger.Components.DaggerMainComponent;
import hu.vadasz.peter.knockmessenger.Components.MainComponent;
import hu.vadasz.peter.knockmessenger.Modules.ApplicationModule;
import hu.vadasz.peter.knockmessenger.Modules.InternetConnectionCheckerModule;
import hu.vadasz.peter.knockmessenger.Modules.NotificationManagerModule;
import hu.vadasz.peter.knockmessenger.Modules.PermissionHandlerModule;
import hu.vadasz.peter.knockmessenger.Modules.ServerDataChangeHandlerModule;
import hu.vadasz.peter.knockmessenger.Modules.ServiceAlarmManagerModule;
import hu.vadasz.peter.knockmessenger.Modules.SharedPreferenceManagerModule;
import hu.vadasz.peter.knockmessenger.Modules.StorageModule;
import hu.vadasz.peter.knockmessenger.Modules.VibratorEngineModule;
import hu.vadasz.peter.knockmessenger.Services.MessageReceiverService;

/**
 * The Application class of the app. This class is responsible for creating dagger's
 * (dependency injection) main component by initializing the necessary (injectable) classes.
 */

public class BaseApplication extends Application {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private static BaseApplication mInstance;

    private MainComponent mMainComponent;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public BaseApplication() {
        mInstance = this;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// UTILS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This methods returns dagger's main component for later use (select classes tin which to
     * inject).
     * @return dagger's main component
     */

    public MainComponent getmMainComponent() {
        return mMainComponent;
    }

    /**
     * The instance of the initialized application object can be queried by this method.
     * @return instance of the application
     */

    public static BaseApplication getInstance() {return mInstance; }

    /**
     * This method configures Stetho dev tool for debugging, see result: chrome://inspect
     */

    private void initializeStetho() {
        Stetho.initializeWithDefaults(this);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// UTILS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// APPLICATION OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate() {
        super.onCreate();

        initializeStetho();

        mMainComponent = DaggerMainComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .storageModule(new StorageModule())
                .vibratorEngineModule(new VibratorEngineModule())
                .internetConnectionCheckerModule(new InternetConnectionCheckerModule())
                .permissionHandlerModule(new PermissionHandlerModule())
                .sharedPreferenceManagerModule(new SharedPreferenceManagerModule())
                .serverDataChangeHandlerModule(new ServerDataChangeHandlerModule())
                .notificationManagerModule(new NotificationManagerModule())
                .serviceAlarmManagerModule(new ServiceAlarmManagerModule())
                .build();

        startMessageReceivingService();
    }

    /**
     * This method starts the background service which receives incoming messages.
     */

    public void startMessageReceivingService() {
        stopService(new Intent(this, MessageReceiverService.class));
        startService(new Intent(this, MessageReceiverService.class));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// APPLICATION OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
