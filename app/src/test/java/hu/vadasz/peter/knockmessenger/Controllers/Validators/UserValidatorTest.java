package hu.vadasz.peter.knockmessenger.Controllers.Validators;

import org.junit.Test;

import hu.vadasz.peter.knockmessenger.DataPersister.Entities.User;

import static org.junit.Assert.*;

/**
 * This class test UserValidator.
 */

public class UserValidatorTest {

    public static final int MAX_TIME = 1000;
    public static final boolean OK = true;

    @Test(timeout = MAX_TIME)
    public void testTelephoneIsValid_NumberContainsNonDigitCharactersShouldReturnFalse() {
        assertEquals(!OK, UserValidator.telephoneIsValid("123abc4566"));
    }

    @Test(timeout = MAX_TIME)
    public void testTelephoneIsValid_NumberIsTooShortShouldReturnFalse() {
        assertEquals(!OK, UserValidator.telephoneIsValid("123"));
    }

    @Test(timeout = MAX_TIME)
    public void testTelephoneIsValid_NumbersSecondCharacterIsPlusShouldReturnFalse() {
        assertEquals(!OK, UserValidator.telephoneIsValid("1+23456789"));
    }

    @Test(timeout = MAX_TIME)
    public void testTelephoneIsValid_NumberIsEmptyShouldReturnFalse() {
        assertEquals(!OK, UserValidator.telephoneIsValid(""));
    }

    @Test(timeout = MAX_TIME)
    public void testTelephoneIsValid_NumberIsNullShouldReturnFalse() {
        assertEquals(!OK, UserValidator.telephoneIsValid(null));
    }

    @Test(timeout = MAX_TIME)
    public void testTelephoneIsValid_NumberIsValidWithoutPlusSymbolShouldReturnTrue() {
        assertEquals(OK, UserValidator.telephoneIsValid("123456789"));
    }

    @Test(timeout = MAX_TIME)
    public void testTelephoneIsValid_NumberIsValidWithPlusSymbolShouldReturnTrue() {
        assertEquals(OK, UserValidator.telephoneIsValid("+123456789"));
    }

    @Test(timeout = MAX_TIME)
    public void testUserIsValid_NameIsEmptyShouldReturnFalse() {
        assertEquals(!OK, UserValidator.userIsValid(new User(1L, "", "123456789", "")));
    }

    @Test(timeout = MAX_TIME)
    public void testUserIsValid_TelephoneIsIncorrectShouldReturnFalse() {
        assertEquals(!OK, UserValidator.userIsValid(new User(1L, "XY", "123A", "")));
    }

    @Test(timeout = MAX_TIME)
    public void testUserIsValid_UserIsCorrectShouldReturnTrue() {
        assertEquals(OK, UserValidator.userIsValid(new User(1L, "XY", "+123456789", "")));
    }
}