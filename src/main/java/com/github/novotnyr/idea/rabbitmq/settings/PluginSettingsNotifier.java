package com.github.novotnyr.idea.rabbitmq.settings;

import com.intellij.util.messages.Topic;

public interface PluginSettingsNotifier {
    Topic<PluginSettingsNotifier> PLUGIN_SETTINGS_APPLIED_TOPIC = Topic.create("RabbitMQ Script Settings changed", PluginSettingsNotifier.class);

    void notifyPluginSettingsApplied();
}
