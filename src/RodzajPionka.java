public enum RodzajPionka {
    Jasny(1), Ciemny(-1), JDama(2), CDama(-2),;

    public int id;
    RodzajPionka(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
