package org.yapr.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * @author Dimitri
 *
 */
public class ConfigurationTest {

	/**
	 * Test method for {@link org.yapr.domain.Configuration#Configuration()}.
	 * Check that the default, no-argument constructor initialize the instance with sensible attributes.
	 */
	@Test
	public void test_Configuration() {
		Configuration instance = new Configuration();
		assertEquals(0, instance.getHoursOffset());
		assertNotNull(instance.getDateFormat());
		assertNull(instance.getTargetFolder());
		assertNotNull(instance.getRenamers());
		assertEquals(0, instance.getRenamers().size());
	}

	/**
	 * Test method for {@link org.yapr.domain.Configuration#Configuration(java.text.SimpleDateFormat, int, java.lang.String)}.
	 * Check that the provided constructor arguments are correctly set as attributes of the returned instance.
	 */
	@Test
	public void test_ConfigurationSimpleDateFormatIntString() {
		int hoursOffset = -1;
		String targetFolder = "/tmp";
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy-HH'h'mm'm'ss");

		Configuration instance = new Configuration(sdf, hoursOffset, targetFolder);
		assertEquals(hoursOffset, instance.getHoursOffset());
		assertNotNull(instance.getTargetFolder());
		assertEquals(targetFolder, instance.getTargetFolder());
		assertNotNull(instance.getDateFormat());
		assertEquals(sdf.toPattern(), instance.getDateFormat().toPattern());
		assertNotNull(instance.getRenamers());
		assertEquals(0, instance.getRenamers().size());
	}

	/**
	 * Test method for {@link org.yapr.domain.Configuration#getDateFormat()}.
	 * Test method for {@link org.yapr.domain.Configuration#setDateFormat(java.text.SimpleDateFormat)}.
	 */
	@Test
	public void test_getSetDateFormat() {
		Configuration instance = new Configuration();
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy-HH'h'mm'm'ss");

		instance.setDateFormat(null);
		assertNull(instance.getDateFormat());

		instance.setDateFormat(sdf);
		assertNotNull(instance.getDateFormat());
		assertEquals(sdf.toPattern(), instance.getDateFormat().toPattern());

		sdf.applyPattern("yyyy-MM-dd-HH'h'mm'm'ss");

		instance.setDateFormat(sdf);
		assertNotNull(instance.getDateFormat());
		assertEquals("yyyy-MM-dd-HH'h'mm'm'ss", instance.getDateFormat().toPattern());

		instance.setDateFormat(null);
		assertNull(instance.getDateFormat());
	}

	/**
	 * Test method for {@link org.yapr.domain.Configuration#getHoursOffset()}.
	 * Test method for {@link org.yapr.domain.Configuration#setHoursOffset(int)}.
	 */
	@Test
	public void test_getSetHoursOffset() {
		Configuration instance = new Configuration();

		instance.setHoursOffset(2);
		assertEquals(2, instance.getHoursOffset());

		instance.setHoursOffset(0);
		assertEquals(0, instance.getHoursOffset());

		instance.setHoursOffset(-1);
		assertEquals(-1, instance.getHoursOffset());
	}

	/**
	 * Test method for {@link org.yapr.domain.Configuration#getTargetFolder()}.
	 * Test method for {@link org.yapr.domain.Configuration#setTargetFolder(java.lang.String)}.
	 */
	@Test
	public void test_getSetTargetFolder() {
		Configuration instance = new Configuration();

		instance.setTargetFolder(".");
		assertNotNull(instance.getTargetFolder());
		assertEquals(".", instance.getTargetFolder());

		instance.setTargetFolder(null);
		assertNull(instance.getTargetFolder());

		instance.setTargetFolder("../tmp");
		assertNotNull(instance.getTargetFolder());
		assertEquals("../tmp", instance.getTargetFolder());
	}

	/**
	 * Test method for {@link org.yapr.domain.Configuration#getRenamers()}.
	 * Test method for {@link org.yapr.domain.Configuration#setRenamers(java.util.List)}.
	 */
	@Test
	public void test_getSetRenamers() {
		Configuration instance = new Configuration();
		List<FileRenamer> renamers = new ArrayList<FileRenamer>();
		renamers.add(new LyingRenamer());

		instance.setRenamers(renamers);
		assertNotNull(instance.getRenamers());
		assertEquals(1, instance.getRenamers().size());
		assertTrue(instance.getRenamers().get(0) instanceof LyingRenamer);
	}
}
