package hu.vadasz.peter.knockmessenger.Modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.vadasz.peter.knockmessenger.Tools.VibratorEngine;

/**
 * This class provides a singleton VibratorEngine object.
 */

@Module(includes = ApplicationModule.class)
public class VibratorEngineModule {

    @Provides
    @Singleton
    VibratorEngine provideVibratorEngine(Context appContext) {
        return new VibratorEngine(appContext);
    }
}
