package hu.vadasz.peter.knockmessenger.Dialogs;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import lombok.Getter;

/**
 * Abstract class for number picker dialogs.
 */

public abstract class AbstractNumberPickerDialogPreference extends DialogPreference {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    public static final int MIN_MEASURE_TIME = 100;
    public static final int DEFAULT_MEASURE_TIME = 500;
    public static final int MAX_MEASURE_TIME = 2000;

    public static final int MIN_MIC_SENSITIVITY = 1;
    public static final int DEFAULT_MIC_SENSITIVITY = 5;
    public static final int MAX_MIC_SENSITIVITY = 10;

    public static final boolean DEFAULT_WRAP_SELECTOR_WHEEL = true;

    /// CONSTANTS -- END

    protected int minValue;
    protected int maxValue;
    protected boolean wrapSelectorWheel;
    protected NumberPicker numberPicker;

    @Getter
    private int value;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public AbstractNumberPickerDialogPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AbstractNumberPickerDialogPreference(Context context, AttributeSet attributeSet, int style) {
        super(context, attributeSet, style);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// DialogPreference OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a view element for the number picker dialog with specified layout.
     * @return
     */

    @Override
    protected View onCreateDialogView() {
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        numberPicker = new NumberPicker(getContext());
        numberPicker.setLayoutParams(layoutParams);

        FrameLayout dialogView = new FrameLayout(getContext());
        dialogView.addView(numberPicker);

        return dialogView;
    }

    /**
     * Binds the dialog to the view element from the application's context.
     * @param view the view to bind to the dialog.
     */

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        numberPicker.setMinValue(minValue);
        numberPicker.setMaxValue(maxValue);
        numberPicker.setWrapSelectorWheel(wrapSelectorWheel);
        defaultValue();
    }

    /**
     * This method is called before the dialog is closed to persist the chosen value if needed.
     * @param positiveResult
     */

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            numberPicker.clearFocus();
            setValue(numberPicker.getValue());
        }
    }

    /**
     * Is called when the default value is needed.
     * @param attributes
     * @param index
     * @return
     */
    @Override
    protected Object onGetDefaultValue(TypedArray attributes, int index) {
        return attributes.getInt(index, minValue);
    }

    /**
     *
     * @param restorePersistedValue
     * @param defaultValue
     */

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setValue(restorePersistedValue ? getPersistedInt(minValue) : (Integer) defaultValue);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// DIALOG PREFERENCE OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Persists the value.
     * @param value the value to persist.
     */

    protected void setValue(int value) {
        this.value = value;
        persistInt(this.value);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Get the picker's default value.
     */

    protected abstract void defaultValue();

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ABSTRACT METHODS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}

