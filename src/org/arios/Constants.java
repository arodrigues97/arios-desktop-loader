package org.arios;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * A list of constants used for the launcher.
 * @author Vexia
 *
 */
public class Constants {
	
	/**
	 * The dimension size of the launcher.
	 */
	public static final Dimension SIZE = new Dimension(325, 155);
	
	/**
	 * The title of this launcher.
	 */
	public static final String TITLE = "Arios Launcher V2";

	/**
	 * The website link.
	 */
	public static final String WEBSITE = "http://www.ariosrsps.com/";

	/**
	 * The cache name.
	 */
	public static final String CACHE_NAME = File.separator + ".arios_498";

	/**
	 * The gamepack URL.
	 */
	public static final String GAMEPACK_URL = WEBSITE + "live/arios-gamepack.jar";

	/**
	 * The launcher URL.
	 */
	public static final String LAUNCHER_URL = WEBSITE + "live/arios-launcher.jar";

	/**
	 * The revision check URL.
	 */
	public static final String REVISION_CHECK_URL = WEBSITE + "live/settings.php";

	/**
	 * The launcher revision.
	 */
	public static final int LAUNCHER_REVISION = 16;
	
	/**
	 * Constructs a new {@Code Constants} {@Code Object}
	 */
	private Constants() {
		/*
		 * empty.
		 */
	}
	
	/**
	 * Reads a link and returns the data.
	 * @param link the link.
	 * @return the data.
	 */
	public static String readLink(String link) {
		String data = "";
		URL obj = null;
		try {
			obj = new URL(link);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		HttpURLConnection con;
		try {
			con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			int responseCode = con.getResponseCode();
			if (responseCode != 200) {
				return data;
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				data+= line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	/**
	 * Launches a URL and opens a browser.
	 * @param url the url.
	 * @throws Throwable the throwable if thrown.
	 */
	public static void launch(String url) throws Throwable {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			if (desktop.isSupported(Action.BROWSE))
				try {
					desktop.browse(URI.create(url));
					return;
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		try {
			Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the cache path.
	 * @return The cache path.
	 */
	public static String getCachePath() { 
		final String OS = System.getProperty("os.name").toUpperCase();
		if (OS.contains("WIN")) {
			return new StringBuilder(System.getenv("APPDATA")).append(Constants.CACHE_NAME).toString();
		} else if (OS.contains("MAC")) {
			return new StringBuilder(System.getProperty("user.home") + Constants.CACHE_NAME).toString();
		} else if (OS.contains("NUX")) {
			return System.getProperty("user.home");
		}
		return new StringBuilder(System.getProperty("user.dir")).toString();
	}
}
