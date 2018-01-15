import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Baza {
    private static final Logger LOGGER = Logger.getLogger(Baza.class.getSimpleName(), "LogsMessages");
    private Connection conn;
    private Statement stat;
    private String url;

    public Baza() {
        loadProperties();
        try {
            conn = DriverManager.getConnection(url);
            stat = conn.createStatement();
            LOGGER.info("db.openInfo");
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "db.open", e);
        }
    }


    public void closeConnection() {
        try {
            conn.close();
            LOGGER.info("db.closeInfo");
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "db.close", e);
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

    public void zapiszWyniki(Gracz p1, Gracz p2) throws SQLException {
        ResultSet rs;
        int generatedKey1 = -1, generatedKey2 = -1;

        String insertPlayer1 = "INSERT INTO PZ.dbo.Player (name, color, avatarName, numberOfMoves, time, winner) " +
                "VALUES (\'" + p1.getNazwa() + "\', NULL, \'" + p1.getAvatarName() + "\', NULL, NULL, " + (p1.isWon() ? 1 : 0) + ")";

        String insertPlayer2 = "INSERT INTO PZ.dbo.Player (name, color, avatarName, numberOfMoves, time, winner) " +
                "VALUES (\'" + p2.getNazwa() + "\', NULL, \'" + p2.getAvatarName() + "\', NULL, NULL, " + (p2.isWon() ? 1 : 0) + ")";

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
        LOGGER.info("db.saveInfo");
    }

    public void sprawdzWyniki(WynikiTableModel data) throws SQLException {
        ResultSet rs;
        ArrayList<WynikGra> wynikGras = new ArrayList<>();
        ArrayList<Wyniki> wynikis = new ArrayList<>();
        Gracz p1, p2;
        String date, p1ID = null, p2ID = null;

        String selectGame = "SELECT TOP 3 *  FROM Game ORDER BY date DESC";

        rs = stat.executeQuery(selectGame);
        int i = 0;
        while (rs.next()) {
            wynikGras.add(new WynikGra(rs.getString("date"), rs.getString("id_player1"), rs.getString("id_player2")));
        }

        for (WynikGra wg : wynikGras) {
            p1 = getPlayer(wg.getPlayer1ID());
            p2 = getPlayer(wg.getPlayer2ID());
            data.addWynik(new Wyniki(p1, p2, wg.getDate()));
            data.fireTableDataChanged();
        }
        LOGGER.info("db.readInfo");
    }

    public Gracz getPlayer(String id) throws SQLException {
        String slelectPlayer1 = "SELECT * FROM Player WHERE id_player1=" + id;
        Gracz player = null;
        ResultSet rs = stat.executeQuery(slelectPlayer1);
        while (rs.next()) {
            player = new Gracz();
            player.setNazwa(rs.getString("name"));
            player.setWon(rs.getInt("winner") == 1);
            player.setAvatarName(rs.getString("avatarName"));
        }
        return player;
    }

    private void loadProperties() {
        Properties properties = new Properties();
        InputStream input = null;
        String propertiesName = "config.properties";
        try {
            input = getClass().getResource(propertiesName).openStream();
            properties.load(input);
            url = properties.getProperty("dbURL");
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, "properties.open", ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "properties.close", e);
                }
            }
        }
    }
}
