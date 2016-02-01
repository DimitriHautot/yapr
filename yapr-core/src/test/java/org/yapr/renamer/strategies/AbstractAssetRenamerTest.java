package org.yapr.renamer.strategies;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.junit.Test;
import org.yapr.domain.Configuration;
import org.yapr.tests.FileSystemInitializer;

/**
 * @author Dimitri
 *
 */
public class AbstractAssetRenamerTest {

	private FileSystemInitializer _fileSystem;

	public AbstractAssetRenamerTest() {
		_fileSystem = new FileSystemInitializer(getClass().getSimpleName());
	}

	/**
	 * Test method for {@link org.yapr.renamer.strategies.AbstractAssetRenamer#newFile(java.lang.String, java.util.Date, java.lang.String, org.yapr.domain.Configuration)}.
	 */
	@Test
	public void test_newFile() {
		Configuration configuration = new Configuration();
		configuration.setTargetFolder(_fileSystem.getBaseFolder().toString());
		Date now = new Date();

		AbstractAssetRenamer instance = new AbstractAssetRenamer() {
			@Override
			public File rename(File asset, Configuration configuration) {
				// Don't care; only test the newFile(...) method
				return asset;
			}
			@Override
			public boolean accept(File asset) {
				// Don't care; only test the newFile(...) method
				return false;
			}
			@Override
			public Date getDateToUse(File file) {
				// Don't care; only test the newFile(...) method
				return null;
			}
		};

		File file1 = instance.newFile(null, now, "tst", configuration);
		assertTrue(file1 != null);
		assertTrue(file1.getName().equals(configuration.getDateFormat().format(now) + ".tst"));
		try {
			file1.createNewFile();
			file1.deleteOnExit();
		} catch (IOException e) {
			if (file1 != null) {
				file1.delete();
			}
			fail("Unable to create 'file1'");
		}

		File file2 = instance.newFile(null, now, "tst", configuration);
		file2.deleteOnExit();
		assertTrue(file2.getName().equals(configuration.getDateFormat().format(now) + "a.tst"));
		file2.delete();
	}

}
