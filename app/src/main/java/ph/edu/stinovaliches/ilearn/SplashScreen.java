package ph.edu.stinovaliches.ilearn;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(() -> startWelcomeScreen(), 1000);
    }

    void startWelcomeScreen() {
        final Intent welcomeIntent = new Intent(this, WelcomeActivity.class);

        startActivity(welcomeIntent);
    }
}
