package pl.edu.wat.wcy.pz.score;

public class ScoreGame {
    //private static final Logger LOGGER = Logger.getLogger(pl.edu.wat.wcy.pz.score.ScoreGame.class.getSimpleName(), "LogsMessages");
    private String date, player1ID, player2ID;

    public ScoreGame(String date, String player1ID, String player2ID) {
        this.date = date;
        this.player1ID = player1ID;
        this.player2ID = player2ID;
    }

    public String getDate() {
        return date;
    }

    public ScoreGame setDate(String date) {
        this.date = date;
        return this;
    }

    public String getPlayer1ID() {
        return player1ID;
    }

    public ScoreGame setPlayer1ID(String player1ID) {
        this.player1ID = player1ID;
        return this;
    }

    public String getPlayer2ID() {
        return player2ID;
    }

    public ScoreGame setPlayer2ID(String player2ID) {
        this.player2ID = player2ID;
        return this;
    }
}
