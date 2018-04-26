package hu.vadasz.peter.knockmessenger.Modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.vadasz.peter.knockmessenger.DataPersister.Server.ServerDataChangeHandler;

/**
 * Created by Peti on 2018. 04. 15..
 */

@Module
public class ServerDataChangeHandlerModule {

    @Singleton
    @Provides
    public ServerDataChangeHandler provideServerDataChangeHandler() {
        return new ServerDataChangeHandler();
    }

}
