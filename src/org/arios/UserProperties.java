package org.arios;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

/**
 * The properties of the Users desktop client.
 * @author Emperor
 * @author Vexia
 *
 */
public class UserProperties {
	
	/**
	 * The properties file.
	 */
	private final Properties properties = new Properties();

	/**
	 * The launch revision.
	 */
	private int launchRevision;

	/**
	 * The launch revision read from the revision data URL.
	 */
	private int launchRevArgument;

	/**
	 * The current revision.
	 */
	private int revision;

	/**
	 * The revision read from the revision data URL.
	 */
	private int revisionArgument;
	
	/**
	 * The cache file.
	 */
	private File cache;
	
	/**
	 * The gamepack file.
	 */
	private File gamepack;
	
	/**
	 * The launcher file.
	 */
	private File launcher;
	
	/**
	 * The property file.
	 */
	private File propertyFile;

	/**
	 * Constructs a new {@Code Properties} {@Code Object}
	 */
	public UserProperties() {
		this.launchRevision = Constants.LAUNCHER_REVISION;
		this.launchRevArgument = Constants.LAUNCHER_REVISION;
		this.revision = -1;
		this.revisionArgument = -1;
	}
	
	/**
	 * Prepares the cache directories.
	 */
	public void prepare() {
		cache = new File(Constants.getCachePath());
		if (!cache.exists()) {
			cache.mkdir();
		}
		gamepack = new File(Constants.getCachePath() + File.separator + "gamepack.jar");
		setLauncher(new File(Constants.getCachePath() + File.separator + "launcher.jar"));
		System.out.println("[Launcher]: Working in cache directory: " + Constants.getCachePath());
	}

	/**
	 * Initializes the user properties.
	 */
	public void init() {
		loadProperties();
		updateRevisions();
	}


	/**
	 * Updates the revision arguments from the website.
	 */
	private void updateRevisions() {
		Launcher.getLauncher().getFrame().updateMessage("Checking for updates");
		Launcher.getLauncher().getFrame().updatePercentage(0);
		Launcher.getLauncher().pause(400);
		String data = Constants.readLink(Constants.REVISION_CHECK_URL);
		if (data.length() < 2) {
			revisionArgument = revision;
			System.err.println("[Launcher]: Error! The revision arguments could not be parsed.");
			return;
		}
		Launcher.getLauncher().getFrame().updatePercentage(50);
		String[] settings = data.split("<br>");
		revisionArgument = Integer.parseInt(settings[0]);
		launchRevArgument = Integer.parseInt(settings[1]);
		Launcher.getLauncher().pause(400);
		Launcher.getLauncher().getFrame().updatePercentage(100);
		System.out.println("[Launcher]: Revision updates: revisionArgument=" + revisionArgument + ", launcherRevArgument=" + launchRevArgument + ", : revision=" + revision + ", launcherRevision=" + launchRevision + "");
	}
	
	/**
	 * Loads the saved properties.
	 */
	private void loadProperties() {
		propertyFile = new File(Constants.getCachePath() + File.separator + "arios.props");
		if (!propertyFile.exists()) {
			Launcher.getLauncher().getFrame().updateMessage("Properties file does not exist");
			Launcher.getLauncher().getFrame().updatePercentage(100);
			return;
		}
		try {
			FileReader reader = new FileReader(propertyFile);
			properties.load(reader);
			reader.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		Launcher.getLauncher().getFrame().updatePercentage(100);
		revision = Integer.parseInt(properties.getProperty("revision", "-1"));
		launchRevision = Integer.parseInt(properties.getProperty("launch_rev", "-1"));
		System.out.println("[Launcher]: Loaded properties: revision=" + revision + ", launchRevision=" + launchRevision + ", revisionArgument=" + revisionArgument + ", launchRevisionArgument=" + launchRevArgument + ".");
	}
	
	/**
	 * Saves the properties to the file.
	 */
	public void save() {
		properties.setProperty("revision", Integer.toString(revision));
		properties.setProperty("launch_rev", Integer.toString(launchRevision));
		try {
			FileWriter out = new FileWriter(propertyFile);
			properties.store(out, "Arios client desktop properties.");
			out.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the game file.
	 * @param launcher the launcher.
	 * @return the file.
	 */
	public File getGameFile(boolean launcher) {
		return launcher ? this.launcher : gamepack;
	}
	
	/**
	 * Checks if an update is required.
	 * @return {@code True} if so.
	 */
	public boolean isUpdateRequired() {
		if (!gamepack.exists()) {
			Launcher.getLauncher().getFrame().updatePercentage(100);
			return true;
		}
		if (revisionArgument != revision) {
			revision = revisionArgument;
			return true;
		}
		return false;
	}

	/**
	 * Gets the launchRevision.
	 * @return the launchRevision.
	 */
	public int getLaunchRevision() {
		return launchRevision;
	}

	/**
	 * Sets the launchRevision.
	 * @param launchRevision the launchRevision to set
	 */
	public void setLaunchRevision(int launchRevision) {
		this.launchRevision = launchRevision;
	}

	/**
	 * Gets the launchRevArgument.
	 * @return the launchRevArgument.
	 */
	public int getLaunchRevArgument() {
		return launchRevArgument;
	}

	/**
	 * Sets the launchRevArgument.
	 * @param launchRevArgument the launchRevArgument to set
	 */
	public void setLaunchRevArgument(int launchRevArgument) {
		this.launchRevArgument = launchRevArgument;
	}

	/**
	 * Gets the revision.
	 * @return the revision.
	 */
	public int getRevision() {
		return revision;
	}

	/**
	 * Sets the revision.
	 * @param revision the revision to set
	 */
	public void setRevision(int revision) {
		this.revision = revision;
	}

	/**
	 * Gets the revisionArgument.
	 * @return the revisionArgument.
	 */
	public int getRevisionArgument() {
		return revisionArgument;
	}

	/**
	 * Sets the revisionArgument.
	 * @param revisionArgument the revisionArgument to set
	 */
	public void setRevisionArgument(int revisionArgument) {
		this.revisionArgument = revisionArgument;
	}


	/**
	 * Gets the properties.
	 * @return the properties.
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * Gets the gamepack.
	 * @return the gamepack.
	 */
	public File getGamepack() {
		return gamepack;
	}

	/**
	 * Gets the launcher.
	 * @return the launcher.
	 */
	public File getLauncher() {
		return launcher;
	}

	/**
	 * Sets the launcher.
	 * @param launcher the launcher to set
	 */
	public void setLauncher(File launcher) {
		this.launcher = launcher;
	}

}
