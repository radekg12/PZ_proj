package pl.edu.wat.wcy.pz.database;

import pl.edu.wat.wcy.pz.checkers.Player;
import pl.edu.wat.wcy.pz.exceptions.TooShortGameException;
import pl.edu.wat.wcy.pz.score.Score;
import pl.edu.wat.wcy.pz.score.ScoreGame;
import pl.edu.wat.wcy.pz.score.ScoreTableModel;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    private static final Logger LOGGER = Logger.getLogger(Database.class.getSimpleName(), "LogsMessages");
    private Connection conn;
    private Statement stat;
    private String url;

    public Database() {
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

    public void saveScore(Player p1, Player p2) throws TooShortGameException, SQLException {
        ResultSet rs;
        int generatedKey1 = -1, generatedKey2 = -1;
        if ((p1.getMoves() + p2.getMoves()) < 5)
            throw new TooShortGameException();
        String hexColor = "#" + Integer.toHexString(p1.getColor().getRGB()).substring(2).toUpperCase();
        String insertPlayer1 = "INSERT INTO PZ.dbo.Player (name, color, avatarName, numberOfMoves, time, winner) " +
                "VALUES (\'" + p1.getName() + "\', \'" + hexColor + "\', \'" + p1.getAvatarName() + "\', " + p1.getMoves() + ", " + p1.getTime() + ", " + (p1.isWon() ? 1 : 0) + ")";

        hexColor = "#" + Integer.toHexString(p2.getColor().getRGB()).substring(2).toUpperCase();
        String insertPlayer2 = "INSERT INTO PZ.dbo.Player (name, color, avatarName, numberOfMoves, time, winner) " +
                "VALUES (\'" + p2.getName() + "\', \'" + hexColor + "\', \'" + p2.getAvatarName() + "\', " + p2.getMoves() + ", " + p2.getTime() + ", " + (p2.isWon() ? 1 : 0) + ")";

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

    public void checkScore(ScoreTableModel data) throws SQLException {
        ResultSet rs;
        ArrayList<ScoreGame> scoreGames = new ArrayList<>();
        ArrayList<Score> scores = new ArrayList<>();
        Player p1, p2;
        String date, p1ID = null, p2ID = null;

        String selectGame = "SELECT TOP 4 *  FROM Game ORDER BY date DESC";

        rs = stat.executeQuery(selectGame);
        int i = 0;
        while (rs.next()) {
            scoreGames.add(new ScoreGame(rs.getString("date"), rs.getString("id_player1"), rs.getString("id_player2")));
        }

        for (ScoreGame wg : scoreGames) {
            p1 = getPlayer(wg.getPlayer1ID());
            p2 = getPlayer(wg.getPlayer2ID());
            data.addScore(new Score(p1, p2, wg.getDate()));
            data.fireTableDataChanged();
        }
        LOGGER.info("db.readInfo");
    }

    public Player getPlayer(String id) throws SQLException {
        String slelectPlayer1 = "SELECT * FROM Player WHERE id_player1=" + id;
        Player player = null;
        ResultSet rs = stat.executeQuery(slelectPlayer1);
        while (rs.next()) {
            player = new Player();
            player.setName(rs.getString("name"));
            player.setWon(rs.getInt("winner") == 1);
            player.setAvatarName(rs.getString("avatarName"));
            player.setColor(Color.decode(rs.getString("color")));
            player.setTime(rs.getInt("time"));
            player.setMoves(rs.getInt("numberOfMoves"));
        }
        return player;
    }

    private void loadProperties() {
        Properties properties = new Properties();
        InputStream input = null;
        String propertiesName = "config.properties";
        try {
            input = getClass().getClassLoader().getResource(propertiesName).openStream();
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
