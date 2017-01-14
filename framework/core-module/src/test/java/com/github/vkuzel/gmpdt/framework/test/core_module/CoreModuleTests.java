package com.github.vkuzel.gmpdt.framework.test.core_module;

import com.github.vkuzel.gmpdt.app.core_module.CoreModuleApplication;
import com.github.vkuzel.gmpdt.app.core_module.CoreModuleSpeaker;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CoreModuleApplication.class, TestEnvironmentContext.class})
public class CoreModuleTests {

	@Autowired
	private CoreModuleSpeaker coreModuleSpeaker;

	@Test
	public void testAvailableSpeakers() {
		Assert.assertEquals(CoreModuleSpeaker.class.getName(), coreModuleSpeaker.getName());
	}
}
