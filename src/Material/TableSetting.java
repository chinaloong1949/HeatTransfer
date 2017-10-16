/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Material;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author yk
 */
public class TableSetting implements TableCellRenderer{

    private int row;
    private int column;
    private Color color;
    private JTable table;
    private TableCellRenderer tableCellRenderer;

    public TableSetting() {

    }

    public void setColor(JTable table, int row, int column, Color color) {
        this.table = table;
        this.row = row;
        this.column = column;
        this.color = color;
        this.tableCellRenderer = table.getCellRenderer(row, column);
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        Component renderer =tableCellRenderer.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);
        if (row==this.row&&column==this.column) 
        {
            renderer.setBackground(color);
        } 
        return renderer;
    }

}
