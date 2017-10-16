/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author yk
 */
public class SpecialTable extends JTable {


    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        if (row >= 2 && column == 5) {
            return new DefaultCellEditor(new JComboBox());
        }
        return super.getCellEditor();
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        if (row >= 2 && column == 5) {
            return new TableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table,
                        Object value, boolean isSelected, boolean hasFocus,
                        int row, int column) {
                    return new JComboBox();
                }
            };
        }
        return super.getCellRenderer(row, column);
    }

}
