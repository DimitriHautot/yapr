package org.yapr.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.yapr.domain.Configuration;
import org.yapr.domain.FileRenamer;
import org.yapr.domain.LyingRenamer;
import org.yapr.tests.FileSystemInitializer;

/**
 * @author Dimitri
 *
 */
public class YaprCoreTest {

	private FileSystemInitializer _fileSystem;

	public YaprCoreTest() {
		_fileSystem = new FileSystemInitializer(getClass().getSimpleName());
	}

	/**
	 * Test method for {@link org.yapr.core.YaprCore#process(org.yapr.domain.Configuration, java.io.File[])}.
	 * Check that a bad {@link Configuration} instance is well detected.
	 */
	@Test
	public void test_processConfigurationFileArray_bad_configuration() {
		YaprCore instance = new YaprCore();
		Configuration configuration = null;

		try {
			instance.process(configuration, _fileSystem.getAllFiles());
			fail("Provided Configuration instance is null");
		} catch (RuntimeException expected) {
			expected = null;
		}

		try {
			configuration = new Configuration();
			configuration.setRenamers(null);
			instance.process(configuration, _fileSystem.getAllFiles());
			fail("Provided renamers list is null");
		} catch (RuntimeException expected) {
			expected = null;
		}

		try {
			configuration.setRenamers(new ArrayList<FileRenamer>());
			instance.process(configuration, _fileSystem.getAllFiles());
			fail("Provided renamers list is empty");
		} catch (RuntimeException expected) {
			expected = null;
		}
	}

	/**
	 * Test method for {@link org.yapr.core.YaprCore#process(org.yapr.domain.Configuration, java.io.File[])}.
	 */
	@Test
	public void test_processConfigurationFileArray_bad_array() {
		YaprCore instance = new YaprCore();
		Configuration configuration = new Configuration();
		List<FileRenamer> renamers = new ArrayList<FileRenamer>();
		renamers.add(new LyingRenamer());
		configuration.setRenamers(renamers);

		int processed = instance.process(configuration, (File[])null);
		assertEquals(0, processed);

		processed = instance.process(configuration, new File[]{});
		assertEquals(0, processed);
	}

	/**
	 * Test method for {@link org.yapr.core.YaprCore#process(org.yapr.domain.Configuration, java.io.File[])}.
	 */
	@Test
	public void test_processConfigurationFileArray_good_array() {
		YaprCore instance = new YaprCore();
		Configuration configuration = new Configuration();
		List<FileRenamer> renamers = new ArrayList<FileRenamer>();
		renamers.add(new LyingRenamer());
		configuration.setRenamers(renamers);

		int processed = instance.process(configuration, _fileSystem.getAllFiles());
		assertEquals(_fileSystem.getAllFiles().length, processed);

		processed = instance.process(configuration, new File[]{});
		assertEquals(0, processed);
	}

	/**
	 * Test method for {@link org.yapr.core.YaprCore#process(org.yapr.domain.Configuration, java.io.File)}.
	 */
	@Test
	public void test_processConfigurationFileBoolean() {
		YaprCore instance = new YaprCore();
		Configuration configuration = new Configuration();
		List<FileRenamer> renamers = new ArrayList<FileRenamer>();
		renamers.add(new LyingRenamer());
		configuration.setRenamers(renamers);
		configuration.setProcessSubFolders(false);

		int processed = instance.process(configuration, _fileSystem.getBaseFolder());
		assertEquals(6, processed);
	}
}
