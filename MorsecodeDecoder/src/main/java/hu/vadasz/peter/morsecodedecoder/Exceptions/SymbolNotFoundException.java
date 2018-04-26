package hu.vadasz.peter.morsecodedecoder.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This exception is threw if we are blocked in the code tree.
 */

@AllArgsConstructor
public class SymbolNotFoundException extends Exception {

    @Getter
    private String message;
}
