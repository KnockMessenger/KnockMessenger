package hu.vadasz.peter.knockmessenger.Controllers.Validators;

import java.util.ArrayList;
import java.util.List;

import hu.vadasz.peter.morsecodedecoder.Code.Code;


/**
 * This class validates the codes before persisting them.
 */

public class CodeSettingsValidator {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    public static final boolean CODE_EXISTS = true;
    public static final boolean CODE_IS_VALID = true;
    public static final boolean CODE_IS_PREFIX = true;

    /// CONSTANTS -- END

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// VALIDATOR METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method checks if the new code already exists or not.
     * @param codes the persisted codes.
     * @param code the new code.
     * @return
     */

    public static boolean codeExists(List<Code> codes, Code code) {
        for (Code c : codes) {
            if (!c.equals(code) && c.getNumericCode().equals(code.getNumericCode())) {
                return CODE_EXISTS;
            }
        }

        return !CODE_EXISTS;
    }

    public static boolean morseCodeIsValid(List<Code> codes, Code code) {
        if (code.getNumericCode().length() < Code.MORSE_CODE_LENGTH_LOWER_LIMIT ||
                code.getText().isEmpty()) {
            return !CODE_IS_VALID;
        }

        return !codeExists(codes, code);
    }

    public static boolean huffmanCodeIsValid(List<Code> codes, Code code) {
        if (code.getNumericCode().length() < Code.HUFFMAN_CODE_LENGTH_LOWER_LIMIT ||
                code.getText().isEmpty()) {
            return !CODE_IS_VALID;
        }

        return codeIsPrefix(codes, code) && !codeExists(codes, code);
    }

    public static boolean controlCodeIsValid(List<Code> codes, Code code) {
        List<Code> controls = new ArrayList<>();
        for (Code c : codes) {
            if (!c.isInputSymbol()) {
                controls.add(c);
            }
        }

        if (code.isMorse()) {
            return morseCodeIsValid(controls, code);
        } else {
            return huffmanCodeIsValid(controls, code);
        }
    }

    public static boolean codeIsValid(List<Code> codes, Code code) {
        List<Code> inputs = new ArrayList<>();
        for (Code c : codes) {
            if (c.isInputSymbol()) {
                inputs.add(c);
            }
        }

        if (code.isMorse()) {
            return morseCodeIsValid(inputs, code);
        } else {
            return huffmanCodeIsValid(inputs, code);
        }
    }

    public static boolean codeIsPrefix(List<Code> codes, Code code) {
        if (!code.isMorse()) {
            for (Code c : codes) {
                if (!c.isMorse() && !c.equals(code) && (c.getNumericCode().startsWith(code.getNumericCode()) || code.getNumericCode().startsWith(c.getNumericCode()))) {
                    return !CODE_IS_PREFIX;
                }
            }
        }

        return CODE_IS_PREFIX;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// VALIDATOR METHODS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
