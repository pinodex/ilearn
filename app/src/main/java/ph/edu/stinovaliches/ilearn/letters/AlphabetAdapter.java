package ph.edu.stinovaliches.ilearn.letters;

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
 * Created by raphm on 22/03/2018.
 */

public class AlphabetAdapter extends BaseAdapter {
    private Context context;

    private ArrayList<String[]> alphabet;

    private Typeface typeface;

    public AlphabetAdapter(Context context, ArrayList<String[]> alphabet) {
        this.context = context;
        this.alphabet = alphabet;

        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/kid_marker.ttf");
    }

    @Override
    public int getCount() {
        return alphabet.size();
    }

    @Override
    public Object getItem(int i) {
        return alphabet.get(i);
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

            view = inflater.inflate(R.layout.layout_alphabet_letter, null);
        }

        String[] entry = alphabet.get(i);

        TextView letter = view.findViewById(R.id.letter);

        letter.setTypeface(typeface);
        letter.setText(entry[0]);

        return view;
    }
}
