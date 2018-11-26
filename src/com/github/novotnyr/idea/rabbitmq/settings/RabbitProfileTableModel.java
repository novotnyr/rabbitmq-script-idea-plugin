package com.github.novotnyr.idea.rabbitmq.settings;

import javax.swing.table.AbstractTableModel;
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
                return profiles.getPassword();
            case PROTOCOL:
                return profiles.getProtocol();
            default:
                return "N/A";
        }
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

    public void setRabbitProfiles(List<RabbitProfile> rabbitProfiles) {
        this.configurations.clear();
        this.configurations.addAll(rabbitProfiles);
    }


}
