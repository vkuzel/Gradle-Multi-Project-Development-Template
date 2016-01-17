package com.github.vkuzel.gradle_multi_project_development_template.framework.core_module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
public class CoreModuleApplication {

    @Autowired
    List<Speaker> allSpeakersInProject;

    public static void main(String[] args) {
        SpringApplication.run(CoreModuleApplication.class, args);
    }

    @PostConstruct
    public void introduceSpeakers() {
        System.out.println("Speakers introduction: ");
        allSpeakersInProject.forEach(Speaker::introduce);
    }
}
