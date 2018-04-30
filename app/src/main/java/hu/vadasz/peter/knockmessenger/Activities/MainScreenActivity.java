package hu.vadasz.peter.knockmessenger.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.joda.time.DateTime;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.vadasz.peter.knockmessenger.Adapters.MessageAdapter;
import hu.vadasz.peter.knockmessenger.Controllers.Validators.ExternalStorageValidator;
import hu.vadasz.peter.knockmessenger.Controllers.Validators.InternetConnectionValidator;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Friend;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Message;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.User;
import hu.vadasz.peter.knockmessenger.DataPersister.Managers.CodeDataManager;
import hu.vadasz.peter.knockmessenger.DataPersister.Server.ServerDataChangeHandler;
import hu.vadasz.peter.knockmessenger.Dialogs.AlertDialogs;
import hu.vadasz.peter.knockmessenger.Managers.SharedPreferenceManager;
import hu.vadasz.peter.knockmessenger.PermissionHandler.PermissionHandler;
import hu.vadasz.peter.knockmessenger.R;
import hu.vadasz.peter.knockmessenger.Services.MessageReceiverService;
import hu.vadasz.peter.morsecodedecoder.Decoder.Utils.MorseCodeTable;

/**
 * The application'MORSE_CODE main screen.
 */

public class MainScreenActivity extends BaseActivity implements MessageAdapter.MessageAdapterListener,
        ServerDataChangeHandler.MessageReceivedListener {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    public static final boolean MENU_ITEM_CHECKED = true;
    public static final int SYSTEM_HALT = 0;

    /// CONSTANTS -- END

    /// VIEWS

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.message_sendingActivity_button)
    FloatingActionButton messageSendingButton;

    @BindView(R.id.profiler_button)
    FloatingActionButton profilerButton;

    @BindView(R.id.mainScreenActivity_drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.mainScreenActivity_main_menu)
    NavigationView navigationView;

    @BindView(R.id.mainScreenActivity_messages)
    RecyclerView messagesRecyclerView;

    @BindView(R.id.mainScreen_progressBar)
    ProgressBar progressBar;

    @BindView(R.id.mainScreenActivity_messages_icon)
    ImageView imageView;

    /// VIEWS -- END

    private MessageAdapter adapter;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ACTIVITY OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*for (int i = 0; i < 250; ++i) {
            Message m = new Message();
            m.setDeleted(false);
            m.setKey("key");
            m.setDateTime(new DateTime().getMillis());
            m.setFromTelephone("111111111");
            m.setToTelephone("123456789");
            m.setMessage("aaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbbcccccccccccccccccccccccccc");
            messageDataManager.newMessage(m);
        }*/
        setContentView(R.layout.activity_main_screen);
        ButterKnife.bind(this);

        progressBar.setVisibility(View.GONE);

        checkExternalStorage();

        permissionHandler.askForPermission(this, new String[] {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PermissionHandler.REQUEST_RECORD_AUDIO_AND_EXTERNAL_STORAGE_PERMISSION_CODE);

        setSupportActionBar(toolbar);

        initApplication();
        initMainMenuEventListeners();
        loadData();

        initOnClickListeners();

        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageAdapter(messageDataManager.getMessages(), this);
        messagesRecyclerView.setAdapter(adapter);

        messagesRecyclerView.setHasFixedSize(RECYCLER_VIEW_HAS_FIXED_SIZE);

        if (adapter.getItemCount() == 0) {
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (sharedPreferenceManager.getBoolean(SharedPreferenceManager.SYSTEM_NOTIFICATION_PREFERENCE_KEY)) {
            showMarshmallowError();
        }

        serverDataChangeHandler.addMessageReceivedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        serverDataChangeHandler.removeMessageReceivedListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ProfileActivity.REGISTRATE_USER_REQUEST && resultCode == ProfileActivity.SAVE_SUCCESS) {
            User user = data.getParcelableExtra(ProfileActivity.EXTRA_USER);
            user.setId(null);
            userDataManager.registrate(user);
            stopService(new Intent(this, MessageReceiverService.class));
            startService(new Intent(this, MessageReceiverService.class));
        } else if (requestCode == ProfileActivity.UPDATE_USER_REQUEST && resultCode == ProfileActivity.SAVE_SUCCESS) {
            if (data.getBooleanExtra(ProfileActivity.EXTRA_DELETE_USER, !ProfileActivity.DELETE_USER)) {
                deleteUser();
            } else {
                userDataManager.updateUser((User) data.getParcelableExtra(ProfileActivity.EXTRA_USER));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_screen_activity_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.mainScreenActivity_openDrawer:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ACTIVITY OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void initOnClickListeners() {
        messageSendingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showMessageSendingActivity();
            }
        });

        profilerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreenActivity.this, KnockGraphActivity.class));
            }
        });
    }

    /**
     * Initializes default preferences and files if needed.
     */

    private void initApplication() {
        if (sharedPreferenceManager.getInt(SharedPreferenceManager.SHORT_UNIT_TIME_PREFERENCE_KEY) ==
                SharedPreferenceManager.DEFAULT_INT) {
            sharedPreferenceManager.saveInt(SharedPreferenceManager.SHORT_UNIT_TIME_PREFERENCE_KEY,
                    getResources().getInteger(R.integer.pref_measure_time_default_value));
        }

        if (!codeDataManager.jsonFileExists(CodeDataManager.CODE_TABLE_NAME)) {
            codeDataManager.saveAllCodesJson(MorseCodeTable.getDefaultCodeTable(), CodeDataManager.CODE_TABLE_NAME);
        }
    }

    private void initMainMenuEventListeners() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //item.setChecked(MENU_ITEM_CHECKED);
                drawerLayout.closeDrawers();

                switch (item.getItemId()) {
                    case R.id.mainScreenActivity_settings:
                        openSettings();
                        break;
                    case R.id.mainScreenActivity_codes:
                        openCodesActivity();
                        break;
                    case R.id.mainScreenActivity_profile:
                        showProfileActivity();
                        break;
                    case R.id.mainScreenActivity_friends:
                        showFriendsActivity();
                        break;
                    case R.id.mainScreenActivity_exit:
                        exit();
                        break;
                    case R.id.mainScreenActivity_deleteMessages:
                        deleteAllMessages();
                }

                return true;
            }
        });
    }

    /**
     * This method loads the persisted data from files.
     */

    private void loadData() {
        if (sharedPreferenceManager.getBoolean(SharedPreferenceManager.MODE_PREFERENCE_KEY)) {
            codeDataManager.loadMorseCodes();
        } else {
            codeDataManager.loadHuffmanCodes();
        }

        userDataManager.loadUser();
    }

    private void deleteUser() {
        messageDataManager.deleteAllMessages();
        userDataManager.deleteAllFriends();
        userDataManager.deleteUser();
        adapter.dataSetChanged();
        adapter.notifyDataSetChanged();
    }

    /**
     * Opens the settings Activity.
     */

    private void openSettings() {
        startActivity(new Intent(this, MainSettingsActivity.class));
    }

    /**
     * Opens the code settings activity.
     */

    private void openCodesActivity() {
        startActivity(new Intent(this, CodesActivity.class));
    }

    /**
     * This method checks whether the external storage (SD card) is available or not and stops
     * the application with an error message if not.
     */

    private void checkExternalStorage() {
        if (!ExternalStorageValidator.storageIsOk()) {
            AlertDialogs alertDialogs = new AlertDialogs(this);
            final AlertDialog dialog = alertDialogs.createAlertDialog(getString(R.string.mainScreenActivity_storageError_title),
                    getString(R.string.mainScreenActivity_storageError_message));

            dialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.cancel();
                    finish();
                }
            }, getResources().getInteger(R.integer.mainScreenActivity_wait_if_storageError_time));
        }
    }

    private void showMessageSendingActivity() {
        showFriendsActivity();
    }

    private void showProfileActivity() {
        if (userDataManager.isLoggedIn()) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra(ProfileActivity.EXTRA_UPDATE_USER, ProfileActivity.UPDATE_USER);
            intent.putExtra(ProfileActivity.EXTRA_USER, userDataManager.getUser());
            startActivityForResult(intent, ProfileActivity.UPDATE_USER_REQUEST);
        } else {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra(ProfileActivity.EXTRA_REGISTRATE_USER, ProfileActivity.REGISTRATE_USER);
            startActivityForResult(intent, ProfileActivity.REGISTRATE_USER_REQUEST);
        }
    }

    private void showFriendsActivity() {
        if (!userDataManager.isLoggedIn()) {
            showErrorMessage(getString(R.string.mainScreenActivity_user_dataNotFound_error));
            return;
        }

        startActivity(new Intent(this, FriendsActivity.class));
    }

    private void exit() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(SYSTEM_HALT);
    }

    private void deleteAllMessages() {
        messageDataManager.deleteAllMessages();
        adapter.dataSetChanged();
        adapter.notifyDataSetChanged();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Friend getActualFriend() {
        return null;
    }

    @Override
    public boolean isFriend(String tel) {
        return userDataManager.isFriend(tel);
    }

    @Override
    public String getUserTel() {
        return userDataManager.getUser().getTelephone();
    }

    @Override
    public User getUser() { return userDataManager.getUser(); }

    @Override
    public void loading() {
        //progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dataLoaded() {
        //progressBar.setVisibility(View.GONE);
    }

    @Override
    public void noMessages() {
        Log.i("MAIN_SCREEN", "NO messages");
        imageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void messageReceived() {
        adapter.dataSetChanged();
        adapter.notifyDataSetChanged();
        imageView.setVisibility(View.INVISIBLE);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// METHODS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

}
