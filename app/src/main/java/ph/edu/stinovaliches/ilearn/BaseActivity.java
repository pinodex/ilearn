package ph.edu.stinovaliches.ilearn;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import ph.edu.stinovaliches.ilearn.letters.LettersActivity;
import ph.edu.stinovaliches.ilearn.letters.ViewLetterActivity;
import ph.edu.stinovaliches.ilearn.numbers.NumbersActivity;
import ph.edu.stinovaliches.ilearn.rhymes.RhymesActivity;
import ph.edu.stinovaliches.ilearn.rhymes.SongActivity;

public class BaseActivity extends AppCompatActivity {

    SfxManager sfx = SfxManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        enterImmersiveMode();
    }

    @Override
    protected void onResume() {
        super.onResume();

        enterImmersiveMode();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void enterImmersiveMode() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
    }

    public void onHelpButtonClick() {
        if (getClass() == LettersActivity.class) {
            showHelpDialog(R.string.help_letters_activity);
        }

        if (getClass() == ViewLetterActivity.class) {
            showHelpDialog(R.string.help_view_letter_activity);
        }

        if (getClass() == NumbersActivity.class) {
            showHelpDialog(R.string.help_numbers_activity);
        }

        if (getClass() == RhymesActivity.class) {
            showHelpDialog(R.string.help_rhymes_activity);
        }

        if (getClass() == SongActivity.class) {
            showHelpDialog(R.string.help_song_activity);
        }
    }

    private void showHelpDialog(int stringId) {
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);

        builder.setTitle("Help");
        builder.setMessage(stringId);

        builder.setPositiveButton("Close", ((dialogInterface, i) -> {
            enterImmersiveMode();

            dialogInterface.cancel();
        }));

        AlertDialog dialog = builder.create();

        dialog.show();
        enterImmersiveMode();
    }

    protected void setHeaderListeners() {
        ImageView homeImageBtn, helpImageBtn;

        homeImageBtn = findViewById(R.id.btn_home);
        helpImageBtn = findViewById(R.id.btn_help);

        if (homeImageBtn != null) {
            homeImageBtn.setOnClickListener(new HomeButtonOnClickListener());
        }

        if (helpImageBtn != null) {
            helpImageBtn.setOnClickListener((view) -> onHelpButtonClick());
        }
    }

    protected class HomeButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            sfx.play("home_click");

            finish();

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
}
