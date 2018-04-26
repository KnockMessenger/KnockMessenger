package hu.vadasz.peter.knockmessenger.Modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.vadasz.peter.knockmessenger.Tools.InternetConnectionChecker;

/**
 * This class provides a singleton object to check the internet connection.
 */

@Module(includes = ApplicationModule.class)
public class InternetConnectionCheckerModule {

    @Provides
    @Singleton
    InternetConnectionChecker provideInternetConnectionChecker(Context context) {
        return new InternetConnectionChecker(context);
    }
}
