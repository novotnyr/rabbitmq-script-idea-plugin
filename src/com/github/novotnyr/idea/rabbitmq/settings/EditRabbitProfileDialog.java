package com.github.novotnyr.idea.rabbitmq.settings;

import com.github.novotnyr.rabbitmqadmin.RabbitConfiguration;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class EditRabbitProfileDialog extends DialogWrapper {
    private JTextField profileNameTextField;
    private JPanel rootPanel;
    private JTextField hostTextField;
    private JFormattedTextField portTextField;
    private JTextField virtualHostTextField;
    private JTextField userTextField;
    private JPasswordField passwordTextField;
    private JComboBox<RabbitConfiguration.Protocol> protocolComboBox;
    private JCheckBox allowInsecureSslCheckBox;

    private RabbitProfile rabbitProfile;

    public EditRabbitProfileDialog(Project project, RabbitProfile rabbitProfile) {
        super(project);
        this.rabbitProfile = rabbitProfile;
        init();
        setTitle("Create RabbitMQ Profile");
    }

    public EditRabbitProfileDialog(Project project) {
        this(project, new RabbitProfile());
    }

    @Override
    protected void init() {
        super.init();
        this.profileNameTextField.setText(this.rabbitProfile.getName());
        this.hostTextField.setText(String.valueOf(this.rabbitProfile.getHost()));
        this.portTextField.setText(String.valueOf(this.rabbitProfile.getPort()));
        this.virtualHostTextField.setText(this.rabbitProfile.getVirtualHost());
        this.userTextField.setText(this.rabbitProfile.getUser());
        this.passwordTextField.setText(this.rabbitProfile.getPassword());
        this.allowInsecureSslCheckBox.setSelected(this.rabbitProfile.isAllowingInsecureTls());
        RabbitConfiguration.Protocol[] protocols = { RabbitConfiguration.Protocol.HTTP, RabbitConfiguration.Protocol.HTTPS };
        DefaultComboBoxModel<RabbitConfiguration.Protocol> protocolComboBoxModel = new DefaultComboBoxModel<RabbitConfiguration.Protocol>(protocols);
        protocolComboBoxModel.setSelectedItem(this.rabbitProfile.getProtocol());
        this.protocolComboBox.setModel(protocolComboBoxModel);
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        String text = this.portTextField.getText();
        if (!StringUtils.isNumeric(text)) {
            return new ValidationInfo("Port must be a number", this.portTextField);
        }
        return super.doValidate();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return rootPanel;
    }

    @Override
    protected void doOKAction() {
        this.rabbitProfile.setName(this.profileNameTextField.getText());
        this.rabbitProfile.setHost(this.hostTextField.getText());
        this.rabbitProfile.setPort(Integer.parseInt(this.portTextField.getText()));
        this.rabbitProfile.setVirtualHost(this.virtualHostTextField.getText());
        this.rabbitProfile.setUser(this.userTextField.getText());
        this.rabbitProfile.setPassword(this.passwordTextField.getText());
        this.rabbitProfile.setProtocol((RabbitConfiguration.Protocol) this.protocolComboBox.getSelectedItem());
        this.rabbitProfile.setAllowingInsecureTls(this.allowInsecureSslCheckBox.isSelected());
        super.doOKAction();
    }

    public RabbitProfile getRabbitProfile() {
        return this.rabbitProfile;
    }
}
