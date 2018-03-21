package ph.edu.stinovaliches.ilearn.letters;

import ph.edu.stinovaliches.ilearn.R;
import ph.edu.stinovaliches.ilearn.BaseActivity;
import ph.edu.stinovaliches.ilearn.SfxManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class LettersActivity extends BaseActivity {

    GridView alphabetGrid;

    ArrayList<String[]> alphabet = new ArrayList<>();

    SfxManager sfx = SfxManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_letters);

        setHeaderListeners();

        try {
            XmlPullParser list = getResources().getXml(
                    getResources().getIdentifier("alphabet", "xml", getPackageName())
            );

            while (list.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (list.getEventType() == XmlPullParser.START_TAG) {
                    if (list.getName().equals("entry")) {
                        alphabet.add(new String[] {
                                list.getAttributeValue(null, "letter"),
                                list.getAttributeValue(null, "word"),
                                list.getAttributeValue(null, "picture")
                        });
                    }
                }

                list.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        AlphabetAdapter alphabetAdapter = new AlphabetAdapter(this, alphabet);

        alphabetGrid = findViewById(R.id.alphabet_grid);

        alphabetGrid.setAdapter(alphabetAdapter);

        alphabetGrid.setOnItemClickListener(new OnLetterClickListener());
    }

    private class OnLetterClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(LettersActivity.this, ViewLetterActivity.class);
            String[] entry = alphabet.get(i);

            intent.putExtra("letter", entry[0]);
            intent.putExtra("word", entry[1]);
            intent.putExtra("picture", entry[2]);

            sfx.play("next");

            startActivity(intent);
        }
    }

}
