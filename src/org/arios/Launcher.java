package org.arios;


/**
 * Launches the desktop client.
 * @author Emperor
 * @author Vexia
 *
 */
public final class Launcher {

	/**
	 * The user properties.
	 */
	private final UserProperties properties = new UserProperties();

	/**
	 * The launcher singleton.
	 */
	private static final Launcher LAUNCHER = new Launcher();

	/**
	 * The launcher frame.
	 */
	private final LauncherFrame frame = new LauncherFrame();
	
	/**
	 * The update manager.
	 */
	private final UpdateManager updateManager = new UpdateManager();

	/**
	 * Constructs a new {@Code Launcher} {@Code Object}
	 */
	private Launcher() {
		/*
		 * empty.
		 */
	}

	/**
	 * The main method used to extecute the launcher.
	 * @param args The arguments cast on runtime.
	 * @throws Throwable  the throwable exception.
	 */
	public static void main(String[] args) throws Throwable {
		try {
			LAUNCHER.launch();
		} catch(Throwable throwable) {
			throwable.printStackTrace();
			LAUNCHER.getFrame().showErrorDialogue();
		}
	}

	/**
	 * Launches this application.
	 */
	public void launch() {
		properties.prepare();
		Launcher.getLauncher().getFrame().init();
		properties.init();
		if (!updateManager.download(true)) {
			updateManager.load(false);
		}
	}

	/**
	 * Pauses the current thread.
	 * @param duration The duration to pause for.
	 */
	public void pause(long duration) {
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the properties.
	 * @return the properties.
	 */
	public UserProperties getProperties() {
		return properties;
	}

	/**
	 * Gets the launcher.
	 * @return the launcher.
	 */
	public static Launcher getLauncher() {
		return LAUNCHER;
	}

	/**
	 * Gets the frame.
	 * @return the frame.
	 */
	public LauncherFrame getFrame() {
		return frame;
	}

	/**
	 * Gets the updateManager.
	 * @return the updateManager.
	 */
	public UpdateManager getUpdateManager() {
		return updateManager;
	}

}