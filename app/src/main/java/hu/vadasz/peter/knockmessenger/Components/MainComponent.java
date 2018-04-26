package hu.vadasz.peter.knockmessenger.Components;

import javax.inject.Singleton;

import dagger.Component;
import hu.vadasz.peter.knockmessenger.Activities.BaseActivity;
import hu.vadasz.peter.knockmessenger.Activities.BaseActivityWithKnockDecoder;
import hu.vadasz.peter.knockmessenger.Activities.BrowseFriendsActivity;
import hu.vadasz.peter.knockmessenger.BroadcastReceivers.AlarmReceiver;
import hu.vadasz.peter.knockmessenger.BroadcastReceivers.RebootReceiver;
import hu.vadasz.peter.knockmessenger.Controllers.Validators.InternetConnectionValidator;
import hu.vadasz.peter.knockmessenger.Fragments.SettingsFragment;
import hu.vadasz.peter.knockmessenger.Managers.NotificationManager;
import hu.vadasz.peter.knockmessenger.Managers.ServiceAlarmManager;
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
 * This interface is used for dependency injection. Modules are the module classes of the injectable
 * classes.
 * Methods show in which classes dependencies can be injected.
 */

@Singleton
@Component (modules = {ApplicationModule.class, StorageModule.class, PermissionHandlerModule.class,
        VibratorEngineModule.class, InternetConnectionCheckerModule.class,
        SharedPreferenceManagerModule.class, ServerDataChangeHandlerModule.class, NotificationManagerModule.class,
        ServiceAlarmManagerModule.class})
public interface MainComponent {

    /**
     * Inject to BaseActivity class, should be called in BaseActivity's onCreate method.
     * @param baseActivity class to inject
     */

    void inject(BaseActivity baseActivity);
    void inject(BrowseFriendsActivity browseFriendsActivity);
    void inject(BaseActivityWithKnockDecoder baseActivityWithKnockDecoder);
    void inject(InternetConnectionValidator internetConnectionValidator);
    void inject(SettingsFragment settingsFragment);
    void inject(NotificationManager notificationManager);
    void inject(AlarmReceiver alarmReceiver);
    void inject(MessageReceiverService messageReceiverService);
    void inject(RebootReceiver rebootReceiver);
}
