package ph.edu.stinovaliches.ilearn.reading;

import android.content.Intent;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import ph.edu.stinovaliches.ilearn.BaseActivity;
import ph.edu.stinovaliches.ilearn.R;
import ph.edu.stinovaliches.ilearn.SfxManager;
import ph.edu.stinovaliches.ilearn.TtsManager;

import static android.speech.SpeechRecognizer.RESULTS_RECOGNITION;

public class ReadingActivity extends BaseActivity {

    ViewGroup container;
    RelativeLayout playButtonContainer, wordContainer;
    ImageView playButton;
    TextView txtWord;

    SfxManager sfx = SfxManager.getInstance();
    TextToSpeech tts = TtsManager.getInstance().getTts();

    SpeechRecognizer speechRecognizer;

    ArrayList<String> words = new ArrayList<>();
    Random random = new Random();

    String currentWord;
    int incorrectAttempts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reading);

        setHeaderListeners();

        try {
            XmlPullParser list = getResources().getXml(
                    getResources().getIdentifier("words", "xml", getPackageName())
            );

            while (list.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (list.getEventType() == XmlPullParser.START_TAG) {
                    if (list.getName().equals("word")) {
                        words.add(list.nextText());
                    }
                }

                list.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        container = findViewById(R.id.container);
        playButtonContainer = findViewById(R.id.play_button_container);
        wordContainer = findViewById(R.id.word_container);
        playButton = findViewById(R.id.play_button);
        txtWord = findViewById(R.id.word);

        playButton.setOnClickListener(new PlayButtonOnClickListener());

        wordContainer.setVisibility(View.GONE);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new SpeechRecognitionListener());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        speechRecognizer.stopListening();
    }

    private void getNewWord() {
        int rIndex = random.nextInt(words.size());

        currentWord = words.get(rIndex);

        speakCurrentWord();

        new Handler().postDelayed(() -> listenToWord(), 1000);
    }

    private void speakCurrentWord() {
        txtWord.setText(currentWord);

        tts.speak(currentWord, TextToSpeech.QUEUE_ADD, null);
    }

    private void listenToWord() {
        Intent speechintent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        speechintent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS , 1000);
        speechintent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,  RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechintent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechintent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Repeat the word");

        speechRecognizer.startListening(speechintent);
    }

    private class PlayButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            TransitionManager.beginDelayedTransition(container);

            playButtonContainer.setVisibility(View.GONE);

            sfx.play("game_start");

            tts.speak("Repeat after me.", TextToSpeech.QUEUE_ADD, null);

            new Handler().postDelayed(() -> {
                wordContainer.setVisibility(View.VISIBLE);

                getNewWord();
            }, 2000);
        }
    }

    private class SpeechRecognitionListener implements RecognitionListener {

        @Override
        public void onReadyForSpeech(Bundle bundle) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int i) {
            Log.d("Error", i + "");
        }

        @Override
        public void onResults(Bundle bundle) {
            speechRecognizer.stopListening();

            ArrayList<String> result = bundle.getStringArrayList(RESULTS_RECOGNITION);

            if (!result.contains(currentWord)) {
                incorrectAttempts++;

                if (incorrectAttempts >= 3) {
                    getNewWord();

                    return;
                }

                tts.speak("Say the word", TextToSpeech.QUEUE_ADD, null);

                speakCurrentWord();

                new Handler().postDelayed(() -> listenToWord(), 2000);

                return;
            }

            incorrectAttempts = 0;

            sfx.play("game_coin");
            tts.speak("Correct!", TextToSpeech.QUEUE_ADD, null);

            getNewWord();
        }

        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    }
}
