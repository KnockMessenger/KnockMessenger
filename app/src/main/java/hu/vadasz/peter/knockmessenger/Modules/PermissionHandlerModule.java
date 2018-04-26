package hu.vadasz.peter.knockmessenger.Modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.vadasz.peter.knockmessenger.PermissionHandler.PermissionHandler;

/**
 * This class is part of dependency injection, provides singleton PermissionHandler class. The methods
 * are used by dagger.
 */

@Module
public class PermissionHandlerModule {

    /**
     * This method return a new PermissionHandler instance, method is used by dagger.
     * @return new PermissionHandler class.
     */

    @Provides
    @Singleton
    public PermissionHandler providePermissionHandler() {
        return new PermissionHandler();
    }
}
