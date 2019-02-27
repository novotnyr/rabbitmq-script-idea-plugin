package com.github.novotnyr.idea.rabbitmq.settings;

import com.github.novotnyr.rabbitmqadmin.RabbitConfiguration;

public class RabbitProfile {
    private String name;

    private RabbitConfiguration rabbitConfiguration = new RabbitConfiguration();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RabbitConfiguration getRabbitConfiguration() {
        return rabbitConfiguration;
    }

    public void setRabbitConfiguration(RabbitConfiguration rabbitConfiguration) {
        this.rabbitConfiguration = rabbitConfiguration;
    }

    public String getHost() {
        return this.rabbitConfiguration.getHost();
    }

    public int getPort() {
        return this.rabbitConfiguration.getPort();
    }

    public String getVirtualHost() {
        return this.rabbitConfiguration.getVirtualHost();
    }

    public String getUser() {
        return this.rabbitConfiguration.getUser();
    }

    public String getPassword() {
        return this.rabbitConfiguration.getPassword();
    }

    public RabbitConfiguration.Protocol getProtocol() {
        return this.rabbitConfiguration.getProtocol();
    }

    public void setPort(int port) {
        this.rabbitConfiguration.setPort(port);
    }

    public void setHost(String host) {
        this.rabbitConfiguration.setHost(host);
    }

    public void setVirtualHost(String virtualHost) {
        this.rabbitConfiguration.setVirtualHost(virtualHost);
    }

    public void setUser(String user) {
        this.rabbitConfiguration.setUser(user);
    }

    public void setPassword(String password) {
        this.rabbitConfiguration.setPassword(password);
    }

    public void setProtocol(RabbitConfiguration.Protocol protocol) {
        this.rabbitConfiguration.setProtocol(protocol);
    }

    public boolean isAllowingInsecureTls() {
        return this.rabbitConfiguration.isAllowingInsecureTls();
    }

    public void setAllowingInsecureTls(boolean allowingInsecureTls) {
        this.rabbitConfiguration.setAllowingInsecureTls(allowingInsecureTls);
    }


}
