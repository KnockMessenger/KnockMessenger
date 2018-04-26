package hu.vadasz.peter.knockmessenger.PermissionHandler;

import android.support.v7.app.AppCompatActivity;

/**
 * This is class ensure to request runtime permissions.
 */

public class PermissionHandler {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    public static final int REQUEST_RECORD_AUDIO_AND_EXTERNAL_STORAGE_PERMISSION_CODE = 250;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method asks the user for the permissions given in the permissions parameter.
     * @param activity the activity which requests for permissions.
     * @param permissions the requested permissions.
     * @param permissionId the unique id for the request, static members of this class should be used.
     */

    public void askForPermission(AppCompatActivity activity, String[] permissions, int permissionId) {

        activity.requestPermissions(permissions, permissionId);
    }
}
