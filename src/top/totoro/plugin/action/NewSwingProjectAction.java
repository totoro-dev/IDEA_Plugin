package top.totoro.plugin.action;

import com.intellij.ide.actions.NewProjectAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class NewSwingProjectAction extends NewProjectAction {

    private static final String TAG = NewSwingProjectAction.class.getSimpleName();

    public NewSwingProjectAction() {
//        super("新建Swing项目");
        getTemplatePresentation().setText("创建Swing项目");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        super.actionPerformed(anActionEvent);
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
    }

//    private static boolean isInvokedFromNewSubMenu(@NotNull AnAction action, @NotNull AnActionEvent e) {
//        Log.d(TAG, "isInvokedFromNewSubMenu");
//        return true;
//    }
}
