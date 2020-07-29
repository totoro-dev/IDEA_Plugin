package top.totoro.plugin.test;
// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.ui.Messages;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import top.totoro.plugin.file.Log;

public class SimpleCompletionContributor extends CompletionContributor {

    public SimpleCompletionContributor() {
        Log.d("SimpleCompletionContributor", "SimpleCompletionContributor init");
        Log.d("SimpleCompletionContributor", "condition : " + PlatformPatterns.psiElement(SimpleTypes.VALUE));
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(SimpleTypes.VALUE),
                new CompletionProvider<CompletionParameters>() {
                    public void addCompletions(@NotNull CompletionParameters parameters,
                                               @NotNull ProcessingContext context,
                                               @NotNull CompletionResultSet resultSet) {
                        Log.d("SimpleCompletionContributor", "addCompletions() params : " + parameters.getCompletionType().name());
                        Log.d("SimpleCompletionContributor", "addCompletions() resultSet : " + resultSet.isStopped());
                        resultSet.addElement(LookupElementBuilder.create("Hello"));
                        Log.d("SimpleCompletionContributor", "end");
                    }
                }
        );
    }
}
