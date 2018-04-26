package hu.vadasz.peter.knockmessenger.Controllers.Validators;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import hu.vadasz.peter.morsecodedecoder.Code.Code;

import static org.junit.Assert.*;

/**
 * This class is to test CodeSettingsValidator util class.
 */

public class CodeSettingsValidatorTest {

    public static final int MAX_TIME = 1000;
    public static final boolean OK = true;

    private List<Code> codes;
    private List<Code> controlCodes;

    @Before
    public void init() {
        codes = new ArrayList<>();
        codes.add(new Code(1, "012", "A"));
        codes.add(new Code(2, "10002", "B"));
        codes.add(new Code(3, "10102", "C"));
        codes.add(new Code(4, "1002", "D"));
        codes.add(new Code(5, "02", "E"));

        codes.add(new Code(33, "010", "E", !Code.MORSE_CODE));
        codes.add(new Code(34, "000", "A", !Code.MORSE_CODE));
        codes.add(new Code(35, "1111", "T", !Code.MORSE_CODE));
        codes.add(new Code(36, "1000", "L", !Code.MORSE_CODE));

        controlCodes = new ArrayList<>();
        controlCodes.add(new Code(68, "0110", "BackSpace", !Code.MORSE_CODE, Code.Type.BACK_SPACE_SYMBOL));
        controlCodes.add(new Code(69, "111", "Home", !Code.MORSE_CODE, Code.Type.HOME_SYMBOL));
        controlCodes.add(new Code(70, "110", "End", !Code.MORSE_CODE, Code.Type.END_SYMBOL));
        controlCodes.add(new Code(28, "012","BackSpace", Code.MORSE_CODE, Code.Type.BACK_SPACE_SYMBOL));
    }

    @Test(timeout = MAX_TIME)
    public void testCodeExists_ShouldReturnTrue() {
        assertEquals(OK, CodeSettingsValidator.codeExists(codes, new Code(7, "012", "A")));
    }

    @Test(timeout = MAX_TIME)
    public void testCodeExists_ShouldReturnFalse() {
        assertEquals(!OK, CodeSettingsValidator.codeExists(codes, new Code(8, "1112", "O")));
    }

    @Test(timeout = MAX_TIME)
    public void testCodeExists_SameCodeWithSameIdShouldReturnFalse() {
        assertEquals(!OK, CodeSettingsValidator.codeExists(codes, new Code(1, "012", "A")));
    }

    @Test(timeout = MAX_TIME)
    public void testCodeExists_EmptyCodesShouldReturnFalse() {
        assertEquals(!OK, CodeSettingsValidator.codeExists(new ArrayList<Code>(), new Code(1, "012", "A")));
    }

    @Test(timeout = MAX_TIME)
    public void testMorseCodeIsValid_CodeEmptyShouldReturnFalse() {
        assertEquals(!OK, CodeSettingsValidator.morseCodeIsValid(new ArrayList<Code>(), new Code(1,"", "A")));
    }

    @Test(timeout = MAX_TIME)
    public void testMorseCodeIsValid_CodeIsTooShortShouldReturnFalse() {
        assertEquals(!OK, CodeSettingsValidator.morseCodeIsValid(new ArrayList<Code>(), new Code(2, "1", "A")));
    }

    @Test(timeout = MAX_TIME)
    public void testMorseCodeIsValid_CodeLengthIsValidShouldReturnTrue() {
        assertEquals(OK, CodeSettingsValidator.morseCodeIsValid(new ArrayList<Code>(), new Code(1, "12", "A")));
    }

    @Test(timeout = MAX_TIME)
    public void testMorseCodeIsValid_CodeExistsShouldReturnFalse() {
        assertEquals(!OK, CodeSettingsValidator.morseCodeIsValid(codes, new Code(8, "012", "A")));
    }

    @Test(timeout = MAX_TIME)
    public void testMorseCodeIsValid_CodeIsCorrectShouldReturnTrue() {
        assertEquals(OK, CodeSettingsValidator.morseCodeIsValid(codes, new Code(9,"1112", "O")));
    }

    @Test(timeout = MAX_TIME)
    public void testHuffmanCodeIsValid_CodeIsEmptyShouldReturnFalse() {
        assertEquals(!OK, CodeSettingsValidator.huffmanCodeIsValid(new ArrayList<Code>(), new Code(1, "", "A")));
    }

    @Test(timeout = MAX_TIME)
    public void testHuffmanCodeIsValid_CodeLengthIsOkShouldReturnTrue() {
        assertEquals(OK, CodeSettingsValidator.huffmanCodeIsValid(new ArrayList<Code>(), new Code(1, "1", "A")));
    }

    @Test(timeout = MAX_TIME)
    public void testHuffmanCodeIsValid_CodeIsNotPrefixShouldReturnFalse() {
        assertEquals(!OK, CodeSettingsValidator.huffmanCodeIsValid(codes, new Code(9, "0", "A", !Code.MORSE_CODE)));
    }

    @Test(timeout = MAX_TIME)
    public void testHuffmanCodeIsValid_CodeExistsShouldReturnFalse() {
        assertEquals(!OK, CodeSettingsValidator.huffmanCodeIsValid(codes, new Code(9, "010", "A", !Code.MORSE_CODE)));
    }

    @Test(timeout = MAX_TIME)
    public void testHuffmanCodeIsValid_CodeIsCorrectShouldReturnTrue() {
        assertEquals(OK, CodeSettingsValidator.huffmanCodeIsValid(codes, new Code(41, "11011", "M", !Code.MORSE_CODE)));
    }

    @Test(timeout = MAX_TIME)
    public void testCodeIsPrefix_ShouldReturnTrue() {
        assertEquals(OK, CodeSettingsValidator.codeIsPrefix(codes, new Code(41, "11011", "M", !Code.MORSE_CODE)));
    }

    @Test(timeout = MAX_TIME)
    public void testCodeIsPrefix_ShouldReturnFalse() {
        assertEquals(!OK, CodeSettingsValidator.codeIsPrefix(codes, new Code(1, "0", "A", !Code.MORSE_CODE)));
    }

    @Test(timeout = MAX_TIME)
    public void testCodeIsPrefix_CodeStartsWithAnotherShouldReturnFalse() {
        assertEquals(!OK, CodeSettingsValidator.codeIsPrefix(codes, new Code(1, "0101110", "A", !Code.MORSE_CODE)));
    }

    @Test(timeout = MAX_TIME)
    public void testCodeIsPrefix_CodeExistsShouldReturnFalse() {
        assertEquals(!OK, CodeSettingsValidator.codeIsPrefix(codes, new Code(1, "010", "E", !Code.MORSE_CODE)));
    }

    @Test(timeout = MAX_TIME)
    public void testCodeIsPrefix_EmptyListShouldReturnTrue() {
        assertEquals(OK, CodeSettingsValidator.codeIsPrefix(new ArrayList<Code>(), new Code(1, "010", "E", !Code.MORSE_CODE)));
    }

    @Test(timeout = MAX_TIME)
    public void testCodeIsValid_MorseCodeShouldReturnTrue() {
        assertEquals(OK, CodeSettingsValidator.codeIsValid(codes, new Code(1, "111000", "A")));
    }

    @Test(timeout = MAX_TIME)
    public void testCodeIsValid_MorseCodeShouldReturnFalse() {
        assertEquals(!OK, CodeSettingsValidator.codeIsValid(codes, new Code(1, "010", "XYZ")));
    }

    @Test(timeout = MAX_TIME)
    public void testCodeIsValid_HuffmanCodeShouldReturnTrue() {
        assertEquals(OK, CodeSettingsValidator.codeIsValid(codes, new Code(1, "111000", "A", !Code.MORSE_CODE)));
    }

    @Test(timeout = MAX_TIME)
    public void testCodeIsValid_HuffmanCodeShouldReturnFalse() {
        assertEquals(!OK, CodeSettingsValidator.codeIsValid(codes, new Code(1, "01", "XYZ", !Code.MORSE_CODE)));
    }

    @Test(timeout = MAX_TIME)
    public void testControlCodeIsValid_MorseControlCodeShouldReturnTrue() {
        assertEquals(OK, CodeSettingsValidator.controlCodeIsValid(controlCodes,
                new Code(1, "0112", "TEST", Code.MORSE_CODE, Code.Type.HOME_SYMBOL)));
    }

    @Test(timeout = MAX_TIME)
    public void testControlCodeIsValid_MorseControlCodeShouldReturnFalse() {
        assertEquals(!OK, CodeSettingsValidator.controlCodeIsValid(controlCodes,
                new Code(1, "012","TEST" ,Code.MORSE_CODE, Code.Type.HOME_SYMBOL)));
    }

    @Test(timeout = MAX_TIME)
    public void testControlCodeIsValid_HuffmanControlCodeShouldReturnTrue() {
        assertEquals(OK, CodeSettingsValidator.controlCodeIsValid(controlCodes,
                new Code(1,"1010111111", "TEST", !Code.MORSE_CODE, Code.Type.END_SYMBOL)));
    }

    @Test(timeout = MAX_TIME)
    public void testControlCodeIsValid_HuffmanControlCodeShouldReturnFalse() {
        assertEquals(!OK, CodeSettingsValidator.controlCodeIsValid(controlCodes,
                new Code(1, "11001111", "TEST", !Code.MORSE_CODE, Code.Type.BACK_SPACE_SYMBOL)));
    }

    @Test(timeout = MAX_TIME)
    public void testControlCodeIsValid_EmptyListShouldReturnTrue() {
        assertEquals(OK, CodeSettingsValidator.controlCodeIsValid(new ArrayList<Code>(),
                new Code(1, "111", "TEST", !Code.MORSE_CODE, Code.Type.HOME_SYMBOL)));
    }
}