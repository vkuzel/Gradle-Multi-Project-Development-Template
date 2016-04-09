package com.github.vkuzel.gradle_multi_project_development_template.project2;

import com.github.vkuzel.gradle_multi_project_development_template.framework.core_module.Speaker;
import org.springframework.stereotype.Component;

@Component
public class Project2Speaker implements Speaker {

    @Override
    public String getName() {
        return this.getClass().getName();
    }
}
