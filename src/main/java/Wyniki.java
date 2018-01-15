public class Wyniki {

    private Gracz player1;
    private String date;
    private Gracz player2;

    public Wyniki(Gracz player1, Gracz player2, String date) {
        this.player1 = player1;
        this.player2 = player2;
        this.date = date;
    }


    public Object getColumnValue(int columnIndex) {
        Object res = null;
        try {
            res = getClass().getDeclaredFields()[columnIndex].get(this);
        } catch (IllegalAccessException e) {
            //TODO log
            e.printStackTrace();
        }
        return res;
    }


    public static int getFieldsCount() {
        return Wyniki.class.getDeclaredFields().length;
    }

    public static String getColumnName(int columnIndex) {
        return Wyniki.class.getDeclaredFields()[columnIndex].getName();
    }

    public Gracz getPlayer1() {
        return player1;
    }

    public Wyniki setPlayer1(Gracz player1) {
        this.player1 = player1;
        return this;
    }

    public Gracz getPlayer2() {
        return player2;
    }

    public Wyniki setPlayer2(Gracz player2) {
        this.player2 = player2;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Wyniki setDate(String date) {
        this.date = date;
        return this;
    }

}
