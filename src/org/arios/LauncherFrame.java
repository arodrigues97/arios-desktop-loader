package org.arios;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * Represents the launcher frame where the components are drawn.
 * @author Vexia
 *
 */
public class LauncherFrame extends JFrame {

	/**
	 * The main serial version UID.
	 */
	private static final long serialVersionUID = 916654612291471856L;

	/**
	 * The update message.
	 */
	private String message = "Loading the configurations";
	
	/**
	 * The status.
	 */
	private String status = "Idle";
	
	/**
	 * The percentage of loading.
	 */
	private double percentage = 0.0;
	
	/**
	 * If we have finished loading.
	 */
	private boolean loaded;

	/**
	 * Constructs a new {@Code LauncherFrame} {@Code Object}
	 */
	public LauncherFrame() {
		super(Constants.TITLE);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (!loaded) {
			update();
		}
	}

	/**
	 * Initializes the frame.
	 */
	public void init() {
		getContentPane().setBackground(Color.BLACK);
		setLocation(540, 320);
		setLayout(null);
		toFront();
		setSize(Constants.SIZE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setIconImage(getToolkit().getImage(getClass().getResource("favicon.png")));
		setVisible(true);
		toFront();
		repaint();
	}
	
	/**
	 * Updates the frame graphics.
	 */
	public void update() {
		final Graphics graphics = getContentPane().getGraphics();
		if (graphics == null) {
			return;
		}
		final Color color = new Color(140, 17, 17);
		int positionY = (int) (getSize().getHeight() - 66);
		int positionX = 8;
		int x1 = (int) (getSize().getWidth() - 22);
		graphics.setColor(color);
		graphics.drawRect(positionX, positionY, x1, 33);
		graphics.fillRect(positionX + 2, positionY + 2, (int) (percentage * 3), 30);
		graphics.setColor(Color.BLACK);
		graphics.fillRect(positionX + 2 + (int) (percentage * 3), positionY + 2, x1 - (int) (percentage * 3) - 2, 30);
		graphics.drawRect(positionX + 1, positionY + 1, x1, 31);
		graphics.setColor(Color.WHITE);
		graphics.drawString((message) + "... - " + ((int) percentage) + "%", 72, positionY + 22);
		graphics.drawImage(getToolkit().getImage(getClass().getResource("favicon.png")), 190, 10, null);
		graphics.drawString("Current revision: " + Launcher.getLauncher().getProperties().getRevision(), 10, 20);
		graphics.drawString("Revision argument: " + Launcher.getLauncher().getProperties().getRevisionArgument(), 10, 35);
		graphics.drawString("Launcher revision: " + Launcher.getLauncher().getProperties().getLaunchRevision(), 10, 50);
		graphics.drawString("Launcher Rev Argument: " + Launcher.getLauncher().getProperties().getLaunchRevArgument(), 10, 65);
		graphics.drawString("Status: " + status, 10, 80);
	}
	
	/**
	 * Updates the percentage completed of launching.
	 * @param percentage the percentage.
	 */
	public void updatePercentage(double percentage) {
		if (percentage > 100.0) {
			percentage = 100.0;
		}
		if (this.percentage != percentage) {
			this.percentage = percentage;
			update();
		}
	}

	/**
	 * Updates the message to be drawn.
	 * @param message the message.
	 * @param repaint if a repaint is needed.
	 */
	public void updateMessage(String message, boolean repaint) {
		if (repaint) {
			repaint();
		}
		this.message = message;
	}
	
	/**
	 * Updates the message to be drawn.
	 * @param message the message.
	 */
	public void updateMessage(String message) {
		updateMessage(message, true);
	}
	
	/**
	 * Updates the status.
	 * @param status the status.
	 */
	public void updateStatus(String status) {
		repaint();
		this.status = status;
	}
	
	/**
	 * Shows the error dialogue.
	 */
	public void showErrorDialogue() {
		JLabel label = new JLabel();
		Font font = label.getFont();
		StringBuffer style = new StringBuffer("font-family:" + font.getFamily() + ";");
		style.append("font-weight:" + (font.isBold() ? "bold" : "normal") + ";");
		style.append("font-size:" + font.getSize() + "pt;");
		JEditorPane ep = new JEditorPane("text/html", "<html><body style=\"" + style + "\">"	+ "Failed to load - download <a href=\"" + Constants.GAMEPACK_URL + "/\">" + Constants.GAMEPACK_URL + "</a>"+ "</body></html>");
		ep.addHyperlinkListener(new HyperlinkListener() {
			@Override
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
					try {
						Constants.launch(e.getURL().toString());
					} catch (Throwable t) {
						t.printStackTrace();
					}
				}
			}
		});
		ep.setEditable(false);
		ep.setBackground(label.getBackground());
		JOptionPane.showMessageDialog(this, ep);
	}
	
	/**
	 * Gets the percentage.
	 * @return the percentage.
	 */
	public double getPercentage() {
		return percentage;
	}

	/**
	 * Gets the message.
	 * @return the message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Gets the loaded.
	 * @return the loaded.
	 */
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * Sets the loaded.
	 * @param loaded the loaded to set
	 */
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	/**
	 * Gets the status.
	 * @return the status.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	
}
