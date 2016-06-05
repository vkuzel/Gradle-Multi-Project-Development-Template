package com.github.vkuzel.gmpdt.test.project1;

import com.github.vkuzel.gmpdt.app.project1.Project1Speaker;
import com.github.vkuzel.gmpdt.app.core_module.CoreModuleApplication;
import com.github.vkuzel.gmpdt.app.core_module.CoreModuleSpeaker;
import com.github.vkuzel.gmpdt.app.core_module.ResourceManager;
import com.github.vkuzel.gmpdt.app.module1.Module1Speaker;
import com.github.vkuzel.gmpdt.app.module2.Module2Speaker;
import com.github.vkuzel.gmpdt.framework.test.core_module.TestEnvironmentContext;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {CoreModuleApplication.class, TestEnvironmentContext.class})
public class Project1Tests {

    private static final Logger log = LoggerFactory.getLogger(Project1Tests.class);

    @Autowired
    CoreModuleSpeaker coreModuleSpeaker;

    @Autowired
    Module1Speaker module1Speaker;

    @Autowired
    Module2Speaker module2Speaker;

    @Autowired
    Project1Speaker project1Speaker;

    @Autowired
    ResourceManager resourceManager;

    @Test
    public void testAvailableSpeakers() {
        Assert.assertEquals(CoreModuleSpeaker.class.getName(), coreModuleSpeaker.getName());
        Assert.assertEquals(Module1Speaker.class.getName(), module1Speaker.getName());
        Assert.assertEquals(Module2Speaker.class.getName(), module2Speaker.getName());
        Assert.assertEquals(Project1Speaker.class.getName(), project1Speaker.getName());
    }

    @Test
    public void testDependencyManager() throws IOException {
        List<Resource> resources = resourceManager.findIndependentProjectResourcesFirst("res.txt");
        Assert.assertEquals(4, resources.size());
        Assert.assertTrue(resources.get(0).getURL().toString().contains("core-module"));
        Assert.assertTrue(resources.get(1).getURL().toString().contains("module2"));
        Assert.assertTrue(resources.get(2).getURL().toString().contains("module1"));
        Assert.assertTrue(resources.get(3).getURL().toString().contains("project1"));
    }
}
