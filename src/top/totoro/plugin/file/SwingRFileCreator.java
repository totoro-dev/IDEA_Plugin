package top.totoro.plugin.file;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

    private static final Map<File, Map<File, List<String>>> idInRFilesMap = new ConcurrentHashMap<>();

    public static void createIdGroup(String projectPath, File res, String[] ids) {
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
                Map<File, List<String>> idInFileMap = idInRFilesMap.computeIfAbsent(RFile, key -> new ConcurrentHashMap<>());
                List<String> originFileIds = new ArrayList<>(Arrays.asList(ids));
                idInFileMap.put(res, originFileIds.stream().distinct().collect(Collectors.toList()));
                // 处理加入的id
                String[] totals = contents.split(idClassStartRegex);
                if (totals.length == 1) {
                    finalContents = new StringBuilder(totals[0].substring(0, totals[0].lastIndexOf("}")));
                    finalContents.append(idClassStart);
                    AtomicInteger idIndex = new AtomicInteger();
                    StringBuilder finalContents1 = finalContents;
                    idInFileMap.forEach((file, idList) -> {
                        for (String id : idList) {
                            finalContents1.append(idFieldStart).append(id).append("=0x").append(Integer.toHexString(idIndex.get())).append(";\n");
                            idIndex.getAndIncrement();
                        }
                    });
                    finalContents1.append("\t}\n}");
                    finalContents = finalContents1;
                } else if (totals.length == 2) {
                    finalContents = new StringBuilder(totals[0]);
                    finalContents.append(idClassStart);
                    AtomicInteger idIndex = new AtomicInteger();
                    StringBuilder finalContents1 = finalContents;
                    idInFileMap.forEach((file, idList) -> {
                        for (String id : idList) {
//                            Log.d(TAG, "append id : " + id);
                            finalContents1.append(idFieldStart).append(id).append("=0x").append(Integer.toHexString(idIndex.get())).append(";\n");
                            idIndex.getAndIncrement();
                        }
                    });
                    finalContents1.append("\t}\n}");
                    finalContents = finalContents1;
                }
                setRFileContent(RFile, finalContents.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkFileHasLoad(String projectPath, String filePath) {
        File projectFile = new File(projectPath + RFilePath);
        File file = new File(filePath);
        return idInRFilesMap.get(projectFile) != null
                && idInRFilesMap.get(projectFile).get(file) != null;
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
