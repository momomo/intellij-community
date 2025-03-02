// Copyright 2000-2022 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.ui;

import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.completion.PlainPrefixMatcher;
import com.intellij.codeInsight.completion.PrefixMatcher;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.textCompletion.TextCompletionCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TextFieldWithAutoCompletionWithCache<T> extends TextFieldWithAutoCompletion<T> {
  private boolean myShowBottomPanel = false;

  public TextFieldWithAutoCompletionWithCache(@Nullable Project project,
                                              @NotNull TextFieldWithAutoCompletionWithCacheListProvider<T> provider,
                                              boolean showCompletionHint, @Nullable String text, boolean showBottomPanel) {
    super(project, provider, showCompletionHint, text);

    myShowBottomPanel = showBottomPanel;
  }

  @NotNull
  public static TextFieldWithAutoCompletionWithCache<String> create(@NotNull TextCompletionCache<String> cache,
                                                                    boolean prefixMatchesOnly,
                                                                    @Nullable Project project,
                                                                    @Nullable Icon icon,
                                                                    boolean showCompletionHint,
                                                                    @Nullable String text,
                                                                    boolean showBottomPanel) {
    return new TextFieldWithAutoCompletionWithCache<>(project, new StringsCompletionWithCacheProvider(cache, prefixMatchesOnly, icon), showCompletionHint, text, showBottomPanel);
  }

  @Override
  protected @NotNull EditorEx createEditor() {
    EditorEx editor = super.createEditor();
    editor.putUserData(AutoPopupController.SHOW_BOTTOM_PANEL_IN_LOOKUP_UI, myShowBottomPanel);
    return editor;
  }

  private static class StringsCompletionWithCacheProvider extends TextFieldWithAutoCompletionWithCacheListProvider<String> implements DumbAware {
    @Nullable private final Icon myIcon;
    private final boolean myPrefixMatchesOnly;

    private StringsCompletionWithCacheProvider(@NotNull TextCompletionCache<String> cache, boolean prefixMatchesOnly, @Nullable Icon icon) {
      super(cache);
      myIcon = icon;
      myPrefixMatchesOnly = prefixMatchesOnly;
    }

    @Override
    public int compare(String item1, String item2) {
      return StringUtil.compare(item1, item2, false);
    }

    @Override
    protected Icon getIcon(@NotNull String item) {
      return myIcon;
    }

    @NotNull
    @Override
    protected String getLookupString(@NotNull String item) {
      return item;
    }

    @Override
    public @Nullable PrefixMatcher createPrefixMatcher(@NotNull String prefix) {
      return new PlainPrefixMatcher(prefix, myPrefixMatchesOnly);
    }
  }
}
