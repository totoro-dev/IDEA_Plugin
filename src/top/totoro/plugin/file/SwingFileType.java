package top.totoro.plugin.file;

import com.intellij.icons.AllIcons;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class SwingFileType extends LanguageFileType {

    public static final SwingFileType INSTANCE = new SwingFileType();

    public static class SwingLanguage extends Language {
        public static final SwingLanguage INSTANCE = new SwingLanguage();

        private SwingLanguage() {
            super("Swing");
        }
    }

    public SwingFileType() {
        super(SwingLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "xml";
    }

    @NotNull
    @Override
    public String getName() {
        return "Swing";
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getDescription() {
        return "Swing";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "xml";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return AllIcons.FileTypes.Xml;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Nullable
    @Override
    public String getCharset(@NotNull VirtualFile virtualFile, @NotNull byte[] bytes) {
        System.out.println(virtualFile.getPath()+", "+virtualFile.getExtension());
        if (Objects.equals(virtualFile.getExtension(), "xml")) {
            if (SwingProjectInfo.getSwingProject(virtualFile.getPath()) == null) return "UTF-8";
            String data = new String(bytes, StandardCharsets.UTF_8);
            String[] ids = data.split("id *= *\"");
            for (int i = 1; i < ids.length; i++) {
                ids[i] = ids[i].substring(0, ids[i].indexOf("\""));
            }
            System.out.println("project : " + SwingProjectInfo.getSwingProject(virtualFile.getPath()).getProject().getBasePath());
            SwingRFileCreator.createIdGroup(SwingProjectInfo.getSwingProject(virtualFile.getPath()).getProject().getBasePath(), null);
        }
        return "UTF-8";
    }
}
