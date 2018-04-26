package hu.vadasz.peter.knockmessenger.Controllers.Validators;

import hu.vadasz.peter.knockmessenger.DataPersister.Entities.User;

/**
 * This class is to validate user's data.
 */

public class UserValidator {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    public static final boolean VALID = true;
    public static final char PLUS = '+';
    public static final int MIN_TEL_LENGTH = 9;

    /// CONSTANTS -- END

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method validates a user, the name and the telephone number, the name can not be empty.
     * @param user the user to validate.
     * @return
     */

    public static boolean userIsValid(User user) {
        return !user.getName().isEmpty() && telephoneIsValid(user.getTelephone());
    }

    /**
     * This method validates telephone numbers. A valid telephone number contains digits only
     * the first character can be a + symbol and has a minimum length.
     * @param tel the telephone number to validate.
     * @return
     */

    public static boolean telephoneIsValid(String tel) {
        if (tel == null || tel.length() < MIN_TEL_LENGTH) {
            return !VALID;
        }

        for (int i = 0; i < tel.length(); ++i) {
            if (i == 0 && !Character.isDigit(tel.charAt(i)) && tel.charAt(i) != PLUS) {
                return !VALID;
            } else if ( i > 0 && !Character.isDigit(tel.charAt(i))) {
                return !VALID;
            }
        }

        return VALID;
    }
}
