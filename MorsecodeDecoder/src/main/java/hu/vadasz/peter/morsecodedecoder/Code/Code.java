package hu.vadasz.peter.morsecodedecoder.Code;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents codes. There can be two type of codes, Morse type code, with a gap at the end,
 * and Huffman type codes which should be prefix. 0 = short syllable, 1 = long syllable,
 * 2 = gap/space. Each Morse-code should end with a gap. This class does not validate the codes.
 * This class also stores the action of a code, average codes are INPUT_SYMBOLS. Codes which have
 * an other Type can be used for actions.
 */

@AllArgsConstructor
@NoArgsConstructor
public class Code implements Parcelable {

    public enum Type {INPUT_SYMBOL, CHANGE_MODE_SYMBOL, BACK_SPACE_SYMBOL, HOME_SYMBOL, END_SYMBOL}

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    public static final int MORSE_CODE_LENGTH_LOWER_LIMIT = 2;
    public static final int HUFFMAN_CODE_LENGTH_LOWER_LIMIT = 1;
    public static final int CODE_LENGTH_LOWER_LIMIT = 1;
    public static final String INIT_STR = "";
    public static final int DEFAULT_ID = -1;
    public static final boolean MORSE_CODE = true;

    /// CONSTANTS -- END

    @Getter
    @Setter
    private int id = DEFAULT_ID;

    private String code = INIT_STR;

    @Getter
    @Setter
    private String text = INIT_STR;

    @Getter
    @Setter
    private boolean morse;

    @Getter
    @Setter
    private Type type;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public Code(int id, String code, String text) {
        this.id = id;
        this.code = code;
        this.text = text;
        morse = MORSE_CODE;
        type = Type.INPUT_SYMBOL;
    }

    public Code(int id, String code, String text, boolean morse) {
        this.id = id;
        this.code = code;
        this.text = text;
        this.morse = morse;
        type = Type.INPUT_SYMBOL;
    }

    public Code(Code rhs) {
        id = rhs.id;
        code = rhs.code;
        text = rhs.text;
        morse = rhs.morse;
        type = rhs.type;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method returns the stored code.
     * @return
     */

    public String getNumericCode() {
        return code;
    }

    /**
     * This method returns the string representation of the coe using (*,-) symbols.
     * @return
     */

    public String getCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < code.length(); ++i) {
            if (code.charAt(i) == '0') {
                sb.append("*");
            } else if (code.charAt(i) == '1') {
                sb.append("-");
            }
        }

        return sb.toString();
    }

    /**
     * This method sets the inner code.
     * @param code the code should contain * and - symbols.
     */

    public void setCode(String code) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < code.length(); ++i) {
            if (code.charAt(i) == '*') {
                sb.append("0");
            } else if (code.charAt(i) == '-') {
                sb.append("1");
            }
        }

        if (morse) {
            sb.append("2");
        }

        this.code = sb.toString();
    }

    /**
     * This method deletes the code at the last position if the code is not empty.
     */

    public void deleteCode() {
        StringBuilder sb = new StringBuilder(code);
        if (morse && code.length() > CODE_LENGTH_LOWER_LIMIT) {
            sb.deleteCharAt(code.length() - 2);
        } else if (!morse && code.length() >= CODE_LENGTH_LOWER_LIMIT) {
            sb.deleteCharAt(code.length() - 1);
        }

        code = sb.toString();
    }

    public boolean isInputSymbol() {
        return type == Type.INPUT_SYMBOL;
    }

    @Override
    public String toString() {
        return "(" + code + " : " + text + " - " + type.toString() +")";
    }

    @Override
    public boolean equals(Object other) {

        if (other == null) {
            return false;
        } else if (other == this) {
            return true;
        } else if (this.getClass() != other.getClass()) {
            return false;
        } else if (this.id == ((Code) other).id) {
            return true;
        }

        return false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// PARCELABLE INTERFACE OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(text);
        dest.writeInt(id);
        dest.writeByte((byte) (morse ? 1 : 0));
        dest.writeString(type.name());
    }

    public static final Parcelable.Creator<Code> CREATOR
            = new Parcelable.Creator<Code>() {
        public Code createFromParcel(Parcel in) {
            return new Code(in);
        }

        public Code[] newArray(int size) {
            return new Code[size];
        }
    };

    private Code(Parcel in) {
        code = in.readString();
        text = in.readString();
        id = in.readInt();
        morse = in.readByte() != 0;
        type = Type.valueOf(in.readString());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// PARCELABLE INTERFACE OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

}
