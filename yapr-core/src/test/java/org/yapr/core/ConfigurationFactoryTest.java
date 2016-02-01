package org.yapr.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;

import org.junit.Test;
import org.yapr.domain.Configuration;

/**
 * @author Dimitri
 *
 */
public class ConfigurationFactoryTest {

	/**
	 * Test method for {@link org.yapr.core.ConfigurationFactory#ConfigurationFactory()}.
	 */
	@Test
	public void test_ConfigurationFactory() {
		ConfigurationFactory instance = new ConfigurationFactory();
		assertNotNull(instance);
	}

	/**
	 * Test method for {@link org.yapr.core.ConfigurationFactory#newInstance(int, java.lang.String)}.
	 */
	@Test
	public void test_newInstance_2params() {
		ConfigurationFactory instance = new ConfigurationFactory();
		Configuration configuration = instance.newInstance(3, "/tmp");
		assertEquals(3, configuration.getHoursOffset());

		assertNotNull(configuration.getTargetFolder());
		assertEquals("/tmp", configuration.getTargetFolder());

		assertNotNull(configuration.getRenamers());
		assertTrue(configuration.getRenamers().size() > 0);
	}

	/**
	 * Test method for {@link org.yapr.core.ConfigurationFactory#newInstance(java.text.SimpleDateFormat, int, java.lang.String)}.
	 */
	@Test
	public void test_newInstance_3params() {
		ConfigurationFactory instance = new ConfigurationFactory();
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy-HH'h'mm'm'ss");
		Configuration configuration = instance.newInstance(sdf, 4, "/dev/null");
		assertEquals(4, configuration.getHoursOffset());

		assertNotNull(configuration.getTargetFolder());
		assertEquals("/dev/null", configuration.getTargetFolder());

		assertNotNull(configuration.getRenamers());
		assertTrue(configuration.getRenamers().size() > 0);

		assertNotNull(configuration.getDateFormat());
		assertEquals(sdf.toPattern(), configuration.getDateFormat().toPattern());
	}

}
