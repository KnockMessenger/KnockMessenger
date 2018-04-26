package hu.vadasz.peter.knockmessenger.Controllers.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by Peti on 2018. 03. 09..
 */

@AllArgsConstructor
@NoArgsConstructor
public class DeviceIsOfflineException extends Exception {

    @Getter
    private String message;
}
