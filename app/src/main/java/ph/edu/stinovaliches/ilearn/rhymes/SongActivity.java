package ph.edu.stinovaliches.ilearn.rhymes;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

import ph.edu.stinovaliches.ilearn.BaseActivity;
import ph.edu.stinovaliches.ilearn.R;

public class SongActivity extends BaseActivity {

    TextView txtTitle;
    EditText lyricsView;
    ImageView controlButton;

    String title, moniker;

    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_song);

        setHeaderListeners();

        Intent intent = getIntent();

        txtTitle = findViewById(R.id.title);
        lyricsView = findViewById(R.id.lyrics);
        controlButton = findViewById(R.id.control_button);

        title = intent.getStringExtra("title");
        moniker = intent.getStringExtra("moniker");

        player = new MediaPlayer();

        try {
            AssetFileDescriptor afd = getAssets().openFd("songs/" + moniker + ".ogg");

            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());

            afd.close();

            player.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        player.start();

        try {
            InputStream lyricsContent = getAssets().open("songs/" + moniker + ".txt");

            int size = lyricsContent.available();
            byte[] buffer = new byte[size];

            lyricsContent.read(buffer);
            lyricsContent.close();

            lyricsView.setText(new String(buffer));
        } catch (Exception e) {
            e.printStackTrace();
        }

        controlButton.setOnClickListener(new ControlOnClickListener());
        controlButton.setImageResource(android.R.drawable.ic_media_pause);

        txtTitle.setText(title);
    }

    @Override
    protected void onResume() {
        super.onResume();

        player.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        player.stop();
        player = null;
    }

    @Override
    protected void onStop() {
        super.onStop();

        player.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();

        player.stop();
    }

    private class ControlOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (player.isPlaying()) {
                player.pause();

                controlButton.setImageResource(android.R.drawable.ic_media_play);

                return;
            }

            player.start();

            controlButton.setImageResource(android.R.drawable.ic_media_pause);
        }
    }
}
