/*
 * Copyright © 2014 jomp16 <joseoliviopedrosa@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See the COPYING file for more details.
 */

package tk.jomp16.ui.sql;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class UsersTableModel extends AbstractTableModel {
    private String[] columns = {"hostmask"};
    private List<String> hostMasks;

    public UsersTableModel() {
        hostMasks = new ArrayList<>();
    }

    public UsersTableModel(List<String> hostMasks) {
        this.hostMasks = hostMasks;
    }

    @Override
    public int getRowCount() {
        return hostMasks.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columns.getClass();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return hostMasks.get(rowIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        hostMasks.set(rowIndex, (String) aValue);

        fireTableCellUpdated(rowIndex, 0);
    }

    public void add(String hostmask) {
        hostMasks.add(hostmask);

        int lastIndex = getRowCount() - 1;

        fireTableRowsInserted(lastIndex, lastIndex);
    }

    public void remove(int rowIndex) {
        hostMasks.remove(rowIndex);

        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public List<String> getHostMasks() {
        return hostMasks;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
}
