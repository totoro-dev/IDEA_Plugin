package top.totoro.plugin.file;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.ModuleListener;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class SwingModuleListener implements ModuleListener {
    @Override
    public void moduleAdded(@NotNull Project project, @NotNull Module module) {
        System.out.println("module "+module.getModuleFilePath());
    }
}
