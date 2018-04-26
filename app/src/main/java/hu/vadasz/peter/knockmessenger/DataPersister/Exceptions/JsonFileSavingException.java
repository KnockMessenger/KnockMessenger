package hu.vadasz.peter.knockmessenger.DataPersister.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This exception is threw if a file can not be saved to the app's internal storage.
 */

@AllArgsConstructor
public class JsonFileSavingException extends Exception {

    @Getter
    private String message;
}
