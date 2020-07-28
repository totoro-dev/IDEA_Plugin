package top.totoro.plugin.file;

import com.intellij.externalSystem.JavaProjectData;
import com.intellij.openapi.project.Project;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.intellij.openapi.project.impl.ProjectImpl.CREATION_TIME;
import static com.intellij.openapi.project.impl.ProjectImpl.CREATION_TRACE;

public class SwingProjectInfo {
    private Project project;
    private static final Map<String, SwingProjectInfo> projects = new ConcurrentHashMap<>();

    public static void setProject(Project project) {
        if (projects.get(project.getBasePath()) == null) {
            SwingProjectInfo swingProjectInfo = new SwingProjectInfo();
            swingProjectInfo.project = project;
            projects.put(project.getBasePath(), swingProjectInfo);

            System.out.println("setProject path = "+project.getUserData(CREATION_TIME));
        }
    }

    public static SwingProjectInfo getSwingProject(String filePath) {
        Set<String> projectKeys = projects.keySet();
        String projectFilePath = "";
        for (String projectKey : projectKeys) {
            if (filePath.contains(projectKey)) {
                if (Math.max(projectFilePath.length(), projectKey.length()) > projectFilePath.length()) {
                    projectFilePath = projectKey;
                }
            }
        }
        return projects.get(projectFilePath);
    }

    public Project getProject() {
        return project;
    }
}
