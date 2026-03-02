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

import com.intellij.lexer.LexerBase;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Minimal lexer that treats the entire file as a single code token.
 * Gherkin parsing is handled by the shared analysis engine; this lexer
 * exists only to satisfy the IntelliJ parser definition requirement.
 */
public final class GherkinLexer extends LexerBase {

    private CharSequence buffer;
    private int startOffset;
    private int endOffset;
    private boolean atEnd;

    @Override
    public void start(@NotNull CharSequence buffer, int startOffset, int endOffset, int initialState) {
        this.buffer = buffer;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.atEnd = (startOffset >= endOffset);
    }

    @Override
    public int getState() {
        return 0;
    }

    @Nullable
    @Override
    public IElementType getTokenType() {
        return atEnd ? null : GherkinTokenType.CODE;
    }

    @Override
    public int getTokenStart() {
        return startOffset;
    }

    @Override
    public int getTokenEnd() {
        return endOffset;
    }

    @Override
    public void advance() {
        atEnd = true;
    }

    @NotNull
    @Override
    public CharSequence getBufferSequence() {
        return buffer;
    }

    @Override
    public int getBufferEnd() {
        return endOffset;
    }
}
