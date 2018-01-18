package pl.edu.wat.wcy.pz.score;

import pl.edu.wat.wcy.pz.actions.ChangeLanguageAction;
import pl.edu.wat.wcy.pz.frame.MainFrame;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScoreTableModel extends AbstractTableModel {
    private static final Logger LOGGER = Logger.getLogger(ScoreTableModel.class.getSimpleName(), "LogsMessages");
    private ArrayList<Score> scores;
    private ResourceBundle rb;

    public ScoreTableModel(ArrayList<Score> scores, MainFrame frame) {
        rb = ChangeLanguageAction.getRb();
        this.scores = scores;
    }


    public void addWynik(Score country) {
        if (this.scores != null) {
            this.scores.add(country);
        }
    }

    @Override
    public int getRowCount() {
        return scores.size();
    }

    @Override
    public int getColumnCount() {
        return Score.getFieldsCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value = null;
        try {
            value = scores.get(rowIndex).getColumnValue(columnIndex);
        } catch (IllegalAccessException e) {
            LOGGER.log(Level.WARNING, "score.columnValue", e);
        }
        return value;
    }

    @Override
    public String getColumnName(int column) {
        return rb.getString(Score.getColumnName(column));
    }
}
