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

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.qualimetry.sonar.gherkin.analyzer.checks.CheckList;
import com.qualimetry.sonar.gherkin.analyzer.parser.FeatureParser;
import com.qualimetry.sonar.gherkin.analyzer.parser.model.FeatureFile;
import com.qualimetry.sonar.gherkin.analyzer.visitor.BaseCheck;
import com.qualimetry.sonar.gherkin.analyzer.visitor.FeatureContext;
import com.qualimetry.sonar.gherkin.analyzer.visitor.FeatureWalker;
import com.qualimetry.sonar.gherkin.analyzer.visitor.Issue;
import org.jetbrains.annotations.NotNull;
import org.sonar.check.Rule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * IntelliJ inspection that runs the shared Qualimetry Gherkin analysis engine.
 * Uses the same {@link CheckList} and {@link BaseCheck} classes as the
 * SonarQube plugin and VS Code extension.
 */
public final class GherkinInspection extends LocalInspectionTool {

    private static final FeatureParser PARSER = new FeatureParser();

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        PsiFile psiFile = holder.getFile();
        if (!(psiFile instanceof GherkinFile)) {
            return PsiElementVisitor.EMPTY_VISITOR;
        }

        GherkinAnalyzerSettings settings = GherkinAnalyzerSettings.getInstance();
        if (!settings.enabled) {
            return PsiElementVisitor.EMPTY_VISITOR;
        }

        return new PsiElementVisitor() {
            @Override
            public void visitFile(@NotNull PsiFile file) {
                runAnalysis(file, holder, settings);
            }
        };
    }

    private void runAnalysis(@NotNull PsiFile psiFile, @NotNull ProblemsHolder holder,
                             @NotNull GherkinAnalyzerSettings settings) {
        Document document = psiFile.getViewProvider().getDocument();
        if (document == null) {
            return;
        }

        String content = document.getText();
        String uri = psiFile.getVirtualFile() != null
                ? psiFile.getVirtualFile().getPath()
                : psiFile.getName();

        FeatureFile featureFile;
        try {
            featureFile = PARSER.parse(uri, content);
        } catch (IOException e) {
            return;
        }

        List<BaseCheck> checks = instantiateChecks(settings);
        if (checks.isEmpty()) {
            return;
        }

        for (BaseCheck check : checks) {
            FeatureContext context = new FeatureContext(featureFile, null, content);
            check.setContext(context);
            FeatureWalker.walk(featureFile, check);

            String ruleKey = getRuleKey(check);
            ProblemHighlightType highlightType = resolveHighlightType(ruleKey, check, settings);

            for (Issue issue : context.getIssues()) {
                registerProblem(psiFile, document, holder, issue, ruleKey, highlightType);
            }
        }
    }

    private List<BaseCheck> instantiateChecks(@NotNull GherkinAnalyzerSettings settings) {
        List<BaseCheck> checks = new ArrayList<>();
        for (Class<? extends BaseCheck> clazz : CheckList.getAllChecks()) {
            Rule rule = clazz.getAnnotation(Rule.class);
            if (rule == null) continue;

            String key = rule.key();
            if (!settings.isRuleEnabled(key)) {
                continue;
            }

            try {
                checks.add(clazz.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                // skip checks that cannot be instantiated
            }
        }
        return checks;
    }

    private ProblemHighlightType resolveHighlightType(String ruleKey, BaseCheck check,
                                                       GherkinAnalyzerSettings settings) {
        String overrideSeverity = settings.getRuleSeverity(ruleKey);
        if (overrideSeverity != null) {
            return SeverityMapper.toHighlightType(overrideSeverity);
        }
        return SeverityMapper.toHighlightType(check.getClass());
    }

    private void registerProblem(@NotNull PsiFile psiFile, @NotNull Document document,
                                  @NotNull ProblemsHolder holder,
                                  @NotNull Issue issue, @NotNull String ruleKey,
                                  @NotNull ProblemHighlightType highlightType) {
        Integer line = issue.line();
        if (line == null || line < 1 || line > document.getLineCount()) {
            return;
        }

        int lineStartOffset = document.getLineStartOffset(line - 1);

        PsiElement element = psiFile.findElementAt(lineStartOffset);
        if (element == null) {
            return;
        }

        String message = "[" + ruleKey + "] " + issue.message();
        holder.registerProblem(element, message, highlightType);
    }

    private String getRuleKey(BaseCheck check) {
        Rule rule = check.getClass().getAnnotation(Rule.class);
        return (rule != null) ? rule.key() : "unknown";
    }
}
