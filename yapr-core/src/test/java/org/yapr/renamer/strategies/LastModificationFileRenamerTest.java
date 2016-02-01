package org.yapr.renamer.strategies;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yapr.domain.Configuration;
import org.yapr.tests.FileSystemInitializer;

public class LastModificationFileRenamerTest {

	private static FileSystemInitializer fsInitializer;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private LastModificationFileRenamer instance = new LastModificationFileRenamer();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		fsInitializer = new FileSystemInitializer(null);
	}

	@Test
	public void test_accept() {
		assertFalse(instance.accept(null));
	}

	@Test
	public void test_rename() throws URISyntaxException, IOException {
		Configuration configuration = new Configuration();
		String t1String = configuration.getDateFormat().format(new Date(System.currentTimeMillis() - 5000)) + ".jpg";
		
		File folder = new File(ClassLoader.getSystemResource(".").toURI());
		File file = fsInitializer.createTempFile(null, ".jpg", folder);
		
		assertTrue(instance.accept(file));
		File renamedFile = instance.rename(file, configuration);
		assertNotNull(renamedFile);
		assertFalse(file.getName().equals(renamedFile.getName()));
		logger.info("File '{}' has been renamed to {}", file, renamedFile);
		
		assertTrue(t1String.compareTo(renamedFile.getName()) < 0);

		String t2String = configuration.getDateFormat().format(new Date(System.currentTimeMillis() + 5000));
		
		assertTrue(renamedFile.getName().compareTo(t2String + ".jpg") < 0);
	}

}
