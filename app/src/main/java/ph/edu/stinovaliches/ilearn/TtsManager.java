package ph.edu.stinovaliches.ilearn;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

/**
 * Created by raphm on 22/03/2018.
 */

public class TtsManager {
    private static TtsManager instance;

    private TextToSpeech tts;

    private Context context;

    private boolean initialized = false;

    public static TtsManager getInstance() {
        return instance;
    }

    private TtsManager(Context context) {
        this.context = context;

        tts = new TextToSpeech(context, new TtsOnInitListener());
    }

    public static void initialize(Context context) {
        if (instance == null)
            instance = new TtsManager(context);
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void speak(String message, boolean block) {
        tts.speak(message, TextToSpeech.QUEUE_ADD, null);

        while (block && tts.isSpeaking()) {
            // Block the thread. Async sucks
        }
    }

    public void speak(String message) {
        speak(message, true);
    }

    private class TtsOnInitListener implements TextToSpeech.OnInitListener {
        @Override
        public void onInit(int i) {
            initialized = true;
        }
    }
}
