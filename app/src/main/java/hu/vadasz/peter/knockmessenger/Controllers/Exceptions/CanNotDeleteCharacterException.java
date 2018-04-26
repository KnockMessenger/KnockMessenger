package hu.vadasz.peter.knockmessenger.Controllers.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Peti on 2018. 03. 09..
 */

@AllArgsConstructor
public class CanNotDeleteCharacterException extends Exception {

    @Getter
    private String message;
}
