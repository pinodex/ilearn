package ph.edu.stinovaliches.ilearn.numbers;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

import ph.edu.stinovaliches.ilearn.BaseActivity;
import ph.edu.stinovaliches.ilearn.R;
import ph.edu.stinovaliches.ilearn.SfxManager;

public class NumbersActivity extends BaseActivity {

    ImageView numberImage, controlPrevious, controlNext;
    TextView txtNumber;

    int currentNumber = 1;

    SfxManager sfx = SfxManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_numbers);

        setHeaderListeners();

        numberImage = findViewById(R.id.image);
        controlPrevious = findViewById(R.id.control_previous);
        controlNext = findViewById(R.id.control_next);
        txtNumber = findViewById(R.id.current_number);

        controlPrevious.setOnClickListener(view -> {
            currentNumber--;

            if (currentNumber <= 0) {
                currentNumber = 10;
            }

            showNumber(currentNumber);

            sfx.play("previous");
        });

        controlNext.setOnClickListener(view -> {
            currentNumber++;

            if (currentNumber >= 11) {
                currentNumber = 1;
            }

            showNumber(currentNumber);

            sfx.play("next");
        });

        showNumber(currentNumber);
    }

    private void showNumber(int number) {
        try {
            InputStream is = getAssets().open("numbers/" + number + ".png");

            Drawable drawable = Drawable.createFromStream(is, null);

            numberImage.setImageDrawable(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }

        txtNumber.setText(String.valueOf(number));
    }
}
