package hu.vadasz.peter.knockdetector.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Exception to indicate AudioRecorder init errors.
 */

@Data
@AllArgsConstructor
public class AudioRecorderInitException extends Exception {

    private String message;
}
