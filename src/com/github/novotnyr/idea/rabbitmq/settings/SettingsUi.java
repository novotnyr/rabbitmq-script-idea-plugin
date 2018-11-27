package com.github.novotnyr.idea.rabbitmq.settings;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.BaseConfigurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.DoubleClickListener;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import java.awt.event.MouseEvent;

public class SettingsUi extends BaseConfigurable implements DumbAware {

    private final Project project;

    private JPanel rootPanel;
    private JPanel tablePanel;

    private JBTable table;

    private RabbitProfileTableModel profileTableModel = new RabbitProfileTableModel();

    public SettingsUi(Project project) {
        this.project = project;
        this.tablePanel.add(new JBScrollPane(this.table));

        this.table.setModel(this.profileTableModel);
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "RabbitMQ Script";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return this.rootPanel;
    }

    @Override
    public void reset() {
        PluginSettings pluginSettings = ServiceManager.getService(this.project, PluginSettings.class);
        this.profileTableModel.setRabbitProfiles(pluginSettings.getRabbitProfiles());
    }

    @Override
    public void apply() throws ConfigurationException {
        PluginSettings pluginSettings = ServiceManager.getService(this.project, PluginSettings.class);
        pluginSettings.setRabbitProfiles(this.profileTableModel.getRabbitProfiles());
    }

    private void createUIComponents() {
        this.table = new JBTable();
        this.table.setAutoCreateRowSorter(true);
        this.table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        this.table.setDefaultRenderer(RabbitProfileTableModel.Password.class, new RabbitProfileTableModel.PasswordTableCellRenderer());

        this.tablePanel = ToolbarDecorator.createDecorator(this.table)
                .setAddAction(this::onAddAction)
                .setRemoveAction(this::onRemoveAction)
                .setRemoveActionUpdater(this::removeActionUpdate)
                .setEditAction(this::onEditAction)
                .disableUpDownActions()
                .createPanel();
        new DoubleClickListener() {
            @Override
            protected boolean onDoubleClick(MouseEvent mouseEvent) {
                onEditAction(null);
                return true;
            }
        }.installOn(this.table);
    }

    private void onAddAction(AnActionButton anActionButton) {
        EditRabbitProfileDialog dialog = new EditRabbitProfileDialog(this.project);
        if (dialog.showAndGet()) {
            RabbitProfile rabbitProfile = dialog.getRabbitProfile();
            this.profileTableModel.add(rabbitProfile);
            setModified(true);
        }
    }

    private void onRemoveAction(AnActionButton anActionButton) {
        this.profileTableModel.removeAtIndex(this.table.getSelectedRow());
        setModified(true);
    }

    private boolean removeActionUpdate(AnActionEvent event) {
        return table.getSelectedRow() > -1;
    }

    private void onEditAction(@Nullable AnActionButton anActionButton) {
        int selectedRow = this.table.getSelectedRow();
        RabbitProfile rabbitProfile = this.profileTableModel.getRabbitProfile(selectedRow);

        EditRabbitProfileDialog dialog = new EditRabbitProfileDialog(this.project, rabbitProfile);
        if (dialog.showAndGet()) {
            RabbitProfile updatedRabbitProfile = dialog.getRabbitProfile();
            this.profileTableModel.setRabbitProfile(selectedRow, updatedRabbitProfile);
            setModified(true);
        }
    }


}
