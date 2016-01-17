package com.github.vkuzel.gradle_multi_project_development_template.framework.module2;

import com.github.vkuzel.gradle_multi_project_development_template.framework.core_module.Speaker;

public class Module2Speaker implements Speaker {
    @Override
    public void introduce() {
        System.out.println("I am " + this.getClass().getName());
    }
}
