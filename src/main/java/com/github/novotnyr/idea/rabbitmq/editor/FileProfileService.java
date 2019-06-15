package com.github.novotnyr.idea.rabbitmq.editor;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@State(name = "file-profile", storages = @Storage("rabbitmq-scripting.xml"))
public class FileProfileService implements PersistentStateComponent<FileProfileService.State>  {
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

    public void setProfile(String file, String profileName) {
        this.state.profileMapping.put(file, profileName);
    }

    public Optional<String> getProfile(String file) {
        return Optional.ofNullable(this.state.profileMapping.get(file));
    }

    public static class State {
        public Map<String, String> profileMapping = new HashMap<>();
    }
}
