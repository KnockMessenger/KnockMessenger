package hu.vadasz.peter.knockmessenger.Controllers;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import hu.vadasz.peter.knockmessenger.Controllers.Exceptions.DeviceIsOfflineException;
import hu.vadasz.peter.knockmessenger.Controllers.Exceptions.InvalidUserException;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.User;
import hu.vadasz.peter.knockmessenger.DataPersister.Managers.UserDataManager;
import hu.vadasz.peter.knockmessenger.Tools.InternetConnectionChecker;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by Peti on 2018. 05. 12..
 */

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    public static final boolean OK = true;
    public static final int MAX_TIME = 1000;

    @Mock
    private Context context;

    @Mock
    private UserDataManager dataManager;

    @Mock
    private InternetConnectionChecker internetConnectionChecker;

    private UserController userController;

    @Test(timeout = MAX_TIME, expected = DeviceIsOfflineException.class)
    public void testTrySaveUser_ShouldThrowDeviceIsOfflineException() throws InvalidUserException, DeviceIsOfflineException {
        when(internetConnectionChecker.checkConnection()).thenReturn(false);
        userController = new UserController(dataManager, context, internetConnectionChecker);
        userController.trySaveUser(null, null);
    }

    @Test(timeout = MAX_TIME, expected = InvalidUserException.class)
    public void testTrySaveUser_ShouldThrowInvalidUserException() throws InvalidUserException, DeviceIsOfflineException {
        when(internetConnectionChecker.checkConnection()).thenReturn(true);
        userController = new UserController(dataManager, context, internetConnectionChecker);
        userController.trySaveUser(new User(1L, "","", ""), null);
    }
}