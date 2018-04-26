package hu.vadasz.peter.knockmessenger.Modules;

import android.content.Context;
import android.provider.ContactsContract;

import org.greenrobot.greendao.database.Database;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.DaoMaster;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.DaoSession;
import hu.vadasz.peter.knockmessenger.DataPersister.JSON.JsonParser;
import hu.vadasz.peter.knockmessenger.DataPersister.Managers.CodeDataManager;
import hu.vadasz.peter.knockmessenger.DataPersister.Managers.DataBaseManager;
import hu.vadasz.peter.knockmessenger.DataPersister.Managers.MessageDataManager;
import hu.vadasz.peter.knockmessenger.DataPersister.Managers.UserDataManager;
import hu.vadasz.peter.knockmessenger.DataPersister.Server.ServerSideDatabase;

/**
 * This module provides access to local database.
 */

@Module(includes = ApplicationModule.class)
public class StorageModule {

    private static final String DB_NAME = "knockMessenger-db";

    @Provides
    @Singleton
    DaoSession provideDaoSession(Context appContext) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(appContext, DB_NAME);
        Database db = helper.getWritableDb();

        //DaoMaster.dropAllTables(db, true);
        DaoMaster.createAllTables(db, true);

        return new DaoMaster(db).newSession();
    }

    @Provides
    @Singleton
    DataBaseManager provideDatabaseManager(DaoSession daoSession) {
        return new DataBaseManager(daoSession);
    }

    @Provides
    @Singleton
    JsonParser provideJsonParser(Context context) {
        return new JsonParser(context);
    }

    @Provides
    @Singleton
    CodeDataManager provideCodeDataManager(DataBaseManager dataBaseManager, JsonParser jsonParser) {
        return new CodeDataManager(dataBaseManager, jsonParser);
    }

    @Provides
    @Singleton
    ServerSideDatabase provideServerSideDatabase() {
        return new ServerSideDatabase();
    }

    @Provides
    @Singleton
    UserDataManager provideUserDataManager(DataBaseManager dataBaseManager, ServerSideDatabase serverSideDatabase) {
        return new UserDataManager(dataBaseManager, serverSideDatabase);
    }

    @Singleton
    @Provides
    MessageDataManager provideMessageDataManager(DataBaseManager dataBaseManager, ServerSideDatabase serverSideDatabase) {
        return new MessageDataManager(dataBaseManager, serverSideDatabase);
    }
}
