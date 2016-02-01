package org.yapr.domain;

import java.io.File;

public interface FileRenamer {

	/**
	 * Test if the proposed file can be renamed by the instance implementing
	 * this interface.<br>
	 * 
	 * @param file
	 *            The file to be renamed
	 * 
	 * @return {@code true} if it can, {@code false} otherwise.
	 */
	public boolean accept(File file);

	/**
	 * Effectively rename the file using the provided configuration
	 * 
	 * @param file
	 *            The file to rename
	 * @param configuration
	 *            The configuration rules to apply by the renaming process
	 * @return If the file has previously been accepted by invoking the
	 *         {@link #accept(Object)}, the return value should be an instance
	 *         of {@link File}, denoting the renamed file, unless a runtime
	 *         exception occurred, or the media does not embed the particular
	 *         metadata the current instance of {@link AssetRenamer} relies on
	 *         (EXIF metadata, ...). <br>
	 *         If the file has <i>not</i> been accepted by invoking the
	 *         {@link #accept(Object)} the return value is less predictable.
	 *         However, it should stick to the contract: an instance of
	 *         {@code File} if the renaming was successful, {@code null}
	 *         otherwise.
	 */
	public File rename(File file, Configuration configuration);

}
