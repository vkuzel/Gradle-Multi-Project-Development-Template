package com.github.vkuzel.gradle_multi_project_development_template.framework.core_module;

import org.springframework.stereotype.Component;

@Component
public class CoreModuleSpeaker implements Speaker {
    @Override
    public String getName() {
        return this.getClass().getName();
    }
}
