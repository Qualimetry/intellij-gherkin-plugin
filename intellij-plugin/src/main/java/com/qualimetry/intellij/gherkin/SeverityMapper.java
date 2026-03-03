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

import com.intellij.codeInspection.ProblemHighlightType;
import com.qualimetry.sonar.gherkin.analyzer.visitor.BaseCheck;
import org.sonar.check.Priority;
import org.sonar.check.Rule;

/**
 * Maps Sonar {@link Priority} from the engine's {@code @Rule} annotation
 * to IntelliJ {@link ProblemHighlightType}.
 */
public final class SeverityMapper {

    private SeverityMapper() {
    }

    public static ProblemHighlightType toHighlightType(Class<? extends BaseCheck> checkClass) {
        Rule rule = checkClass.getAnnotation(Rule.class);
        if (rule == null) {
            return ProblemHighlightType.WARNING;
        }
        return fromPriority(rule.priority());
    }

    public static ProblemHighlightType toHighlightType(String sonarSeverity) {
        if (sonarSeverity == null || sonarSeverity.isBlank()) {
            return ProblemHighlightType.WARNING;
        }
        return switch (sonarSeverity.trim().toLowerCase()) {
            case "blocker", "critical" -> ProblemHighlightType.ERROR;
            case "major" -> ProblemHighlightType.WARNING;
            case "minor" -> ProblemHighlightType.WEAK_WARNING;
            case "info" -> ProblemHighlightType.INFORMATION;
            default -> ProblemHighlightType.WARNING;
        };
    }

    public static ProblemHighlightType fromPriority(Priority priority) {
        if (priority == null) {
            return ProblemHighlightType.WARNING;
        }
        return switch (priority) {
            case BLOCKER, CRITICAL -> ProblemHighlightType.ERROR;
            case MAJOR -> ProblemHighlightType.WARNING;
            case MINOR -> ProblemHighlightType.WEAK_WARNING;
            case INFO -> ProblemHighlightType.INFORMATION;
        };
    }
}
