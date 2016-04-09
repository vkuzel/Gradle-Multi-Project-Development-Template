package com.github.vkuzel.gradle_multi_project_development_template.framework.core_module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
@ComponentScan("com.github.vkuzel.gradle_multi_project_development_template")
public class CoreModuleApplication {

    @Autowired
    private List<Speaker> allSpeakersInProject;

    @Autowired
    private ProjectDependencyManager dependencyManager;

    public static void main(String[] args) {
        SpringApplication.run(CoreModuleApplication.class, args);
    }

    @PostConstruct
    public void introduceSpeakers() {
        System.out.println("Speakers introduction: ");
        allSpeakersInProject.forEach(speaker -> System.out.println("I am " + speaker.getName()));

        List<Resource> resources = dependencyManager.findIndependentProjectResourcesFirst("res.txt");
        resources.forEach(resource -> {
            try {
                System.out.println("Resource in " + resource.getURL().toString());
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        });
    }
}
