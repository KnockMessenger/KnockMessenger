package hu.vadasz.peter.knockmessenger.Controllers.Validators;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import hu.vadasz.peter.knockmessenger.Tools.InternetConnectionChecker;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by Peti on 2018. 05. 12..
 */

@RunWith(MockitoJUnitRunner.class)
public class InternetConnectionValidatorTest {

    public static final int MAX_TIME = 1000;
    public static final boolean OK = true;

    @Mock
    private InternetConnectionChecker internetConnectionChecker;

    private InternetConnectionValidator internetConnectionValidator;

    @Test(timeout = MAX_TIME)
    public void testValidateConnection_ShouldReturnTrue() {
        when(internetConnectionChecker.checkConnection()).thenReturn(InternetConnectionValidator.CONNECTION_IS_OK);
        internetConnectionValidator = new InternetConnectionValidator(internetConnectionChecker);

        assertEquals(OK, internetConnectionValidator.validateConnection());
    }

    @Test(timeout = MAX_TIME)
    public void testValidateConnection_ShouldReturnFalse() {
        when(internetConnectionChecker.checkConnection()).thenReturn(!InternetConnectionValidator.CONNECTION_IS_OK);
        internetConnectionValidator = new InternetConnectionValidator(internetConnectionChecker);

        assertEquals(!OK, internetConnectionValidator.validateConnection());
    }
}