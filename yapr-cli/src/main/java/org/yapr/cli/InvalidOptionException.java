package org.yapr.cli;

/**
 * @author Dimitri
 *
 */
public class InvalidOptionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String errorMessage = null;
	private String executableNameForHelpMessage = null;
	private int exitCode = 0;

	public InvalidOptionException(int exitCode, String errorMessage, String executableNameForHelpMessage) {
		this(exitCode, errorMessage);
		this.executableNameForHelpMessage = executableNameForHelpMessage;
	}

	public InvalidOptionException(int exitCode, String errorMessage) {
		this.exitCode = exitCode;
		this.errorMessage = errorMessage;
	}

	public int getExitCode() {
		return exitCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getExecutableNameForHelpMessage() {
		return executableNameForHelpMessage;
	}
}
