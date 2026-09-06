package com.qualimetry.intellij.gherkin;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Both IDE clients must authenticate the same way. This client previously sent only a Bearer
 * header, so a server or proxy that accepts just the Basic form worked in VS Code and failed here.
 */
class SonarQubeAuthParityTest {

    private static SonarQubeImportService service(String token) {
        return new SonarQubeImportService("https://sonar.example.test", token);
    }

    @Test
    void sendsTheTokenAsABearerHeader() {
        assertEquals("Bearer squ_abc123", service("squ_abc123").bearerAuth());
    }

    @Test
    void fallsBackToBasicWithTheTokenAsUsernameAndAnEmptyPassword() {
        String expected = "Basic " + Base64.getEncoder()
                .encodeToString("squ_abc123:".getBytes(StandardCharsets.UTF_8));
        assertEquals(expected, service("squ_abc123").basicAuth());
    }

    @Test
    void sendsNoAuthorizationWhenThereIsNoToken() {
        assertNull(service("").bearerAuth());
        assertNull(service("").basicAuth());
        assertNull(service(null).bearerAuth());
        assertNull(service(null).basicAuth());
    }

    @Test
    void surroundingWhitespaceInAPastedTokenIsIgnored() {
        assertEquals("Bearer squ_abc123", service("  squ_abc123  ").bearerAuth());
    }

    @Test
    void normalizesServerUrlsTheSameWayTheVsCodeClientDoes() {
        assertEquals("https://sonar.example.test", SonarQubeImportService.normalizeUrl("sonar.example.test"));
        assertEquals("https://sonar.example.test", SonarQubeImportService.normalizeUrl("https://sonar.example.test/"));
        assertEquals("http://localhost:9000", SonarQubeImportService.normalizeUrl("http://localhost:9000///"));
        assertEquals("https://sonar.example.test", SonarQubeImportService.normalizeUrl("  sonar.example.test  "));
    }
}
