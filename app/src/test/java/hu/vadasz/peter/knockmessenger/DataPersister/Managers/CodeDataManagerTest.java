package hu.vadasz.peter.knockmessenger.DataPersister.Managers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import hu.vadasz.peter.knockmessenger.DataPersister.JSON.JsonParser;
import hu.vadasz.peter.morsecodedecoder.Code.Code;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CodeDataManagerTest {

    public static final boolean OK = true;
    public static final int MAX_TIME = 1000;

    @Mock
    private DataBaseManager dataBaseManager;

    @Mock
    private JsonParser jsonParser;

    private CodeDataManager codeDataManager;

    @Test(timeout = MAX_TIME)
    public void testSaveAllCodesJson_ShouldReturnSaveSuccess() {
        codeDataManager = new CodeDataManager(dataBaseManager, jsonParser);
        assertEquals(OK, codeDataManager.saveAllCodesJson(null, null));
    }

    @Test(timeout = MAX_TIME)
    public void testGetCodeListsFromJson_ShouldReturnCorrectList() {
        List<Code> testList = new ArrayList<>();
        testList.add(new Code(1,"", ""));
        when(jsonParser.decodeCodeList("")).thenReturn(testList);
        codeDataManager = new CodeDataManager(dataBaseManager, jsonParser);
        assertThat(codeDataManager.getCodeListFromJson(""), is(testList));
    }

    @Test(timeout = MAX_TIME)
    public void testJsonFileExists_ShouldReturnTrue() {
        when(jsonParser.jsonFileExists("")).thenReturn(true);
        codeDataManager = new CodeDataManager(dataBaseManager, jsonParser);
        assertEquals(OK, codeDataManager.jsonFileExists(""));
    }

    @Test(timeout = MAX_TIME)
    public void testJsonFileExists_ShouldReturnFalse() {
        when(jsonParser.jsonFileExists("")).thenReturn(false);
        codeDataManager = new CodeDataManager(dataBaseManager, jsonParser);
        assertEquals(!OK, codeDataManager.jsonFileExists(""));
    }

    @Test(timeout = MAX_TIME)
    public void testLoadMorseCodes_ShouldReturnCorrectCodeList() {
        List<Code> codes = new ArrayList<>();
        codes.add(new Code(28, "012","BackSpace", Code.MORSE_CODE, Code.Type.BACK_SPACE_SYMBOL));
        codes.add(new Code(30,"002","Home", Code.MORSE_CODE, Code.Type.HOME_SYMBOL));
        codes.add(new Code(31, "112","End", Code.MORSE_CODE, Code.Type.END_SYMBOL));
        codes.add(new Code(32, "1110002","Control mode", Code.MORSE_CODE, Code.Type.CHANGE_MODE_SYMBOL));
        codes.add(new Code(33, "010", "E", !Code.MORSE_CODE));
        codes.add(new Code(34, "000", "A", !Code.MORSE_CODE));
        when(jsonParser.decodeCodeList(CodeDataManager.CODE_TABLE_NAME)).thenReturn(codes);
        codeDataManager = new CodeDataManager(dataBaseManager, jsonParser);
        codeDataManager.loadMorseCodes();
        boolean allIsMorse = true;
        for (Code code : codeDataManager.getActualCodes()) {
            allIsMorse = allIsMorse && code.isMorse();
        }
        assertEquals(OK, allIsMorse && codeDataManager.getActualCodes().size() == 4);
    }

    @Test(timeout = MAX_TIME)
    public void testLoadHuffmanCodes_ShouldReturnCorrectCodeList() {
        List<Code> codes = new ArrayList<>();
        codes.add(new Code(28, "012","BackSpace", Code.MORSE_CODE, Code.Type.BACK_SPACE_SYMBOL));
        codes.add(new Code(30,"002","Home", Code.MORSE_CODE, Code.Type.HOME_SYMBOL));
        codes.add(new Code(31, "112","End", Code.MORSE_CODE, Code.Type.END_SYMBOL));
        codes.add(new Code(32, "1110002","Control mode", Code.MORSE_CODE, Code.Type.CHANGE_MODE_SYMBOL));
        codes.add(new Code(33, "010", "E", !Code.MORSE_CODE));
        codes.add(new Code(34, "000", "A", !Code.MORSE_CODE));
        when(jsonParser.decodeCodeList(CodeDataManager.CODE_TABLE_NAME)).thenReturn(codes);
        codeDataManager = new CodeDataManager(dataBaseManager, jsonParser);
        codeDataManager.loadHuffmanCodes();
        boolean allIsHuffman = true;
        for (Code code : codeDataManager.getActualCodes()) {
            allIsHuffman = allIsHuffman && !code.isMorse();
        }
        assertEquals(OK, allIsHuffman && codeDataManager.getActualCodes().size() == 2);
    }

    @Test(timeout = MAX_TIME)
    public void testAddCode_ShouldCacheCode() {
        when(jsonParser.decodeCodeList(CodeDataManager.CODE_TABLE_NAME)).thenReturn(new ArrayList<Code>());
        codeDataManager = new CodeDataManager(dataBaseManager, jsonParser);
        codeDataManager.loadMorseCodes();
        codeDataManager.addCode(new Code(1, "012", "A"));
        assertEquals(OK, codeDataManager.getActualCodes().size() == 1 && codeDataManager.getActualCodes().get(0).getNumericCode().equals("012"));
    }

    @Test(timeout = MAX_TIME)
    public void testDeleteCode_ShouldCacheCode() {
        when(jsonParser.decodeCodeList(CodeDataManager.CODE_TABLE_NAME)).thenReturn(new ArrayList<Code>());
        codeDataManager = new CodeDataManager(dataBaseManager, jsonParser);
        codeDataManager.loadMorseCodes();
        codeDataManager.addCode(new Code(1, "012", "A"));
        codeDataManager.deleteCode(new Code(1, "012", "A"));
        assertEquals(OK, codeDataManager.getActualCodes().isEmpty());
    }

    @Test(timeout = MAX_TIME)
    public void testUpdateCode_ShouldUpdateCode() {
        when(jsonParser.decodeCodeList(CodeDataManager.CODE_TABLE_NAME)).thenReturn(new ArrayList<Code>());
        codeDataManager = new CodeDataManager(dataBaseManager, jsonParser);
        codeDataManager.loadMorseCodes();
        codeDataManager.addCode(new Code(1, "012", "A"));
        codeDataManager.updateCode(new Code(1,"012", "B"));
        assertEquals(OK, codeDataManager.getActualCodes().size() == 1 && codeDataManager.getActualCodes().get(0).getText().equals("B"));
    }
}