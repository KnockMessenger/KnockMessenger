package hu.vadasz.peter.knockmessenger.Controllers;

import android.content.Context;

import com.google.firebase.database.ValueEventListener;

import hu.vadasz.peter.knockmessenger.Controllers.Exceptions.DeviceIsOfflineException;
import hu.vadasz.peter.knockmessenger.Controllers.Exceptions.InvalidUserException;
import hu.vadasz.peter.knockmessenger.Controllers.Validators.InternetConnectionValidator;
import hu.vadasz.peter.knockmessenger.Controllers.Validators.UserValidator;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.User;
import hu.vadasz.peter.knockmessenger.DataPersister.Managers.UserDataManager;
import hu.vadasz.peter.knockmessenger.R;

/**
 * Created by Peti on 2018. 04. 11..
 */

public class UserController {

    private UserDataManager dataManager;
    private InternetConnectionValidator internetConnectionValidator;
    private Context context;

    public UserController(UserDataManager dataManager, Context context) {
        this.dataManager = dataManager;
        this.context = context;
        internetConnectionValidator = new InternetConnectionValidator();
    }

    private void checkConditions(User user) throws DeviceIsOfflineException, InvalidUserException {
        if (!internetConnectionValidator.validateConnection()) {
            throw new DeviceIsOfflineException(context.getString(R.string.device_offline_error));
        } else if (!UserValidator.userIsValid(user)) {
            throw new InvalidUserException(context.getString(R.string.profileActivity_invalidUser_error));
        }
    }

    public void trySaveUser(User user, ValueEventListener listener) throws DeviceIsOfflineException, InvalidUserException {
        checkConditions(user);
        dataManager.trySaveUser(user.getTelephone(), listener);
    }

    public void deleteUser(User user) {

    }
}
