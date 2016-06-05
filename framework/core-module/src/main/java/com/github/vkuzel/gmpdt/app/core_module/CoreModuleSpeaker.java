package com.github.vkuzel.gmpdt.app.core_module;

import org.springframework.stereotype.Component;

@Component
public class CoreModuleSpeaker implements Speaker {
    @Override
    public String getName() {
        return this.getClass().getName();
    }
}
