import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WynikiTableModel extends AbstractTableModel {
    private static final Logger LOGGER = Logger.getLogger(WynikiTableModel.class.getSimpleName(), "LogsMessages");
    private ArrayList<Wyniki> wynikis;
    private ResourceBundle rb;

    public WynikiTableModel(ArrayList<Wyniki> wynikis, OknoGlowne frame) {
        rb = frame.getMenu().getRb();
        this.wynikis = wynikis;
    }


    public void addWynik(Wyniki country) {
        if (this.wynikis != null) {
            this.wynikis.add(country);
        }
    }

    @Override
    public int getRowCount() {
        return wynikis.size();
    }

    @Override
    public int getColumnCount() {
        return Wyniki.getFieldsCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value = null;
        try {
            value = wynikis.get(rowIndex).getColumnValue(columnIndex);
        } catch (IllegalAccessException e) {
            LOGGER.log(Level.WARNING, "score.columnValue", e);
        }
        return value;
    }

    @Override
    public String getColumnName(int column) {
        return rb.getString(Wyniki.getColumnName(column));
    }
}
