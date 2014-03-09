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

public class ChannelTableModel extends AbstractTableModel {
    private String[] columns = {"Channel"};
    private List<String> channels;

    public ChannelTableModel() {
        this.channels = new ArrayList<>();
    }

    public ChannelTableModel(List<String> channels) {
        this.channels = channels;
    }

    @Override
    public int getRowCount() {
        return channels.size();
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
        return channels.get(rowIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        channels.set(rowIndex, (String) aValue);

        fireTableCellUpdated(rowIndex, 0);
    }

    public void add(String channel) {
        channels.add(channel);

        int lastIndex = getRowCount() - 1;

        fireTableRowsInserted(lastIndex, lastIndex);
    }

    public void remove(int rowIndex) {
        channels.remove(rowIndex);

        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    public List<String> getChannels() {
        return channels;
    }
}
