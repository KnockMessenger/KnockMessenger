package hu.vadasz.peter.morsecodedecoder.Code;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * This class is to test Code class.
 */

public class CodeTest {

    public static final int MAX_TIME = 1000;
    public static final String TEST_TEXT = "TEST_TEXT";
    public static final boolean OK = true;

    @Test(timeout = MAX_TIME)
    public void testGetCode_MorseShouldReturnShortSyllable() {
        Code code = new Code(1, "02",TEST_TEXT);
        assertEquals("*", code.getCode());
    }

    @Test(timeout = MAX_TIME)
    public void testGetCode_MorseShouldReturnLongSyllable() {
        Code code = new Code(1, "12",TEST_TEXT);
        assertEquals("-", code.getCode());
    }

    @Test(timeout = MAX_TIME)
    public void testGetCode_MorseShouldReturnMixedSyllables() {
        Code code = new Code(1, "100112", TEST_TEXT);
        assertEquals("-**--", code.getCode());
    }

    @Test(timeout = MAX_TIME)
    public void testGetCode_MorseShouldReturnEmptyString() {
        Code code = new Code(1, "2", TEST_TEXT);
        assertEquals(OK, code.getCode().isEmpty());
    }

    @Test(timeout = MAX_TIME)
    public void testGetCode_HuffmanShouldReturnShortSyllable() {
        Code code = new Code(1, "0", TEST_TEXT, !Code.MORSE_CODE);
        assertEquals("*", code.getCode());
    }

    @Test(timeout = MAX_TIME)
    public void testGetCode_HuffmanShouldReturnLongSyllable() {
        Code code = new Code(1, "1", TEST_TEXT, !Code.MORSE_CODE);
        assertEquals("-", code.getCode());
    }

    @Test(timeout = MAX_TIME)
    public void testGetCode_HuffmanShouldReturnMixedSyllables() {
        Code code = new Code(1,"111000", TEST_TEXT, !Code.MORSE_CODE);
        assertEquals("---***", code.getCode());
    }

    @Test(timeout = MAX_TIME)
    public void testSetCode_MorseCodeShouldBeGapSymbol() {
        Code code = new Code(1, "1", TEST_TEXT);
        code.setCode("");
        assertEquals("2", code.getNumericCode());
    }

    @Test(timeout = MAX_TIME)
    public void testSetCode_MorseCodeShouldBeShortSyllable() {
        Code code = new Code(1, "", TEST_TEXT);
        code.setCode("*");
        assertEquals("02", code.getNumericCode());
    }

    @Test(timeout = MAX_TIME)
    public void testSetCode_MorseCodeShouldBeLongSyllable() {
        Code code = new Code(1, "", TEST_TEXT);
        code.setCode("-");
        assertEquals("12", code.getNumericCode());
    }

    @Test(timeout = MAX_TIME)
    public void testSetCode_MorseCodeShouldBeMixedSyllables() {
        Code code = new Code(1, "", TEST_TEXT);
        code.setCode("**--**");
        assertEquals("0011002", code.getNumericCode());
    }

    @Test(timeout = MAX_TIME)
    public void testSetCode_HuffmanCodeShouldBeEmpty() {
        Code code = new Code(1, "11", TEST_TEXT, !Code.MORSE_CODE);
        code.setCode("...");
        assertEquals(OK, code.getNumericCode().isEmpty());
    }

    @Test(timeout = MAX_TIME)
    public void testSetCode_HuffmanCodeShouldBeShortSyllable() {
        Code code = new Code(1, "", TEST_TEXT, !Code.MORSE_CODE);
        code.setCode("*");
        assertEquals("0", code.getNumericCode());
    }

    @Test(timeout = MAX_TIME)
    public void testSetCode_HuffmanCodeShouldBeLongSyllable() {
        Code code = new Code(1, "", TEST_TEXT, !Code.MORSE_CODE);
        code.setCode("-");
        assertEquals("1", code.getNumericCode());
    }

    @Test(timeout = MAX_TIME)
    public void testSetCode_HuffmanCodeShouldBeMixed() {
        Code code = new Code(1, "", TEST_TEXT, !Code.MORSE_CODE);
        code.setCode("**--**");
        assertEquals("001100", code.getNumericCode());
    }

    @Test(timeout = MAX_TIME)
    public void testDelete_MorseCodeShouldDeleteLastCharacter() {
        Code code = new Code(1, "012", TEST_TEXT);
        code.deleteCode();
        assertEquals("02", code.getNumericCode());
    }

    @Test(timeout = MAX_TIME)
    public void testDelete_MorseCodeShouldNotDeleteGap() {
        Code code = new Code(1, "2", TEST_TEXT);
        code.deleteCode();
        assertEquals("2", code.getNumericCode());
    }

    @Test(timeout = MAX_TIME)
    public void testDelete_HuffmanCodeShouldDeleteLastCharacter() {
        Code code = new Code(1, "011", TEST_TEXT, !Code.MORSE_CODE);
        code.deleteCode();
        assertEquals("01", code.getNumericCode());
    }

    @Test(timeout = MAX_TIME)
    public void testDelete_HuffmanCodeShouldBeEmpty() {
        Code code = new Code(1, "0", TEST_TEXT, !Code.MORSE_CODE);
        code.deleteCode();
        assertEquals(OK, code.getNumericCode().isEmpty());
    }

    @Test(timeout = MAX_TIME)
    public void testDelete_HuffmanCodeShouldNotDeleteNothing() {
        Code code = new Code(1, "", TEST_TEXT, !Code.MORSE_CODE);
        code.deleteCode();
        assertEquals(OK, code.getNumericCode().isEmpty());
    }

    @Test(timeout = MAX_TIME)
    public void testToString() {
        Code code = new Code(1, "111", TEST_TEXT);
        assertEquals("(111 : " + TEST_TEXT + " - " + Code.Type.INPUT_SYMBOL.toString() + ")", code.toString());
    }

    @Test(timeout = MAX_TIME)
    public void testEquals_SameObjectsShouldReturnTrue() {
        Code code = new Code(1, "111", TEST_TEXT);
        assertEquals(OK, code.equals(code));
    }

    @Test(timeout = MAX_TIME)
    public void testEquals_DifferentObjectTypesShouldReturnFalse() {
        Code code = new Code(1, "111", TEST_TEXT);
        assertEquals(!OK, code.equals(new Integer(1)));
    }

    @Test(timeout = MAX_TIME)
    public void testEquals_DifferentCodesShouldReturnFalse() {
        Code code = new Code(1, "111", TEST_TEXT);
        assertEquals(!OK, code.equals(new Code(2, "111", TEST_TEXT)));
    }

    @Test(timeout = MAX_TIME)
    public void testEquals_ShouldReturnTrue() {
        Code code = new Code(1, "111", TEST_TEXT);
        assertEquals(OK, code.equals(new Code(1, "000", TEST_TEXT)));
    }
}