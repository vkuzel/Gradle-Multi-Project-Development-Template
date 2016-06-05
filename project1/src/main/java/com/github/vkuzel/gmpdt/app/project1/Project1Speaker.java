package com.github.vkuzel.gmpdt.app.project1;

import com.github.vkuzel.gmpdt.app.core_module.Speaker;
import org.springframework.stereotype.Component;

@Component
public class Project1Speaker implements Speaker {
    @Override
    public String getName() {
        return this.getClass().getName();
    }
}
