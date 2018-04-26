package hu.vadasz.peter.knockdetector.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This exception is threw if there is an undefined syllable.
 */

@AllArgsConstructor
public class UnrecognizedSyllableException extends Exception {

    @Getter
    private String message;
}
