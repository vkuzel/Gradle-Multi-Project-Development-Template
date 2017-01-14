package com.github.vkuzel.gmpdt.app.core_module;

import com.github.vkuzel.gradle_project_dependencies.ProjectDependencies;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.*;

@Component
public class ResourceManager {

    private static final String PROJECT_DEPENDENCIES_FILE = "dependencies.ser";
    private static final PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    private final List<Dependencies> independentProjectsFirst = new ArrayList<>();
    private final Comparator<Resource> INDEPENDENT_PROJECT_RESOURCES_FIRST = (resource1, resource2) -> {
        try {
            return Integer.compare(
                    getProjectIndex(resource1.getURL()),
                    getProjectIndex(resource2.getURL())
            );
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    };

    public List<Resource> findIndependentProjectResourcesFirst(String pathOnClasspath) {
        List<Resource> resources;
        try {
            resources = Arrays.asList(resourceResolver.getResources("classpath*:" + pathOnClasspath));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        resources.sort(INDEPENDENT_PROJECT_RESOURCES_FIRST);

        return resources;
    }

    private int getProjectIndex(URL resourceUrl) {
        int index = -1;
        for (Dependencies dependencies : independentProjectsFirst) {
            if (dependencies.isInProject(resourceUrl)) {
                if (index != -1) {
                    throw new IllegalStateException("Two projects (" + independentProjectsFirst.get(index).getProjectName() + " and " + dependencies.getProjectName()
                            + ") has been found for resource " + resourceUrl.toString() + "!" +
                            " Please make sure all project names are unique!");
                }
                index = independentProjectsFirst.indexOf(dependencies);
            }
        }
        if (index == -1) {
            throw new IllegalStateException("Project dependencies for resource " + resourceUrl.toString() + " wasn't found!" +
                    " Please make sure that gradle serializedProjectDependenciesPath task has been executed.");
        }
        return index;
    }

    @PostConstruct
    private void loadProjectDependencies() {
        Resource[] dependenciesFiles;
        try {
            dependenciesFiles = resourceResolver.getResources("classpath*:" + PROJECT_DEPENDENCIES_FILE);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        for (Resource dependenciesFile : dependenciesFiles) {
            Dependencies dependencies = deserializeDependencies(dependenciesFile);

            independentProjectsFirst.remove(dependencies);

            int furtherChildPosition = dependencies.getDependencies().stream()
                    .mapToInt(this::getProjectIndex).max().orElse(-1);

            independentProjectsFirst.add(furtherChildPosition + 1, dependencies);
        }
    }

    private int getProjectIndex(String projectName) {
        for (Dependencies dependencies : independentProjectsFirst) {
            if (Objects.equals(dependencies.getProjectName(), projectName)) {
                return independentProjectsFirst.indexOf(dependencies);
            }
        }
        return -1;
    }

    private Dependencies deserializeDependencies(Resource dependenciesFile) {
        try (
                InputStream inputStream = dependenciesFile.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)
        ) {
            return new Dependencies(dependenciesFile, (ProjectDependencies) objectInputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    private static class Dependencies {
        private final ProjectDependencies projectDependencies;
        private final String projectPath;

        private Dependencies(Resource dependenciesFile, ProjectDependencies projectDependencies) {
            this.projectDependencies = projectDependencies;
            try {
                String dependenciesFileName = dependenciesFile.getFilename();
                String dependenciesFilePath = dependenciesFile.getURL().getPath();
                projectPath = dependenciesFilePath.substring(0, dependenciesFilePath.lastIndexOf(dependenciesFileName));
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }

        private String getProjectName() {
            return projectDependencies.getName();
        }

        private boolean isInProject(URL resourceUrl) {
            String resourcePath = resourceUrl.getPath();
            if (resourcePath.startsWith(projectPath)) {
                String withoutProjectPath = resourcePath.substring(projectPath.length());
                return !withoutProjectPath.contains(ResourceUtils.JAR_URL_SEPARATOR);
            }
            return false;
        }

        private List<String> getDependencies() {
            return projectDependencies.getDependencies();
        }
    }
}
