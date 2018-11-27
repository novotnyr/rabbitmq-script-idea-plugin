package com.github.novotnyr.idea.rabbitmq.editor;

import com.github.novotnyr.idea.rabbitmq.settings.PluginSettings;
import com.github.novotnyr.idea.rabbitmq.settings.RabbitProfile;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorNotificationPanel;
import com.intellij.ui.EditorNotifications;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.YAMLFileType;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProfileNotificationProvider extends EditorNotifications.Provider {
    private static final Key<EditorNotificationPanel> KEY = Key.create("com.github.novotnyr.idea.rabbitmq.editor.ProfileNotificationProvider");

    private final Project project;
    private final FileProfileService fileProfileService;

    public ProfileNotificationProvider(Project project, FileProfileService fileProfileService) {
        this.project = project;
        this.fileProfileService = fileProfileService;
    }

    @NotNull
    @Override
    public Key getKey() {
        return KEY;
    }

    @Nullable
    @Override
    public JComponent createNotificationPanel(@NotNull VirtualFile virtualFile, @NotNull FileEditor fileEditor) {
        if (!(virtualFile.getFileType() instanceof YAMLFileType)) {
            return null;
        }
        RabbitMqProfileNotificationPanel panel = new RabbitMqProfileNotificationPanel(this.project, this.fileProfileService, virtualFile) {
            @Override
            protected void onRabbitProfileChanged(RabbitProfile rabbitProfile) {
                ProfileNotificationProvider.this.fileProfileService.setProfile(virtualFile.getPath(), rabbitProfile.getName());
            }
        };
        panel.setText("RabbitMQ");
        return panel;
    }

    public static class RabbitMqProfileNotificationPanel extends EditorNotificationPanel {
        private final FileProfileService fileProfileService;

        public RabbitMqProfileNotificationPanel(Project project, FileProfileService fileProfileService, VirtualFile virtualFile) {
            this.fileProfileService = fileProfileService;
            PluginSettings pluginSettings = ServiceManager.getService(project, PluginSettings.class);
            pluginSettings.getRabbitProfiles();

            RabbitProfileComboBoxModel model = new RabbitProfileComboBoxModel(project) {
                @Override
                protected void fireSelectionChanged(RabbitProfile selectedRabbitProfile) {
                    RabbitMqProfileNotificationPanel.this.onRabbitProfileChanged(selectedRabbitProfile);
                }
            };
            Optional<String> profile = fileProfileService.getProfile(virtualFile.getPath());
            profile.ifPresent(model::setSelectedItem);
            ComboBox profileComboBox = new ComboBox(model);
            addToLayout(profileComboBox);
        }

        private void addToLayout(ComboBox switcher) {
            LayoutManager layout = getLayout();
            if (layout instanceof BorderLayout) {
                BorderLayout borderLayout = (BorderLayout) layout;
                for (int i = 0; i < getComponentCount(); i++) {
                    Component component = getComponent(i);
                    if (component instanceof JPanel) {
                        JPanel panel = (JPanel) component;
                        // kill all components and replace it with one large combobox
                        panel.removeAll();
                        panel.add(switcher);
                    }
                }
            }
        }

        protected void onRabbitProfileChanged(RabbitProfile rabbitProfile) {
            // do  nothing
        }
    }

    public static class RabbitProfileComboBoxModel extends AbstractListModel<String> implements ComboBoxModel<String>  {
        private final Project project;

        private RabbitProfile selectedRabbitProfile = new ImplicitRabbitProfile();

        public RabbitProfileComboBoxModel(Project project) {
            this.project = project;
        }

        @Override
        public int getSize() {
            return getRabbitProfiles().size();
        }

        @Override
        public String getElementAt(int index) {
            return render(getRabbitProfiles().get(index));
        }

        @Override
        public void setSelectedItem(Object anItem) {
            for (RabbitProfile rabbitProfile : getRabbitProfiles()) {
                if (rabbitProfile.getName().equals(anItem)) {
                    this.selectedRabbitProfile = rabbitProfile;
                    fireSelectionChanged(this.selectedRabbitProfile);
                }
            }
        }

        protected void fireSelectionChanged(RabbitProfile selectedRabbitProfile) {
            // do nothing
        }

        @Override
        public String getSelectedItem() {
            if (this.selectedRabbitProfile == null) {
                if (getRabbitProfiles().isEmpty()) {
                    return null;
                } else {
                    this.selectedRabbitProfile = getRabbitProfiles().get(0);
                }
            }
            return render(this.selectedRabbitProfile);
        }

        private List<RabbitProfile> getRabbitProfiles() {
            PluginSettings pluginSettings = ServiceManager.getService(project, PluginSettings.class);
            List<RabbitProfile> rabbitProfiles = new ArrayList<>(pluginSettings.getRabbitProfiles());
            rabbitProfiles.add(0, ImplicitRabbitProfile.INSTANCE);
            return rabbitProfiles;
        }

        private String render(RabbitProfile rabbitProfile) {
            return rabbitProfile.getName().toString();
        }


    }

    public static class ImplicitRabbitProfile extends RabbitProfile {
        public static final ImplicitRabbitProfile INSTANCE = new ImplicitRabbitProfile();

        @Override
        public String getName() {
            return "[default] Pick Up From File";
        }
    }
}
