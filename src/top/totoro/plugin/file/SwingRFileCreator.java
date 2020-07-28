package top.totoro.plugin.file;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SwingRFileCreator {
    private static String RFilePath = File.separator + "src" +
            File.separator + "main" +
            File.separator + "java" +
            File.separator + "swing" +
            File.separator + "R.java";

    public static void createIdGroup(String projectPath, List<String> ids) {
        File RFile = new File(projectPath + RFilePath);
        try {
            if (!RFile.getParentFile().exists()) {
                RFile.getParentFile().mkdirs();
            }
            System.out.println("create R file = " + RFile.createNewFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
