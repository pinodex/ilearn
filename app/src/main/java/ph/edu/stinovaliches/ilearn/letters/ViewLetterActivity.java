package ph.edu.stinovaliches.ilearn.letters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.seismic.ShakeDetector;

import java.io.InputStream;

import ph.edu.stinovaliches.ilearn.BaseActivity;
import ph.edu.stinovaliches.ilearn.R;
import ph.edu.stinovaliches.ilearn.SfxManager;

public class ViewLetterActivity extends BaseActivity {

    String letter, word, picture;

    ViewGroup animationContainer;

    LinearLayout answerContainer;
    TextView txtLetter, txtWord;
    ImageView imPicture;

    Vibrator vibrator;

    Typeface typeface;

    SfxManager sfx = SfxManager.getInstance();

    private boolean isRevealed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_letter);

        setHeaderListeners();

        typeface = Typeface.createFromAsset(getAssets(), "fonts/kid_marker.ttf");

        Intent intent = getIntent();

        letter = intent.getStringExtra("letter");
        word = intent.getStringExtra("word");
        picture = intent.getStringExtra("picture");

        animationContainer = findViewById(R.id.animation_container);

        answerContainer = findViewById(R.id.answer_container);
        txtLetter = findViewById(R.id.letter);
        txtWord = findViewById(R.id.word);
        imPicture = findViewById(R.id.picture);

        answerContainer.setVisibility(View.GONE);

        txtLetter.setTypeface(typeface);
        txtLetter.setText(letter);

        txtWord.setText(word);

        try {
            InputStream is = getAssets().open("objects/" + picture);

            Drawable drawable = Drawable.createFromStream(is, null);

            imPicture.setImageDrawable(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(new OnShakeListener());

        sd.start(sensorManager);
    }

    private class OnShakeListener implements ShakeDetector.Listener {

        @Override
        public void hearShake() {
            TransitionManager.beginDelayedTransition(animationContainer);

            if (isRevealed) {
                vibrator.vibrate(200);

                sfx.play("reveal2");

                answerContainer.setVisibility(View.GONE);
                txtLetter.setVisibility(View.VISIBLE);

                isRevealed = false;

                return;
            }

            vibrator.vibrate(200);

            sfx.play("reveal1");

            answerContainer.setVisibility(View.VISIBLE);
            txtLetter.setVisibility(View.GONE);

            isRevealed = true;
        }
    }
}
