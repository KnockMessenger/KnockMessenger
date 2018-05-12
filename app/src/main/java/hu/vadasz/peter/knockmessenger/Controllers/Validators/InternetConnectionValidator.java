package hu.vadasz.peter.knockmessenger.Controllers.Validators;

import javax.inject.Inject;

import hu.vadasz.peter.knockmessenger.Application.BaseApplication;
import hu.vadasz.peter.knockmessenger.Tools.InternetConnectionChecker;

/**
 * This class is responsible for checking internet connection.
 */

public class InternetConnectionValidator {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    public static final boolean CONNECTION_IS_OK = true;

    /// CONSTANTS -- END

    /// DEPENDENCIES

    @Inject
    InternetConnectionChecker internetConnectionChecker;

    /// DEPENDENCIES -- END

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public InternetConnectionValidator() {
        BaseApplication.getInstance().getmMainComponent().inject(this);
    }

    public InternetConnectionValidator(InternetConnectionChecker internetConnectionChecker) {
        this.internetConnectionChecker = internetConnectionChecker;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean validateConnection() {
        return internetConnectionChecker.checkConnection();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// METHODS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
