/*
 * Copyright 2026 SHAZAM Analytics Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qualimetry.intellij.gherkin;

import com.intellij.util.ui.FormBuilder;

import javax.swing.*;

/**
 * Settings panel for the Qualimetry Gherkin Analyzer plugin.
 */
final class GherkinAnalyzerSettingsPanel {

    private final JCheckBox enabledCheckBox;
    private final JPanel mainPanel;

    GherkinAnalyzerSettingsPanel() {
        enabledCheckBox = new JCheckBox("Enable Qualimetry Gherkin Analyzer");
        enabledCheckBox.setSelected(true);

        mainPanel = FormBuilder.createFormBuilder()
                .addComponent(enabledCheckBox)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
    }

    JComponent getComponent() {
        return mainPanel;
    }

    boolean isEnabled() {
        return enabledCheckBox.isSelected();
    }

    void setEnabled(boolean enabled) {
        enabledCheckBox.setSelected(enabled);
    }
}
