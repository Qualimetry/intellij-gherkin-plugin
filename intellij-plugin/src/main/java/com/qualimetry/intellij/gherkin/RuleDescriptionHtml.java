package com.qualimetry.intellij.gherkin;

import java.util.List;

/**
 * Builds the HTML document shown for a rule. Kept free of Swing and JCEF types so the markup
 * can be asserted directly.
 */
public final class RuleDescriptionHtml {

    private RuleDescriptionHtml() {
    }

    public static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    public static String render(
            RuleContentService.RuleContent rule,
            String background,
            String foreground,
            String linkColor) {
        StringBuilder html = new StringBuilder();
        html.append("<h1>").append(escape(rule.name())).append("</h1>");
        html.append("<p class=\"meta\"><code>").append(escape(rule.key())).append("</code>")
                .append(" &middot; ").append(escape(rule.severity()))
                .append(" &middot; ").append(escape(rule.type().replace('_', ' ')));
        if (!rule.tags().isEmpty()) {
            html.append(" &middot; ").append(escape(String.join(", ", rule.tags())));
        }
        html.append("</p>");
        html.append("<p class=\"meta\">")
                .append(rule.defaultActive() ? "Enabled in the recommended profile" : "Not enabled by default");
        if (rule.remediation() != null) {
            html.append(" &middot; about ").append(escape(rule.remediation())).append(" to fix");
        }
        html.append("</p>");

        List<RuleContentService.Section> sections = rule.sections();
        for (RuleContentService.Section section : sections) {
            html.append("<h2 class=\"section\">").append(escape(section.title())).append("</h2>");
            html.append(section.html());
        }
        return html.toString();
    }

    /**
     * Colours are passed in from the running IDE theme so the panel does not stay light while
     * the rest of the IDE is dark.
     */
    public static String wrap(String bodyHtml, String background, String foreground, String linkColor) {
        return "<html><head><style>"
                + "body { background:" + background + "; color:" + foreground
                + "; font-family: sans-serif; font-size: 12px; padding: 4px 10px; line-height: 1.45; }"
                + "h1 { font-size: 15px; margin-bottom: 2px; }"
                + "h2.section { font-size: 13px; margin-top: 16px; border-bottom: 1px solid " + foreground + "40; }"
                + "h2 { font-size: 12px; margin: 10px 0 4px; }"
                + ".meta { opacity: 0.75; margin: 2px 0; }"
                + "pre { background:" + foreground + "14; padding: 8px; overflow-x: auto; font-family: monospace; }"
                + "code { font-family: monospace; }"
                + "table { border-collapse: collapse; }"
                + "th, td { border: 1px solid " + foreground + "40; padding: 3px 7px; text-align: left; }"
                + "a { color:" + linkColor + "; }"
                + "</style></head><body>" + bodyHtml + "</body></html>";
    }
}
