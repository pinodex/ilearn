package ph.edu.stinovaliches.ilearn;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent splashScreenIntent = new Intent(this, WelcomeActivity.class);

        startActivity(splashScreenIntent);

        finish();
    }
}
