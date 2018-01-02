import java.sql.*;

public class Baza {

    private Connection conn;
    private Statement stat;
    private String url;

    public Baza() {
        url = "jdbc:sqlserver://pzprojekt.database.windows.net:1433;database=PZ;" +
                "user=User1;" +
                "password=Password1;" +
                "encrypt=true;" +
                "trustServerCertificate=false;" +
                "hostNameInCertificate=*.database.windows.net;" +
                "loginTimeout=30;";
        try {
            conn = DriverManager.getConnection(url);
            stat = conn.createStatement();
        } catch (SQLException e) {
            System.out.println("Problem z otwarciem polaczenia");
            e.printStackTrace();
        }
    }


    public void closeConnection() {
        try {
            conn.close();
            System.out.println("Połączono z bazą");
        } catch (SQLException e) {
            System.err.println("Problem z zamknieciem polaczenia");
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public Statement getStat() {
        return stat;
    }

    public void setStat(Statement stat) {
        this.stat = stat;
    }

    public void zapiszWyniki(WygranaEvent event) throws SQLException {
        Gracz p1 = event.getWygrany();
        Gracz p2 = event.getPrzegrany();
        Gra g = event.getGra();
        ResultSet rs = null;
        int generatedKey1 = -1, generatedKey2 = -1;

        String insertPlayer1 = "INSERT INTO PZ.dbo.Player (name, color, avatarName, numberOfMoves, time, winner) " +
                "VALUES (\'" + p1.getNazwa() + "\', 'NULL', 'NULL', NULL, NULL, " + (p1.isWon() ? 1 : 0) + ")";

        String insertPlayer2 = "INSERT INTO PZ.dbo.Player (name, color, avatarName, numberOfMoves, time, winner) " +
                "VALUES (\'" + p2.getNazwa() + "\', 'NULL', 'NULL', NULL, NULL, " + (p2.isWon() ? 1 : 0) + ")";

        stat.execute(insertPlayer1, Statement.RETURN_GENERATED_KEYS);
        rs = stat.getGeneratedKeys();
        while (rs.next()) {
            generatedKey1 = rs.getInt(1);
        }

        stat.execute(insertPlayer2, Statement.RETURN_GENERATED_KEYS);
        rs = stat.getGeneratedKeys();
        while (rs.next()) {
            generatedKey2 = rs.getInt(1);
        }
        String insertGame = "INSERT INTO PZ.dbo.Game (id_player1, id_player2, date)" +
                "VALUES (" + generatedKey1 + ", " + generatedKey2 + ", GETDATE() )";
        stat.execute(insertGame);
        System.out.println("zapisano wynik do bazy");

    }

}
