package hu.vadasz.peter.knockmessenger.Modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.vadasz.peter.knockmessenger.Managers.SharedPreferenceManager;

/**
 * This class provides a class to manage main settings (shared preferences).
 */

@Module(includes = ApplicationModule.class)
public class SharedPreferenceManagerModule {

    @Provides
    @Singleton
    public SharedPreferenceManager provideSharedPreferenceManager(Context context) {
        return new SharedPreferenceManager(context);
    }
}
