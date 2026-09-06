package com.qualimetry.intellij.gherkin;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.jcef.JBCefApp;
import com.intellij.ui.jcef.JBCefBrowser;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;

/**
 * Renders a rule description as HTML.
 *
 * JCEF gives full styling but is absent from some JetBrains runtimes, so a Swing editor pane
 * takes over there rather than the panel showing nothing.
 */
public final class RuleDescriptionPanel implements Disposable {

    private final JPanel root = new JPanel(new BorderLayout());
    private final JBCefBrowser browser;
    private final JEditorPane fallback;

    public RuleDescriptionPanel() {
        if (JBCefApp.isSupported()) {
            browser = new JBCefBrowser();
            fallback = null;
            root.add(browser.getComponent(), BorderLayout.CENTER);
        } else {
            browser = null;
            fallback = new JEditorPane();
            fallback.setEditable(false);
            fallback.setContentType("text/html");
            fallback.setBorder(JBUI.Borders.empty(8));
            JScrollPane scroll = new JScrollPane(fallback);
            scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            root.add(scroll, BorderLayout.CENTER);
        }
        showEmptyState();
    }

    public JComponent getComponent() {
        return root;
    }

    @Override
    public void dispose() {
        if (browser != null) {
            Disposer.dispose(browser);
        }
    }

    public void showEmptyState() {
        render("<p>Select a Qualimetry Gherkin finding to read its rule.</p>");
    }

    public void show(RuleContentService.RuleContent rule) {
        if (rule == null) {
            showEmptyState();
            return;
        }
        render(RuleDescriptionHtml.render(rule, editorBackground(), editorForeground(), linkColor()));
    }

    private void render(String bodyHtml) {
        String document = RuleDescriptionHtml.wrap(
                bodyHtml, editorBackground(), editorForeground(), linkColor());
        if (browser != null) {
            browser.loadHTML(document);
        } else {
            fallback.setText(document);
            fallback.setCaretPosition(0);
        }
    }

    private static String editorBackground() {
        return toHex(UIUtil.getPanelBackground());
    }

    private static String editorForeground() {
        return toHex(UIUtil.getLabelForeground());
    }

    private static String linkColor() {
        return toHex(UIUtil.getLabelForeground());
    }

    private static String toHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
}
