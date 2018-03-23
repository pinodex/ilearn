package ph.edu.stinovaliches.ilearn.reading;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.os.Bundle;
import android.transition.TransitionManager;
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
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.speech.SpeechRecognizer.RESULTS_RECOGNITION;

public class ReadingActivity extends BaseActivity {

    ViewGroup container;
    RelativeLayout playButtonContainer, wordContainer;
    ImageView playButton;
    TextView txtWord, txtScore;

    ScoreManager scoreManager = ScoreManager.getInstance();

    SfxManager sfx = SfxManager.getInstance();
    TtsManager tts = TtsManager.getInstance();

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

        if (!hasRecordAudioPermission()) {
            new AppSettingsDialog.Builder(this).build().show();

            finish();

            return;
        }

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
        txtScore = findViewById(R.id.score);

        txtScore.setText(scoreManager.getScoreString());

        playButton.setOnClickListener(new PlayButtonOnClickListener());

        wordContainer.setVisibility(View.GONE);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new SpeechRecognitionListener());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (speechRecognizer != null) {
            speechRecognizer.stopListening();

            speechRecognizer.destroy();
        }
    }

    private boolean hasRecordAudioPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.RECORD_AUDIO);
    }

    private void getNewWord() {
        incorrectAttempts = 0;

        int rIndex = random.nextInt(words.size());

        currentWord = words.get(rIndex);

        speakCurrentWord();

        listenToWord();
    }

    private void speakCurrentWord() {
        txtWord.setText(currentWord);

        tts.speak(currentWord);
    }

    private void listenToWord() {
        Intent speechintent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        speechintent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS , 500);
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

            tts.speak("Repeat after me.", false);

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
            switch (i) {
                case SpeechRecognizer.ERROR_NO_MATCH:
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                case SpeechRecognizer.ERROR_NETWORK:
                    tts.speak("I cannot hear you.");

                    tts.speak("Say the word");

                    incorrectAttempts++;

                    if (incorrectAttempts >= 3) {
                        getNewWord();

                        return;
                    }

                    speakCurrentWord();

                    listenToWord();

                    break;
            }
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

                tts.speak("Say the word");

                speakCurrentWord();

                listenToWord();

                return;
            }

            scoreManager.increment();

            txtScore.setText(scoreManager.getScoreString());

            sfx.play("game_coin");
            tts.speak("Correct!");

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
