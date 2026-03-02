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

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

/**
 * Minimal parser definition for Gherkin feature files. The actual analysis
 * is performed by the shared Qualimetry engine via the inspection; this
 * definition exists to register the Gherkin language so IntelliJ treats
 * .feature files as first-class citizens.
 */
public final class GherkinParserDefinition implements ParserDefinition {

    private static final IFileElementType FILE_ELEMENT_TYPE =
            new IFileElementType(GherkinLanguage.INSTANCE);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new GherkinLexer();
    }

    @NotNull
    @Override
    public PsiParser createParser(Project project) {
        return (root, builder) -> {
            var marker = builder.mark();
            while (!builder.eof()) {
                builder.advanceLexer();
            }
            marker.done(GherkinElementType.FILE);
            return builder.getTreeBuilt();
        };
    }

    @NotNull
    @Override
    public IFileElementType getFileNodeType() {
        return FILE_ELEMENT_TYPE;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return new com.intellij.psi.impl.source.tree.LeafPsiElement(node.getElementType(), node.getText());
    }

    @NotNull
    @Override
    public PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new GherkinFile(viewProvider);
    }
}
