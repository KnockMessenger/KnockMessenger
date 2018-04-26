package hu.vadasz.peter.knockmessenger.DataPersister.Managers;

import java.util.ArrayList;
import java.util.List;

import hu.vadasz.peter.knockmessenger.DataPersister.Exceptions.JsonFileSavingException;
import hu.vadasz.peter.knockmessenger.DataPersister.JSON.JsonParser;
import hu.vadasz.peter.morsecodedecoder.Code.Code;
import lombok.Getter;

/**
 * This class provides cache and codes data from the local database.
 */

public class CodeDataManager {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    public static final String CODE_TABLE_NAME = "km_code_table";

    public static final int MORSE_CODE_TYPE = 1;
    public static final int HUFFMAN_CODE_TYPE = 2;

    public static final boolean SAVE_SUCCESS = true;

    /// CONSTANTS -- END

    private DataBaseManager dataBaseManager;
    private JsonParser jsonParser;

    @Getter
    private List<Code> allCodes;

    @Getter
    private List<Code> actualCodes;

    @Getter
    private int codeType;

    private int maxCodePosition;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// DATABASE OPERATIONS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public CodeDataManager(DataBaseManager dataBaseManager, JsonParser jsonParser) {
        this.dataBaseManager = dataBaseManager;
        this.jsonParser = jsonParser;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// DATABASE OPERATIONS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// JSON OPERATIONS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean saveAllCodesJson(List<Code> codes, String name) {
        try {
            jsonParser.encodeCodeList(codes, name);
        } catch (JsonFileSavingException e) {
            return !SAVE_SUCCESS;
        }

        return SAVE_SUCCESS;
    }

    /**
     * This method loads List of allCodes from a specified file from the app's internal storage.
     * @param name the file name
     * @return
     */

    public List<Code> getCodeListFromJson(String name) {
        return jsonParser.decodeCodeList(name);
    }

    /**
     * This method checks that the file in the parameter exists in the app's internal storage.
     * @param name the name of the file to check.
     * @return
     */

    public boolean jsonFileExists(String name) {
        return jsonParser.jsonFileExists(name);
    }

    /**
     * This methods loads the Morse allCodes from the stored json file to the cache.
     */

    public void loadMorseCodes() {
        this.allCodes = new ArrayList<>();
        codeType = MORSE_CODE_TYPE;
        loadCodes(getCodeListFromJson(CODE_TABLE_NAME), true);
    }

    /**
     * This methods loads the Huffman allCodes from the stored json file to the cache.
     */

    public void loadHuffmanCodes() {
        this.allCodes = new ArrayList<>();
        codeType = HUFFMAN_CODE_TYPE;
        loadCodes(getCodeListFromJson(CODE_TABLE_NAME), false);
    }

    /**
     * This method converts the hashMap of allCodes to list and loads the allCodes to the cache.
     * @param codes
     */

    private void loadCodes(List<Code> codes, boolean morse) {
        maxCodePosition = 0;
        this.allCodes = new ArrayList<>();
        this.actualCodes = new ArrayList<>();

        for (Code code : codes) {
            if (code.getId() > maxCodePosition) {
                maxCodePosition = code.getId();
            }

            this.allCodes.add(code);
            if (code.isMorse() == morse) {
                this.actualCodes.add(code);
            }
        }
        maxCodePosition++;
    }

    /**
     * This method saves the cached allCodes to the json file.
     */

    public void saveCodes() {
        saveAllCodesJson(allCodes, CODE_TABLE_NAME);
    }

    /**
     * By this method a new code can be added to the cache, the new code will be persisted.
     * @param code
     */

    public void addCode(Code code) {
        code.setId(maxCodePosition++);
        actualCodes.add(code);
        allCodes.add(code);
        saveCodes();
    }

    /**
     * By this method a code can be deleted, the changes will be persisted.
     * @param code the code to delete
     */

    public void deleteCode(Code code) {
        allCodes.remove(code);
        actualCodes.remove(code);
        saveCodes();
    }

    /**
     * By this method a code can be updated, the changes will be permitted.
     * @param code the code to  update.
     */

    public void updateCode(Code code) {
        for (Code c : actualCodes) {
            if (c.equals(code)) {
                c.setCode(code.getCode());
                c.setText(code.getText());
            }
        }
        saveCodes();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// JSON OPERATIONS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
