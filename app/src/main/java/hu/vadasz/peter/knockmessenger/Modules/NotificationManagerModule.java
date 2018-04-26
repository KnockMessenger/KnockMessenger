package hu.vadasz.peter.knockmessenger.Modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.vadasz.peter.knockmessenger.Managers.NotificationManager;

/**
 * Created by Peti on 2018. 04. 22..
 */

@Module(includes = {ApplicationModule.class})
public class NotificationManagerModule {

    @Singleton
    @Provides
    public NotificationManager provideNotificationManagerModule(Context context) {
        return new NotificationManager(context);
    }
}
