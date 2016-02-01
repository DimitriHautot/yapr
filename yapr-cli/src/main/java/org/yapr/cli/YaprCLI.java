package org.yapr.cli;

import java.io.File;
import java.text.SimpleDateFormat;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.LoggerFactory;
import org.yapr.core.ConfigurationFactory;
import org.yapr.core.YaprCore;
import org.yapr.domain.Configuration;

/**
 * Cette classe est le point d'entrée de l'invocation en ligne de commande.
 * 
 * Created on Dec 22, 2011
 * @author Dimitri
 */
public class YaprCLI {

	protected Options options = null;
	protected HelpFormatter formatter = null;

	/**
	 * <p>Possible options:
	 * <ul>
	 *   <li>R - process subfolders recursively</li>
	 *   <li>h - show usage & help information</li>
	 * </ul></p>
	 * <p>Possible arguments:
	 * <ul>
	 *   <li>source - the process input source folder</li>
	 *   <li>target - the process output target folder</li>
	 *   <li>renamingPattern - the renaming pattern for processed files; use the java.text.SimpleDateFormat syntax</li>
	 *   <li>hoursOffset - the hours offset (to apply daylight saving to EXIF metadata)</li>
	 * </ul></p>
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		YaprCLI yaprCLI = new YaprCLI();
		Configuration configuration = null;

		try {
			configuration = yaprCLI.prepareConfiguration(args);
			YaprCore core = new YaprCore();
			int processed = core.process(configuration);
			LoggerFactory.getLogger(YaprCLI.class).info("Processed {} items.", processed);

		} catch (InvalidOptionException exception) {
			if (exception.getErrorMessage() != null) {
				System.err.println(exception.getErrorMessage());
			}
			if (exception.getExecutableNameForHelpMessage() != null) {
				yaprCLI.formatter.printHelp(exception.getExecutableNameForHelpMessage(), yaprCLI.options);
			}
			System.exit(exception.getExitCode());
		}
	}

	public YaprCLI() {
		options = createOptions();
		formatter = new HelpFormatter();
	}

	protected Configuration prepareConfiguration(String[] args) throws InvalidOptionException {
		boolean processSubFolders = false;
		String strSourceFolder = null;
		File sourceFolder = null;
		String strTargetFolder = null;
		File targetFolder = null;
		String pattern = "yyyy-MM-dd-HH'h'mm'm'ss";
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		int hoursOffset = 0;

		CommandLineParser gnuParser = new GnuParser();
		CommandLine gnuLine = null;
		try {
			gnuLine = gnuParser.parse(options, args);
		} catch (ParseException exp) {
			throw new InvalidOptionException(1, "Parsing failed.  Reason: " + exp.getMessage(), "yapr");
		}

		// OPTIONAL - Help / usage
		if (gnuLine.hasOption("h") || gnuLine.hasOption("help")) {
			throw new InvalidOptionException(0, null, "yapr");
		}

		// MANDATORY - Source folder
		if (gnuLine.hasOption("source")) {
			strSourceFolder = gnuLine.getOptionValue("source");
			sourceFolder = new File(strSourceFolder);
			if (! sourceFolder.isDirectory()) {
				throw new InvalidOptionException(1, "Source folder \"" + strSourceFolder + "\" is not a valid folder!");
			} else if (! sourceFolder.canRead()) {
				throw new InvalidOptionException(1, "Source folder \"" + strSourceFolder + "\" cannot be read!");
			}
		} else {
			throw new InvalidOptionException(1, null, "yapr");
		}

		// OPTIONAL - Target folder
		if (gnuLine.hasOption("target")) {
			strTargetFolder = gnuLine.getOptionValue("target");
			targetFolder = new File(strTargetFolder);
			if (! targetFolder.isDirectory()) {
				throw new InvalidOptionException(1, "Target folder \"" + strTargetFolder + "\" is not a valid folder!");
			} else if (! targetFolder.canWrite()) {
				throw new InvalidOptionException(1, "Target folder \"" + strTargetFolder + "\" cannot be written into!");
			}
		}

		// OPTIONAL - Process subfolders recursively
		if (gnuLine.hasOption("R")) {
			processSubFolders = true;
		}

		// OPTIONAL - Hours offset
		if (gnuLine.hasOption("offset")) {
			hoursOffset = Integer.parseInt(gnuLine.getOptionValue("offset"));
		}

		// OPTIONAL - Renaming pattern
		if (gnuLine.hasOption("dateFormat")) {
			pattern = gnuLine.getOptionValue("dateFormat");
			try {
				dateFormat.applyPattern(pattern);
			} catch (IllegalArgumentException ex) {
				throw new InvalidOptionException(1, "Renaming pattern \"" + pattern + "\" is invalid, according to java.text.SimpleDateFormat.applyPattern(...)");
			}
		}

		ConfigurationFactory configurationFactory = new ConfigurationFactory();
		Configuration configuration = configurationFactory.newInstance(dateFormat, hoursOffset, strTargetFolder);
		configuration.setProcessSubFolders(processSubFolders);
		configuration.setSourceFolder(sourceFolder);

		return configuration;
	}

	@SuppressWarnings("static-access") // Commons CLI's responsibility, not Yapr's :-)
	protected Options createOptions() {
		Options options = new Options();
		options.addOption("h", false, "print this message");
		options.addOption("help", false, "print this message");
		options.addOption("R", false, "process subfolders recursively (default is to not process subfolders");
		Option sourceFolder = OptionBuilder.withArgName("folder").hasArg().withDescription("the process input source folder (mandatory; no default)").create("source");
		options.addOption(sourceFolder);
		Option targetFolder = OptionBuilder.withArgName("folder").hasArg().withDescription("the process output target folder (default is same directory as source file)").create("target");
		options.addOption(targetFolder);
		Option hoursOffset = OptionBuilder.withArgName("offset=value").hasArg().withValueSeparator().withDescription("the hours offset (to apply daylight saving to EXIF metadata - default is 0)").create("offset");
		options.addOption(hoursOffset);
		Option renamingPattern = OptionBuilder.withArgName("pattern").hasArg().withDescription("the renaming pattern for processed files; use the java.text.SimpleDateFormat syntax - default is \"yyyy-MM-dd-HH'h'mm'm'ss\"").create("dateFormat");
		options.addOption(renamingPattern);

		return options;
	}

}
