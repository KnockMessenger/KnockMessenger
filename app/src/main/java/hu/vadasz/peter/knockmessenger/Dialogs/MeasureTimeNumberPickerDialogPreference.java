package hu.vadasz.peter.knockmessenger.Dialogs;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import hu.vadasz.peter.knockmessenger.R;

/**
 * This class provides a number picker dialog to specify the length of the knock measuring.
 */

public class MeasureTimeNumberPickerDialogPreference extends AbstractNumberPickerDialogPreference {

    public MeasureTimeNumberPickerDialogPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, android.R.attr.dialogPreferenceStyle);
    }

    public MeasureTimeNumberPickerDialogPreference(Context context, AttributeSet attributeSet, int style) {
        super(context, attributeSet, style);
        setPositiveButtonText(R.string.pref_number_picker_ok);
        setNegativeButtonText(R.string.pref_number_picker_cancel);

        TypedArray attributes = context.obtainStyledAttributes(attributeSet, R.styleable.AbstractNumberPickerDialogPreference);
        minValue = attributes.getInteger(R.styleable.AbstractNumberPickerDialogPreference_minValue, MIN_MEASURE_TIME);
        maxValue = attributes.getInteger(R.styleable.AbstractNumberPickerDialogPreference_maxValue, MAX_MEASURE_TIME);
        wrapSelectorWheel = attributes.getBoolean(R.styleable.AbstractNumberPickerDialogPreference_wrapSelectorWheel, DEFAULT_WRAP_SELECTOR_WHEEL);
        attributes.recycle();
    }

    @Override
    protected void defaultValue() {
        numberPicker.setValue(getPersistedInt(DEFAULT_MEASURE_TIME));
    }
}
