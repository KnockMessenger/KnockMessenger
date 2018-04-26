package hu.vadasz.peter.knockdetector.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Exception threw if decoder's buffer can not be found.
 */

@AllArgsConstructor
public class SignalReceiverNotFoundException extends Exception {

    @Getter
    private String message;
}
