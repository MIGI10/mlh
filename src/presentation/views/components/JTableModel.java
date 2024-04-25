package presentation.views.components;

import presentation.Globals;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Component class that allows us to override some of the functionalities we need modified from
 * DefaultTableModel and allows for custom transactions needed in our project.
 *
 * @author Group 6
 * @version 1.0
 */
public class JTableModel extends DefaultTableModel {
    /**
     * Table that each class object manages.
     */
    JTable table;
    /**
     * Scroll containing each table.
     */
    JScrollPane scroll;
    /**
     * Object that renders each object to be added before it is added.
     */
    DefaultTableCellRenderer renderer;
    /**
     * String array containing the column names of the table
     */
    String[] columnNames;
    /**
     * Ids of the songs or playlists the tables will contain in each row.
     */
    int[] rowIds;

    /**
     * Constructor method of JTableModel.
     * Initializes the attributes of the class's object as well as the aesthetics the table will have
     * and prepares the table to be able to receive JButtons as well as strings.
     *
     * @param names column names of the table
     */
    public JTableModel(String[] names) {

        table = new JTable(this);
        table.getTableHeader().setBackground(Globals.greenSpotify);
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font("Calibri", Font.PLAIN, 15));
        table.setBackground(Color.GRAY);
        table.setRowHeight(30);
        table.setFont(new Font("Calibri", Font.PLAIN, 12));
        table.setCellSelectionEnabled(false);
        table.setShowGrid(false);
        table.setRowMargin(0);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(false);

        columnNames = names;

        scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.black);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jTable, Object object, boolean bln, boolean bln1, int row, int col) {
                if (object instanceof JButton) {
                    return (JButton) object;
                }
                return super.getTableCellRendererComponent(jTable, object.toString(), bln, false, row, col);
            }
        };
    }

    /**
     * Updates table with the new information passed to it.
     *
     * @param update all the information the table will contain after this method is run.
     */
    public void updateTable(Object[][] update){
        setDataVector(update, columnNames);
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            for (int i = 0; i < update[0].length; i++){
                table.getColumnModel().getColumn(i).setCellRenderer(renderer);
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){
        }
    }

    /**
     * Updates table with the new information passed to it as well with its ids.
     *
     * @param update all the information the table will contain after this method is run.
     * @param idsUpdate the new ids for each row
     */
    public void updateTable(Object[][] update, int[] idsUpdate){
        rowIds = idsUpdate;
        setDataVector(update, columnNames);
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            for (int i = 0; i < update[0].length; i++){
                table.getColumnModel().getColumn(i).setCellRenderer(renderer);
            }
        }
        catch (ArrayIndexOutOfBoundsException ignored){
        }
    }

    /**
     * Returns the id of the row passed.
     *
     * @param row row indicating which to return the id from that row.
     */
    public int getRowId(int row){
        return rowIds[row];
    }

    /**
     * Getter for the JTable in the object.
     *
     * @return returns the JTable in the object.
     */
    public JTable getTable(){
        return table;
    }

    /**
     * Getter for the JScrollPane in the object.
     *
     * @return returns the JScrollPane in the object.
     */
    public JScrollPane getScrollPane(){
        return scroll;
    }

    /**
     * Makes the selected column of the selected width.
     *
     * @param column number of the column whose width is to be modified.
     * @param width the column will be set to.
     */
    public void setColumnWidth(int column,  int width){
        table.getColumnModel().getColumn(column).setMaxWidth(width);
        table.getColumnModel().getColumn(column).setMinWidth(width);

    }

    /**
     * Changes the object of the cell selected to the object passed.
     *
     * @param row of the cell to be modified.
     * @param col column of the cell to be modified.
     */
    public void changeCellObject(int row, int col, Object object) {
        table.setValueAt(object, row, col);
    }

    /**
     * Makes the cell specified not editable.
     *
     * @param row of the cell to be modified.
     * @param column of the cell to be modified.
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
