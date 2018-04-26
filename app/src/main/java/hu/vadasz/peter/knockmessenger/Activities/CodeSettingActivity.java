package hu.vadasz.peter.knockmessenger.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.vadasz.peter.knockmessenger.Controllers.Validators.CodeSettingsValidator;
import hu.vadasz.peter.knockmessenger.Managers.SharedPreferenceManager;
import hu.vadasz.peter.knockmessenger.R;
import hu.vadasz.peter.knockmessenger.databinding.ActivityCodeSettingBinding;
import hu.vadasz.peter.morsecodedecoder.Code.Code;

/**
 * This activity controls the code settings.
 */

public class CodeSettingActivity extends BaseActivity {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    public static final int CODE_UPDATE_REQUEST = 1;
    public static final int CODE_CREATE_REQUEST = 2;
    public static final int CODE_UPDATE_SUCCESS = 3;
    public static final int CODE_CREATE_SUCCESS = 4;

    public static final String EXTRA_CODE = "EXTRA_CODE";
    public static final String EXTRA_IS_CREATE_CODE = "EXTRA_IS_CREATE_CODE";
    public static final String EXTRA_IS_UPDATE_CODE = "EXTRA_IS_UPDATE_CODE";

    public static final boolean ENABLED = true;

    /// CONSTANTS -- END

    @BindView(R.id.codeSettings_longSyllable_button)
    ImageButton longSyllableButton;

    @BindView(R.id.codeSettings_shortSyllable_button)
    ImageButton shortSyllableButton;

    @BindView(R.id.codeSettings_deleteSyllable_button)
    ImageButton deleteSyllableButton;

    @BindView(R.id.codeSettings_codeText)
    TextView codeText;

    @BindView(R.id.codeSettings_text)
    EditText text;

    private Code code;

    private ActivityCodeSettingBinding binding;
    private boolean create;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ACTIVITY OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_setting);

        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_code_setting);

        ButterKnife.bind(this);

        initOnClickListeners();
        initChangeListeners();

        Intent intent = getIntent();
        create = intent.getBooleanExtra(EXTRA_IS_CREATE_CODE, false);

        if (!create) {
            code = intent.getParcelableExtra(EXTRA_CODE);
        } else {
            code = new Code();
            code.setType(Code.Type.INPUT_SYMBOL);
            code.setMorse(sharedPreferenceManager.getBoolean(SharedPreferenceManager.MODE_PREFERENCE_KEY));
        }

        binding.setCode(code);
        setCursors();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.code_settings_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.codeSettingsActivity_save:
                saveCode();
                break;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ACTIVITY OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method initializes the onClick listeners of the view items.
     */

    private void initOnClickListeners() {
        shortSyllableButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                code.setCode(code.getCode() + "*");
                binding.setCode(code);
            }
        });

        longSyllableButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                code.setCode(code.getCode() + "-");
                binding.setCode(code);
            }
        });

        deleteSyllableButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                code.deleteCode();
                binding.setCode(code);
            }
        });
    }

    /**
     * This method initializes the changeListener of the view items.
     */

    private void initChangeListeners() {
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                code.setText(text.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    /**
     * This method is responsible for code persisting and activity finishing.
     */

    private void saveCode() {
        if (code.isInputSymbol() && !CodeSettingsValidator.codeIsValid(codeDataManager.getActualCodes(), code)) {
            showErrorMessage(getString(R.string.codeSettingsActivity_code_invalid_error));
            return;
        } else if (!code.isInputSymbol() && !CodeSettingsValidator.controlCodeIsValid(codeDataManager.getActualCodes(), code)) {
            showErrorMessage(getString(R.string.codeSettingsActivity_code_invalid_error));
            return;
        }

        if (code.getType() == Code.Type.CHANGE_MODE_SYMBOL && !CodeSettingsValidator.codeIsValid(codeDataManager.getActualCodes(), code)) {
            showErrorMessage(getString(R.string.codeSettingsActivity_code_invalid_error));
            return;
        }

        Intent intent = new Intent(this, CodesActivity.class);
        intent.putExtra(EXTRA_CODE, code);
        if (create) {
            setResult(CODE_CREATE_SUCCESS, intent);
        } else {
            setResult(CODE_UPDATE_SUCCESS, intent);
        }
        finish();
    }

    /**
     * This method sets th editText type field's cursors to the end of the text.
     */

    private void setCursors() {
        text.setSelection(text.getText().length());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
