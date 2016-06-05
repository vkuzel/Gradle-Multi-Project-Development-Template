package com.github.vkuzel.gmpdt.app.module2;

import com.github.vkuzel.gmpdt.app.core_module.Speaker;
import org.springframework.stereotype.Component;

@Component
public class Module2Speaker implements Speaker {
    @Override
    public String getName() {
        return this.getClass().getName();
    }
}
