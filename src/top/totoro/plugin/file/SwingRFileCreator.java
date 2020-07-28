package top.totoro.plugin.file;

import java.io.*;
import java.text.MessageFormat;

public class SwingRFileCreator {
    private static final String TAG = SwingRFileCreator.class.getSimpleName();
    private static final String RFilePath = File.separator + "src" +
            File.separator + "main" +
            File.separator + "java" +
            File.separator + "swing" +
            File.separator + "R.java";
    private static final String DEFAULT_R_FILE_CONTENT = "package swing;\n\npublic class R{\n\n}";

    private static final String idClassStart = "\tpublic static class id {\n";
    private static final String idClassStartRegex = "\tpublic static class id \\{\n";
    private static final String idFieldStart = "\t\tpublic static final int ";

    public static void createIdGroup(String projectPath, String[] ids) {
        File RFile = new File(projectPath + RFilePath);
        try {
            // 确定R.java文件已生成
            if (!RFile.getParentFile().exists()) {
                boolean mkdirs = RFile.getParentFile().mkdirs();
                if (mkdirs) {
                    if (!RFile.exists()) {
                        Log.d(TAG, "create R file " + (RFile.createNewFile() ? "success" : "false"));
                    }
                } else {
                    Log.d(TAG, "mkdirs " + RFile.getParentFile().getPath() + " false");
                }
            }
            if (RFile.exists()) {
                String contents = getRFileContent(RFile);
                StringBuilder finalContents = new StringBuilder(contents);
                // 处理加入的id
                String[] totals = contents.split(idClassStartRegex);
                if (totals.length == 1) {
                    finalContents = new StringBuilder(totals[0].substring(0, totals[0].lastIndexOf("}")));
                    finalContents.append(idClassStart);
                    for (int i = 0; i < ids.length; i++) {
                        String id = ids[i];
                        finalContents.append(idFieldStart).append(id).append("=0x").append(i).append(";\n");
                    }
                    finalContents.append("\t}\n}");
                } else if (totals.length == 2) {
                    finalContents = new StringBuilder(totals[0]);
                    finalContents.append(idClassStart);
                    String originIdsContent = totals[1].substring(0, totals[1].indexOf("}"));
                    Log.d(TAG, "originIdsContent : \n" + originIdsContent);
                    String[] originIds = originIdsContent.split(idFieldStart);
                    // 将原有的id字段按照从小到大的顺序重新排序，防止后面id冲突
                    for (int i = 0; i < originIds.length; i++) {
                        Log.d(TAG, "originId = " + originIds[i]);
                        if (originIds[i].indexOf("=0x") > 0) {
                            String idName = originIds[i].substring(0, originIds[i].indexOf("=0x"));
                            originIds[i] = idName + "=0x" + i + ";\n";
                            finalContents.append(idFieldStart).append(originIds[i]);
                        }
                    }
                    // 需要把换行符去掉，否则正则表达式匹配失败
                    originIdsContent = originIdsContent.replace("\n", "");
                    for (int i = originIds.length, j = 0; j < ids.length; i++, j++) {
                        String regex = ".*" + idFieldStart + ids[j] + "=0x.*";
                        Log.d(TAG, "regex = " + regex);
                        if (originIdsContent.matches(regex)) {
                            // R.java中已经存在该id字段，不需要继续添加
                            Log.d(TAG, "match id : " + ids[j]);
                        } else {
                            // 添加新的id字段
                            finalContents.append(idFieldStart).append(ids[j]).append("=0x").append(j).append(";\n");
                        }
                    }
                    finalContents.append("\t}\n}");
                }
                setRFileContent(RFile, finalContents.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取R.java文件的全部内容，按行分
     *
     * @param RFile R.java的具体目标文件
     * @return 全部文件内容，如果没有就返回默认内容
     */
    private static String getRFileContent(File RFile) {
        StringBuilder content = new StringBuilder();
        try (FileReader fr = new FileReader(RFile); BufferedReader br = new BufferedReader(fr)) {
            String line = "";
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (content.length() == 0) {
            // 没有内容，返回初始化R.java
            return DEFAULT_R_FILE_CONTENT;
        }
        return content.toString();
    }

    /**
     * 将最终的R.java文件内容写入文件中
     *
     * @param RFile   R.java的具体文件
     * @param content 文件内容
     */
    private static void setRFileContent(File RFile, String content) {
        try (FileWriter fileWriter = new FileWriter(RFile, false)) {
            fileWriter.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
