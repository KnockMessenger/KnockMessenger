package hu.vadasz.peter.morsecodedecoder.Interfaces;

import java.util.concurrent.ConcurrentLinkedQueue;

import hu.vadasz.peter.morsecodedecoder.Exceptions.MorseCodeDecoderInternalException;

/**
 * Interface implemented by MorseCodeDecoder class. The module can be used by this interface.
 */

public interface MorseCodeDecoderInterface {

    /**
     * Starts the decoding.
     * @throws MorseCodeDecoderInternalException
     */

    void start();

    /**
     * Stops the decoding.
     */

    void stop();

    /**
     * Gives back the decoder's buffer.
     * @return
     */

    ConcurrentLinkedQueue<Integer> getBuffer();
}
