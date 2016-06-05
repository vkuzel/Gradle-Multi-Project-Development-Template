// IntelliJ IDEA places all resources-marked source sets to an application's
// classpath. To prevent this class to be scanned by Spring's component scan
// this class is located in package *.test.*.
package com.github.vkuzel.gmpdt.framework.test.core_module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class TestEnvironmentContext {

    private static final Logger log = LoggerFactory.getLogger(TestEnvironmentContext.class);

    @PostConstruct
    private void initTestEnvironment() {
        log.info("Test environment initialized.");
    }
}
