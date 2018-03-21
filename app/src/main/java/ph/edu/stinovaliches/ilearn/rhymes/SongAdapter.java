package ph.edu.stinovaliches.ilearn.rhymes;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ph.edu.stinovaliches.ilearn.R;

/**
 * Created by raphm on 21/03/2018.
 */

public class SongAdapter extends BaseAdapter {
    private Context context;

    private ArrayList<String[]> songs;

    private LayoutInflater mInflater;

    private Typeface typeface;

    public SongAdapter(Context context, ArrayList<String[]> songs) {
        this.context = context;
        this.songs = songs;

        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/kid_marker.ttf");
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int i) {
        return songs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.layout_song, null);
        }

        String[] entry = songs.get(i);

        TextView title = view.findViewById(R.id.title);

        title.setTypeface(typeface);
        title.setText(entry[0]);

        return view;
    }
}
