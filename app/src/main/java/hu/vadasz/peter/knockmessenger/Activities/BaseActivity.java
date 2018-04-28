package hu.vadasz.peter.knockmessenger.Activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import javax.inject.Inject;

import hu.vadasz.peter.knockmessenger.Application.BaseApplication;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Friend;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Message;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.User;
import hu.vadasz.peter.knockmessenger.DataPersister.Managers.CodeDataManager;
import hu.vadasz.peter.knockmessenger.DataPersister.Managers.MessageDataManager;
import hu.vadasz.peter.knockmessenger.DataPersister.Managers.UserDataManager;
import hu.vadasz.peter.knockmessenger.DataPersister.Server.ServerDataChangeHandler;
import hu.vadasz.peter.knockmessenger.Managers.NotificationManager;
import hu.vadasz.peter.knockmessenger.Managers.ServiceAlarmManager;
import hu.vadasz.peter.knockmessenger.Managers.SharedPreferenceManager;
import hu.vadasz.peter.knockmessenger.PermissionHandler.PermissionHandler;
import hu.vadasz.peter.knockmessenger.R;
import hu.vadasz.peter.knockmessenger.Tools.VibratorEngine;

/**
 * Class to collect common methods of activities and manage dependency injection.
 */

public abstract class BaseActivity extends AppCompatActivity implements ChildEventListener {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    public static final String EXTRA_NETWORK_ERROR_KEY = "EXTRA_NETWORK_ERROR_KEY";
    public static final boolean EXTRA_NETWORK_ERROR = true;
    public static final String EMPTY_TEXT = "";
    public static final boolean RECYCLER_VIEW_HAS_FIXED_SIZE = true;
    /// CONSTANTS -- END

    @Inject
    protected CodeDataManager codeDataManager;

    @Inject
    protected UserDataManager userDataManager;

    @Inject
    protected PermissionHandler permissionHandler;

    @Inject
    protected VibratorEngine vibratorEngine;

    @Inject
    protected SharedPreferenceManager sharedPreferenceManager;

    @Inject
    protected ServerDataChangeHandler serverDataChangeHandler;

    @Inject
    protected MessageDataManager messageDataManager;

    @Inject
    protected NotificationManager notificationManager;

    @Inject
    protected ServiceAlarmManager serviceAlarmManager;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ACTIVITY OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BaseApplication.getInstance().getmMainComponent().inject(this);

        userDataManager.loadFriends();
    }

    @Override
    public void onResume() {
        super.onResume();
        userDataManager.setServerDataChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        userDataManager.removeServerDataChaneListener(this);
        if (userDataManager.isLoggedIn()) {
            serviceAlarmManager.createAlarmToStartService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean permissionGranted = false;

        switch(requestCode) {

            case PermissionHandler.REQUEST_RECORD_AUDIO_AND_EXTERNAL_STORAGE_PERMISSION_CODE:
                for (int result : grantResults) {
                    permissionGranted = result == PackageManager.PERMISSION_GRANTED;
                    if (!permissionGranted) {
                        break;
                    }
                }
                break;
        }

        if(!permissionGranted) {
            clearAndStartActivity(new Intent(this, PermissionDeniedActivity.class));
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ACTIVITY OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// UTILS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Starts new Activity with specified Intent, and finishes the actual activity.
     * @param intent
     */

    protected void clearAndStartActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * This method makes the Activity to fit the screen.
     */

    protected void makeActivityFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
    }

    /**
     * This method keeps the device awake while the activity is running.
     */

    protected void keepDeviceAwake() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * This method pop ups a snackbBar with the given error message.
     * @param message the error.
     */

    protected void showErrorMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * This method pop ups a snackbBar with the given error message and finishes the activity.
     * @param message the error.
     */

    protected void showErrorMessageAndFinish(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                .addCallback(new Snackbar.Callback() {

                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        finish();
                    }

                })
                .show();
    }

    protected void showMarshmallowError() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            showErrorMessage(getString(R.string.android_alarmManager_versionNotification));
            notificationManager.createNotification(getString(R.string.android_system_title), getString(R.string.android_alarmManager_versionNotification),
                    NotificationManager.SYSTEM_NOTIFICATION_ID, !NotificationManager.NOTIFICATION_WITH_MEDIA);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// UTILS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FireBase ChildEventListener INTERFACE OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
        if (dataSnapshot.exists()) {
            serverDataChangeHandler.friendAdded(new Friend(dataSnapshot.getValue(User.class)));
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
        if (dataSnapshot.exists()) {
            Friend friend = new Friend(dataSnapshot.getValue(User.class));
            if (!friend.getTel().equals(userDataManager.getUser().getTelephone())) {
                if (userDataManager.isFriend(friend)) {
                    userDataManager.updateFriend(friend);
                }
                serverDataChangeHandler.friendChanged(friend);
            }
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            Friend friend = new Friend(dataSnapshot.getValue(User.class));
            if (userDataManager.isFriend(friend)) {
                userDataManager.deleteFriend(friend);
            }
            serverDataChangeHandler.friendDeleted(friend);
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

    @Override
    public void onCancelled(DatabaseError databaseError) {}

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FireBase ChildEventListener INTERFACE OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
