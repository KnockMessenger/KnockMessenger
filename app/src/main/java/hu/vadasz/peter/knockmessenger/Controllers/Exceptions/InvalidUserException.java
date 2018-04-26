package hu.vadasz.peter.knockmessenger.Controllers.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This exception used when the user data is invalid.
 */

@AllArgsConstructor
public class InvalidUserException extends Exception {

    @Getter
    private String message;
}
