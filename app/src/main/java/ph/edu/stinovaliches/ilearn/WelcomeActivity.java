package ph.edu.stinovaliches.ilearn;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ph.edu.stinovaliches.ilearn.letters.LettersActivity;
import ph.edu.stinovaliches.ilearn.numbers.NumbersActivity;
import ph.edu.stinovaliches.ilearn.reading.ReadingActivity;
import ph.edu.stinovaliches.ilearn.rhymes.RhymesActivity;

public class WelcomeActivity extends BaseActivity {

    SparseArray<Class> menuMap = new SparseArray<>();

    ViewGroup container;
    RelativeLayout logo, plank;

    public MediaPlayer bgmPlayer;

    ImageView menuImage;
    SfxManager sfx;
    TtsManager tts;

    {
        menuMap.put(-16777216, LettersActivity.class);
        menuMap.put(-15658735, NumbersActivity.class);
        menuMap.put(-14342875, RhymesActivity.class);
        menuMap.put(-13224394, ReadingActivity.class);
        menuMap.put(-11184811, AboutActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        container = findViewById(R.id.container);
        logo = findViewById(R.id.logo);
        plank = findViewById(R.id.plank);
        menuImage = findViewById(R.id.menu_image);

        logo.setVisibility(View.GONE);
        plank.setVisibility(View.GONE);

        bgmPlayer = MediaPlayer.create(this, R.raw.background_music);

        bgmPlayer.setLooping(true);
        bgmPlayer.start();

        SfxManager.initialize(this);
        sfx = SfxManager.getInstance();

        TtsManager.initialize(this);

        // Animate intro

        new Handler().postDelayed(() -> {
            TransitionManager.beginDelayedTransition(container);

            logo.setVisibility(View.VISIBLE);
        }, 1000);

        new Handler().postDelayed(() -> {
            TransitionManager.beginDelayedTransition(container);

            plank.setVisibility(View.VISIBLE);
        }, 2000);

        menuImage.setOnTouchListener(new MenuOnTouchListener());

        requestPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();

        bgmPlayer.seekTo(0);
        bgmPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        bgmPlayer.pause();
    }

    private class MenuOnTouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            final int action, x, y;

            action = motionEvent.getAction();
            x = (int) motionEvent.getX();
            y = (int) motionEvent.getY();

            if (action == MotionEvent.ACTION_UP) {
                int touchColor = getHotspotColor(R.id.menu_image_mask, x, y);

                Class nextActivity = menuMap.get(touchColor);

                if (nextActivity != null) {
                    sfx.play("menu_click");

                    Intent switchActivity = new Intent(WelcomeActivity.this, nextActivity);

                    startActivity(switchActivity);
                }
            }

            return true;
        }

        int getHotspotColor(int id, int x, int y) {
            ImageView imageView = findViewById(id);

            imageView.setDrawingCacheEnabled(true);

            Bitmap hotspots = Bitmap.createBitmap(imageView.getDrawingCache());

            imageView.setDrawingCacheEnabled(false);

            return hotspots.getPixel(x, y);
        }
    }
}
