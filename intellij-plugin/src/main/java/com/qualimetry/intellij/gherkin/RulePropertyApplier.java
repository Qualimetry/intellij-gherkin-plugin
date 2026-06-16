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

import com.qualimetry.sonar.gherkin.analyzer.visitor.BaseCheck;
import org.sonar.check.RuleProperty;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Applies imported quality-profile parameter values onto a check's
 * {@code @RuleProperty}-annotated fields.
 */
final class RulePropertyApplier {

    private RulePropertyApplier() {
    }

    static void apply(BaseCheck check, Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return;
        }
        for (Class<?> clazz = check.getClass(); clazz != null && clazz != Object.class; clazz = clazz.getSuperclass()) {
            for (Field field : clazz.getDeclaredFields()) {
                RuleProperty property = field.getAnnotation(RuleProperty.class);
                if (property == null) {
                    continue;
                }
                String paramKey = property.key() == null || property.key().isEmpty()
                        ? field.getName()
                        : property.key();
                String value = params.get(paramKey);
                if (value == null) {
                    value = params.get(field.getName());
                }
                if (value != null) {
                    setField(check, field, value);
                }
            }
        }
    }

    private static void setField(BaseCheck check, Field field, String value) {
        field.setAccessible(true);
        try {
            Class<?> type = field.getType();
            if (type == int.class || type == Integer.class) {
                field.set(check, Integer.parseInt(value.trim()));
            } else if (type == long.class || type == Long.class) {
                field.set(check, Long.parseLong(value.trim()));
            } else if (type == boolean.class || type == Boolean.class) {
                field.set(check, Boolean.parseBoolean(value.trim()));
            } else if (type == double.class || type == Double.class) {
                field.set(check, Double.parseDouble(value.trim()));
            } else if (type == String.class) {
                field.set(check, value);
            }
        } catch (ReflectiveOperationException | NumberFormatException e) {
            // invalid imported value; the check keeps its default
        }
    }
}
