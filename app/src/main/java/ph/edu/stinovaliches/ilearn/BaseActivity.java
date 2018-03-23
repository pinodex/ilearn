package ph.edu.stinovaliches.ilearn;

import android.Manifest;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.List;

import ph.edu.stinovaliches.ilearn.letters.LettersActivity;
import ph.edu.stinovaliches.ilearn.letters.ViewLetterActivity;
import ph.edu.stinovaliches.ilearn.numbers.NumbersActivity;
import ph.edu.stinovaliches.ilearn.reading.ReadingActivity;
import ph.edu.stinovaliches.ilearn.rhymes.RhymesActivity;
import ph.edu.stinovaliches.ilearn.rhymes.SongActivity;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class BaseActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    SfxManager sfx = SfxManager.getInstance();

    String[] perms = { Manifest.permission.RECORD_AUDIO };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        enterImmersiveMode();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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

        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, list)) {
            new AppSettingsDialog.Builder(this).build().show();

            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Permission Request Denied");
        builder.setMessage("Some features may not work correctly.");

        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    public void requestPermissions() {
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this,
                    "iLearn requires access to microphone", 110, perms);
        }
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

        if (getClass() == ReadingActivity.class) {
            showHelpDialog(R.string.help_reading_activity);
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
