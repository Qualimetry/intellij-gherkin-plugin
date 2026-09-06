package com.qualimetry.intellij.gherkin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intellij.openapi.diagnostic.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads the rule catalogue bundled in the analyzer JAR.
 *
 * The catalogue ships with the plugin rather than being fetched, so a rule can be read with no
 * SonarQube connection.
 */
public final class RuleContentService {

    private static final Logger LOG = Logger.getInstance(RuleContentService.class);
    private static final String RESOURCE =
            "/com/qualimetry/sonar/gherkin/analyzer/rule-content.json";

    /** One rule, with its description already split into the sections the UI shows as tabs. */
    public record Section(String key, String title, String html) {
    }

    public record RuleContent(
            String key,
            String name,
            String severity,
            String type,
            List<String> tags,
            String remediation,
            boolean defaultActive,
            String helpUrl,
            List<Section> sections) {
    }

    private static RuleContentService instance;

    private final Map<String, RuleContent> rulesByKey;

    private RuleContentService(Map<String, RuleContent> rulesByKey) {
        this.rulesByKey = rulesByKey;
    }

    public static synchronized RuleContentService getInstance() {
        if (instance == null) {
            instance = new RuleContentService(load());
        }
        return instance;
    }

    public RuleContent get(String ruleKey) {
        return rulesByKey.get(ruleKey);
    }

    public int size() {
        return rulesByKey.size();
    }

    private static Map<String, RuleContent> load() {
        Map<String, RuleContent> rules = new LinkedHashMap<>();
        try (InputStream stream = RuleContentService.class.getResourceAsStream(RESOURCE)) {
            if (stream == null) {
                LOG.warn("Rule descriptions are unavailable: " + RESOURCE + " is not on the classpath.");
                return rules;
            }
            JsonObject root = JsonParser
                    .parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8))
                    .getAsJsonObject();

            List<String> order = strings(root.getAsJsonArray("sectionOrder"));
            JsonObject titles = root.getAsJsonObject("sectionTitles");

            for (JsonElement element : root.getAsJsonArray("rules")) {
                JsonObject rule = element.getAsJsonObject();
                String key = string(rule, "key");
                rules.put(key, new RuleContent(
                        key,
                        string(rule, "name"),
                        string(rule, "severity"),
                        string(rule, "type"),
                        strings(rule.getAsJsonArray("tags")),
                        string(rule, "remediation"),
                        rule.has("defaultActive") && rule.get("defaultActive").getAsBoolean(),
                        string(rule, "helpUrl"),
                        sections(rule.getAsJsonObject("sections"), order, titles)));
            }
        } catch (Exception e) {
            LOG.warn("Could not read the bundled rule descriptions", e);
        }
        return rules;
    }

    private static List<Section> sections(JsonObject sections, List<String> order, JsonObject titles) {
        List<Section> result = new ArrayList<>();
        if (sections == null) {
            return result;
        }
        for (String key : order) {
            String html = string(sections, key);
            if (html != null && !html.isBlank()) {
                String title = titles != null ? string(titles, key) : null;
                result.add(new Section(key, title == null ? key : title, html));
            }
        }
        return result;
    }

    private static List<String> strings(JsonArray array) {
        List<String> values = new ArrayList<>();
        if (array != null) {
            for (JsonElement element : array) {
                values.add(element.getAsString());
            }
        }
        return values;
    }

    private static String string(JsonObject object, String name) {
        return object != null && object.has(name) && object.get(name).isJsonPrimitive()
                ? object.get(name).getAsString()
                : null;
    }
}
