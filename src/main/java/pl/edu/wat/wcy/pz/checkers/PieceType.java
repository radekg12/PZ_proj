package pl.edu.wat.wcy.pz.checkers;

public enum PieceType {
    Light(1), Dark(-1), LQueen(2), DQueen(-2),;

    private int id;

    PieceType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
