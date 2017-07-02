package client;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class UIChat extends JDialog {


	private JTextField txtMessage;
	private JTextArea txtAreaMessage;
	private JButton btnSend;

	private String username;
	private UIClients uiClients;
	private boolean isPrivate;

	public UIChat(String username, UIClients uiClients, boolean isPrivate) {
		this.username = username;
		this.uiClients = uiClients;
		this.isPrivate = isPrivate;
		
		initialize();
		initializeEvents();
	}

	private void initialize() {
				
		this.setTitle(this.username);
		this.setBounds(100, 100, 482, 327);
		this.setDefaultCloseOperation( JDialog.DISPOSE_ON_CLOSE );
		this.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(10, 50));
		panel.setMinimumSize(new Dimension(10, 50));		
		this.getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));

		btnSend = new JButton("ENVIAR");
		panel.add(btnSend, BorderLayout.EAST);

		txtMessage = new JTextField();
		panel.add(txtMessage, BorderLayout.CENTER);
		txtMessage.setColumns(10);

		JScrollPane scrollPane = new JScrollPane();		
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);

		txtAreaMessage = new JTextArea();
		txtAreaMessage.setEditable(false);
		txtAreaMessage.setFont(new Font("Verdana", Font.BOLD, 12));
		scrollPane.setViewportView(txtAreaMessage);
		
		this.setLocationRelativeTo(uiClients);
		this.setVisible(true);	
	}

	private void initializeEvents() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				openExitWindowConfirmation();
			}
		});
		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				onSendClick(e);
			}
		});
		txtMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent VK) {
				if (VK.getKeyCode() == KeyEvent.VK_ENTER) {
					onPressEnter();
				}
			}
		});
	}

	private void openExitWindowConfirmation() {
		int opcion = JOptionPane.showConfirmDialog(this, "Si sale del chat se eliminarán todas las conversaciones",
				"Confirmación", JOptionPane.YES_NO_OPTION);
		if (opcion == JOptionPane.YES_OPTION)
		{
			this.uiClients.removeChat(this.username);			
		}
	}

	private void onSendClick(MouseEvent e) {
		this.sendMessage();
	}

	private void onPressEnter() {
		this.sendMessage();
	}

	/*
	 * Envia un mensaje usando la UIClients
	 */
	private void sendMessage() {
		try {
			this.uiClients.sendMessage(this.username, txtMessage.getText(), this.isPrivate);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "No se pudo enviar el mensaje.", "Error con servidor",
					JOptionPane.INFORMATION_MESSAGE);
		}
		this.txtMessage.setText("");
	}

	/*
	 * Recibe un mensaje y lo muestra en pantalla
	 */
	public void receiveMessage(String username, String message) {
		txtAreaMessage.append(username + ": " + message + "\n");
	}
}
