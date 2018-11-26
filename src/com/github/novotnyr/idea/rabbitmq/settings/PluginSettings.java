package com.github.novotnyr.idea.rabbitmq.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@State(name = "settings", storages = @Storage("rabbitmq-scripting.xml"))
public class PluginSettings implements PersistentStateComponent<PluginSettings.State> {
    private State state = new State();

    @Nullable
    @Override
    public State getState() {
        return this.state;
    }

    @Override
    public void loadState(State state) {
        this.state = state;
    }

    public List<RabbitProfile> getRabbitProfiles() {
        State state = this.getState();
        if (state == null || state.rabbitProfiles == null) {
            return new ArrayList<>();
        }
        return state.rabbitProfiles;
    }

    public void setRabbitProfiles(List<RabbitProfile> rabbitProfiles) {
        this.getState().rabbitProfiles = rabbitProfiles;
    }

    public Optional<RabbitProfile> findProfileByName(String name) {
        for (RabbitProfile rabbitProfile : getRabbitProfiles()) {
            if (rabbitProfile.getName().equals(name)) {
                return Optional.of(rabbitProfile);
            }
        }
        return Optional.empty();
    }

    public static class State {
        public List<RabbitProfile> rabbitProfiles = new ArrayList<>();
    }


}
