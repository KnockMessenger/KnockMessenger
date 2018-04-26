package hu.vadasz.peter.knockmessenger.Controllers.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This exception is thrown if the user tries to send an empty message.
 */

@AllArgsConstructor
public class EmptyMessageException extends Exception {

    @Getter
    private String message;
}
