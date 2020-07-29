package top.totoro.plugin.action;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import top.totoro.plugin.file.Log;

public class SwingGroup extends DefaultActionGroup {

    private static final String TAG = SwingGroup.class.getSimpleName();
    private static String chooseDirPath = "";
    private static NewSwingFile newSwingFile = new NewSwingFile("New Swing File");

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        Log.d(TAG, "actionPerformed : " + e.getPlace());
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        Log.d(TAG, "update : " + e.getPlace() + ", " + e.getInputEvent());
        DataContext dataContext = e.getDataContext();
        VirtualFile root = DataKeys.VIRTUAL_FILE.getData(dataContext);
        if (root == null) return;
        chooseDirPath = root.getPath();
        removeAll();
        if (root.getPath().contains("resources")) {
            if (root.getName().equals("resources") || root.getName().equals("layout")) {
                newSwingFile.setPath(chooseDirPath);
                newSwingFile.setChooseFile(root);
                add(newSwingFile);
            }
        }
    }

}
