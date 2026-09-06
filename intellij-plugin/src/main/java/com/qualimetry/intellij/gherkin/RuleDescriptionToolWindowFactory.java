package com.qualimetry.intellij.gherkin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public final class RuleDescriptionToolWindowFactory implements ToolWindowFactory {

    public static final String TOOL_WINDOW_ID = "Qualimetry Gherkin Rule";

    private static final Map<Project, RuleDescriptionPanel> PANELS = new ConcurrentHashMap<>();

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        RuleDescriptionPanel panel = PANELS.computeIfAbsent(project, p -> new RuleDescriptionPanel());
        Content content = ContentFactory.getInstance().createContent(panel.getComponent(), "", false);
        content.setDisposer(() -> {
            PANELS.remove(project, panel);
            Disposer.dispose(panel);
        });
        toolWindow.getContentManager().addContent(content);
    }

    /**
     * Opens the tool window on the rule, creating its content on first use. Focus stays in the
     * editor so reading a rule does not interrupt typing.
     */
    public static void show(Project project, String ruleKey) {
        RuleContentService.RuleContent rule = RuleContentService.getInstance().get(ruleKey);
        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(TOOL_WINDOW_ID);
        if (toolWindow == null) {
            return;
        }
        toolWindow.activate(() -> {
            RuleDescriptionPanel panel = PANELS.get(project);
            if (panel != null) {
                panel.show(rule);
            }
        }, false);
    }
}
