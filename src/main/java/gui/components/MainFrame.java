package gui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.swing.JFrame;

import database.DatabaseManager;
import gui.components.menubar.MenuBar;

public class MainFrame extends JFrame {
	private SheetMusicCanvas canvas;
	private ControlPanel controlPanel;
	private MenuBar menuBar;
	private long startTime;
	private long stopTime;
	private DatabaseManager databaseManager;
	
	public SheetMusicCanvas getCanvas() {
		return canvas;
	}

	public MainFrame() {
		super("Sheet Music Maker");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setPreferredSize(new Dimension(800, 600));
		
		setLayout(new BorderLayout());
		canvas = new SheetMusicCanvas();
		controlPanel = new ControlPanel(canvas, this);
		menuBar = new MenuBar(canvas, this);
		
		add(canvas, BorderLayout.CENTER);
		add(controlPanel, BorderLayout.WEST);
		setJMenuBar(menuBar);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		startTime = System.currentTimeMillis();
		databaseManager = new DatabaseManager();
		
		addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
        		stopTime = System.currentTimeMillis();
        		dispose();
        		try {
					databaseManager.openConnection();
					databaseManager.saveUsageTime(getDeviceID(), stopTime - startTime);
					databaseManager.closeConnection();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
	}
	
	private static String getDeviceID() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface network = interfaces.nextElement();
                byte[] mac = network.getHardwareAddress();
                if (mac != null) {
                    StringBuilder macAddress = new StringBuilder();
                    for (byte b : mac) {
                        macAddress.append(String.format("%02X", b));
                    }
                    return macAddress.toString();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "UNKNOWN_DEVICE";
    }
}
