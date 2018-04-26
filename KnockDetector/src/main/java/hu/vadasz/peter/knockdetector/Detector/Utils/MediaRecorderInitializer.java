package hu.vadasz.peter.knockdetector.Detector.Utils;

import android.media.MediaRecorder;
import android.os.Environment;

import java.io.IOException;

/**
 * This class initializes the MediaRecorder and cheks if external storage is available.
 */

public class MediaRecorderInitializer {

    /**
     * This method initializes the MediaRecorder.
     * @param sdCardPath path to save the tmp audio file.
     * @return recorder the initialized MediaRecorder
     * @throws IOException
     */

    public static void initializeRecorder(MediaRecorder recorder, String sdCardPath) {
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES) + sdCardPath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
