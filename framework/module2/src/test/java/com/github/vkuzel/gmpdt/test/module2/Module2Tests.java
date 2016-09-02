package com.github.vkuzel.gmpdt.test.module2;

import com.github.vkuzel.gmpdt.app.core_module.CoreModuleApplication;
import com.github.vkuzel.gmpdt.app.core_module.CoreModuleSpeaker;
import com.github.vkuzel.gmpdt.app.module2.Module2Speaker;
import com.github.vkuzel.gmpdt.framework.test.core_module.TestEnvironmentContext;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {CoreModuleApplication.class, TestEnvironmentContext.class})
public class Module2Tests {

    @Autowired
    CoreModuleSpeaker coreModuleSpeaker;

    @Autowired
    Module2Speaker module2Speaker;

    @Test
    public void testAvailableSpeakers() {
        Assert.assertEquals(CoreModuleSpeaker.class.getName(), coreModuleSpeaker.getName());
        Assert.assertEquals(Module2Speaker.class.getName(), module2Speaker.getName());
    }
}
