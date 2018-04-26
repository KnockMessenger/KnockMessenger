package hu.vadasz.peter.morsecodedecoder.Decoder.Utils;

import java.util.ArrayList;
import java.util.List;

import hu.vadasz.peter.morsecodedecoder.Code.Code;

/**
 * This class provides codeTable for Morse code abc,  Huffman code abc and control symbols. Currently
 * this class provides international Morse-codes, and Huffman-code for Hungarian abc.
 */

public class MorseCodeTable {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private List<Code> codeTable;

    private static final List<Code> defaultCodeTable = new ArrayList<>();

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public MorseCodeTable() {
        initDefaultCodeTable();
        codeTable = defaultCodeTable;
    }

    public MorseCodeTable(List<Code> codeTable) {
        this.codeTable = new ArrayList<>(codeTable);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method fills the default code table with the default Huffman-codes and Morse-Codes.
     */

    public static void initDefaultCodeTable() {
        defaultCodeTable.clear();
        defaultCodeTable.add(new Code(1, "012", "A"));
        defaultCodeTable.add(new Code(2, "10002", "B"));
        defaultCodeTable.add(new Code(3, "10102", "C"));
        defaultCodeTable.add(new Code(4, "1002", "D"));
        defaultCodeTable.add(new Code(5, "02", "E"));
        defaultCodeTable.add(new Code(6,"00102", "F"));
        defaultCodeTable.add(new Code(7, "1102", "G"));
        defaultCodeTable.add(new Code(8, "00002", "H"));
        defaultCodeTable.add(new Code(9, "002", "I"));
        defaultCodeTable.add(new Code(10, "01112", "J"));
        defaultCodeTable.add(new Code(11, "1012", "K"));
        defaultCodeTable.add(new Code(12, "01002", "L"));
        defaultCodeTable.add(new Code(13, "112", "M"));
        defaultCodeTable.add(new Code(14, "102", "N"));
        defaultCodeTable.add(new Code(15, "1112", "O"));
        defaultCodeTable.add(new Code(16, "01102", "P"));
        defaultCodeTable.add(new Code(17, "11012", "Q"));
        defaultCodeTable.add(new Code(18, "0102", "R"));
        defaultCodeTable.add(new Code(19, "0002", "S"));
        defaultCodeTable.add(new Code(20, "1112", "O"));
        defaultCodeTable.add(new Code(21, "12", "T"));
        defaultCodeTable.add(new Code(22, "0012", "U"));
        defaultCodeTable.add(new Code(23, "00012", "V"));
        defaultCodeTable.add(new Code(24, "0112", "W"));
        defaultCodeTable.add(new Code(25, "10012", "X"));
        defaultCodeTable.add(new Code(26, "10112", "Y"));
        defaultCodeTable.add(new Code(27, "11002", "Z"));
        defaultCodeTable.add(new Code(28, "012","BackSpace", Code.MORSE_CODE, Code.Type.BACK_SPACE_SYMBOL));
        defaultCodeTable.add(new Code(30,"002","Home", Code.MORSE_CODE, Code.Type.HOME_SYMBOL));
        defaultCodeTable.add(new Code(31, "112","End", Code.MORSE_CODE, Code.Type.END_SYMBOL));
        defaultCodeTable.add(new Code(32, "1110002","Control mode", Code.MORSE_CODE, Code.Type.CHANGE_MODE_SYMBOL));

        /// Huffman-codes

        defaultCodeTable.add(new Code(33, "010", "E", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(34, "000", "A", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(35, "1111", "T", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(36, "1000", "L", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(37, "0111", "N", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(38, "0110", "MORSE_CODE", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(39, "0011", "K", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(40, "11101", "O", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(41, "11011", "M", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(42, "11010", "Z", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(43, "11001", "R", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(44, "10111", "I", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(45, "10110", "G", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(46, "10100", "Á", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(47, "10011", "É", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(48, "00101", "Y", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(49, "111001", "D", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(50, "110001" ,"B", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(51, "110000", "V", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(52, "101010" ,"H", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(53, "001001", "J", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(54, "001000", "Ö", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(55, "1110000", "F", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(56, "1010111", "U", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(57, "1010110", "P", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(58, "1001011", "Ő", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(59, "1001010", "Ó", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(60, "1001001", "C", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(61, "1001000", "Ü", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(62, "11100010", "Í", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(63, "111000111", "Ú", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(64, "1110001101", "Ű", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(65, "11100011001", "X", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(66, "111000110000", "W", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(67, "111000110001", "Q", !Code.MORSE_CODE));
        defaultCodeTable.add(new Code(68, "010", "BackSpace", !Code.MORSE_CODE, Code.Type.BACK_SPACE_SYMBOL));
        defaultCodeTable.add(new Code(69, "111", "Home", !Code.MORSE_CODE, Code.Type.HOME_SYMBOL));
        defaultCodeTable.add(new Code(70, "110", "End", !Code.MORSE_CODE, Code.Type.END_SYMBOL));
        defaultCodeTable.add(new Code(71, "001100","Control mode", !Code.MORSE_CODE, Code.Type.CHANGE_MODE_SYMBOL));
    }

    /**
     * This method provides reference to the default Morse-code table, and inits it if necessary.
     * @return
     */

    public static List<Code> getDefaultCodeTable() {
        if (defaultCodeTable.isEmpty()) {
            initDefaultCodeTable();
        }

        return defaultCodeTable;
    }

    /**
     * This method searches the symbol for the given code.
     * @param code the code.
     * @return
     */

    public Code findSymbol(String code) {
        for (Code c : codeTable) {
            if (c.isInputSymbol() && c.getNumericCode().equals(code)) {
                return c;
            }
        }

        Code change = getModeChangeCode();
        if (change != null && change.getNumericCode().equals(code)) {
            return change;
        }

        return null;
    }

    /**
     * This method searches the control symbol for the given code.
     * @param code the code.
     * @return
     */

    public Code findControlSymbol(String code) {
        for (Code c : codeTable) {
            if (!c.isInputSymbol() && c.getNumericCode().equals(code)) {
                return c;
            }
        }

        return null;
    }

    /**
     * This method gives back the input symbol codes from the code table in a list.
     * @return
     */

    public List<String> getCodesList() {
        List<String> codes = new ArrayList<>();
        for (Code code : codeTable) {
            if (code.isInputSymbol() || code.getType() == Code.Type.CHANGE_MODE_SYMBOL) {
                codes.add(code.getNumericCode());
            }
        }

        return codes;
    }

    /**
     * This method gives back the control symbol codes from the code table in a list.
     * @return
     */

    public List<String> getControlCodesList() {
        List<String> codes = new ArrayList<>();
        for (Code code : codeTable) {
            if (!code.isInputSymbol()) {
                codes.add(code.getNumericCode());
            }
        }

        return codes;
    }

    /**
     * This method returns the code which belongs to the mode changer symbol.
     * @return
     */

    private Code getModeChangeCode() {
        for (Code code : codeTable) {
            if (code.getType() == Code.Type.CHANGE_MODE_SYMBOL) {
                return code;
            }
        }

        return null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

}
