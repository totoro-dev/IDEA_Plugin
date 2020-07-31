package top.totoro.plugin.action;

import com.intellij.ide.util.newProjectWizard.AbstractProjectWizard;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ProjectBuilder;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ui.configuration.DefaultModulesProvider;
import com.intellij.openapi.roots.ui.configuration.ModulesConfigurator;
import com.intellij.openapi.roots.ui.configuration.actions.NewModuleAction;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.totoro.plugin.file.Log;
import top.totoro.plugin.file.SwingResGroupCreator;
import top.totoro.plugin.ui.NewSwingModuleWrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static top.totoro.plugin.constant.Constants.DEFAULT_MAIN_ACTIVITY_CONTENT;
import static top.totoro.plugin.constant.Constants.DEFAULT_SWING_FILE_CONTENT;

public class NewSwingModuleAction extends NewModuleAction {

    private static final String TAG = NewSwingModuleAction.class.getSimpleName();
    private VirtualFile virtualFile;

    public NewSwingModuleAction() {
        getTemplatePresentation().setText("新建Swing模块");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
//        super.actionPerformed(e);
        final Project project = getEventProject(e);
        if (project == null) {
            return;
        }
        Object dataFromContext = prepareDataFromContext(e);

        String defaultPath = null;
        virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (virtualFile != null && virtualFile.isDirectory()) {
            defaultPath = virtualFile.getPath();
        }
        NewSwingModuleWrapper wizard = new NewSwingModuleWrapper(project, new DefaultModulesProvider(project), defaultPath);

        Log.d(TAG, "actionPerformed start");
        if (wizard.showAndGet()) {
            createModuleFromWizard(project, dataFromContext, wizard);
        }
        Log.d(TAG, "actionPerformed end");
    }

    @Nullable
    public Module createModuleFromWizard(Project project, @Nullable Object dataFromContext, AbstractProjectWizard wizard) {
        Log.d(TAG, "createModuleFromWizard start");
        final ProjectBuilder builder = wizard.getBuilder(project);
        if (builder == null) return null;
        Module module;
        if (builder instanceof ModuleBuilder) {
            Log.d(TAG, "createModuleFromWizard start as ModuleBuilder");
            module = ((ModuleBuilder) builder).commitModule(project, null);
            if (module != null) {
                processCreatedModule(module, dataFromContext);
            }
            createSwingProjectFiles(module);
            return module;
        } else {
            Log.d(TAG, "createModuleFromWizard start not as ModuleBuilder");
            List<Module> modules = builder.commit(project, null, new DefaultModulesProvider(project));
            if (builder.isOpenProjectSettingsAfter()) {
                ModulesConfigurator.showDialog(project, null, null);
            }
            module = modules == null || modules.isEmpty() ? null : modules.get(0);
        }
        project.save();
        return module;
    }

    private void createSwingProjectFiles(Module module) {
        String modulePath = module.getModuleFilePath().substring(0, module.getModuleFilePath().lastIndexOf("/"));
        /************** 生成资源文件 *****************/
        String resPath = modulePath + "/src/main/resources";
        File resDir = new File(resPath);
        Log.d(this, "resPath : " + resPath);
        if (!resDir.exists()) {
            Log.d(this, "! resDir.exists()");
            resDir.mkdirs();
        }
        File layoutDir = new File(resDir + "/layout");
        File mipmapDir = new File(resDir + "/mipmap");
        File valuesDir = new File(resDir + "/values");
        File main_activity = new File(layoutDir.getPath() + "/activity_main.swing");
        try {
            layoutDir.mkdirs();
            mipmapDir.mkdirs();
            valuesDir.mkdirs();
            main_activity.createNewFile();
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(main_activity), StandardCharsets.UTF_8);
            osw.write(DEFAULT_SWING_FILE_CONTENT);
            osw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /************** 生成Java文件 *****************/
        String javaPath = modulePath + "/src/main/java";
        File javaDir = new File(javaPath + "/ui");
        Log.d(this, "javaPath : " + javaPath);
        if (!javaDir.exists()) {
            Log.d(this, "! javaDir.exists()");
            javaDir.mkdirs();
        }
        File MainActivity = new File(javaPath + "/ui/MainActivity.java");
        try {
            MainActivity.createNewFile();
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(MainActivity), StandardCharsets.UTF_8);
            osw.write(DEFAULT_MAIN_ACTIVITY_CONTENT);
            osw.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        SwingResGroupCreator.createResGroup(modulePath, main_activity, DEFAULT_SWING_FILE_CONTENT);
        // 刷新目录
        virtualFile.refresh(false, true);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
    }

    @NotNull
    @Override
    public String getActionText(boolean isInNewSubmenu, boolean isInJavaIde) {
        return "新建Swing模块";
    }
}
