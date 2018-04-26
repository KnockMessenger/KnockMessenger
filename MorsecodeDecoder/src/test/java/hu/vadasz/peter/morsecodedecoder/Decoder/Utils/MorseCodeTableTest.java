package hu.vadasz.peter.morsecodedecoder.Decoder.Utils;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hu.vadasz.peter.morsecodedecoder.Code.Code;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by Peti on 2018. 03. 31..
 */
public class MorseCodeTableTest {

    public static final int MAX_TIME = 1000;
    public static final boolean OK = true;

    private MorseCodeTable codeTable;

    @Before
    public void init() {
        List<Code> codes = new ArrayList<>();
        codes.add(new Code(65,"1110001100", "X", !Code.MORSE_CODE));
        codes.add(new Code(66,"111000110000", "W", !Code.MORSE_CODE));
        codes.add(new Code(67,"111000110001", "Q", !Code.MORSE_CODE));
        codes.add(new Code(68,"010", "BackSpace", !Code.MORSE_CODE, Code.Type.BACK_SPACE_SYMBOL));
        codes.add(new Code(32,"1110002","Control mode", Code.MORSE_CODE, Code.Type.CHANGE_MODE_SYMBOL));
        codes.add(new Code(1,"012", "A"));
        codes.add(new Code(2,"10002", "B"));
        codes.add(new Code(3,"10102", "C"));
        codeTable = new MorseCodeTable(codes);
    }

    @Test(timeout = MAX_TIME)
    public void testFindSymbol_ShouldReturnCorrectMorseSymbol() {
        assertEquals("A", codeTable.findSymbol("012").getText());
    }

    @Test(timeout = MAX_TIME)
    public void testFindSymbol_SholdReturnCorrectHuffmanSymbol() {
        assertEquals("X", codeTable.findSymbol("1110001100").getText());
    }

    @Test(timeout = MAX_TIME)
    public void testFindSymbol_ShouldReturnChangeSymbol() {
        assertThat(codeTable.findSymbol("1110002").getType(), is(Code.Type.CHANGE_MODE_SYMBOL));
    }

    @Test(timeout = MAX_TIME)
    public void testFindSymbol_ShouldReturnNull() {
        assertEquals(null, codeTable.findSymbol("11134"));
    }

    @Test(timeout = MAX_TIME)
    public void testFindSymbol_ShouldNotReturnControlSymbol() {
        assertEquals(null, codeTable.findSymbol("010"));
    }

    @Test(timeout = MAX_TIME)
    public void testFindSymbol_WithoutChangeModeSymbol() {
        List<Code> codes = new ArrayList<>();
        codes.add(new Code(68,"010", "BackSpace", !Code.MORSE_CODE, Code.Type.BACK_SPACE_SYMBOL));
        MorseCodeTable table = new MorseCodeTable(codes);
        assertEquals(null, table.findSymbol("111000"));
    }

    @Test(timeout = MAX_TIME)
    public void testFindControlSymbol_ShouldReturnCorrectMorseControlSymbol() {
        assertThat(codeTable.findControlSymbol("1110002").getType(), is(Code.Type.CHANGE_MODE_SYMBOL));
    }

    @Test(timeout = MAX_TIME)
    public void testFindControlSymbol_ShouldReturnCorrectHuffmanControlSymbol() {
        assertThat(codeTable.findControlSymbol("010").getType(), is(Code.Type.BACK_SPACE_SYMBOL));
    }

    @Test(timeout = MAX_TIME)
    public void testFindControlSymbol_ShouldReturnNull() {
        assertEquals(null, codeTable.findSymbol("333111"));
    }

    @Test(timeout = MAX_TIME)
    public void testFindControlSymbol_ShouldNotReturnInputSymbol() {
        assertEquals(null, codeTable.findControlSymbol("012"));
    }

    @Test(timeout = MAX_TIME)
    public void testGetCodeList() {
        List<String> codes = new ArrayList<>();
        codes.add("1110001100");
        codes.add("111000110000");
        codes.add("111000110001");
        codes.add("012");
        codes.add("10002");
        codes.add("10102");
        codes.add("1110002");
        List<String> result = codeTable.getCodesList();
        Collections.sort(result);
        Collections.sort(codes);

        assertArrayEquals(codes.toArray(), result.toArray());
    }

    @Test(timeout = MAX_TIME)
    public void testGetCodeList_ShouldReturnEmptyList() {
        List<Code> codes = new ArrayList<>();
        codes.add(new Code(68,"010", "BackSpace", !Code.MORSE_CODE, Code.Type.BACK_SPACE_SYMBOL));
        codes.add(new Code(32,"1110002","Home", Code.MORSE_CODE, Code.Type.HOME_SYMBOL));
        MorseCodeTable table = new MorseCodeTable(codes);
        assertEquals(OK, table.getCodesList().isEmpty());
    }

    @Test(timeout = MAX_TIME)
    public void testGetControlCodesList() {
        List<String> codes = new ArrayList<>();
        codes.add("010");
        codes.add("1110002");
        assertEquals(codes, codeTable.getControlCodesList());
    }

    @Test(timeout = MAX_TIME)
    public void testGetControlCodeList_ShouldReturnEmptyList() {
        List<Code> codes = new ArrayList<>();
        codes.add(new Code(68,"010", "A", !Code.MORSE_CODE, Code.Type.INPUT_SYMBOL));
        codes.add(new Code(32,"1110002","B", Code.MORSE_CODE, Code.Type.INPUT_SYMBOL));
        MorseCodeTable table = new MorseCodeTable(codes);
        assertEquals(OK, table.getControlCodesList().isEmpty());
    }
}