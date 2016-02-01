package org.yapr.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Options;
import org.junit.Test;
import org.yapr.domain.Configuration;

/**
 * @author Dimitri
 * TODO Check all possible failures for each possible option.
 */
public class YaprCLITest {

	/**
	 * Test method for {@link org.yapr.cli.YaprCLI#YaprCLI()}.
	 */
	@Test
	public void test_YaprCLI() {
		YaprCLI instance = new YaprCLI();
		assertNotNull(instance.options);
		assertNotNull(instance.formatter);
	}

	/**
	 * Test method for {@link org.yapr.cli.YaprCLI#prepareConfiguration(java.lang.String[])}.
	 * Check that the help option is well supported.
	 */
	@Test
	public void test_prepareConfiguration_help() {
		YaprCLI instance = new YaprCLI();
		String[] args = null;
		List<String[]> arrays = new ArrayList<String[]>();
		arrays.add(new String[]{"-h"});
		arrays.add(new String[]{"-help"});

		for (int loop = 0; loop < arrays.size(); loop++) {
			try {
				args = arrays.get(loop);
				instance.prepareConfiguration(args);
				fail("Unexpected Configuration instance received while help/usage message was requested");
			} catch (InvalidOptionException exception) {
				assertEquals("For '" + args[0] + "', exit code must be 0", 0, exception.getExitCode());
				assertNotNull("For '" + args[0] + "', executable name must be provided", exception.getExecutableNameForHelpMessage());
				assertTrue("For '" + args[0] + "', there is no expected error message", null == exception.getErrorMessage());
			}
		}
	}

	/**
	 * Test method for {@link org.yapr.cli.YaprCLI#prepareConfiguration(java.lang.String[])}.
	 * Check that all possible options passed via the command-line interpreter are well reflected in the generated configuration.
	 */
	@Test
	public void test_prepareConfiguration_allOptions_success() {
		YaprCLI instance = new YaprCLI();
		String[] args = null;
		Configuration configuration = null;

		args = new String[]{"-source", ".", "-R", "-target", System.getProperty("java.io.tmpdir"), "-offset", "-1", "-dateFormat", "dd_MM_yyyy-HH'h'mm'm'ss"};
		configuration = instance.prepareConfiguration(args);

		assertNotNull(configuration);

		assertNotNull(configuration.getSourceFolder());
		assertEquals(".", configuration.getSourceFolder().toString());

		assertTrue(configuration.isProcessSubFolders());

		assertNotNull(configuration.getTargetFolder());
		assertEquals(System.getProperty("java.io.tmpdir"), configuration.getTargetFolder().toString());

		assertEquals(-1, configuration.getHoursOffset());

		assertNotNull(configuration.getDateFormat());
		assertEquals("dd_MM_yyyy-HH'h'mm'm'ss", configuration.getDateFormat().toPattern());
	}

	/**
	 * Test method for {@link org.yapr.cli.YaprCLI#prepareConfiguration(java.lang.String[])}.
	 * @throws IOException 
	 */
	@Test
	public void test_prepareConfiguration_mandatoryOption_source() throws IOException {
		YaprCLI instance = new YaprCLI();
		Configuration configuration = instance.prepareConfiguration(new String[]{"-source", "."});
		assertNotNull(configuration);
		assertNotNull(configuration.getSourceFolder());
		assertEquals(".", configuration.getSourceFolder().toString());

		// Check that the source directory parameter is mandatory
		try {
			configuration = instance.prepareConfiguration(new String[]{});
		} catch (InvalidOptionException exception) {
			assertEquals(1, exception.getExitCode());
			assertTrue(null == exception.getErrorMessage());
			assertNotNull(exception.getExecutableNameForHelpMessage());
		}

		// Check that a file cannot be used as the source directory
		File file = File.createTempFile("YaprCLITest-", null);
		file.deleteOnExit();
		try {
			configuration = instance.prepareConfiguration(new String[]{"-source", file.toString()});
		} catch (InvalidOptionException exception) {
			assertEquals(1, exception.getExitCode());
			assertNotNull(exception.getErrorMessage());
			assertTrue(null == exception.getExecutableNameForHelpMessage());
		}
	}

	/**
	 * Test method for {@link org.yapr.cli.YaprCLI#createOptions()}.
	 */
	@Test
	public void test_createOptions() {
		YaprCLI instance = new YaprCLI();
		Options options = instance.createOptions();
		assertNotNull(options);
		assertNotNull(options.getOptions());
		assertEquals(7, options.getOptions().size());
		assertTrue(options.hasOption("h"));
		assertTrue(options.hasOption("help"));
		assertTrue(options.hasOption("R"));
		assertTrue(options.hasOption("source"));
		assertTrue(options.hasOption("target"));
		assertTrue(options.hasOption("offset"));
		assertTrue(options.hasOption("dateFormat"));
	}

}
