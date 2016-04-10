package com.github.vkuzel.gradle_multi_project_development_template.framework.core_module;

import com.github.vkuzel.gradle_dependency_graph.Node;
import com.github.vkuzel.gradle_dependency_graph.Node.Project;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Component
public class ResourceManager {

    private static final String PROJECT_DEPENDENCY_GRAPH_FILE = "dependencies.ser";
    private final PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    private final List<Project> independentProjectsFirst = new ArrayList<>();
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
        for (Project project : independentProjectsFirst) {
            String path = resourceUrl.getFile();
            if (ResourceUtils.isJarURL(resourceUrl)) {
                int separatorIndex = path.substring(0, path.lastIndexOf(ResourceUtils.JAR_URL_SEPARATOR))
                        .lastIndexOf(ResourceUtils.JAR_URL_SEPARATOR);
                if (separatorIndex != -1) {
                    path = path.substring(separatorIndex);
                }
            }

            if (path.contains(project.getDir())) {
                if (index != -1) {
                    throw new IllegalStateException("Two projects (" + independentProjectsFirst.get(index).getName() + " and " + project.getName()
                            + ") has been found for resource " + resourceUrl.toString() + "!" +
                            " Please check the name of each module is unique!");
                }
                index = independentProjectsFirst.indexOf(project);
            }
        }
        if (index == -1) {
            throw new IllegalStateException("Project for resource " + resourceUrl.toString() + " wasn't found in project dependency graph!" +
                    " Please make sure that gradle generateDependencyGraph task has been executed.");
        }
        return index;
    }

    @PostConstruct
    private void loadProjectDependencies() {
        Resource resource = resourceResolver.getResource("classpath:" + PROJECT_DEPENDENCY_GRAPH_FILE);
        if (!resource.exists()) {
            throw new IllegalStateException("Project dependency graph file " + PROJECT_DEPENDENCY_GRAPH_FILE + " is not found." +
                    " Make sure that `gradle generateDependencyGraph` task has been executed and dependencyGraphPath build property is properly configured.");
        }

        Node dependencyGraph;
        try (
                InputStream inputStream = resource.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)
        ) {
            dependencyGraph = (Node) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }

        buildProjectList(dependencyGraph);
    }

    private void buildProjectList(Node dependencyGraph) {
        Project project = dependencyGraph.getProject();
        independentProjectsFirst.remove(project);

        int furtherChildPosition = dependencyGraph.getChildren().stream()
                .mapToInt(node -> independentProjectsFirst.indexOf(node.getProject())).max().orElse(-1);

        independentProjectsFirst.add(furtherChildPosition + 1, project);

        dependencyGraph.getChildren().forEach(this::buildProjectList);
    }
}
