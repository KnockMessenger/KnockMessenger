package hu.vadasz.peter.morsecodedecoder.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Exception threw if internal error appears.
 */

@AllArgsConstructor
public class MorseCodeDecoderInternalException extends Exception {

    @Getter
    private String message;
}
