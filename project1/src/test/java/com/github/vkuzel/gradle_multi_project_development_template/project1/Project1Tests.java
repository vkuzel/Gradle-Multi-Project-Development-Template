package com.github.vkuzel.gradle_multi_project_development_template.project1;

import com.github.vkuzel.gradle_multi_project_development_template.framework.core_module.CoreModuleApplication;
import com.github.vkuzel.gradle_multi_project_development_template.framework.core_module.CoreModuleSpeaker;
import com.github.vkuzel.gradle_multi_project_development_template.framework.core_module.ResourceManager;
import com.github.vkuzel.gradle_multi_project_development_template.framework.module1.Module1Speaker;
import com.github.vkuzel.gradle_multi_project_development_template.framework.module2.Module2Speaker;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CoreModuleApplication.class)
public class Project1Tests {

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
        Assert.assertTrue(resources.get(0).getURL().toString().endsWith("/framework/core-module/build/resources/main/res.txt"));
        Assert.assertTrue(resources.get(1).getURL().toString().endsWith("/framework/module2/build/resources/main/res.txt"));
        Assert.assertTrue(resources.get(2).getURL().toString().endsWith("/framework/module1/build/resources/main/res.txt"));
        Assert.assertTrue(resources.get(3).getURL().toString().endsWith("/project1/build/resources/main/res.txt"));
    }
}
