package com.github.vkuzel.gradle_multi_project_development_template.framework.core_module;

import com.github.vkuzel.gradle_dependency_graph.Node;
import com.sun.istack.internal.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

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
public class ProjectDependencyManager {

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

    @NotNull
    public List<Resource> findIndependentProjectResourcesFirst(@NotNull String pathOnClasspath) {
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
            if (resourceUrl.getPath().contains(project.getDir())) {
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
        Node dependencyGraph;
        try (
                InputStream inputStream = resource.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)
        ) {
            dependencyGraph = (Node) objectInputStream.readObject();
        } catch (IOException e) {
            throw new IllegalStateException("Project dependency graph file " + PROJECT_DEPENDENCY_GRAPH_FILE + " cannot be read." +
                    " Make sure that `gradle generateDependencyGraph` task has been executed and dependencyGraphPath build property is properly configured.", e);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }

        buildProjectList(dependencyGraph);
    }

    private void buildProjectList(Node dependencyGraph) {
        Project project = new Project(dependencyGraph);
        independentProjectsFirst.remove(project);

        int furtherChildPosition = dependencyGraph.getChildren().stream()
                .mapToInt(node -> independentProjectsFirst.indexOf(new Project(node))).max().orElse(-1);

        independentProjectsFirst.add(furtherChildPosition + 1, project);

        dependencyGraph.getChildren().forEach(this::buildProjectList);
    }

    private static class Project { // TODO Move this to the Node class...
        private final String name;
        private final String dir;

        private Project(Node node) {
            this.name = node.getProjectName();
            this.dir = node.getProjectDir();
        }

        public String getName() {
            return name;
        }

        public String getDir() {
            return dir;
        }

        @Override
        public String toString() {
            return "Project{" +
                    "name='" + name + '\'' +
                    ", dir='" + dir + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Project project = (Project) o;

            if (!name.equals(project.name)) return false;
            return dir.equals(project.dir);

        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + dir.hashCode();
            return result;
        }
    }
}
