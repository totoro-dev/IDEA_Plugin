package top.totoro.plugin.file;

import com.intellij.icons.AllIcons;
import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.JavaDocElementType;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class SwingFileType extends LanguageFileType {
    private static final String TAG = SwingFileType.class.getSimpleName();

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
        Log.d(TAG, "getCharset()");
//        for (VirtualFile child : virtualFile.getChildren()) {
//            if (virtualFile.getPath().contains("src/main/resources")) {
//                if (!Objects.equals(virtualFile.getExtension(), "xml")) continue;
//                SwingProjectInfo project = SwingProjectInfo.getSwingProject(virtualFile.getPath());
//                if (project == null) continue;
//                String swingFilePath = virtualFile.getPath();
//                Log.d(TAG, "swingFilePath : " + swingFilePath);
//                if (swingFilePath.lastIndexOf("src/main") > 0) {
//                    String modulePath = swingFilePath.substring(0, swingFilePath.lastIndexOf("src/main"));
//                    if (SwingRFileCreator.checkFileHasLoad(modulePath, swingFilePath)) continue;
//                }
//                try {
//                    String contents = new String(child.contentsToByteArray(), StandardCharsets.UTF_8);
//                    extractCharsetFromFileContent(project.getProject(), virtualFile, contents.subSequence(0, contents.length()));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                try {
//                    getCharset(child, child.contentsToByteArray());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

//        if (Objects.equals(virtualFile.getExtension(), "xml") && virtualFile.getPath().contains("src/main/resources")) {
//            if (SwingProjectInfo.getSwingProject(virtualFile.getPath()) == null) return "UTF-8";
//            String swingFilePath = virtualFile.getPath();
//            Log.d(TAG, "swingFilePath : " + swingFilePath);
//            if (swingFilePath.lastIndexOf("src/main") > 0) {
//                String modulePath = swingFilePath.substring(0, swingFilePath.lastIndexOf("src/main"));
//                if (SwingRFileCreator.checkFileHasLoad(modulePath, swingFilePath)) return "UTF-8";
//                Log.d(TAG, "project : " + modulePath);
//                String data = null;
//                try {
//                    data = new String(virtualFile.contentsToByteArray(), StandardCharsets.UTF_8);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return "UTF-8";
//                }
//                String[] ids = data.split("id *= *\"");
//                if (ids.length > 0) {
//                    String[] finalIds = new String[ids.length - 1];
//                    for (int i = 1; i < ids.length; i++) {
//                        finalIds[i - 1] = ids[i].substring(0, ids[i].indexOf("\""));
//                        Log.d(TAG, "id " + (i - 1) + " : " + finalIds[i - 1]);
//                    }
//                    SwingRFileCreator.createIdGroup(modulePath, new File(swingFilePath), finalIds);
//                }
//            }
//            return "UTF-8";
//        }
        return super.getCharset(virtualFile, bytes);
    }

    @Override
    public Charset extractCharsetFromFileContent(@Nullable Project project, @Nullable VirtualFile virtualFile, @NotNull CharSequence content) {
        Log.d(TAG, "extractCharsetFromFileContent()");
        if (virtualFile == null) return super.extractCharsetFromFileContent(project, null, content);
        if (Objects.equals(virtualFile.getExtension(), "xml") && virtualFile.getPath().contains("src/main/resources")) {
            if (SwingProjectInfo.getSwingProject(virtualFile.getPath()) == null)
                return super.extractCharsetFromFileContent(project, virtualFile, content);
            String swingFilePath = virtualFile.getPath();
            Log.d(TAG, "swingFilePath : " + swingFilePath);
            if (swingFilePath.lastIndexOf("src/main") > 0) {
                String modulePath = swingFilePath.substring(0, swingFilePath.lastIndexOf("src/main"));
                Log.d(TAG, "project : " + modulePath);
                String data = content.toString();
                String[] ids = data.split("id *= *\"");
                if (ids.length > 0) {
                    String[] finalIds = new String[ids.length - 1];
                    for (int i = 1; i < ids.length; i++) {
                        finalIds[i - 1] = ids[i].substring(0, ids[i].indexOf("\""));
                    }
                    SwingRFileCreator.createIdGroup(modulePath, new File(swingFilePath), finalIds);
                }
            }
        }
        return super.extractCharsetFromFileContent(project, virtualFile, content);
    }

    @Override
    public boolean isSecondary() {
        Log.d(TAG, "isSecondary()");
        return super.isSecondary();
    }
}
