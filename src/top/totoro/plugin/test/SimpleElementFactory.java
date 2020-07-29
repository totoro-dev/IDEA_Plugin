package top.totoro.plugin.test;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import top.totoro.plugin.file.SwingFileType;

public class SimpleElementFactory {
    public static SimpleProperty createProperty(Project project, String name) {
        final SimpleFile file = createFile(project, name);
        return (SimpleProperty) file.getFirstChild();
    }

    public static SimpleFile createFile(Project project, String text) {
        String name = "dummy.simple";
        return (SimpleFile) PsiFileFactory.getInstance(project).
                createFileFromText(name, SwingFileType.INSTANCE, text);
    }
}