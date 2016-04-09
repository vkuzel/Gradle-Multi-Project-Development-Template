package com.github.vkuzel.gradle_multi_project_development_template.framework.core_module;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URL;
import java.net.URLClassLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CoreModuleApplication.class)
public class CoreModuleTests {

	@Autowired
	CoreModuleSpeaker coreModuleSpeaker;

	@Test
	public void testAvailableSpeakers() {
		Assert.assertEquals(CoreModuleSpeaker.class.getName(), coreModuleSpeaker.getName());
	}

}
