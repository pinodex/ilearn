package ph.edu.stinovaliches.ilearn.reading;

/**
 * Created by raphm on 23/03/2018.
 */

public class ScoreManager {
    private static final ScoreManager ourInstance = new ScoreManager();

    public static ScoreManager getInstance() {
        return ourInstance;
    }

    private int score = 0;

    private ScoreManager() {
    }

    public void increment() {
        score++;
    }

    public String getScoreString() {
        return String.valueOf(score);
    }
}
