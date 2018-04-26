package hu.vadasz.peter.knockmessenger.Controllers.Validators;

import android.os.Environment;

/**
 * Created by Peti on 2018. 03. 31..
 */

public class ExternalStorageValidator {

    /**
     * This method checks the external storage.
     * @return true if external storage is OK.
     */

    public static boolean storageIsOk() {

        String storageStatus = Environment.getExternalStorageState();

        if (storageStatus.equals(Environment.MEDIA_REMOVED)
                || storageStatus.equals(Environment.MEDIA_UNMOUNTED)
                || storageStatus.equals(Environment.MEDIA_UNMOUNTABLE)
                || storageStatus.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            return false;
        } else {
            if (storageStatus.equals(Environment.MEDIA_MOUNTED)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
