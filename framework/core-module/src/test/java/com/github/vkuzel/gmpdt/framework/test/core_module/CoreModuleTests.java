package com.github.vkuzel.gmpdt.framework.test.core_module;

import com.github.vkuzel.gmpdt.app.core_module.CoreModuleApplication;
import com.github.vkuzel.gmpdt.app.core_module.CoreModuleSpeaker;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {CoreModuleApplication.class, TestEnvironmentContext.class})
public class CoreModuleTests {

	@Autowired
	CoreModuleSpeaker coreModuleSpeaker;

	@Test
	public void testAvailableSpeakers() {
		Assert.assertEquals(CoreModuleSpeaker.class.getName(), coreModuleSpeaker.getName());
	}

	public static void ahoj() {
		System.out.println("xxx");
	}

	public static void hello() {

	}
}
