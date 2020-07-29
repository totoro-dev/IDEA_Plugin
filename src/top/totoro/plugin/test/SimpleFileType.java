package top.totoro.plugin.test;
// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.totoro.plugin.file.Log;
import top.totoro.plugin.file.SwingProjectInfo;
import top.totoro.plugin.file.SwingResGroupCreator;

import javax.swing.*;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class SimpleFileType extends LanguageFileType {
    private static final String TAG = SimpleFileType.class.getSimpleName();

    public static final SimpleFileType INSTANCE = new SimpleFileType();

    private SimpleFileType() {
        super(SimpleLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getName() {
        return "Swing File";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Swing language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "swing";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return SimpleIcons.FILE;
    }

    @Override
    public String getCharset(@NotNull VirtualFile file, @NotNull byte[] content) {
        if (file.getPath().contains("src/main/resources")) {
            if (!Objects.equals(file.getExtension(), getDefaultExtension())) return StandardCharsets.UTF_8.name();
        }
        return super.getCharset(file, content);
    }

    @Override
    public Charset extractCharsetFromFileContent(@Nullable Project project, @Nullable VirtualFile virtualFile, @NotNull CharSequence content) {
        Log.d(TAG, "extractCharsetFromFileContent()");
        if (virtualFile == null) return super.extractCharsetFromFileContent(project, null, content);
        if (Objects.equals(virtualFile.getExtension(), getDefaultExtension()) && virtualFile.getPath().contains("src/main/resources")) {
            if (SwingProjectInfo.getSwingProject(virtualFile.getPath()) == null)
                return super.extractCharsetFromFileContent(project, virtualFile, content);
            String swingFilePath = virtualFile.getPath();
            Log.d(TAG, "swingFilePath : " + swingFilePath);
            if (swingFilePath.lastIndexOf("src/main") > 0) {
                String modulePath = swingFilePath.substring(0, swingFilePath.lastIndexOf("src/main"));
                Log.d(TAG, "project : " + modulePath);
                SwingResGroupCreator.createResGroup(modulePath, new File(swingFilePath), content.toString());
                virtualFile.refresh(false, true);
            }
        }
        return super.extractCharsetFromFileContent(project, virtualFile, content);
    }

}