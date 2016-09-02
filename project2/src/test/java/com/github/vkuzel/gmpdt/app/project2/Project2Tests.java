package com.github.vkuzel.gmpdt.app.project2;

import com.github.vkuzel.gmpdt.app.core_module.CoreModuleApplication;
import com.github.vkuzel.gmpdt.app.core_module.CoreModuleSpeaker;
import com.github.vkuzel.gmpdt.app.module1.Module1Speaker;
import com.github.vkuzel.gmpdt.framework.test.core_module.TestEnvironmentContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {CoreModuleApplication.class, TestEnvironmentContext.class})
public class Project2Tests {

    @Autowired
    CoreModuleSpeaker coreModuleSpeaker;

    @Autowired
    Module1Speaker module1Speaker;

    @Autowired
    Project2Speaker project2Speaker;

    @Test
    public void testAvailableSpeakers() {
        Assert.assertEquals(CoreModuleSpeaker.class.getName(), coreModuleSpeaker.getName());
        Assert.assertEquals(Module1Speaker.class.getName(), module1Speaker.getName());
        Assert.assertEquals(Project2Speaker.class.getName(), project2Speaker.getName());
    }
}
