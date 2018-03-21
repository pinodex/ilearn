package ph.edu.stinovaliches.ilearn.rhymes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

import ph.edu.stinovaliches.ilearn.BaseActivity;
import ph.edu.stinovaliches.ilearn.R;

public class RhymesActivity extends BaseActivity {

    ListView songList;

    ArrayList<String[]> songs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rhymes);

        setHeaderListeners();

        // Read song list
        try {
            XmlPullParser list = getResources().getXml(
                    getResources().getIdentifier("songs", "xml", getPackageName())
            );

            while (list.getEventType() != XmlPullParser.END_DOCUMENT) {
                if (list.getEventType() == XmlPullParser.START_TAG) {
                    if (list.getName().equals("song")) {
                        songs.add(new String[] {
                                list.getAttributeValue(null, "title"),
                                list.getAttributeValue(null, "moniker")
                        });
                    }
                }

                list.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SongAdapter songAdapter = new SongAdapter(this, songs);

        songList = findViewById(R.id.song_list);

        songList.setAdapter(songAdapter);
        songList.setOnItemClickListener(new OnSongClickListener());
    }

    private class OnSongClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(RhymesActivity.this, SongActivity.class);
            String[] song = songs.get(i);

            intent.putExtra("title", song[0]);
            intent.putExtra("moniker", song[1]);

            startActivity(intent);
        }
    }
}
