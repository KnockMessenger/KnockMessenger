package hu.vadasz.peter.morsecodedecoder.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This exception is thrown if CodeTable is initialized with unknown language.
 */

@AllArgsConstructor
public class LanguageNotFoundException extends Exception {

    @Getter
    private String message;

}
