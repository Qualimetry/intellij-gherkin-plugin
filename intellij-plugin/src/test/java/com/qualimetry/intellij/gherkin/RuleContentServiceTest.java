package com.qualimetry.intellij.gherkin;

import com.qualimetry.sonar.gherkin.analyzer.checks.CheckList;
import org.junit.jupiter.api.Test;
import org.sonar.check.Rule;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The bundled catalogue is what the tool window renders, so it has to stay in step with the
 * rules the analyzer actually ships.
 */
class RuleContentServiceTest {

    private static List<String> analyzerRuleKeys() {
        List<String> keys = new ArrayList<>();
        for (Class<?> check : CheckList.getAllChecks()) {
            Rule rule = check.getAnnotation(Rule.class);
            if (rule != null) {
                keys.add(rule.key());
            }
        }
        return keys;
    }

    @Test
    void everyAnalyzerRuleHasBundledContent() {
        RuleContentService service = RuleContentService.getInstance();
        List<String> missing = new ArrayList<>();
        for (String key : analyzerRuleKeys()) {
            if (service.get(key) == null) {
                missing.add(key);
            }
        }
        assertTrue(missing.isEmpty(), "rules with no bundled description: " + missing);
    }

    @Test
    void catalogueCoversTheWholeRuleSet() {
        assertEquals(analyzerRuleKeys().size(), RuleContentService.getInstance().size());
    }

    @Test
    void everyRuleExplainsTheProblemAndTheFix() {
        RuleContentService service = RuleContentService.getInstance();
        List<String> incomplete = new ArrayList<>();
        for (String key : analyzerRuleKeys()) {
            List<RuleContentService.Section> sections = service.get(key).sections();
            boolean cause = sections.stream().anyMatch(s -> s.key().equals("root_cause"));
            boolean fix = sections.stream().anyMatch(s -> s.key().equals("how_to_fix"));
            if (!cause || !fix) {
                incomplete.add(key);
            }
        }
        assertTrue(incomplete.isEmpty(), "rules missing a core section: " + incomplete);
    }

    @Test
    void sectionsCarryDisplayTitlesRatherThanRawKeys() {
        RuleContentService.RuleContent rule =
                RuleContentService.getInstance().get(analyzerRuleKeys().get(0));
        assertNotNull(rule);
        for (RuleContentService.Section section : rule.sections()) {
            assertNotEqualsIgnoringCase(section.key(), section.title());
            assertFalse(section.title().isBlank());
        }
    }

    @Test
    void renderedHtmlEscapesRuleMetadataAndKeepsAuthoredMarkup() {
        RuleContentService.RuleContent rule =
                RuleContentService.getInstance().get(analyzerRuleKeys().get(0));
        String html = RuleDescriptionHtml.render(rule, "#fff", "#000", "#00f");

        assertTrue(html.contains(RuleDescriptionHtml.escape(rule.name())));
        assertTrue(html.contains(rule.key()));
        // The authored description is trusted markup and must survive rendering.
        assertTrue(html.contains("<h2"));
    }

    @Test
    void escapingProtectsAgainstMetadataThatLooksLikeMarkup() {
        assertEquals("&lt;b&gt;x&lt;/b&gt;", RuleDescriptionHtml.escape("<b>x</b>"));
        assertEquals("a &amp; b", RuleDescriptionHtml.escape("a & b"));
        assertEquals("", RuleDescriptionHtml.escape(null));
    }

    private static void assertNotEqualsIgnoringCase(String unexpected, String actual) {
        assertFalse(unexpected.equalsIgnoreCase(actual),
                "section title should be human readable, got: " + actual);
    }
}
