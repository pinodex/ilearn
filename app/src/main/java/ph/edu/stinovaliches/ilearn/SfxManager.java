package ph.edu.stinovaliches.ilearn;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.AUDIO_SERVICE;

/**
 * Created by raphm on 21/03/2018.
 */

public class SfxManager {
    private static SfxManager instance;

    private Context context;

    private HashMap<String, Integer> sfxMap = new HashMap<>();
    private HashMap<String, Integer> sfxIds = new HashMap<>();

    private AudioManager audioManager;
    private SoundPool soundPool;

    private static final int MAX_STREAMS = 5;

    private boolean loaded;

    private SfxManager(Context context) {
        this.context = context;

        sfxMap.put("menu_click", R.raw.menu_click);
        sfxMap.put("home_click", R.raw.home_click);
        sfxMap.put("previous", R.raw.click2);
        sfxMap.put("next", R.raw.click1);
        sfxMap.put("reveal1", R.raw.reveal1);
        sfxMap.put("reveal2", R.raw.reveal2);
    }

    public static void initialize(Context context) {
        if (instance == null)
            instance = new SfxManager(context);

        instance.load();
    }

    public static SfxManager getInstance() {
        return instance;
    }

    private void load() {
        // Initialize audio

        audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);

        this.soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);

        if (Build.VERSION.SDK_INT >= 21 ) {
            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);

            this.soundPool = builder.build();
        }

        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            this.loaded = true;
        });

        // Load sound effects

        for (Map.Entry<String, Integer> entry: sfxMap.entrySet()) {
            sfxIds.put(entry.getKey(),
                    this.soundPool.load(context, entry.getValue(), 1));
        }
    }

    public void play(String name) {
        if (!this.loaded) {
            return;
        }

        if (!this.sfxIds.containsKey(name)) {
            return;
        }

        float currentVolumeIndex = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolumeIndex  = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        float volume = currentVolumeIndex / maxVolumeIndex;

        int id = this.sfxIds.get(name);

        this.soundPool.play(id, volume, volume, 1, 0, 1f);
    }
}
