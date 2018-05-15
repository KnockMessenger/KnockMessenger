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
import hu.vadasz.peter.knockmessenger.Tools.InternetConnectionChecker;

/**
 * This class controls the user data managing.
 */

public class UserController {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private UserDataManager dataManager;
    private InternetConnectionValidator internetConnectionValidator;
    private Context context;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public UserController(UserDataManager dataManager, Context context) {
        this.dataManager = dataManager;
        this.context = context;
        internetConnectionValidator = new InternetConnectionValidator();
    }

    public UserController(UserDataManager dataManager, Context context, InternetConnectionChecker internetConnectionChecker) {
        this.dataManager = dataManager;
        this.context = context;
        internetConnectionValidator = new InternetConnectionValidator(internetConnectionChecker);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method checks if the user's data is valid.
     * @param user the user to check.
     * @throws DeviceIsOfflineException
     * @throws InvalidUserException
     */

    private void checkConditions(User user) throws DeviceIsOfflineException, InvalidUserException {
        if (!internetConnectionValidator.validateConnection()) {
            throw new DeviceIsOfflineException(context.getString(R.string.device_offline_error));
        } else if (!UserValidator.userIsValid(user)) {
            throw new InvalidUserException(context.getString(R.string.profileActivity_invalidUser_error));
        }
    }

    /**
     * This method tries to save the user's data by accessing the server.
     * @param user the user to save.
     * @param listener the class which receives the server's answers.
     * @throws DeviceIsOfflineException
     * @throws InvalidUserException
     */
    public void trySaveUser(User user, ValueEventListener listener) throws DeviceIsOfflineException, InvalidUserException {
        checkConditions(user);
        dataManager.trySaveUser(user.getTelephone(), listener);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

}
