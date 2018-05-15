package hu.vadasz.peter.knockmessenger.Modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.vadasz.peter.knockmessenger.Managers.ServiceAlarmManager;

/**
 * This class provides a singleton ServiceAlarmManager object.
 */

@Module(includes = {ApplicationModule.class})
public class ServiceAlarmManagerModule {

    @Provides
    @Singleton
    public ServiceAlarmManager provideServiceAlarmManage(Context context) {
        return new ServiceAlarmManager(context);
    }
}
