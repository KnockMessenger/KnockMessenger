package hu.vadasz.peter.morsecodedecoder.Decoder;

import android.os.CountDownTimer;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import hu.vadasz.peter.morsecodedecoder.Code.Code;
import hu.vadasz.peter.morsecodedecoder.CodeTree.CodeTreeBuilder;
import hu.vadasz.peter.morsecodedecoder.CodeTree.CodeTreeIterator;
import hu.vadasz.peter.morsecodedecoder.CodeTree.Node;
import hu.vadasz.peter.morsecodedecoder.Decoder.Utils.MorseCodeTable;
import hu.vadasz.peter.morsecodedecoder.Exceptions.SymbolNotFoundException;
import hu.vadasz.peter.morsecodedecoder.Interfaces.MorseCodeDecoderInterface;
import lombok.Getter;
import lombok.Setter;

public class MorseCodeDecoder implements MorseCodeDecoderInterface {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// INTERFACES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Interface implemented by class which handles the decoded signals.
     */

    public interface DecodedSignalReceiver {

        /**
         * Method to called when symbol detected.
         * @param code the decoded signal.
         */
        void signalReceived(Code code);

        /**
         * Method called when the symbol can not be decoded.
         * @param message
         */

        void error(String message);

        /**
         * Notifies the activity how much seconds we have in a node.
         * @param maxTime the maximum length we can spend in a node in millis.
         * @param remainingTime the time we have to spend in the node in millis.
         */
        void timeToLive(long maxTime, long remainingTime);

        /**
         * This method is called to notify the receiver that the timer is stopped.
         */

        void pause();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// INTERFACES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    private static final boolean DECODER_IS_RUNNING = true;

    public static final HashMap<String, String> DEFAULT_TABLE = null;
    public static final int INPUT_MODE = 1;
    public static final int EDIT_MODE = 2;
    public static final long WAITING_IN_NODE_TIME = 10000;
    public static final long NOTIFICATION_PERIOD_TIME = 1;
    public static final String TIME_OUT_ERROR = "Sorry we are out of time! Try again!";

    public static final int SHORT_SYLLABLE = 0;
    public static final int LONG_SYLLABLE = 1;
    public static final int GAP = 2;
    public static final int SPACE = 3;

    public static final String SPACE_TEXT = " ";
    public static final String EMPTY_TEXT = "";

    public static final boolean DECODE_SUCCESS = true;

    public static final String SYMBOL_NOT_FOUND_ERROR = "Undefined symbol!";

    /// CONSTANTS -- END

    private DecodedSignalReceiver decodedSignalReceiver;
    private ConcurrentLinkedQueue<Integer> buffer;

    private boolean running;
    private boolean morseMode;

    @Getter
    @Setter
    private int mode;

    private Node codeTree;
    private CodeTreeIterator codeTreeIterator;
    private MorseCodeTable morseCodeTable;

    private CountDownTimer timer;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public MorseCodeDecoder(final DecodedSignalReceiver decodedSignalReceiver, List<Code> codeTable, boolean morseMode) {
        this.buffer = new ConcurrentLinkedQueue<>();
        this.decodedSignalReceiver = decodedSignalReceiver;
        this.morseMode = morseMode;

        morseCodeTable = codeTable == DEFAULT_TABLE ? new MorseCodeTable() : new MorseCodeTable(codeTable);

        List<String> codes = morseCodeTable.getCodesList();
        codeTree = CodeTreeBuilder.build(codes);
        mode = INPUT_MODE;

        timer = new CountDownTimer(WAITING_IN_NODE_TIME, NOTIFICATION_PERIOD_TIME) {

            @Override
            public void onTick(long l) {
                decodedSignalReceiver.timeToLive(WAITING_IN_NODE_TIME, l );
            }

            @Override
            public void onFinish() {
                sendError(TIME_OUT_ERROR);
            }
        };
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Main method of decoder, examines the buffer and finds the matching symbol or control symbol
     * of the signal in codeTable while the buffer is not empty.
     */

    private void decode() {

        while (running) {
            while (!buffer.isEmpty()) {
                int syllable = buffer.poll();
                try {
                    stepOnCodeTree(syllable);
                } catch (SymbolNotFoundException e) {
                    sendError(e.getMessage());
                }
            }
        }
    }

    /**
     * Finds the matching control symbol of the coming signal depending on the morseMode.
     * @param code from buffer, coming from a signal detector class which has reference to the
     * buffer.
     * @return the decoded symbol.
     */

    private Code findControlSymbol(String code) {
        return morseCodeTable.findControlSymbol(code);
    }

    /**
     * Finds the matching symbol of the coming signal depending on the morseMode.
     * @param code from buffer, coming from a signal detector class which has reference to the
     * buffer.
     * @return the decoded symbol.
     */

    private Code findSymbol(String code) {
        return morseCodeTable.findSymbol(code);
    }

    /**
     * This method checks that it is necessary to change mode.
     * @param code the detected code.
     */

    private void checkMode(String code) {
        Code foundSymbol = findControlSymbol(code);
        if (foundSymbol != null && foundSymbol.getType() == Code.Type.CHANGE_MODE_SYMBOL) {
            changeMode();
        } else {
            codeTreeIterator.reset(codeTree);
        }
    }

    /**
     * This method changes the decoder's mode. If the current mode is input than the new will be edit.
     */

    public void changeMode() {
        if (mode == INPUT_MODE) {
            mode = EDIT_MODE;
            codeTree = CodeTreeBuilder.build(morseCodeTable.getControlCodesList());
        } else {
            mode = INPUT_MODE;
            codeTree = CodeTreeBuilder.build(morseCodeTable.getCodesList());
        }
    }

    /**
     * This method tries to step on the code tree if the current node has a child in the needed
     * direction, short syllable is left child, long syllable is right child.
     * @param syllable the direction.
     * @throws SymbolNotFoundException
     */

    private void stepOnCodeTree(int syllable) throws SymbolNotFoundException {
        decodedSignalReceiver.pause();

        switch(syllable) {
            case SHORT_SYLLABLE:
                if (codeTreeIterator.getCurrentNode().hasShortChild()) {
                    codeTreeIterator.nextShort();
                    sendResult();
                } else {
                    throw new SymbolNotFoundException(SYMBOL_NOT_FOUND_ERROR);
                }
                break;
            case LONG_SYLLABLE:
                if (codeTreeIterator.getCurrentNode().hasLongChild()) {
                    codeTreeIterator.nextLong();
                    sendResult();
                } else {
                    throw new SymbolNotFoundException(SYMBOL_NOT_FOUND_ERROR);
                }
                break;
            case GAP:
                if (codeTreeIterator.getCurrentNode().hasGapChild()) {
                    codeTreeIterator.nextGap();
                    sendResult(EMPTY_TEXT);
                } else {
                    throw new SymbolNotFoundException(SYMBOL_NOT_FOUND_ERROR);
                }
                break;
            case SPACE:
                Log.i("DECODER",codeTreeIterator.getCurrentNode().isLeaf() + "");
                if (codeTreeIterator.getCurrentNode().hasGapChild()) {
                    codeTreeIterator.nextGap();
                    sendResult(SPACE_TEXT);
                } else {
                    throw new SymbolNotFoundException(SYMBOL_NOT_FOUND_ERROR);
                }
                break;
        }
    }

    /**
     * This method sends the decoded symbol to the receiver and resets the code tree's iterator.
     */

    private void sendResult(String space) {
        Code foundSymbol = mode == INPUT_MODE ? findSymbol(codeTreeIterator.getPath()) :
                findControlSymbol(codeTreeIterator.getPath());

        decodedSignalReceiver.signalReceived(foundSymbol);
        checkMode(codeTreeIterator.getPath());
        codeTreeIterator.reset(codeTree);
    }

    /**
     *
     */

    private void sendResult() {
        if (!morseMode) {
            timer.cancel();
            if (!codeTreeIterator.hasNext()) {
                sendResult(EMPTY_TEXT);
            } else {
                timer.start();
            }
        }
    }

    /**
     * This method id called when a symbol can not be detected, due to time or wrong direction in the
     * code tree.
     * @param message
     */

    private void sendError(String message) {
        if (!morseMode) {
            timer.cancel();
        }

        decodedSignalReceiver.error(message);
        codeTreeIterator.reset(codeTree);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// METHODS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// MorseCodeDecoderInterface INTERFACE OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void start() {
        running = DECODER_IS_RUNNING;
        codeTreeIterator = new CodeTreeIterator(codeTree);
        decode();
    }

    @Override
    public void stop() {
        running = !DECODER_IS_RUNNING;
        timer.cancel();
    }

    @Override
    public ConcurrentLinkedQueue<Integer> getBuffer() {
        return this.buffer;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// MorseCodeDecoderInterface INTERFACE OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
