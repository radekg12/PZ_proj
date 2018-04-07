package pl.edu.wat.wcy.pz.checkers;

import pl.edu.wat.wcy.pz.actions.ChangeLanguageAction;
import pl.edu.wat.wcy.pz.actions.ExitAction;
import pl.edu.wat.wcy.pz.actions.SaveAction;
import pl.edu.wat.wcy.pz.events.*;
import pl.edu.wat.wcy.pz.frame.MainFrame;
import pl.edu.wat.wcy.pz.listeners.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CheckersGame extends JComponent implements EndOfTimeListener, ReplayListener, ChangeLanguageListener {
    private static final Logger LOGGER = Logger.getLogger(CheckersGame.class.getSimpleName(), "LogsMessages");
    private int boardSize;
    private Piece activePiece;
    private Piece[][] pieceTab;
    private Square[][] board;
    private Player player1, player2, currentPlayer, currentOpponent;
    private boolean start;
    private ArrayList<ChangeTurnListener> changeTurnListeners = new ArrayList<>();
    private ArrayList<WinListener> winListeners = new ArrayList<>();
    private AbstractAction loadAction;


    public CheckersGame(MainFrame frame, Player player1, Player player2) {
        loadAction = new AbstractAction(null, new ImageIcon(ExitAction.class.getClassLoader().getResource("icons/load_24.png"))) {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadGame();
            }
        };
        frame.getMenu().getFile().add(new SaveAction());
        SaveAction.setCheckersGame(this);
        frame.getMenu().getFile().add(loadAction);
        ChangeLanguageAction.addChangeLanguageListener(this);
        loadProperties();
        pieceTab = new Piece[boardSize][boardSize];
        board = new Square[boardSize][boardSize];
        addMouseListener(new Movements());
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int x = Math.min(getWidth(), getHeight()) / boardSize;
                Piece.setSize(x);
                Square.setSize(x);
                repaint();
            }
        });
        this.player1 = player1;
        this.player2 = player2;
        currentPlayer = player2;
        currentOpponent = player1;
        generateBoard();
        placePieces();
        checkPiecesMove();
        start = true;
        repaint();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public CheckersGame setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        return this;
    }

    public Player getCurrentOpponent() {
        return currentOpponent;
    }

    public CheckersGame setCurrentOpponent(Player currentOpponent) {
        this.currentOpponent = currentOpponent;
        return this;
    }

    private void generateBoard() {
        for (int i = 0; i < boardSize; i++)
            for (int j = 0; j < boardSize; j++)
                if ((i + j) % 2 == 1) board[i][j] = new Square(i, j, Square.getDarkColor());
                else board[i][j] = new Square(i, j, Square.getLightColor());
    }

    private void placePieces() {
        for (int j = 0; j < boardSize / 2 - 1; j++)
            for (int i = 0; i < boardSize; i++)
                if ((i + j) % 2 == 1) addPiece(i, j, currentOpponent);

        for (int j = 1; j <= boardSize / 2 - 1; j++)
            for (int i = 1; i <= boardSize; i++)
                if ((i + j) % 2 == 1) addPiece(boardSize - i, boardSize - j, currentPlayer);

    }

    private void addPiece(int x, int y, Player player) {
        Piece p = new Piece(x, y, player.getK());
        pieceTab[x][y] = p;
        player.addPiece(p);
    }

    private void checkQueenCondition(Piece piece) {
        if (!isOnBoard(piece.getX(), piece.getY() - piece.getK()))
            piece.changeToQueen();
    }

    private void setActiveSquares(Piece piece) {
        cleanActiveSquares();
        if ((piece.getPieceType() == PieceType.LQueen || piece.getPieceType() == PieceType.DQueen))
            setActiveSquaresForQueen(piece);
        //if (piece == null) return;
        int x = piece.getX() + 1;
        int y = piece.getY() - piece.getK();
        if (isOnBoard(x, y)) {
            if (!isOccupied(x, y)) {
                board[piece.getX() + 1][piece.getY() - piece.getK()].setMoveSquare(true);
                piece.setMovePiece(true);
            } else if (isOnBoard(x + 1, y - piece.getK()) && !isOccupied(x + 1, y - piece.getK()) && isOpponent(x, y, piece)) {
                board[piece.getX() + 2][piece.getY() - 2 * piece.getK()].setJumpSquare(true);
                piece.setJumpPiece(true);
            }
            //moze bic do tylu
            y = piece.getY() + piece.getK();
            if (isOnBoard(x, y) && isOccupied(x, y))
                if (isOnBoard(x + 1, y + piece.getK()) && isOpponent(x, y, piece) && !isOccupied(x + 1, y + piece.getK())) {
                    board[piece.getX() + 2][piece.getY() + 2 * piece.getK()].setJumpSquare(true);
                    piece.setJumpPiece(true);
                }
        }

        x = piece.getX() - 1;
        y = piece.getY() - piece.getK();
        if (isOnBoard(x, y)) {
            if (!isOccupied(x, y)) {
                board[piece.getX() - 1][piece.getY() - piece.getK()].setMoveSquare(true);
                piece.setMovePiece(true);
            } else if (isOnBoard(x - 1, y - piece.getK()) && isOpponent(x, y, piece) && !isOccupied(x - 1, y - piece.getK())) {
                board[piece.getX() - 2][piece.getY() - 2 * piece.getK()].setJumpSquare(true);
                piece.setJumpPiece(true);
            }
            //moze bic w tyl
            y = piece.getY() + piece.getK();
            if (isOnBoard(x, y) && isOccupied(x, y))
                if (isOnBoard(x - 1, y + piece.getK()) && !isOccupied(x - 1, y + piece.getK()) && isOpponent(x, y, piece)) {
                    board[piece.getX() - 2][piece.getY() + 2 * piece.getK()].setJumpSquare(true);
                    piece.setJumpPiece(true);
                }
        }
    }

    private void setActiveSquaresForQueen(Piece piece) {
        int x, y;
        for (x = piece.getX() + 1, y = piece.getY() + 1; x < 8 && y < 8; x++, y++)
            if (!hasMove(piece, x, y, 1, 1)) break;
        for (x = piece.getX() + 1, y = piece.getY() - 1; x < 8 && y >= 0; x++, y--)
            if (!hasMove(piece, x, y, 1, -1)) break;
        for (x = piece.getX() - 1, y = piece.getY() + 1; x >= 0 && y < 8; x--, y++)
            if (!hasMove(piece, x, y, -1, 1)) break;
        for (x = piece.getX() - 1, y = piece.getY() - 1; x >= 0 && y >= 0; x--, y--)
            if (!hasMove(piece, x, y, -1, -1)) break;
    }

    private boolean hasMove(Piece piece, int x, int y, int vx, int vy) {
        if (pieceTab[x][y] == null) {
            board[x][y].setMoveSquare(true);
            piece.setMovePiece(true);
        } else if (isOpponent(x, y, piece) && isOnBoard(x + vx, y + vy) && !isOccupied(x + vx, y + vy)) {
            board[x + vx][y + vy].setJumpSquare(true);
            piece.setJumpPiece(true);
            return false;
        } else return false;
        return true;
    }

    private void cleanActiveSquares() {
        for (Square[] pp : board)
            for (Square p : pp) {
                p.setMoveSquare(false);
                p.setJumpSquare(false);
            }
    }

    private void cleanMoveSquares() {
        for (Square[] pp : board)
            for (Square p : pp) {
                p.setMoveSquare(false);
            }
    }

    private void cleanActivePieces() {
        for (Piece p : currentOpponent.getPieces()) {
            p.setMovePiece(false);
            p.setJumpPiece(false);
        }
    }

    private boolean isOpponent(int x, int y, Piece piece) {
        return piece != null && pieceTab[x][y].getK() != piece.getK();
    }

    private boolean isOccupied(int x, int y) {
        return pieceTab[x][y] != null;
    }

    private boolean isOnBoard(int x, int y) {
        return x >= 0 && x <= boardSize - 1 && y >= 0 && y <= boardSize - 1;
    }

    private void changeTurn() {
        Player tmp = currentPlayer;
        currentPlayer = currentOpponent;
        currentOpponent = tmp;
        checkPiecesMove();
        if (!isEndOfGame())
            fireChangeTurnEvent();

    }

    private boolean isEndOfGame() {
        if (currentPlayer.getPieces().isEmpty() || !(playerHasMove() || playerHasJump())) {
            start = false;
            currentOpponent.setWon(true);
            fireWinEvent();
            return true;
        }
        return false;
    }

    private void checkPiecesMove() {
        cleanActivePieces();
        for (Piece p : currentPlayer.getPieces())
            setActiveSquares(p);
        cleanActiveSquares();
        if (playerHasJump())
            currentPlayer.getPieces().stream().filter(p -> p.isMovePiece() && !p.isJumpPiece()).forEach(p -> p.setMovePiece(false));
    }

    public void paint(Graphics g) {
        super.paintComponents(g);
        paintBoard(g);
        paintPieces(g);
    }

    private void paintPieces(Graphics g) {
        if (activePiece != null) {
            g.setColor(activePiece.getColor());
            g.fillOval(activePiece.getX() * Piece.getSize() - 3, activePiece.getY() * Piece.getSize() - 3, Piece.getSize() + 6, Piece.getSize() + 6);
        }

        ArrayList<Piece> pieces = new ArrayList<>(currentPlayer.getPieces());
        pieces.addAll(currentOpponent.getPieces());
        for (Piece p : pieces) {
            if (p.isJumpPiece()) {
                g.setColor(Piece.getJumpColor());
                g.fillOval(p.getX() * Piece.getSize(), p.getY() * Piece.getSize(), Piece.getSize(), Piece.getSize());
                g.setColor(p.getActiveColor());
                g.fillOval(p.getX() * Piece.getSize() + 3, p.getY() * Piece.getSize() + 3, Piece.getSize() - 6, Piece.getSize() - 6);
            } else {
                g.setColor(p.isMovePiece() ? p.getActiveColor() : p.getColor());
                g.fillOval(p.getX() * Piece.getSize(), p.getY() * Piece.getSize(), Piece.getSize(), Piece.getSize());
            }
            if (p.getPieceType() == PieceType.LQueen || p.getPieceType() == PieceType.DQueen)
                g.drawImage(Piece.getCrown(), p.getX() * Piece.getSize(), p.getY() * Piece.getSize(), Piece.getSize(), Piece.getSize(), null);

        }
    }

    private boolean playerHasJump() {
        return currentPlayer.getPieces().stream().anyMatch(Piece::isJumpPiece);
    }

    private boolean playerHasMove() {
        return currentPlayer.getPieces().stream().anyMatch(Piece::isMovePiece);
    }

    private void paintBoard(Graphics g) {
        for (Square[] pp : board)
            for (Square p : pp) {
                g.setColor(p.getSquareColor());
                g.fillRect(p.getColumn() * Square.getSize(), p.getRow() * Square.getSize(), Square.getSize(), Square.getSize());
                if (p.isMoveSquare()) {
                    g.setColor(Square.getMoveColor());
                    g.fillRect(p.getColumn() * Square.getSize(), p.getRow() * Square.getSize(), Square.getSize(), Square.getSize());
                    g.setColor(p.getSquareColor());
                    g.fillRect(p.getColumn() * Square.getSize() + 2, p.getRow() * Square.getSize() + 2, Square.getSize() - 4, Square.getSize() - 4);
                }
                if (p.isJumpSquare()) {
                    g.setColor(Square.getJumpColor());
                    g.fillRect(p.getColumn() * Square.getSize(), p.getRow() * Square.getSize(), Square.getSize(), Square.getSize());
                    g.setColor(p.getSquareColor());
                    g.fillRect(p.getColumn() * Square.getSize() + 2, p.getRow() * Square.getSize() + 2, Square.getSize() - 4, Square.getSize() - 4);
                }

            }
    }

    public int getBoardSize() {
        return boardSize;
    }

    public CheckersGame setBoardSize(int boardSize) {
        this.boardSize = boardSize;
        return this;
    }

    public Piece getActivePiece() {
        return activePiece;
    }

    public CheckersGame setActivePiece(Piece activePiece) {
        this.activePiece = activePiece;
        return this;
    }

    public Piece[][] getPieceTab() {
        return pieceTab;
    }

    public CheckersGame setPieceTab(Piece[][] pieceTab) {
        this.pieceTab = pieceTab;
        return this;
    }

    public Square[][] getBoard() {
        return board;
    }

    public CheckersGame setBoard(Square[][] board) {
        this.board = board;
        return this;
    }

    public Player getPlayer1() {
        return player1;
    }

    public CheckersGame setPlayer1(Player player1) {
        this.player1 = player1;
        return this;
    }

    public Player getPlayer2() {
        return player2;
    }

    public CheckersGame setPlayer2(Player player2) {
        this.player2 = player2;
        return this;
    }

    public boolean isStart() {
        return start;
    }

    public CheckersGame setStart(boolean start) {
        this.start = start;
        return this;
    }

    public ArrayList<ChangeTurnListener> getChangeTurnListeners() {
        return changeTurnListeners;
    }

    public CheckersGame setChangeTurnListeners(ArrayList<ChangeTurnListener> changeTurnListeners) {
        this.changeTurnListeners = changeTurnListeners;
        return this;
    }

    public ArrayList<WinListener> getWinListeners() {
        return winListeners;
    }

    public CheckersGame setWinListeners(ArrayList<WinListener> winListeners) {
        this.winListeners = winListeners;
        return this;
    }

    private void jump(int i, int j) {
        int vi = (activePiece.getX() - i) / Math.abs(activePiece.getX() - i);
        int vj = (activePiece.getY() - j) / Math.abs(activePiece.getY() - j);
        pieceTab[activePiece.getX()][activePiece.getY()] = null;
        currentOpponent.getPieces().remove(pieceTab[i + vi][j + vj]);
        pieceTab[i + vi][j + vj] = null;
        activePiece.setLocation(i, j);
        pieceTab[i][j] = activePiece;
        activePiece.setJumpPiece(false);
        //repaint(); ////
        checkQueenCondition(activePiece);
        checkPiecesMove();
        currentPlayer.movesInc();
        if (!activePiece.isJumpPiece()) {
            activePiece = null;
            changeTurn();
            //repaint(); ////
        } else {
            setActiveSquares(activePiece);
            cleanMoveSquares();
            activePiece.setMovePiece(false);
            repaint();
        }
        repaint();
    }

    private void myMove(int i, int j) {
        pieceTab[activePiece.getX()][activePiece.getY()] = null;
        activePiece.setLocation(i, j);
        pieceTab[i][j] = activePiece;
        checkQueenCondition(activePiece);
        activePiece = null;
        currentPlayer.movesInc();
        changeTurn();
        repaint();
    }

    public synchronized void addWinListener(WinListener l) {
        winListeners.add(l);
    }

    public synchronized void removeWinListener(WinListener l) {
        winListeners.remove(l);
    }

    private synchronized void fireWinEvent() {
        WinEvent event = new WinEvent(this, currentOpponent, currentPlayer);
        for (WinListener l : winListeners) {
            l.win(event);
        }
    }

    public synchronized void addChangeTurnListener(ChangeTurnListener l) {
        changeTurnListeners.add(l);
    }

    public synchronized void removeChangeTurnListener(ChangeLanguageListener l) {
        changeTurnListeners.remove(l);
    }

    private synchronized void fireChangeTurnEvent() {
        ChangeTurnEvent event = new ChangeTurnEvent(this, currentPlayer);
        for (ChangeTurnListener l : changeTurnListeners) l.changeTurn(event);
    }


    @Override
    public void endOfTime(EndOfTimeEvent event) {
        start = false;
        currentOpponent.setWon(true);
        fireWinEvent();
    }

    @Override
    public void replay(ReplayEvent event) {
        activePiece = null;
        player1.getPieces().clear();
        player2.getPieces().clear();
        currentPlayer = player2;
        currentOpponent = player1;
        pieceTab = new Piece[boardSize][boardSize];
        board = new Square[boardSize][boardSize];
        generateBoard();
        placePieces();
        checkPiecesMove();
        if (currentPlayer.getK() == 1) fireChangeTurnEvent();
        start = true;
        repaint();
    }

    public void loadGame() {
        //TODO
        try {
            FileInputStream fileIn = new FileInputStream("game.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            pieceTab = (Piece[][]) in.readObject();
            in.close();
            fileIn.close();

//            fileIn = new FileInputStream("player.ser");
//            in = new ObjectInputStream(fileIn);
//            currentPlayer = (Player) in.readObject();
//            in.close();
//            fileIn.close();
//
//            fileIn = new FileInputStream("opponent.ser");
//            in = new ObjectInputStream(fileIn);
//            currentOpponent = (Player) in.readObject();
//            in.close();
//            fileIn.close();

        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
            return;
        }
        player1.getPieces().clear();
        player2.getPieces().clear();
        for (Piece[] pp : pieceTab)
            for (Piece p : pp) {
                if (p != null)
                    if (p.getK() == -1) player1.getPieces().add(p);
                    else player2.getPieces().add(p);
            }
        activePiece = null;
        checkPiecesMove();
        repaint();
    }


    private void loadProperties() {
        Properties properties = new Properties();
        InputStream input = null;
        String propertiesName = "config.properties";
        try {
            input = getClass().getClassLoader().getResource(propertiesName).openStream();

            properties.load(input);
            boardSize = Integer.parseInt(properties.getProperty("board.boardSize"));
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

    @Override
    public void changeLocal(ChangeLanguageEvent event) {
        SwingUtilities.invokeLater(() -> {
            ResourceBundle rb = event.getRb();
            loadAction.putValue(Action.NAME, rb.getString("load"));
        });
    }

    private class Movements extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            int i = e.getX() / Square.getSize();
            int j = e.getY() / Square.getSize();
            if (isOnBoard(i, j) && start)
                if (activePiece != null && activePiece.isJumpPiece() && board[i][j].isJumpSquare())
                    jump(i, j);
                else if (activePiece != null && activePiece.isMovePiece() && board[i][j].isMoveSquare())
                    myMove(i, j);
                else {
                    if (pieceTab[i][j] != null && pieceTab[i][j].getK() == currentPlayer.getK() && (pieceTab[i][j].isMovePiece())) {
                        activePiece = pieceTab[i][j];
                        setActiveSquares(activePiece);
                        if (activePiece.isJumpPiece()) cleanMoveSquares();
                    }
                    repaint();
                }
        }
    }
}
