package hu.vadasz.peter.knockmessenger.Controllers;

import java.util.List;

import hu.vadasz.peter.knockdetector.Detector.AbstractAudioKnockDetector;
import hu.vadasz.peter.knockdetector.Detector.ComplexAudioKnockDetector;
import hu.vadasz.peter.knockdetector.Detector.SimpleAudioKnockDetector;
import hu.vadasz.peter.knockdetector.Interfaces.KnockDetector;
import hu.vadasz.peter.knockmessenger.Activities.BaseActivityWithKnockDecoder;
import hu.vadasz.peter.morsecodedecoder.Code.Code;
import hu.vadasz.peter.morsecodedecoder.Decoder.MorseCodeDecoder;
import hu.vadasz.peter.morsecodedecoder.Interfaces.MorseCodeDecoderInterface;
import lombok.Getter;

/**
 * Main class of the MessageDetecting service's controller layer. This class is responsible for message
 * detecting features, error handling, and exceptions.
 * The message sending activity should
 * have az instance of this class.
 */

public class MessageDetectingController {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    private static final boolean START_DECODER = true;

    public static final boolean MORSE_MODE = true;

    /// CONSTANTS -- END

    private Thread detectorThread;
    private Thread decoderThread;

    @Getter
    private boolean decoderRunning;

    protected MorseCodeDecoderInterface morseCodeDecoder;

    protected KnockDetector knockDetector;

    private BaseActivityWithKnockDecoder activityWithKnockDecoder;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public MessageDetectingController(BaseActivityWithKnockDecoder activityWithKnockDecoder, int measureTime, List<Code> codeTable,
            boolean morseMode, int amplitudeTheresold) {

        this.activityWithKnockDecoder = activityWithKnockDecoder;
        morseCodeDecoder = new MorseCodeDecoder(activityWithKnockDecoder, codeTable, morseMode);

        if (morseMode) {
            knockDetector = new ComplexAudioKnockDetector(activityWithKnockDecoder, morseCodeDecoder.getBuffer(), measureTime);
        } else {
            knockDetector = new SimpleAudioKnockDetector(activityWithKnockDecoder, morseCodeDecoder.getBuffer(), measureTime);
        }

        ((AbstractAudioKnockDetector) knockDetector).setSensitivity(amplitudeTheresold);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Starts the Decoder and Detector module in new threads, and stores the state of the decoder
     * in the decoderRunning variable.
     */

    public void startDecoder() {
        decoderRunning = START_DECODER;

        decoderThread = new Thread(new Runnable() {

            @Override
            public void run() {
                morseCodeDecoder.start();
            }
        });
        decoderThread.start();

        detectorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    knockDetector.start();
                } catch (Exception e) {
                    stopDecoder();
                }
            }
        });
        detectorThread.start();
    }

    /**
     * Stops the decoder, marks the thread to be killed.
     */

    public void stopDecoder() {
        if (decoderRunning) {
            decoderRunning = !START_DECODER;

            knockDetector.stop();
            decoderThread.interrupt();

            morseCodeDecoder.stop();
            decoderThread.interrupt();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// METHODS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
