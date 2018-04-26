package hu.vadasz.peter.knockmessenger.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.vadasz.peter.knockmessenger.Controllers.Exceptions.DeviceIsOfflineException;
import hu.vadasz.peter.knockmessenger.Controllers.Exceptions.InvalidUserException;
import hu.vadasz.peter.knockmessenger.Controllers.UserController;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.User;
import hu.vadasz.peter.knockmessenger.DataPersister.Server.TimeoutHandler;
import hu.vadasz.peter.knockmessenger.R;
import hu.vadasz.peter.knockmessenger.databinding.ActivityProfileBinding;

public class ProfileActivity extends BaseActivity implements ValueEventListener,
        TimeoutHandler.TimeoutListener {

    public static final int REGISTRATE_USER_REQUEST = 1;
    public static final int UPDATE_USER_REQUEST = 2;
    public static final int SAVE_SUCCESS = 200;

    public static final Long CREATE_ID = 0L;

    public static final String EXTRA_REGISTRATE_USER = "EXTRA_REGISTRATE_USER";
    public static final String EXTRA_UPDATE_USER = "EXTRA_UPDATE_USER";
    public static final String EXTRA_USER = "EXTRA_USER";
    public static final String EXTRA_DELETE_USER = "EXTRA_DELETE_USER";

    public static final boolean UPDATE_USER = true;
    public static final boolean REGISTRATE_USER = true;
    public static final boolean DELETE_USER = true;
    public static final boolean VIEW_ITEM_ENABLED = true;
    public static final boolean SAVING = true;

    public static final String EMPTY_TEXT = "";

    @BindView(R.id.profileActivity_nameText)
    EditText name;

    @BindView(R.id.profileActivity_telText)
    EditText tel;

    @BindView(R.id.profileActivity_progressBar)
    ProgressBar saveProgress;

    private User user;
    private ActivityProfileBinding binding;
    private boolean create;
    private boolean delete;

    private UserController userController;

    private TimeoutHandler timeoutHandler;

    private boolean saving;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        ButterKnife.bind(this);

        saveProgress.setVisibility(View.GONE);

        initChangeListeners();

        userController = new UserController(userDataManager, this);

        Intent intent = getIntent();
        create = intent.getBooleanExtra(EXTRA_REGISTRATE_USER, !REGISTRATE_USER);

        if (create) {
            user = new User();
            user.setId(CREATE_ID);
            user.setName(EMPTY_TEXT);
            user.setTelephone(EMPTY_TEXT);
        } else {
            user = intent.getParcelableExtra(EXTRA_USER);
            tel.setEnabled(!VIEW_ITEM_ENABLED);
        }

        binding.setUser(user);
        setCursors();

        timeoutHandler = new TimeoutHandler(TimeoutHandler.DEFAULT_MEDIUM_TIMEOUT, this);

    }

    @Override
    public void onPause() {
        super.onPause();

        timeoutHandler.stop();
        saving = !SAVING;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.profileActivity_save:
                if (!saving) {
                    saveChanges();
                }
                break;
            case R.id.profileActivity_delete:
                if(!saving) {
                    requestDelete();
                }
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void initChangeListeners() {
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user.setName(name.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        tel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user.setTelephone(tel.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void saveChanges() {
        saveProgress.setVisibility(View.VISIBLE);
        try {
            userController.trySaveUser(user, this);
            saving = SAVING;
            timeoutHandler.start();
        } catch (DeviceIsOfflineException | InvalidUserException e) {
            saveProgress.setVisibility(View.GONE);
            showErrorMessage(e.getMessage());
            timeoutHandler.stop();
        }

    }

    private void saveData() {
        Intent intent = new Intent(this, MainScreenActivity.class);
        intent.putExtra(EXTRA_USER, user);
        setResult(SAVE_SUCCESS, intent);
        finish();
    }

    private void closeAndDelete() {
        Intent intent = new Intent(ProfileActivity.this, MainScreenActivity.class);
        intent.putExtra(EXTRA_USER, user);
        intent.putExtra(EXTRA_DELETE_USER, DELETE_USER);
        setResult(SAVE_SUCCESS, intent);
        finish();
    }

    private void deleteConfirmed() {
        if(saving) {
            return;
        }

        saveProgress.setVisibility(View.VISIBLE);
        try {
            delete = DELETE_USER;
            userController.trySaveUser(user, ProfileActivity.this);
            saving = SAVING;
            timeoutHandler.start();
        } catch (InvalidUserException | DeviceIsOfflineException e) {
            saveProgress.setVisibility(View.GONE);
            showErrorMessage(e.getMessage());
        }
    }

    private void requestDelete() {

        if (create) {
            showErrorMessage(getString(R.string.profileActivity_invalid_delete_error));
            return;
        }
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.codesActivity_confirm_delete_text), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.codesActivity_confirm_delete_yes_text), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      deleteConfirmed();
                    }
                })
                .setActionTextColor(getColor(android.R.color.holo_red_light))
                .show();
    }

    /**
     * This method sets th editText type field's cursors to the end of the text.
     */

    private void setCursors() {
        name.setSelection(name.getText().length());
        tel.setSelection(tel.getText().length());
        name.append(EMPTY_TEXT);
    }

    @Override
    public void onDataChange(final DataSnapshot dataSnapshot) {
        saveProgress.setVisibility(View.GONE);
        saving = !SAVING;
        timeoutHandler.stop();

        if (create && dataSnapshot.exists()) {
            showErrorMessage(getString(R.string.profileActivity_userExistsError));
        } else if (!delete && !create && !dataSnapshot.exists()){
            showErrorMessage(getString(R.string.profileActivity_userNotExistsError));
        } else if (delete){
            if (dataSnapshot.exists()) {
                closeAndDelete();
            } else {
                showErrorMessage(getString(R.string.profileActivity_userNotExistsError));
            }
        } else {
            saveData();
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        saveProgress.setVisibility(View.GONE);
        timeoutHandler.stop();
        saving = !SAVING;

        if (databaseError.getCode() == DatabaseError.DISCONNECTED) {
            showErrorMessage(getString(R.string.profileActivity_disconnectedError));
        }
    }

    @Override
    public void timeout() {
        saveProgress.setVisibility(View.GONE);
        saving = !SAVING;

        showErrorMessage(getString(R.string.connection_timeout_error));
    }
}
