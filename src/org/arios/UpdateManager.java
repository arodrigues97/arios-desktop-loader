package org.arios;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;

import javax.swing.JOptionPane;

/**
 * A manager class used for updating and downloading files.
 * @author Emperor
 * @author Vexia
 *
 */
public class UpdateManager {
	
	/**
	 * If we will run in swift mode.
	 */
	private boolean swift;

	/**
	 * Constructs a new {@Code UpdateManager} {@Code Object}
	 */
	public UpdateManager() {
		/*
		 * empty.
		 */
	}

	/**
	 * Loads the gamepack file.
	 * @param forceDownload If we should redownload the gamepack regardless of revision check.
	 */
	public void load(boolean forceDownload) {
		if (forceDownload || Launcher.getLauncher().getProperties().isUpdateRequired()) {
			pause(100);
			System.out.println("[Launcher]: Desktop client out of date.");
			Launcher.getLauncher().getFrame().updateStatus("Launcher out of date");
			pause(400);
			if (download(false)) {
				Launcher.getLauncher().getProperties().save();
			}
		} else {
			Launcher.getLauncher().getFrame().updateStatus("Launcher up to date");
			pause(400);
			System.out.println("[Launcher]: Desktop client up to date - revision=" + Launcher.getLauncher().getProperties().getRevision() + ".");
		}
		try {
			String[] buttons = {"Vanilla", "Swift Kit"};
			int button = JOptionPane.showOptionDialog(null, "Your choice of a client mode", "Select a Mode", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[1]);
			swift = button == 1;
			launch(false);
		} catch (Throwable t) {
			Launcher.getLauncher().getFrame().setLoaded(false);
			t.printStackTrace();
			if (!forceDownload) {
				load(true);
			} else {
				Launcher.getLauncher().getFrame().showErrorDialogue();
			}
		}
	}

	/**
	 * Downloads a file from the Arios website repository.
	 * @param launcher if we're downloading the launcher.
	 * @return {@code True} if the file was downloaded.
	 */
	public boolean download(boolean launcher) {
		final File file = Launcher.getLauncher().getProperties().getGameFile(launcher);
		final String name = (launcher ? "launcher" : "gamepack");
		updatePercentage(0);
		System.out.println("[Launcher]: Downloading the " + name + ".");
		pause(400);
		if (!launcher || (Launcher.getLauncher().getProperties().getLaunchRevArgument() != Launcher.getLauncher().getProperties().getLaunchRevision() || (!file.exists()))) {
			updateMessage("Downloading " + name);
			System.out.println("[Launcher]: Commited to downloading: " + name + ".");
			if (!launcher) {
				Launcher.getLauncher().getFrame().updateStatus("Gamepack out of date");
				pause(400);
			}
			final ByteBuffer buffer = ByteBuffer.allocate(20000000);
			try {
				HttpURLConnection con = (HttpURLConnection) new URL(launcher ? Constants.LAUNCHER_URL : Constants.GAMEPACK_URL).openConnection();
				con.addRequestProperty("User-Agent", "Mozilla/4.76"); 
				InputStream stream = con.getInputStream();
				while (true) {
					byte[] bs = new byte[1 << 10];
					int amount = stream.read(bs);
					if (amount == -1) {
						break;
					}
					buffer.put(bs, 0, amount);
					updatePercentage(Launcher.getLauncher().getFrame().getPercentage() + 0.055);
				}
				buffer.flip();
				if (launcher) {
					Launcher.getLauncher().getProperties().setLaunchRevision(Launcher.getLauncher().getProperties().getLaunchRevArgument());
					Launcher.getLauncher().getProperties().save();
				} else {
					Launcher.getLauncher().getProperties().setRevision(Launcher.getLauncher().getProperties().getRevisionArgument());
					Launcher.getLauncher().getProperties().save();
					Launcher.getLauncher().getProperties().getGameFile(launcher).delete();
				}
				final FileOutputStream fos = new FileOutputStream(file);
				fos.getChannel().write(buffer);
				fos.close();
				System.out.println("[Launcher]: Writing the " + name + " to the directory.");
				updatePercentage(100.0);
			} catch (Throwable t) {
				t.printStackTrace();
				if (!launcher) {
					return false;
				}
			}
		} else {
			updateMessage(name.replace(name.charAt(0), String.valueOf(name.charAt(0)).toUpperCase().charAt(0)) + " up to date");
			System.out.println("[Launcher]: The " + name + " did not need to be downloaded.");
			pause(1000);
		}
		updatePercentage(100.0);
		if (launcher && file.exists() && Constants.LAUNCHER_REVISION != Launcher.getLauncher().getProperties().getLaunchRevArgument()) {
			launch(true);
		}
		return !launcher;
	}

	/**
	 * Launches a game file.
	 * @param launcher if we're launching the launcher.
	 * @return {@code True} if so.
	 */
	public boolean launch(boolean launcher) {
		Launcher.getLauncher().getFrame().dispose();
		File file = Launcher.getLauncher().getProperties().getGameFile(launcher);
		try {
			Runtime.getRuntime().exec("java -jar " + file.getAbsolutePath() + " " + (!launcher ? (swift ? "true" : "false") : ""));
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			Launcher.getLauncher().getFrame().showErrorDialogue();
			return false;
		}
		System.exit(-1);
		return true;
	}

	/**
	 * Updates the message.
	 * @param message the message.
	 */
	public void updateMessage(String message) {
		Launcher.getLauncher().getFrame().updateMessage(message);
	}

	/**
	 * Updates the percentage completed.
	 * @param percent the percent.
	 */
	public void updatePercentage(double percent) {
		Launcher.getLauncher().getFrame().updatePercentage(percent);
	}

	/**
	 * Pauses the current thread.
	 * @param duration the duration.
	 */
	public void pause(long duration) {
		Launcher.getLauncher().pause(duration);
	}

}
