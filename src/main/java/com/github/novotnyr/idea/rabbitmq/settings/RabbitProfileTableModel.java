package com.github.novotnyr.idea.rabbitmq.settings;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

public class RabbitProfileTableModel extends AbstractTableModel {
    public enum Column {
        NAME("Profile"),
        HOST("Host"),
        PORT("Port"),
        VHOST("Virtual Host"),
        USER("User"),
        PASSWORD("Password"),
        PROTOCOL("Protocol");

        private String description;

        Column(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
    private List<RabbitProfile> configurations = new ArrayList<>();

    @Override
    public int getRowCount() {
        return this.configurations.size();
    }

    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        RabbitProfile profiles = this.configurations.get(rowIndex);
        Column column = Column.values()[columnIndex];
        switch (column) {
            case NAME:
                return profiles.getName();
            case HOST:
                return profiles.getHost();
            case PORT:
                return profiles.getPort();
            case VHOST:
                return profiles.getVirtualHost();
            case USER:
                return profiles.getUser();
            case PASSWORD:
                return new Password(profiles.getPassword());
            case PROTOCOL:
                return profiles.getProtocol();
            default:
                return "N/A";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        Column column = Column.values()[columnIndex];
        switch (column) {
            case NAME:
            case HOST:
            case VHOST:
            case PORT:
            case USER:
            case PROTOCOL:
                return String.class;
            case PASSWORD:
                return Password.class;
            default:
                return super.getColumnClass(columnIndex);
        }

    }

    @Override
    public String getColumnName(int column) {
        return Column.values()[column].getDescription();
    }

    public RabbitProfile getRabbitProfile(int index) {
        return this.configurations.get(index);
    }

    public void add(RabbitProfile rabbitProfile) {
        this.configurations.add(rabbitProfile);
        fireTableDataChanged();
    }

    public void removeAtIndex(int selectedRow) {
        this.configurations.remove(selectedRow);
        fireTableDataChanged();
    }

    public List<RabbitProfile> getRabbitProfiles() {
        return this.configurations;
    }

    public void setRabbitProfile(int index, RabbitProfile rabbitProfile) {
        this.configurations.set(index, rabbitProfile);
    }

    public void setRabbitProfiles(List<RabbitProfile> rabbitProfiles) {
        this.configurations.clear();
        this.configurations.addAll(rabbitProfiles);
        fireTableDataChanged();
    }

    public static class Password {
        private String password;

        public Password(String password) {
            this.password = password;
        }

        public String getPassword() {
            return password;
        }

        public boolean isPresent() {
            return password != null && password.length() > 0;
        }
    }

    public static class PasswordTableCellRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Password password = (Password) value;
            boolean hasPassword = password != null && password.isPresent();
            return table.getDefaultRenderer(Boolean.class)
                    .getTableCellRendererComponent(table, hasPassword, isSelected, hasFocus, row, column);
        }

    }
}
