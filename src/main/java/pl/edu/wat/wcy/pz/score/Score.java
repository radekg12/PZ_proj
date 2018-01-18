package pl.edu.wat.wcy.pz.score;

import pl.edu.wat.wcy.pz.checkers.Player;

public class Score {
    private Player player1;
    private String date;
    private Player player2;

    public Score(Player player1, Player player2, String date) {
        this.player1 = player1;
        this.player2 = player2;
        this.date = date;
    }


    public Object getColumnValue(int columnIndex) throws IllegalAccessException {
        return getClass().getDeclaredFields()[columnIndex].get(this);
    }


    public static int getFieldsCount() {
        return Score.class.getDeclaredFields().length;
    }

    public static String getColumnName(int columnIndex) {
        return Score.class.getDeclaredFields()[columnIndex].getName();
    }

    public Player getPlayer1() {
        return player1;
    }

    public Score setPlayer1(Player player1) {
        this.player1 = player1;
        return this;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Score setPlayer2(Player player2) {
        this.player2 = player2;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Score setDate(String date) {
        this.date = date;
        return this;
    }

}
