package top.totoro.plugin.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class NewSwingFile extends AnAction {

    private Project project;
    private String path;
    private VirtualFile chooseFile;

    public NewSwingFile() {
    }

    public NewSwingFile(@Nls(capitalization = Nls.Capitalization.Title) @Nullable String text) {
        super(text);
    }

    public void setChooseFile(VirtualFile chooseFile) {
        this.chooseFile = chooseFile;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        project = anActionEvent.getProject();
        new FormTestDialog().show();
    }

    public void setPath(String path) {
        this.path = path;
    }

    public class FormTestDialog extends DialogWrapper {

        public FormTestDialog() {
            super(true);
            setTitle("创建Swing布局文件"); //设置会话框标题
            setResizable(false);
            init(); //触发一下init方法，否则swing样式将无法展示在会话框
        }

        private JPanel center = new JPanel();

        private JPanel south = new JPanel();

        private JLabel name = new JLabel("文件名：");
        private JTextField nameContent = new JTextField(30);

        public JPanel initNorth() {
            //定义表单的标题部分，放置到IDEA会话框的顶部位置
            return null;
        }

        public JPanel initCenter() {
            //定义表单的主体部分，放置到IDEA会话框的中央位置
            center.setLayout(new BorderLayout());

            //row1：文件名+文本框
            center.add(name, BorderLayout.WEST);
            center.add(nameContent, BorderLayout.CENTER);
            return center;
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        public JPanel initSouth() {
            //定义表单的提交按钮，放置到IDEA会话框的底部位置
            JButton submit = new JButton("创建");
            submit.setHorizontalAlignment(SwingConstants.CENTER); //水平居中
            submit.setVerticalAlignment(SwingConstants.CENTER); //垂直居中
            south.add(submit);

            //按钮事件绑定
            submit.addActionListener(e -> {
                String filename = nameContent.getText() + ".swing";
                File swingFile = new File(path.endsWith("layout") ? path + "/" + filename : path + "/layout/" + filename);
                if (swingFile.exists()) {
                    Messages.showMessageDialog(project, filename + "已存在", "无法创建", Messages.getErrorIcon());
                    return;
                }
                if (!swingFile.getParentFile().exists()) {
                    swingFile.getParentFile().mkdirs();
                }
                if (swingFile.getParentFile().exists()) {
                    FileWriter fileWriter = null;
                    try {
                        swingFile.createNewFile();
                        fileWriter = new FileWriter(swingFile, false);
                        String fileHeader = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n";
                        fileWriter.write(fileHeader);
                        fileWriter.close();
                        // 刷新目录
                        chooseFile.refresh(false, true);
                        close(1);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });

            return south;
        }

        @Override
        protected JComponent createNorthPanel() {
            return initNorth(); //返回位于会话框north位置的swing样式
        }

        // 特别说明：不需要展示SouthPanel要重写返回null，否则IDEA将展示默认的"Cancel"和"OK"按钮
        @Override
        protected JComponent createSouthPanel() {
            return initSouth();
        }

        @Override
        protected JComponent createCenterPanel() {
            //定义表单的主题，放置到IDEA会话框的中央位置
            return initCenter();
        }
    }

}
