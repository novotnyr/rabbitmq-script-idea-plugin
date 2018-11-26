package com.github.novotnyr.idea.rabbitmq.settings;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.Nullable;

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

    private RabbitProfile rabbitProfile;

    protected EditRabbitProfileDialog(@Nullable Project project) {
        super(project);
        this.rabbitProfile = new RabbitProfile();
        init();
        setTitle("Create RabbitMQ Profile");
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
        super.doOKAction();
    }

    public RabbitProfile getRabbitProfile() {
        return this.rabbitProfile;
    }
}
