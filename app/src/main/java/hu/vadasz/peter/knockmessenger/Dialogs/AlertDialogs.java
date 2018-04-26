package hu.vadasz.peter.knockmessenger.Dialogs;

import android.app.AlertDialog;
import android.content.Context;

import lombok.AllArgsConstructor;

/**
 * This class creates AlertDialogs with specified title and message.
 */

@AllArgsConstructor
public class AlertDialogs {

    private Context context;

    public AlertDialog createAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        return builder.create();
    }
}
