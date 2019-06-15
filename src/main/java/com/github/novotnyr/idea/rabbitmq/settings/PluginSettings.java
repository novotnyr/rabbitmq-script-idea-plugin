package com.github.novotnyr.idea.rabbitmq.settings;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.apache.commons.beanutils.BeanUtils;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@State(name = PluginSettings.NAME, storages = @Storage("rabbitmq-scripting.xml"))
public class PluginSettings implements PersistentStateComponent<PluginSettings.State> {
    public static final String NAME = "settings";

    private State state = new State();

    private PasswordSafe passwordSafe = PasswordSafe.getInstance();

    public static final String NO_PASSWORD = "";

    @Nullable
    @Override
    public State getState() {
        State persistedState = new State();
        for (RabbitProfile rabbitProfile : this.state.rabbitProfiles) {
            RabbitProfile persistedRabbitProfile = new RabbitProfile();
            persistedRabbitProfile.setName(rabbitProfile.getName());
            copy(rabbitProfile, persistedRabbitProfile);
            persistedRabbitProfile.setPassword(NO_PASSWORD);
            persistedState.getRabbitProfiles().add(persistedRabbitProfile);

            CredentialAttributes credentialAttributes = getCredentialAttributes(rabbitProfile);
            this.passwordSafe.setPassword(credentialAttributes, rabbitProfile.getPassword());
        }

        return persistedState;
    }

    private void copy(RabbitProfile rabbitProfile, RabbitProfile persistedRabbitProfile) {
        try {
            BeanUtils.copyProperties(persistedRabbitProfile.getRabbitConfiguration(), rabbitProfile.getRabbitConfiguration());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("Cannot load persistent Rabbit Profile", e);
        }
    }

    @Override
    public void loadState(State persistedState) {
        initializeAccessTokens(persistedState);
        this.state = persistedState;
    }

    public List<RabbitProfile> getRabbitProfiles() {
        State state = this.state;
        if (state == null || state.rabbitProfiles == null) {
            return new ArrayList<>();
        }
        return state.rabbitProfiles;
    }

    public void setRabbitProfiles(List<RabbitProfile> rabbitProfiles) {
        this.state.rabbitProfiles = rabbitProfiles;
    }

    public Optional<RabbitProfile> findProfileByName(String name) {
        for (RabbitProfile rabbitProfile : getRabbitProfiles()) {
            if (rabbitProfile.getName().equals(name)) {
                return Optional.of(rabbitProfile);
            }
        }
        return Optional.empty();
    }

    private void initializeAccessTokens(State persistedState) {
        for (RabbitProfile rabbitProfile : persistedState.rabbitProfiles) {
            CredentialAttributes credentialAttributes = getCredentialAttributes(rabbitProfile);
            Credentials credentials = this.passwordSafe.get(credentialAttributes);
            if (credentials == null) {
                continue;
            }
            String passwordAsString = credentials.getPasswordAsString();
            rabbitProfile.setPassword(passwordAsString);
        }
    }

    private CredentialAttributes getCredentialAttributes(RabbitProfile rabbitProfile) {
        String serviceName = NAME + ":" + rabbitProfile.getName();
        String userName = rabbitProfile.getName();
        return new CredentialAttributes(serviceName, userName, this.getClass(), false);
    }

    public static class State {
        private List<RabbitProfile> rabbitProfiles = new ArrayList<>();

        public List<RabbitProfile> getRabbitProfiles() {
            return rabbitProfiles;
        }

        public void setRabbitProfiles(List<RabbitProfile> rabbitProfiles) {
            this.rabbitProfiles = rabbitProfiles;
        }
    }


}
