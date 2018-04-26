package hu.vadasz.peter.knockmessenger.Controllers.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This exception is thrown if server does not answer in a certain time.
 */

@AllArgsConstructor
public class TimeoutException extends Exception {

    @Getter
    private String message;

}
