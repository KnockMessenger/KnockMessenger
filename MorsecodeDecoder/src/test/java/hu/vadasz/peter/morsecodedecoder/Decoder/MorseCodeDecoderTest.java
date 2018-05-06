package hu.vadasz.peter.morsecodedecoder.Decoder;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import hu.vadasz.peter.morsecodedecoder.Code.Code;
import hu.vadasz.peter.morsecodedecoder.Decoder.Utils.MorseCodeTable;

import static org.junit.Assert.*;

/**
 * Created by Peti on 2018. 05. 06..
 */

@RunWith(MockitoJUnitRunner.class)
public class MorseCodeDecoderTest {

    public static final int MAX_TIME = 1000;
    public static final boolean OK = true;
    public static final boolean MORSE_MODE = true;

    private MorseCodeDecoder morseCodeDecoder;

    private TestDecodedSignalReceiver receiver;

    @Before
    public void init() {
        receiver = new TestDecodedSignalReceiver();
    }

    @Test(timeout = MAX_TIME)
    public void testDecode_inputModeDecoderStoppedWithGapShouldDecodeCorrectMorseSymbol() {
        morseCodeDecoder = new MorseCodeDecoder(receiver, MorseCodeTable.getDefaultCodeTable(), MORSE_MODE);
        Thread testThread = new Thread(new Runnable() {
            @Override
            public void run() {
                morseCodeDecoder.start();
            }
        });
        testThread.start();

        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.LONG_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.GAP);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        morseCodeDecoder.stop();
        testThread.interrupt();

        assertEquals(OK, receiver.getBuffer().size() == 1
                && receiver.getBuffer().get(0).getNumericCode().equals("012")
                && receiver.getBuffer().get(0).isMorse());
    }

    @Test(timeout = MAX_TIME)
    public void testDecode_inputModeDecoderStoppedWithGapShouldDecodeCorrectMorseSymbolTwoTimes() {
        morseCodeDecoder = new MorseCodeDecoder(receiver, MorseCodeTable.getDefaultCodeTable(), MORSE_MODE);
        Thread testThread = new Thread(new Runnable() {
            @Override
            public void run() {
                morseCodeDecoder.start();
            }
        });
        testThread.start();

        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.LONG_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.GAP);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.GAP);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        morseCodeDecoder.stop();
        testThread.interrupt();

        assertEquals(OK, receiver.getBuffer().size() == 2
                && receiver.getBuffer().get(0).getNumericCode().equals("012")
                && receiver.getBuffer().get(1).getNumericCode().equals("02")
                && receiver.getBuffer().get(0).isMorse()
                && receiver.getBuffer().get(1).isMorse());
    }

    @Test(timeout = MAX_TIME)
    public void testDecode_inputModeDecoderStoppedWithSpaceShouldDecodeCorrectMorseSymbol() {
        morseCodeDecoder = new MorseCodeDecoder(receiver, MorseCodeTable.getDefaultCodeTable(), MORSE_MODE);
        Thread testThread = new Thread(new Runnable() {
            @Override
            public void run() {
                morseCodeDecoder.start();
            }
        });
        testThread.start();

        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.LONG_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SPACE);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        morseCodeDecoder.stop();
        testThread.interrupt();

        assertEquals(OK, receiver.getBuffer().size() == 1
                && receiver.getBuffer().get(0).getNumericCode().equals("012")
                && receiver.getBuffer().get(0).isMorse());
    }

    @Test(timeout = MAX_TIME)
    public void testDecode_morseModeDecoderShouldChangeStateToEditMode() {
        morseCodeDecoder = new MorseCodeDecoder(receiver, MorseCodeTable.getDefaultCodeTable(), MORSE_MODE);
        Thread testThread = new Thread(new Runnable() {
            @Override
            public void run() {
                morseCodeDecoder.start();
            }
        });
        testThread.start();

        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.LONG_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.LONG_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.LONG_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.GAP);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        morseCodeDecoder.stop();
        testThread.interrupt();

        assertEquals(OK, receiver.getBuffer().size() == 1
                && receiver.getBuffer().get(0).getNumericCode().equals("1110002")
                && receiver.getBuffer().get(0).isMorse()
                && morseCodeDecoder.getMode() == MorseCodeDecoder.EDIT_MODE);
    }

    @Test(timeout = MAX_TIME)
    public void testDecode_morseModedecoderShouldChangeStateToInputMode() {
        morseCodeDecoder = new MorseCodeDecoder(receiver, MorseCodeTable.getDefaultCodeTable(), MORSE_MODE);
        morseCodeDecoder.setMode(MorseCodeDecoder.EDIT_MODE);
        Thread testThread = new Thread(new Runnable() {
            @Override
            public void run() {
                morseCodeDecoder.start();
            }
        });
        testThread.start();

        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.LONG_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.LONG_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.LONG_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.GAP);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        morseCodeDecoder.stop();
        testThread.interrupt();

        assertEquals(OK, receiver.getBuffer().size() == 1
                && receiver.getBuffer().get(0).getNumericCode().equals("1110002")
                && morseCodeDecoder.getMode() == MorseCodeDecoder.INPUT_MODE);
    }

    @Test(timeout = MAX_TIME)
    public void testDecode_morseModeDecoderShouldCallErrorMethodBecauseCodeNotExists() {
        morseCodeDecoder = new MorseCodeDecoder(receiver, MorseCodeTable.getDefaultCodeTable(), MORSE_MODE);
        Thread testThread = new Thread(new Runnable() {
            @Override
            public void run() {
                morseCodeDecoder.start();
            }
        });
        testThread.start();

        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.LONG_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.LONG_SYLLABLE);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        morseCodeDecoder.stop();
        testThread.interrupt();

        assertEquals(OK, receiver.getBuffer().isEmpty() && receiver.isErrorCalled());
    }

    @Test(timeout = MAX_TIME)
    public void testDecode_morseModeEditModeDecoderShouldReturnCorrectControlSymbol() {
        morseCodeDecoder = new MorseCodeDecoder(receiver, MorseCodeTable.getDefaultCodeTable(), MORSE_MODE);
        morseCodeDecoder.setMode(MorseCodeDecoder.EDIT_MODE);
        Thread testThread = new Thread(new Runnable() {
            @Override
            public void run() {
                morseCodeDecoder.start();
            }
        });
        testThread.start();

        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.LONG_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SPACE);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        morseCodeDecoder.stop();
        testThread.interrupt();

        assertEquals(OK, receiver.getBuffer().size() == 1
                && receiver.getBuffer().get(0).getNumericCode().equals("012")
                && receiver.getBuffer().get(0).isMorse()
                && receiver.getBuffer().get(0).getType().equals(Code.Type.BACK_SPACE_SYMBOL));
    }

    @Test(timeout = MAX_TIME)
    public void testDecode_inputModeDecoderShouldDecodeCorrectHuffmanSymbol() {
        morseCodeDecoder = new MorseCodeDecoder(receiver, MorseCodeTable.getDefaultHuffmanCodes(), !MORSE_MODE);
        Thread testThread = new Thread(new Runnable() {
            @Override
            public void run() {
                morseCodeDecoder.start();
            }
        });
        testThread.start();

        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        morseCodeDecoder.stop();
        testThread.interrupt();

        assertEquals(OK, receiver.getBuffer().size() == 1
                && receiver.getBuffer().get(0).getNumericCode().equals("000")
                && !receiver.getBuffer().get(0).isMorse());
    }

    @Test(timeout = MAX_TIME)
    public void testDecode_huffmanInputModeDecoderDecodeCorrectMorseSymbolTwoTimes() {
        morseCodeDecoder = new MorseCodeDecoder(receiver, MorseCodeTable.getDefaultHuffmanCodes(), !MORSE_MODE);
        Thread testThread = new Thread(new Runnable() {
            @Override
            public void run() {
                morseCodeDecoder.start();
            }
        });
        testThread.start();

        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.LONG_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        morseCodeDecoder.stop();
        testThread.interrupt();

        assertEquals(OK, receiver.getBuffer().size() == 2
                && receiver.getBuffer().get(0).getNumericCode().equals("000")
                && receiver.getBuffer().get(1).getNumericCode().equals("010")
                && !receiver.getBuffer().get(0).isMorse()
                && !receiver.getBuffer().get(0).isMorse());
    }

    @Test(timeout = MAX_TIME)
    public void testDecode_huffmanModeDecoderShouldChangeStateToEditMode() {
        morseCodeDecoder = new MorseCodeDecoder(receiver, MorseCodeTable.getDefaultHuffmanCodes(), !MORSE_MODE);
        Thread testThread = new Thread(new Runnable() {
            @Override
            public void run() {
                morseCodeDecoder.start();
            }
        });
        testThread.start();

        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.LONG_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.LONG_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        morseCodeDecoder.stop();
        testThread.interrupt();

        assertEquals(OK, receiver.getBuffer().size() == 1
                && receiver.getBuffer().get(0).getNumericCode().equals("001100")
                && !receiver.getBuffer().get(0).isMorse()
                && morseCodeDecoder.getMode() == MorseCodeDecoder.EDIT_MODE);
    }

    @Test(timeout = MAX_TIME)
    public void testDecode_huffmanModedecoderShouldChangeStateToInputMode() {
        morseCodeDecoder = new MorseCodeDecoder(receiver, MorseCodeTable.getDefaultHuffmanCodes(), !MORSE_MODE);
        morseCodeDecoder.setMode(MorseCodeDecoder.EDIT_MODE);
        Thread testThread = new Thread(new Runnable() {
            @Override
            public void run() {
                morseCodeDecoder.start();
            }
        });
        testThread.start();

        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.LONG_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.LONG_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        morseCodeDecoder.stop();
        testThread.interrupt();

        assertEquals(OK, receiver.getBuffer().size() == 1
                && receiver.getBuffer().get(0).getNumericCode().equals("001100")
                && !receiver.getBuffer().get(0).isMorse()
                && morseCodeDecoder.getMode() == MorseCodeDecoder.INPUT_MODE);
    }

    @Test(timeout = MAX_TIME)
    public void testDecode_huffmanModeEditModeDecoderShouldReturnCorrectControlSymbol() {
        morseCodeDecoder = new MorseCodeDecoder(receiver, MorseCodeTable.getDefaultHuffmanCodes(), !MORSE_MODE);
        morseCodeDecoder.setMode(MorseCodeDecoder.EDIT_MODE);
        Thread testThread = new Thread(new Runnable() {
            @Override
            public void run() {
                morseCodeDecoder.start();
            }
        });
        testThread.start();

        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.LONG_SYLLABLE);
        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        morseCodeDecoder.stop();
        testThread.interrupt();

        assertEquals(OK, receiver.getBuffer().size() == 1
                && receiver.getBuffer().get(0).getNumericCode().equals("010")
                && !receiver.getBuffer().get(0).isMorse()
                && receiver.getBuffer().get(0).getType().equals(Code.Type.BACK_SPACE_SYMBOL));
    }

    @Test(timeout = MAX_TIME + MorseCodeDecoder.WAITING_IN_NODE_TIME + 500)
    public void testDecode_huffmanTimeout() {
        morseCodeDecoder = new MorseCodeDecoder(receiver, MorseCodeTable.getDefaultHuffmanCodes(), !MORSE_MODE);
        Thread testThread = new Thread(new Runnable() {
            @Override
            public void run() {
                morseCodeDecoder.start();
            }
        });
        testThread.start();

        morseCodeDecoder.getBuffer().add(MorseCodeDecoder.SHORT_SYLLABLE);

        try {
            Thread.sleep(MorseCodeDecoder.WAITING_IN_NODE_TIME + MAX_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        morseCodeDecoder.stop();
        testThread.interrupt();

        assertEquals(OK, receiver.getBuffer().isEmpty() && receiver.isTimeOver());
    }
}